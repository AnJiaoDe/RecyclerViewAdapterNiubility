package com.cy.http;//package com.cy.sdkstrategy_master.http;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2018/12/21 0021.
 */

public abstract class BaseRequestGenerator<T extends BaseRequestGenerator> {
    protected String baseUrl;
    public String url;
    protected String method;
    protected Map<String, Object> params = new HashMap<>();//添加的param
    protected Map<String, String> header = new HashMap<>();//添加的header
    protected Object tag = "tag";

    public T url(String url) {
        this.url = url;
        this.baseUrl = url;
        return (T) this;
    }

    public T method(String method) {
        this.method = method;
        return (T) this;
    }

    public T header(String key, String value) {
        this.header.put(key, value);
        return (T) this;
    }

    public T headers(Map<String, String> header) {
        this.header.clear();
        this.header = header;
        return (T) this;
    }


    public T param(String key, Object value) {
        params.put(key, value);
        return (T) this;

    }

    public T params(Map<String, Object> params) {
        this.params.clear();
        this.params = params;
        return (T) this;

    }

    public T tag(Object tag) {
        if (tag != null) this.tag = tag;
        return (T) this;

    }

    /**
     * 异步请求
     *
     * @param callback
     */
    public void enqueue(Callback callback) {
        if (callback == null) return;

        Call call = new CallEnqueueImpl(generateRequest(tag));
        callback.setTag(tag);
        call.enqueue(callback);

    }


    /**
     * 异步请求
     */
    public void enqueue() {

        final Call call = new CallEnqueueImpl(generateRequest(tag));
        Callback callback = new Callback() {
            @Override
            public void onSuccess(Object response) {

            }

            @Override
            public void onLoading(Object readedPart, int percent, long current, long length) {

            }

            @Override
            public void onCancel(Object readedPart, int percent, long current, long length) {

            }

            @Override
            public void onFail(String errorMsg) {

            }

            @Override
            public void convertSuccess(long contentLenth,String contentType, InputStream inputStream) {

            }
        };
        callback.setTag(tag);
        call.enqueue(callback);

    }
//    /**
//     * 同步请求.阻塞，注意调用的位置，不要影响UI交互，适合轻量数据
//     *
//     * @return
//     */
//
//    public void sync(Callback callback) {
//        if (callback == null) return;
//        Call call = new CallSyncImpl(generateRequest(tag));
//        HttpUtils.getInstance().addCall(call);
//        call.sync(callback);
//    }
//

    /**
     * 未开辟线程
     *
     * @param callback
     */
    public void block(Callback callback) {
        if (callback == null) return;
        Call call = new CallBlockImpl(generateRequest(tag));
        callback.setTag(tag);
        call.block(callback);
    }

    /**
     * 根据不同的请求方式，
     */
    public abstract Request generateRequest(Object tag);
}
