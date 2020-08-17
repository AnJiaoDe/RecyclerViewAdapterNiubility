package com.cy.http;


/**
 * Created by Administrator on 2018/12/21 0021.
 */

public class CallEnqueueImpl<T> extends Call<T> {


    private CallRunnableImpl runnable;


    public CallEnqueueImpl(Request request) {
        super(request);
    }

    @Override
    public void cancel() {
        if (callback != null) callback.cancel();
        if (runnable != null) runnable.cancel();
        HttpUtils.getInstance().removeCall(this);
    }

    @Override
    public void enqueue(final Callback callback) {
        cancel();
        this.callback = callback;
        runnable =new CallRunnableImpl(request, callback);
        HttpUtils.getInstance().addCall(this);
        HttpUtils.getInstance().getExecutorService().execute(runnable);
//        HttpUtils.getInstance().getExecutorService().execute(callThread);

//        HttpUtils.getInstance().getExecutorService().execute(callThread);


    }

}
