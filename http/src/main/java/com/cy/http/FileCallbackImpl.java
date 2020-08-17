package com.cy.http;


import com.cy.http.utils.IOListener;

import java.io.File;
import java.io.InputStream;

/**
 * ************************************************************
 * author：cy
 * version：
 * create：2019/04/09 18:32
 * desc：
 * ************************************************************
 */

public abstract class FileCallbackImpl extends Callback<File> {
    private String pathToSave;
    public FileCallbackImpl(String pathToSave) {
        this.pathToSave = pathToSave;
    }

    public FileCallbackImpl() {
    }

    /**
     * 子线程
     *
     * @param contentLenth
     * @param inputStream
     */
    @Override
    public void convertSuccess(long contentLenth,String contentType, InputStream inputStream) {
        ioUtils.read2File(pathToSave, contentLenth, inputStream, new IOListener<File>() {
            @Override
            public void onCompleted(File result) {
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
    public String getPathToSave() {
        return pathToSave;
    }
}
