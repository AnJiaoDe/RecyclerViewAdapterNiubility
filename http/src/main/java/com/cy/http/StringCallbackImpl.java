package com.cy.http;


import com.cy.http.utils.IOListener;

import java.io.InputStream;

/**
 * Created by Administrator on 2018/12/22 0022.
 */

public abstract class StringCallbackImpl extends Callback<String> {

    @Override
    public void convertSuccess(long contentLenth,String contentType, InputStream inputStream) {
        ioUtils.read2String(contentLenth, inputStream, new IOListener<String>() {
            @Override
            public void onCompleted(String result) {
                HttpUtils.getInstance().removeCallByTag(tag);
                callOnSuccess(result);
            }

            @Override
            public void onLoading(Object readedPart, int percent, long current, long length) {

                callOnLoading(readedPart, percent, current, length);
            }

            @Override
            public void onInterrupted(Object readedPart, int percent, long current, long length) {

                callOnCancel(readedPart, percent, current, length);
            }

            @Override
            public void onFail(String errorMsg) {
                callOnFail(errorMsg);
            }
        });
    }
}
