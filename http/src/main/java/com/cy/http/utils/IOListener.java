package com.cy.http.utils;

/**
 * Created by Administrator on 2018/12/25 0025.
 */

public interface IOListener<T> {
    public void onCompleted(T result);
    public void onLoading(Object readedPart, int percent, long current, long length);
    public void onInterrupted(Object readedPart, int percent, long current, long length);
    public void onFail(String errorMsg);
}
