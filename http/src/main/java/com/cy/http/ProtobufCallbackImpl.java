package com.cy.http;

import java.io.InputStream;

/**
 * ************************************************************
 * author：cy
 * version：
 * create：2019/04/18 09:37
 * desc：
 * ************************************************************
 */

public  abstract class ProtobufCallbackImpl extends Callback<InputStream> {

    @Override
    public void convertSuccess(long contentLenth,String contentType, InputStream inputStream) {
        HttpUtils.getInstance().removeCallByTag(tag);
        callOnSuccess(inputStream);
    }
}
