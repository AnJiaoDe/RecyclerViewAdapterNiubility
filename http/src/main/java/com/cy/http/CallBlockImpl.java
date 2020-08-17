package com.cy.http;



/**
 * ************************************************************
 * author：cy
 * version：
 * create：2018/12/28 17:16
 * desc：
 * ************************************************************
 */

public class CallBlockImpl<T> extends Call<T> {

    public CallBlockImpl(Request request) {
        super(request);
    }

    @Override
    public void cancel() {
        HttpUtils.getInstance().removeCall(this);
    }

    @Override
    protected void block(Callback<T> callback) {
        cancel();
        this.callback = callback;
        HttpUtils.getInstance().addCall(this);
        new CallRunnableImpl(request, callback).run();
//        HttpUtils.getInstance().getExecutorService().execute(callThread);
    }

}
