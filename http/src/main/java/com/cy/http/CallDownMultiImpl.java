package com.cy.http;


import com.cy.http.utils.FileUtils;
import com.cy.http.utils.IOListener;
import com.cy.http.utils.IOUtils;
import com.cy.http.utils.LogUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/12/21 0021.
 */

public class CallDownMultiImpl<T> extends Call<T> {

    private List<Thread> listThread;
    private List<MultiDownFileRunnableImpl> listRunnable;
    private boolean isCallAdded = false;
    private File fileToSave;
    private List<File> list_fileTemp;
    private long size;
    private long totalDowned = 0;
    private String pathToSave;
    private int count_thread = 2;
    private URL url;
    private DownMultiRequest downMultiRequest;
    private long blocksize;
    private List<String> listPath;
    private int percentLast = 0;
    private boolean useCacheSuc = true;

    public CallDownMultiImpl(Request request) {
        super(request);
    }

    @Override
    public void cancel() {
        LogUtils.log("cancel");
        if (callback != null) callback.cancel();
        if (listRunnable != null && !listRunnable.isEmpty()) {
            for (MultiDownFileRunnableImpl multiDownFileRunnable : listRunnable) {
                multiDownFileRunnable.cancel();
            }
        }
        listThread.clear();
        listRunnable.clear();
        HttpUtils.getInstance().removeCall(this);
    }

