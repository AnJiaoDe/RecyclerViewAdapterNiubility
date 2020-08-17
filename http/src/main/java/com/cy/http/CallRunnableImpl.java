package com.cy.http;


import com.cy.http.utils.LogUtils;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.List;
import java.util.Map;

/**
 * ************************************************************
 * author：cy
 * version：
 * create：2019/04/10 17:13
 * desc：
 * ************************************************************
 */
public class CallRunnableImpl implements Runnable {

    private HttpURLConnection httpURLConnection;
    private InputStream inputStream;
    private Request request;
    private Callback callback;

    private boolean isInterrupted = false;

    public CallRunnableImpl(Request request, Callback callback) {
        this.request = request;
        this.callback = callback;

    }

    public void cancel() {
        isInterrupted = true;
    }

    @Override

    public void run() {
        if (isInterrupted) return;
        try {
            if (request.getParamsFile() != null && !request.getParamsFile().isEmpty()) {
                uploadFile();
            } else {
                request();
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
            callback.callOnFail("网络请求失败，MalformedURLException" + e.getMessage());

        } catch (ProtocolException e) {
            try {
                Field methodField = HttpURLConnection.class.getDeclaredField("method");
                methodField.setAccessible(true);
                methodField.set(httpURLConnection, request.getMethod());
            } catch (NoSuchFieldException e1) {
                callback.callOnFail("网络请求失败，NoSuchFieldException" + e1.getMessage());

            } catch (IllegalAccessException e2) {
                callback.callOnFail("网络请求失败，IllegalAccessException" + e2.getMessage());
            }

        } catch (IOException e) {
            callback.callOnFail("网络请求失败，IOException" + e.getMessage());

        } finally {
            if (httpURLConnection != null) {
                httpURLConnection.disconnect();
            }
        }

    }

    private void request() throws IOException {
        if (isInterrupted) return;

        LogUtils.log("request");
        URL url = new URL(request.getUrl());
        httpURLConnection = (HttpURLConnection) url.openConnection();
        Map<String, String> header = request.getHeader();
        if (header != null) {
            for (String key : header.keySet()) {
                httpURLConnection.setRequestProperty(key, header.get(key));
            }
        }
        // 设置请求方式
        httpURLConnection.setRequestMethod(request.getMethod());
        if (request.getMethod() == HttpUtils.METHODS[1]) {
            httpURLConnection.setDoOutput(true);
            StringBuilder stringBuilder = new StringBuilder();
            Map<String, Object> params = request.getParams();
            if (params != null) {

                for (String key : params.keySet()) {
                    stringBuilder.append(key).append("=").append(params.get(key)).append("&");
                }
            }
            OutputStream outputStream;
            if (params != null && params.size() > 0) {
                stringBuilder.deleteCharAt(stringBuilder.length() - 1);
                outputStream = httpURLConnection.getOutputStream();
                outputStream.write(stringBuilder.toString().getBytes());
                outputStream.flush();
                outputStream.close();
            } else if (request.getBodyJson() != null) {
                httpURLConnection.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
                httpURLConnection.setRequestProperty("Accept", "application/json;charset=UTF-8");

                outputStream = httpURLConnection.getOutputStream();
                outputStream.write(request.getBodyJson().getBytes());
                outputStream.flush();
                outputStream.close();
            } else if (request.getByteProto() != null) {
                httpURLConnection.setRequestProperty("Content-Type", "application/x-protobuf");
                httpURLConnection.setRequestProperty("Accept", "application/x-protobuf");

                outputStream = httpURLConnection.getOutputStream();
                outputStream.write(request.getByteProto());
                outputStream.flush();
                outputStream.close();
            }


        }
        long fileLength = 0;
        File file = null;
        if (callback instanceof FileCallbackImpl) {
            file = new File(((FileCallbackImpl) callback).getPathToSave());
            if (file.exists()) {
                fileLength = file.length();
                /*
                 *
                 * GET /down.zip HTTP/1.0
                 * User-Agent: NetFox
                 * RANGE: bytes=2000070- range从0开始，上次肯定是下载到fileLength-1，所以,下次从fileLength开始
                 */
                // 设置断点续传的开始位置
                httpURLConnection.setRequestProperty("Range", "bytes=" + fileLength + "-");
            }

        }
//        LogUtils.log("contentType",httpURLConnection.getContentType());
        /**
         *说明已经下载完毕
         Range设置为最大值会响应416  Requested Range Not Satisfiable
         */
        if (httpURLConnection.getResponseCode() == 416) {
            callback.callOnSuccess(file);
            return;
        }
        if (isInterrupted) return;

        if (httpURLConnection.getResponseCode() == HttpURLConnection.HTTP_OK || httpURLConnection.getResponseCode() == HttpURLConnection.HTTP_PARTIAL) {
            inputStream = httpURLConnection.getInputStream();
            callback.convertSuccess(fileLength + httpURLConnection.getContentLength(), httpURLConnection.getContentType(), inputStream);
        } else {
            callback.callOnFail(httpURLConnection.getResponseCode() + httpURLConnection.getResponseMessage());
        }
    }

    private void uploadFile() throws IOException {
        if (isInterrupted) return;

        URL url = new URL(request.getUrl());
        httpURLConnection = (HttpURLConnection) url.openConnection();
        Map<String, String> header = request.getHeader();
        if (header != null) {
            for (String key : header.keySet()) {
                httpURLConnection.setRequestProperty(key, header.get(key));
            }
        }
        final String PREFIX = "--";// 必须存在
        final String LINE_END = "\r\n";
        final String BOUNDARY = java.util.UUID.randomUUID().toString();//boundary就是request头和上传文件内容的分隔符
        final String CHARSET = "UTF-8";

        httpURLConnection.setRequestProperty("Connection", "keep-alive");
//        httpURLConnection.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
        httpURLConnection.setRequestProperty("Charset", CHARSET);
        httpURLConnection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + BOUNDARY);
        // 设置请求方式
        httpURLConnection.setRequestMethod(request.getMethod());
        if (request.getMethod() == HttpUtils.METHODS[1]) {
            httpURLConnection.setDoOutput(true);
            DataOutputStream outputStream = new DataOutputStream(httpURLConnection.getOutputStream());
            Map<String, Object> params = request.getParams();
            if (params != null) {
                StringBuilder stringBuilder = new StringBuilder();
                for (String key : params.keySet()) {

                    stringBuilder.append(PREFIX).append(BOUNDARY).append(LINE_END);
                    stringBuilder.append("Content-Disposition: form-data; name=\"").append(key).append("\"").append(LINE_END);
                    stringBuilder.append("Content-Type: text/plain; charset=").append(CHARSET).append(LINE_END);
                    stringBuilder.append("Content-Transfer-Encoding: 8bit").append(LINE_END).append(LINE_END);
                    stringBuilder.append(params.get(key));
                    stringBuilder.append(LINE_END);
                }
                outputStream.write(stringBuilder.toString().getBytes());
            }
            Map<String, List<File>> paramsFile = request.getParamsFile();
            //上传文件
            if (paramsFile != null && paramsFile.size() > 0) {
                for (String key : paramsFile.keySet()) {
                    for (File file : paramsFile.get(key)) {


                        StringBuilder sb = new StringBuilder();
                        LogUtils.log("file", file.length());
                        sb.append(PREFIX).append(BOUNDARY).append(LINE_END);
                        sb.append("Content-Disposition: form-data; name=\"")
                                .append(key).append("\"; filename=\"")
                                .append(file.getName()).append("\"")
                                .append(LINE_END);
                        sb.append("Content-Type:")
                                .append("application/octet-stream;charset=").append(CHARSET)
                                .append(LINE_END);
                        sb.append(LINE_END);// 参数头设置完以后需要两个换行，然后才是参数内容
                        outputStream.write(sb.toString().getBytes());

                        DataInputStream in = new DataInputStream(new FileInputStream(file));
                        byte[] buffer = new byte[1024];
                        int len = 0;
                        while ((len = in.read(buffer)) != -1)
                            outputStream.write(buffer, 0, len); // 写入文件
                        in.close();
                        outputStream.write(LINE_END.getBytes());
                    }
                }

            }
            // 请求结束标志
            outputStream.write(new StringBuilder().append(PREFIX).append(BOUNDARY).append(PREFIX).append(LINE_END).toString().getBytes());
            outputStream.flush();
        }
        if (isInterrupted) return;
        if (httpURLConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
            inputStream = httpURLConnection.getInputStream();
            callback.convertSuccess(httpURLConnection.getContentLength(), httpURLConnection.getContentType(), inputStream);
        } else {
            callback.callOnFail(httpURLConnection.getResponseCode() + httpURLConnection.getResponseMessage());
        }
    }

}


