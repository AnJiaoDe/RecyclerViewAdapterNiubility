package com.cy.http;


import com.cy.http.utils.IOUtils;

/**
 * Created by Administrator on 2018/12/21 0021.
 */

public abstract class Callback<T> implements Converter<T> {
    protected Object tag = "tag";

    protected IOUtils ioUtils = new IOUtils();

    public void setTag(Object tag) {
        this.tag = tag;
    }

    /**
     * 对返回数据进行操作的回调，使用enqueue异步请求时，是UI线程
     */
    public abstract void onSuccess(T response);


    /**
     * 进度，使用enqueue异步请求时，是UI线程
     */
    public abstract void onLoading(Object readedPart, int percent, long current, long length);

    /**
     * 进度，使用enqueue异步请求时，是UI线程
     */
    public abstract void onCancel(Object readedPart, int percent, long current, long length);

    /**
     * 请求失败，响应错误等，都会回调该方法，使用enqueue异步请求时，是UI线程
     */
    public abstract void onFail(String errorMsg);

    @Override
    public final void cancel() {
        ioUtils.stop();
    }


    /**
     * 取消，使用enqueue异步请求时，是UI线程
     */


    public void callOnSuccess(final T response) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                onSuccess(response);
            }
        });
    }

    public void callOnLoading(final Object readedPart, final int percent, final long current, final long length) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                onLoading(readedPart, percent, current, length);

            }
        });
    }

    public void callOnFail(final String ErrorMsg) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                onFail(ErrorMsg);

            }
        });
    }

    public void callOnCancel(final Object readedPart, final int percent, final long current, final long length) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                onCancel(readedPart, percent, current, length);

            }
        });
    }

    public void runOnUiThread(Runnable run) {
        HttpUtils.getInstance().getHandler_deliver().post(run);
    }


}
