package com.cy.http;

import android.content.Context;
import android.graphics.Bitmap;

import com.cy.http.utils.BitmapUtils;
import com.cy.http.utils.FileUtils;
import com.cy.http.utils.IOListener;
import com.cy.http.utils.LogUtils;

import java.io.File;
import java.io.InputStream;

/**
 * Created by Administrator on 2018/12/25 0025.
 */

public abstract class BitmapCallbackImpl extends Callback<Bitmap> {

    private Context context;
    private ImageParams imageParams;
    private String cachePath;

    public BitmapCallbackImpl(Context context) {
        this.context = context;
    }

    public void setImageParams(ImageParams imageParams) {
        this.imageParams = imageParams;
    }

    @Override
    public void convertSuccess(final long contentLength, String contentType, InputStream inputStream) {
        cachePath = imageParams.getCachePath();
        Bitmap bitmap = BitmapUtils.decodeBitmapFromStream(inputStream, contentLength, imageParams.getReqWidth(), imageParams.getReqHeight());
        if (bitmap != null && bitmap.getWidth() > 0) {
            callOnSuccess(bitmap);
            Imageloader.getInstance().addBitmapToMemoryCache(imageParams.getUrl(), bitmap);

            if (cachePath == null) {
//                cachePath = imageParams.getCachePathDefault();
                String url = imageParams.getUrl();
                if (url != null && url.contains("/")) {
                    int index_ = url.lastIndexOf("/");

                    String suffix = "";
                    switch (contentType) {
                        case "image/jpeg":
                            suffix = ".jpg";
                            break;
                        default:
                            suffix = ".png";
                            break;
                    }
                    imageParams.setUrl(url);
//            imageParams.setCachePathDefault(context.getCacheDir() + "/" + context.getPackageName() + "/" + name);
                    cachePath = context.getExternalCacheDir() + "/" + context.getPackageName() + "/" + FileUtils.getNameFromUrl(url) + suffix;
                    imageParams.setCachePathDefault(cachePath);
                }
            }

            switch (contentType) {
                case "image/jpeg":
                    BitmapUtils.CompressFileBean compressFileBean = new BitmapUtils.CompressFileBean();
                    compressFileBean.setBitmap(bitmap);
                    compressFileBean.setKb_max(100);
                    compressFileBean.setPathCompressed(cachePath);
                    compressFileBean.setReqWidth(imageParams.getReqWidth());
                    compressFileBean.setReqHeight(imageParams.getReqHeight());
                    BitmapUtils.compressBitmapToFile(compressFileBean, new BitmapUtils.CompressFileCallback() {
                        @Override
                        public void onCompressFileFinished(File file, Bitmap bitmap) {
                            LogUtils.log("onCompressFileFinished");
                        }

                        @Override
                        public void onCompressFileFailed(String errorMsg) {
                            LogUtils.log("onCompressFileFailed", errorMsg);
                        }
                    });
                    break;
                default:
                    BitmapUtils.saveBitmapToFile(bitmap, cachePath);
                    break;
            }

        } else {
            callOnFail("图片下载失败");
        }

//        ioUtils.read2File(cachePath, contentLenth, inputStream, new IOListener<File>() {
//            @Override
//            public void onCompleted(File result) {
//                HttpUtils.getInstance().removeCallByTag(tag);
//                Bitmap bitmap = BitmapUtils.decodeBitmapFromPath(cachePath, imageParams.getReqWidth(), imageParams.getReqHeight());
//                if (bitmap != null && bitmap.getWidth() > 0) {
//                    Imageloader.getInstance().addBitmapToMemoryCache(imageParams.getUrl(),bitmap);
//                    callOnSuccess(bitmap);
//                } else {
//                    callOnFail("图片下载失败");
//                }
//            }
//
//            @Override
//            public void onLoading(Object readedPart, int percent, long current, long length) {
//                callOnLoading(readedPart, percent, current, length);
//
//            }
//
//            @Override
//            public void onInterrupted(Object readedPart, int percent, long current, long length) {
//                callOnCancel(readedPart, percent, current, length);
//
//            }
//
//            @Override
//            public void onFail(String errorMsg) {
//                callOnFail(errorMsg);
//
//            }
//        });
    }

    public ImageParams getImageParams() {
        return imageParams;
    }
}
