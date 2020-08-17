package com.cy.http;

import java.io.InputStream;

/**
 * ************************************************************
 * author：cy
 * version：
 * create：2019/04/10 15:03
 * desc：
 * ************************************************************
 */

public interface Converter<T> {
    /**
     * 对返回数据进行解析的回调，使用enqueue异步请求时，是子线程
     */
    public void convertSuccess(long contentLength,String contentType, InputStream inputStream);

    /***
     * 取消请求
     */
    public void cancel();
}
