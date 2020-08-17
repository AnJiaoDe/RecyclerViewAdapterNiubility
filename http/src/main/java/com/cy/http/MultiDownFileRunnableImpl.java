package com.cy.http;


import com.cy.http.utils.IOListener;
import com.cy.http.utils.IOUtils;
import com.cy.http.utils.LogUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.lang.reflect.Field;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * ************************************************************
 * author：cy
 * version：
 * create：2019/04/10 17:13
 * desc：
 * ************************************************************
 */
public class MultiDownFileRunnableImpl implements Runnable {

    private HttpURLConnection httpURLConnection;
    private URL url;
    private File fileToSave;
    private File fileTemp;
    private int index_thread;
    private long index_start;
    private long index_end;
    private Callback callback;
    private boolean isRunning = true;
    private IOUtils ioUtils = new IOUtils();
    private final String OPTION = "下载文件";
    private boolean isFailed = false;

    private DownMultiRequest downMultiRequest;

    public void setUrl(URL url) {
        this.url = url;
    }

    public void setFileToSave(File fileToSave) {
        this.fileToSave = fileToSave;
    }

    public void setFileTemp(File fileTemp) {
        this.fileTemp = fileTemp;
    }

    public void setIndex_thread(int index_thread) {
        this.index_thread = index_thread;
    }

    public void setIndex_start(long index_start) {
        this.index_start = index_start;
    }

    public void setIndex_end(long index_end) {
        this.index_end = index_end;
    }

    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    public void setDownMultiRequest(DownMultiRequest downMultiRequest) {
        this.downMultiRequest = downMultiRequest;
    }

    public void cancel() {
        LogUtils.log("cancel MultiDownFileRunnableImpl");
        isRunning = false;
    }

    @Override
    public void run() {
        try {
            request();

        } catch (MalformedURLException e) {
            e.printStackTrace();
            callback.callOnFail("网络请求失败，MalformedURLException" + e.getMessage());

        } catch (ProtocolException e) {
            try {
                Field methodField = HttpURLConnection.class.getDeclaredField("method");
                methodField.setAccessible(true);
                methodField.set(httpURLConnection, downMultiRequest.getMethod());
            } catch (NoSuchFieldException e1) {
                callback.callOnFail("网络请求失败，NoSuchFieldException" + e1.getMessage());

            } catch (IllegalAccessException e2) {
                callback.callOnFail("网络请求失败，IllegalAccessException" + e2.getMessage());
            }

        } catch (IOException e) {
            callback.callOnFail("网络请求失败，IOException" + e.getMessage());

        } catch (Exception e) {
            e.printStackTrace();
            callback.callOnFail("网络请求失败，IOException" + e.getMessage());

        } finally {
            if (httpURLConnection != null) httpURLConnection.disconnect();
        }


    }

    private void request() throws Exception {
        httpURLConnection = (HttpURLConnection) url.openConnection();
        // 设置请求方式
        httpURLConnection.setRequestMethod(HttpUtils.METHODS[0]);
        // 接着从上一次的位置继续下载数据
        httpURLConnection.setRequestProperty("Range", "bytes=" + index_start + "-" + index_end);
        /**
         *说明已经下载完毕
         Range设置为最大值会响应416  Requested Range Not Satisfiable
         */
        if (httpURLConnection.getResponseCode() == 416) {
            callback.callOnSuccess("");
            return;
        }
        if (httpURLConnection.getResponseCode() == HttpURLConnection.HTTP_OK || httpURLConnection.getResponseCode() == HttpURLConnection.HTTP_PARTIAL) {
//            inputStream = httpURLConnection.getInputStream();
//            callback.convertSuccess(fileLength + httpURLConnection.getContentLength(), inputStream);
            InputStream inputStream = httpURLConnection.getInputStream();
            RandomAccessFile randomAccessFile_save = new RandomAccessFile(fileToSave, "rw");
            FileChannel fileChannel = randomAccessFile_save.getChannel();

            // 指定文件开始写的位置。
            randomAccessFile_save.seek(index_start);

            int len = 0;
            ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
            byte[] buffer = new byte[1024];

            while (isRunning&&!isFailed && (len = inputStream.read(buffer)) != -1) {

                byteBuffer.clear();
                byteBuffer.put(buffer, 0, len);
                byteBuffer.flip();
                fileChannel.write(byteBuffer);
                index_start += len;
                final int l = len;
                ioUtils.writeStr2File(String.valueOf(index_start), fileTemp.getAbsolutePath(), new IOListener() {
                    @Override
                    public void onCompleted(Object result) {

                        callback.callOnLoading("", l, index_start, index_end);
                    }

                    @Override
                    public void onLoading(Object readedPart, int percent, long current, long length) {

                    }

                    @Override
                    public void onInterrupted(Object readedPart, int percent, long current, long length) {
                        callback.callOnFail(OPTION + "失败,过程被中断或者取消");
                        isFailed = true;
                    }

                    @Override
                    public void onFail(String errorMsg) {
                        callback.callOnFail(OPTION + "失败," + errorMsg);
                        isFailed = true;
                    }
                });

            }
            //中断
            if (len != -1) {
                callback.callOnFail(OPTION + "失败,过程被中断或者取消");
            } else {
                callback.callOnSuccess("");
            }
            fileChannel.close();
            randomAccessFile_save.close();
            inputStream.close();
        } else {
            callback.callOnFail(httpURLConnection.getResponseCode() + httpURLConnection.getResponseMessage());
        }

    }


}


