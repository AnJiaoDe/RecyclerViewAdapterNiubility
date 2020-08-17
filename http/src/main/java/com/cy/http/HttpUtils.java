package com.cy.http;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import com.cy.http.utils.LogUtils;

import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;

/**
 * Created by Administrator on 2018/12/21 0021.
 */

public class HttpUtils {

    public static final String[] METHODS = {
            "GET", "POST", "HEAD", "OPTIONS", "PUT", "DELETE", "TRACE"
    };
    private HttpUtils httpUtils;
    private Handler handler_deliver;              //用于在主线程执行的调度器

    private CopyOnWriteArrayList<Call> list_call;//线程安全
    private ExecutorService executorService= Executors.newCachedThreadPool();//可复用线程的线程池
    private boolean isMultiDownloading=false;
    private HttpUtils() {
        handler_deliver = new Handler(Looper.getMainLooper());
        list_call = new CopyOnWriteArrayList<>();

        // 全局默认信任所有https域名 或 仅添加信任的https域名
        // 使用RequestParams#setHostnameVerifier(...)方法可设置单次请求的域名校验
        HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
            @Override
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        });
    }

    /* 此处使用一个内部类来维护单例 */
    private static class HttpUtilsFactory {
        private static HttpUtils instance = new HttpUtils();
    }


    public synchronized boolean isMultiDownloading() {
        return isMultiDownloading;
    }

    public synchronized void setMultiDownloading(boolean multiDownloading) {
        isMultiDownloading = multiDownloading;
    }

    /* 获取实例 */
    public static HttpUtils getInstance() {

        return HttpUtilsFactory.instance;
    }

    public Handler getHandler_deliver() {
        return handler_deliver;
    }

    /**
     * get请求
     */
    public GetRequestGenerator get(String url) {
        return new GetRequestGenerator().url(url).method(METHODS[0]);
    }
    /**
     * get请求,多线程下载文件
     */
    public DownMultiRequestGenerator downFileMultiThread(String url) {
        return new DownMultiRequestGenerator().url(url).method(METHODS[0]);
    }

//    /**
//     * 下载bitmap
//     */
//    public Imageloader bitmap(Context context) {
//        return new Imageloader(context);
//    }

    /**
     * post请求
     */
    public PostRequestGenerator post(String url) {
        return new PostRequestGenerator().url(url).method(METHODS[1]);
    }

    public void cancelByTag(Object tag) {
        LogUtils.log("cancelByTag");

        if (tag == null) return;
        for (Call call : list_call) {
            LogUtils.log("cancelByTag", call.getRequest().getTag());

            if (call.getRequest().getTag().equals(tag)) {
                LogUtils.log("httputiscancelByTag取消");
                call.cancel();
                list_call.remove(call);
                return;
            }
        }

    }

    public void cancelAll() {

        for (Call call : list_call) {
            LogUtils.log("cancelAll00000000000000");
            call.cancel();
        }
        list_call.clear();

    }

    public ExecutorService getExecutorService() {
        return executorService;
    }

    public void addCall(Call call) {
        list_call.add(call);
    }

    public void removeCall(Call call) {
        list_call.remove(call);
    }

    public void removeCallByTag(Object tag) {
        for (Call call : list_call) {
            if (call.getRequest().getTag().equals(tag)) list_call.remove(call);
        }
    }

    public void runOnUiThread(Runnable runnable) {
        handler_deliver.post(runnable);
    }
}