    @Override
    public void enqueue(final Callback callback) {
        if (HttpUtils.getInstance().isMultiDownloading()) return;
        downMultiRequest = (DownMultiRequest) request;
        pathToSave = downMultiRequest.getPathToSave();
        LogUtils.log("enqueue");
        this.callback = callback;

        listThread = new ArrayList<>();
        listRunnable = new ArrayList<>();
        list_fileTemp = new ArrayList<>();

        LogUtils.log("pathToSave", pathToSave);
        for (int i = 0; i < count_thread; i++) {
            try {
                fileToSave = FileUtils.createFile(pathToSave);
                File fileTemp = FileUtils.createFile(pathToSave + String.valueOf(i));
                list_fileTemp.add(fileTemp);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection httpURLConnection = null;
                try {
                    url = new URL(downMultiRequest.getUrl());
                    httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.setRequestMethod(HttpUtils.METHODS[0]);
                    if (httpURLConnection.getResponseCode() == HttpURLConnection.HTTP_OK)
                        size = httpURLConnection.getContentLength();// 得到服务端返回的文件的大小

                    LogUtils.log("size", size);
                    if (size == 0) {
                        callback.callOnFail("下载文件失败，文件不存在");
                        return;
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                    callback.callOnFail("下载文件失败，" + e.getMessage());
                    return;
                } finally {
                    if (httpURLConnection != null) httpURLConnection.disconnect();
                }

                callback.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //说明已经下载完成
                        if (fileToSave.length() == size) {
                            callback.onSuccess(fileToSave);
                            //删除temp文件
                            delete_temp();
                            return;
                        }
                        if (fileToSave.length() != 0) {
                            //说明没有fileTemp，删除已经下载过但未下载完成的fileToSave
                            for (File file : list_fileTemp) {
                                if (file.length() == 0) {
                                    fileToSave.delete();
                                    downMulti();
                                    return;
                                }
                            }
                            downMultiUseCache();
                            return;
                        }

                        downMulti();

                    }
                });

            }
        }).start();
    }

    private void downMulti() {
        blocksize = size / count_thread;
        LogUtils.log("count_thread", count_thread);
        for (int i = 0; i < count_thread; i++) {
            long startIndex = i * blocksize + i;
            long endIndex;

            if (i == count_thread - 1) {
                // 最后一个线程
                endIndex = size - 1;
            } else {
                endIndex = (i + 1) * blocksize;
            }

            LogUtils.log("startIndex=", startIndex);
            LogUtils.log("endIndex=", endIndex);

            MultiDownFileRunnableImpl multiDownFileRunnable = new MultiDownFileRunnableImpl();
            multiDownFileRunnable.setIndex_start(startIndex);
            multiDownFileRunnable.setIndex_end(endIndex);
            multiDownFileRunnable.setIndex_thread(i);
            multiDownFileRunnable.setFileTemp(list_fileTemp.get(i));
            listRunnable.add(multiDownFileRunnable);

            Thread thread = new Thread(multiDownFileRunnable);
            listThread.add(thread);
        }
        down();
    }

    private void downMultiUseCache() {
        /**
         * 读取记录,线程数量控制为原来的数量
         */
        blocksize = size / count_thread;
        for (int i = 0; i < count_thread; i++) {
            final int ii = i;
            new IOUtils().read2String(list_fileTemp.get(i), new IOListener<String>() {
                @Override
                public void onCompleted(String result) {
                    LogUtils.log("重新下载");
                    long last_result;
                    try {
                        last_result = Long.valueOf(result);
                    } catch (Exception e) {
                        last_result = 0;
                    }
                    long startIndex = last_result + ii;
                    totalDowned += startIndex - (ii * blocksize + ii);
                    long endIndex;

                    if (ii == count_thread - 1) {
                        // 最后一个线程
                        endIndex = size - 1;
                    } else {
                        endIndex = (ii + 1) * blocksize;
                    }
                    LogUtils.log("startIndex___=", startIndex);
                    LogUtils.log("endIndex_____=", endIndex);
                    //比如2个线程，可能有1个线程已经下载完了分配给自己的段
                    if (startIndex < endIndex) {
                        LogUtils.log("startIndex < endIndex");
                        MultiDownFileRunnableImpl multiDownFileRunnable = new MultiDownFileRunnableImpl();
                        multiDownFileRunnable.setIndex_start(startIndex);
                        multiDownFileRunnable.setIndex_end(endIndex);
                        multiDownFileRunnable.setIndex_thread(ii);
                        multiDownFileRunnable.setFileTemp(list_fileTemp.get(ii));
                        listRunnable.add(multiDownFileRunnable);
                        Thread thread = new Thread(multiDownFileRunnable);
                        listThread.add(thread);
                    }

                }

                @Override
                public void onLoading(Object readedPart, int percent, long current, long length) {

                }

                @Override
                public void onInterrupted(Object readedPart, int percent, long current, long length) {
                    useCacheSuc = false;
                    callback.onFail("下载文件失败，过程被中断或者取消");
                }

                @Override
                public void onFail(String errorMsg) {
                    useCacheSuc = false;

                    callback.onFail("下载文件失败," + errorMsg);

                }
            });
            if (!useCacheSuc) break;

        }

        if (useCacheSuc)
            down();

    }

    private void down() {
        for (int i = 0; i < listThread.size(); i++) {
            final MultiDownFileRunnableImpl multiDownFileRunnable = listRunnable.get(i);
            multiDownFileRunnable.setUrl(url);
            multiDownFileRunnable.setFileToSave(fileToSave);
            multiDownFileRunnable.setCallback(new Callback() {
                @Override
                public void onSuccess(Object response) {
                    if (totalDowned == size) {
                        HttpUtils.getInstance().setMultiDownloading(false);
                        callback.onSuccess(fileToSave);
                        delete_temp();
                    }
                }

                @Override
                public void onLoading(Object readedPart, int percent, long current, long length) {

                    //callback.onLoading("", l, index_start, index_end);
                    totalDowned += percent;
                    int per = (int) (totalDowned * 1f / size * 100);
                    //防止不必要的频繁回调
                    if (per - percentLast >= 1)
                        callback.onLoading("", per, totalDowned, size);

                    percentLast = per;
                }

                @Override
                public void onCancel(Object readedPart, int percent, long current, long length) {
                    HttpUtils.getInstance().setMultiDownloading(false);

                    cancel();
                    callback.onCancel(readedPart, percent, current, length);
                }

                @Override
                public void onFail(String errorMsg) {
                    HttpUtils.getInstance().setMultiDownloading(false);

                    cancel();
                    callback.onFail(errorMsg);
                }

                @Override
                public void convertSuccess(long contentLenth,String contentType, InputStream inputStream) {

                }
            });
            multiDownFileRunnable.setDownMultiRequest(downMultiRequest);
            if (!isCallAdded) {
                LogUtils.log("addCall");
                HttpUtils.getInstance().addCall(this);
                isCallAdded = true;
            }
            HttpUtils.getInstance().setMultiDownloading(true);
            listThread.get(i).start();
        }
    }

    private void delete_temp() {
        //删除temp文件
        for (File file : list_fileTemp) {
            file.delete();
        }
    }
}