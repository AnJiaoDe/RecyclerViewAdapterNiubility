//package com.ly.http;
//
//
//import android.graphics.Bitmap;
//
//import BitmapUtils;
//import IOListener;
//import IOUtils;
//import SSLUtils;
//
//import java.io.File;
//import java.io.IOException;
//import java.io.InputStream;
//import java.lang.reflect.Field;
//import java.net.HttpURLConnection;
//import java.net.MalformedURLException;
//import java.net.ProtocolException;
//import java.net.URL;
//import java.util.concurrent.Callable;
//import java.util.concurrent.ExecutionException;
//import java.util.concurrent.FutureTask;
//
///**
// * Created by Administrator on 2018/12/21 0021.
// */
//
//public class CallSyncImpl<T> extends Call<T> {
//    private Request request;
//    //    protected ExecutorService fixedThreadPool;//线程池
//    private IOUtils ioUtils;
//    private ResponseBody<T> syncResponseBody;
//    private Callback callback;
//
//
//    public CallSyncImpl(Request request) {
//        this.request = request;
//    }
//
//
//    @Override
//    public void cancel() {
//        if (ioUtils == null) return;
//        ioUtils.stop();
//
//    }
//
//    @Override
//    public Request getRequest() {
//        return request;
//    }
//
//    @Override
//    public void sync(Callback<T> callback) {
//        if (callback == null) return;
//        this.callback = callback;
//        this.ioUtils = new IOUtils();
//
////        fixedThreadPool = Executors.newFixedThreadPool(1);//线程池
//
//        SyncCallable syncCallable = new SyncCallable();
//        FutureTask<ResponseBody<T>> futureTask = new FutureTask<>(syncCallable);
//        new Thread(futureTask).start();
////        fixedThreadPool.execute(new Thread(futureTask));
//        try {
//            syncResponseBody = futureTask.get();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        } catch (ExecutionException e) {
//            e.printStackTrace();
//        }
//        if (syncResponseBody.getErrorMsg().equals("请求成功")) {
//
//            callback.onSuccess(syncResponseBody.getBody());
//        } else {
//
//            callback.onFail(syncResponseBody.getErrorMsg());
//        }
//
//    }
//
//    private class SyncCallable implements Callable<ResponseBody<T>> {
//
//        @Override
//        public ResponseBody<T> call() {
//            HttpURLConnection httpURLConnection = null;
//
//            InputStream inputStream = null;
//            try {
//                URL url = new URL(request.getUrl());
//                httpURLConnection = (HttpURLConnection) url.openConnection();
//                SSLUtils.trustAllSSL(httpURLConnection);
//
//                //设置出入可用
//                httpURLConnection.setDoInput(true);
//                // 设置输出可用
//                httpURLConnection.setRequestMethod(request.getMethod());
//
//                // 开始连接
//                httpURLConnection.connect();
//
//                if (httpURLConnection.getResponseCode() == 200) {
//                    inputStream = httpURLConnection.getInputStream();
//
//                    if (callback instanceof StringCallbackImpl) {
//
//
//                        ioUtils.read2String(httpURLConnection.getContentLength(), inputStream, new IOListener<String>() {
//                            @Override
//                            public void onCompleted(final String str) {
//                                syncResponseBody = new ResponseBody("请求成功", str);
//                            }
//
//                            @Override
//                            public void onLoading(long current, long length) {
//                                callback.onLoading(current, length);
//                            }
//
//                            @Override
//                            public void onInterrupted() {
//                                syncResponseBody = new ResponseBody("网络请求失败，线程被取消", "");
//
//                            }
//
//                            @Override
//                            public void onFail(String errorMsg) {
//                                syncResponseBody = new ResponseBody(errorMsg, "");
//
//                            }
//                        });
//                    } else if (callback instanceof BitmapCallbackImpl) {
//                        final BitmapCallbackImpl bitmapCallback = (BitmapCallbackImpl) callback;
//
//                            ioUtils.read2ByteArray(httpURLConnection.getContentLength(), inputStream, new IOListener<byte[]>() {
//                                @Override
//                                public void onCompleted(final byte[] result) {
//
//
//                                    Bitmap bitmap = BitmapUtils.decodeBitmapFromBytes(result, bitmapCallback.getReqWidth(), bitmapCallback.getReqHeight());
//                                    if (bitmap != null && bitmap.getWidth() > 0) {
//                                        if (bitmapCallback.getCachePath() != null) {
//                                            File file = new File(((BitmapCallbackImpl) callback).getCachePath());
//                                            if (file != null) {
//                                                BitmapUtils.saveBitmapToFile(bitmap, file);
//                                                syncResponseBody = new ResponseBody("请求成功", bitmap);
//
//                                                return;
//
//                                            }
//                                            syncResponseBody = new ResponseBody("图片下载成功，但缓存失败", bitmap);
//
//                                        }
//                                        syncResponseBody = new ResponseBody("请求成功", bitmap);
//
//
//                                    } else {
//                                        syncResponseBody = new ResponseBody("图片下载失败", bitmap);
//
//                                    }
//
//
//
//                                }
//
//                                @Override
//                                public void onLoading(long current, long length) {
//
//                                    callback.onLoading(current, length);
//
//
//                                }
//
//                                @Override
//                                public void onInterrupted() {
//                                    syncResponseBody = new ResponseBody("网络请求失败，线程被取消", "");
//
//                                }
//
//                                @Override
//                                public void onFail(String errorMsg) {
//                                    syncResponseBody = new ResponseBody(errorMsg, "");
//
//                                }
//                            });
//
//                    }
//
//
//                } else {
//                    syncResponseBody = new ResponseBody(httpURLConnection.getResponseMessage(), "");
//
//                }
//            } catch (MalformedURLException e) {
//                e.printStackTrace();
//                syncResponseBody = new ResponseBody("网络请求失败,"+e.getMessage(), "");
//
//
//            } catch (ProtocolException e) {
//                try {
//                    Field methodField = HttpURLConnection.class.getDeclaredField("method");
//                    methodField.setAccessible(true);
//                    methodField.set(httpURLConnection, request.getMethod());
//                } catch (NoSuchFieldException e1) {
//                    e1.printStackTrace();
//                    syncResponseBody = new ResponseBody("网络请求失败," +e1.getMessage(), "");
//
//                } catch (IllegalAccessException e1) {
//                    e1.printStackTrace();
//                    syncResponseBody = new ResponseBody("网络请求失败," +e1.getMessage(), "");
//                }
//
//            } catch (IOException e) {
//                e.printStackTrace();
//
//                syncResponseBody = new ResponseBody("网络请求失败," + e.getMessage(), "");
//
//
//            } finally {
//                if (httpURLConnection != null) {
//                    httpURLConnection.disconnect();
//                }
//                IOUtils.close(inputStream);
//
//                HttpUtils.getInstance().removeCall(CallSyncImpl.this);
//
//            }
//            return syncResponseBody;
//        }
//    }
//
//
//}
