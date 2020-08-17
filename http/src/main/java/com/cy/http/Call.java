package com.cy.http;//package com.cy.sdkstrategy_master.http;

/**
 * Created by Administrator on 2018/12/21 0021.
 */

public abstract class Call<T> {
    protected Request request;
    protected Callback callback;

    public Call(Request request) {
        this.request = request;
    }

    /**
     * 异步回调执行
     */
    protected void enqueue(Callback<T> callback) {
    }

//    /**
//     * 异步回调执行,一般用于protobuf
//     */
//    protected void enqueueProtobuf(byte[] bytes, Callback<T> callback) {
//    }

    /**
     * 同步请求，开辟了子线程.会阻塞UI线程，但不会引起ANR，注意调用的位置，不要影响UI交互，适合轻量数据
     *
     * @return
     */
    protected void sync(Callback<T> callback) {
    }

    /**
     * 阻塞请求，未开辟线程，需要手动外面套一个子线程，否则会ANR
     *
     * @param callback
     */
    protected void block(Callback<T> callback) {
    }

    protected abstract void cancel();

    protected Request getRequest() {
        return request;
    }

}
