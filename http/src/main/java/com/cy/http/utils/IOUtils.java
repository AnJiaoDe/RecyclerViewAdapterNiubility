package com.cy.http.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;

/**
 * Created by cy on 2018/12/24.
 */

public class IOUtils {
    private boolean isRunning = true;
    private String encodeType = "utf-8";

    public IOUtils() {
        isRunning = true;
    }

    public void setEncodeType(String encodeType) {
        this.encodeType = encodeType;
    }

    public static void close(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void stop() {
        this.isRunning = false;
    }
    public void read2String(String pathFile, IOListener ioListener) {
        read2String(new File(pathFile),ioListener);
    }
    public void read2String(File file, IOListener ioListener) {
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            ioListener.onFail(e.getMessage());
            return;
        }
        read2String(file.length(),fileInputStream, ioListener);
    }
    public void read2String(long contentLength, InputStream inputStream, IOListener ioListener) {

        BufferedReader bufferedReader = null;
        InputStreamReader inputStreamReader = null;
        try {
            inputStreamReader = new InputStreamReader(inputStream, encodeType);
            bufferedReader = new BufferedReader(inputStreamReader);
            StringBuilder sb = new StringBuilder();
            char[] buffer = new char[1024];
            int len = 0;
            long current = 0;
            int percent = 0;
            int percentLast = 0;
            while (isRunning && (len = bufferedReader.read(buffer)) != -1) {
                sb.append(buffer, 0, len);
                current += len;
                percent = (int) (current * 1f / contentLength * 100);
                if (percent - percentLast >= 1) {
                    percentLast = percent;
                    ioListener.onLoading(buffer, percent, current, contentLength);
                }
            }

            //中断
            if (len != -1) {
                ioListener.onInterrupted(buffer, percent, current, contentLength);

            } else {
                ioListener.onCompleted(sb.toString());

            }

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            ioListener.onFail("网络请求失败UnsupportedEncodingException" + e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
            ioListener.onFail("网络请求失败IOException" + e.getMessage());

        } finally {
            close(bufferedReader);
            close(inputStreamReader);
            close(inputStream);
        }
    }

    /**
     * 写文件，加独占锁,读文件，加共享锁,(断点下载需要在网络请求时提交区间参数)
     *
     * @param filePath
     * @param contentLength
     * @param inputStream
     * @param ioListener
     */
    public void read2File(String filePath, long contentLength, InputStream inputStream, IOListener ioListener) {
        File file;
        try {
            file = FileUtils.createFile(filePath);
        } catch (IOException e) {
            e.printStackTrace();
            ioListener.onFail("文件创建失败，请检查路径是否合法以及读写权限");
            return;
        }

        RandomAccessFile randomAccessFile = null;
        FileChannel fileChannel = null;
        FileLock fileLock = null;//文件锁
        try {

            /**
             * 写文件
             */

            randomAccessFile = new RandomAccessFile(file, "rw");
            fileChannel = randomAccessFile.getChannel();
            //此处主要是针对多线程获取文件锁时轮询锁的状态。如果只是单纯获得锁的话，直接fl = fc.tryLock();即可
            while (true) {
                try {
                    fileLock = fileChannel.tryLock();//独占锁
                    break;
                } catch (Exception e) {
                    System.out.println("有其他线程正在操作该文件，当前线程" + Thread.currentThread().getName());
                }
            }
            if (fileLock != null) {
                if (file.length() == contentLength) {
                    //有缓存
                    LogUtils.log("缓存");
                    ioListener.onCompleted(file);
                    return;
                }
                byte[] buffer = new byte[1024];
                int len = 0;
                long current = file.length();
                int percent = (int) (current * 1f / contentLength * 100);
                int percentLast = percent;
                randomAccessFile.seek(current);
                ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
                while (isRunning && (len = inputStream.read(buffer)) != -1) {
//                    randomAccessFile.write(buffer, 0, len);
                    byteBuffer.clear();
                    byteBuffer.put(buffer, 0, len);
                    byteBuffer.flip();
                    fileChannel.write(byteBuffer);
                    current += len;
                    percent = (int) (current * 1f / contentLength * 100);
                    //防止不必要的频繁回调
                    if (percent - percentLast >= 1) {
                        percentLast = percent;
//                        LogUtils.log("当前线程" + Thread.currentThread().getName(), percent + "%");
                        ioListener.onLoading(buffer, percent, current, contentLength);
                    }
                }
                //中断
                if (len != -1) {
                    ioListener.onInterrupted(buffer, percent, current, contentLength);
                    return;
                }
                if (file.length() == contentLength) ioListener.onCompleted(file);
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            ioListener.onFail("文件创建失败，请检查路径是否合法以及读写权限,FileNotFoundException" + e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
            ioListener.onFail("文件下载失败，请检查路径是否合法以及读写权限,IOException" + e.getMessage());
        } finally {
            try {
                if (fileLock != null && fileLock.isValid()) fileLock.release();
            } catch (IOException e) {
                e.printStackTrace();
                ioListener.onFail("文件下载失败，请检查路径是否合法以及读写权限,IOException" + e.getMessage());
            }
            close(fileChannel);
            close(randomAccessFile);
            close(inputStream);
        }
    }

    public void read2ByteArray(long contentLength, InputStream inputStream, IOListener ioListener) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        byte[] buffer = new byte[1024];
        try {
            int len = 0;
            long current = 0;
            int percent = 0;
            int percentLast = 0;
            while (isRunning && (len = inputStream.read(buffer)) != -1) {

                byteArrayOutputStream.write(buffer, 0, len);
                current += len;
                percent = (int) (current * 1f / contentLength * 100);
                if (percent - percentLast >= 1) {
                    percentLast = percent;
                    ioListener.onLoading(buffer, percent, current, contentLength);
                }
            }

            byteArrayOutputStream.flush();
            //中断
            if (len != -1) {
                ioListener.onInterrupted(buffer, percent, current, contentLength);
                return;
            }
            if (byteArrayOutputStream.toByteArray().length > 0) {

                ioListener.onCompleted(byteArrayOutputStream.toByteArray());
            } else {
                ioListener.onFail("可能是文件损坏等原因");
            }
        } catch (IOException e) {
            e.printStackTrace();
            ioListener.onFail("网络请求失败,IOException" + e.getMessage());

        } finally {
            close(byteArrayOutputStream);
            close(inputStream);
        }
    }
    /**
     * 将str写入文件
     */
    public void writeStr2File(String str, String pathFile, IOListener ioListener) {
        BufferedWriter bufferedWriter = null;
        OutputStreamWriter outputStreamWriter = null;
        OutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(pathFile);
            outputStreamWriter = new OutputStreamWriter(outputStream,encodeType);
            bufferedWriter = new BufferedWriter(outputStreamWriter);
            bufferedWriter.write(str);
            ioListener.onCompleted("");
        } catch (IOException e) {
            e.printStackTrace();
            ioListener.onFail(e.getMessage());
        } finally {
            close(bufferedWriter);
            close(outputStreamWriter);
            close(outputStream);
        }
    }
}
