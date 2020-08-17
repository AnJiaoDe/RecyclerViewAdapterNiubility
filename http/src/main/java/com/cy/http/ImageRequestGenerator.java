package com.cy.http;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.provider.ContactsContract;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.cy.http.utils.BitmapUtils;
import com.cy.http.utils.FileUtils;
import com.cy.http.utils.LogUtils;


/**
 * ************************************************************
 * author：cy
 * version：
 * create：2019/04/11 16:32
 * desc：
 * ************************************************************
 */

public class ImageRequestGenerator extends BaseRequestGenerator<ImageRequestGenerator> {
    private ImageView iv;
    private Context context;
    private ImageView.ScaleType scaleType = ImageView.ScaleType.CENTER_INSIDE;
    private ImageParams imageParams;

    public ImageRequestGenerator(Context context) {
        this.context = context;
        this.method(HttpUtils.METHODS[0]);
        imageParams = new ImageParams();
    }

    @Override
    public ImageRequestGenerator url(final String url) {
        imageParams.setUrl(url);
        return super.url(url);
    }

    public ImageRequestGenerator scaleType(ImageView.ScaleType scaleType) {
        this.scaleType = scaleType;
        return this;
    }

    public ImageRequestGenerator cachePath(String cachePath) {
        imageParams.setCachePath(cachePath);
        return this;
    }

    public ImageRequestGenerator cache(boolean cache) {
        imageParams.setCache(cache);
        return this;
    }

    public ImageRequestGenerator width(int reqWidth) {
        imageParams.setReqWidth(reqWidth);
        return this;
    }

    public ImageRequestGenerator height(int reqHeight) {
        imageParams.setReqHeight(reqHeight);
        return this;
    }

    public ImageRequestGenerator into(ImageView iv) {
        this.iv = iv;
        iv.addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() {
            @Override
            public void onViewAttachedToWindow(View v) {

            }

            @Override
            public void onViewDetachedFromWindow(View v) {
                LogUtils.log("onViewDetachedFromWindow");

                Imageloader.getInstance().cancelLoad(url);
            }
        });
        imageParams.setImageView(iv);
        return this;
    }

    public void cancel() {

    }

    public ImageRequestGenerator load() {
        LogUtils.log("ImageRequestGeneratorload");
        if (iv == null)
            throw new RuntimeException("请配置对应的Imageview");
        if (checkCache(null)) return this;
        LogUtils.log("newDownloadPic");
        BitmapCallbackImpl bitmapCallback = new BitmapCallbackImpl(context) {
            @Override
            public void onSuccess(Bitmap response) {
//                iv.setScaleType(scaleType);

                computeSize(iv, response);

                iv.setImageBitmap(response);

            }

            @Override
            public void onLoading(Object readedPart, int percent, long current, long length) {

            }

            @Override
            public void onCancel(Object readedPart, int percent, long current, long length) {

            }

            @Override
            public void onFail(String errorMsg) {
                LogUtils.log(errorMsg);
            }
        };
        bitmapCall(bitmapCallback);

        return this;
    }


    public ImageRequestGenerator load(BitmapCallbackImpl bitmapCallback) {
        bitmapCallback.setImageParams(imageParams);
        if (checkCache(bitmapCallback)) return this;
        bitmapCall(bitmapCallback);
        return this;
    }

    private void bitmapCall(BitmapCallbackImpl bitmapCallback) {
        LogUtils.log("bitmapCall");
        bitmapCallback.setImageParams(imageParams);
        Call call = new CallEnqueueImpl(generateRequest(tag));
        HttpUtils.getInstance().addCall(call);
        call.enqueue(bitmapCallback);
    }

    private boolean checkCache(BitmapCallbackImpl bitmapCallback) {
        LogUtils.log("checkCache");
        if (imageParams.isCache()) {
            LogUtils.log("checkCache___");

            Bitmap bitmap = Imageloader.getInstance().getBitmapFromMemoryCache(imageParams.getUrl());
            if (bitmap != null && bitmap.getWidth() > 0) {
                LogUtils.log("图片缓存");
                if (iv != null && bitmapCallback == null) {
//                    iv.setScaleType(scaleType);
                    computeSize(iv, bitmap);
                    iv.setImageBitmap(bitmap);
                }
                if (bitmapCallback != null) bitmapCallback.onSuccess(bitmap);
                return true;
            }

            String pathCached = "";
            pathCached = imageParams.getCachePath() != null ? imageParams.getCachePath() : imageParams.getCachePathDefault();
            if (pathCached != null) {
                Bitmap bitmap_file = BitmapUtils.decodeBitmapFromPath(pathCached, imageParams.getReqWidth(), imageParams.getReqHeight());
//                Bitmap bitmap_file = BitmapFactory.decodeFile(pathCached);
                if (bitmap_file != null && bitmap_file.getWidth() > 0) {
                    LogUtils.log("图片缓存file");
                    if (iv != null && bitmapCallback == null) {
//                        iv.setScaleType(scaleType);
                        computeSize(iv, bitmap_file);
                        iv.setImageBitmap(bitmap_file);
                    }
                    if (bitmapCallback != null) bitmapCallback.onSuccess(bitmap_file);
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public Request generateRequest(Object tag) {
        Request.Builder builder = new Request.Builder();
        return builder.setTag(tag)
                .setUrl(url)
                .setMethod(method)
                .build();
    }

    private void computeSize(ImageView iv, Bitmap bitmap) {
//        ViewGroup.LayoutParams layoutParams = iv.getLayoutParams();
//        switch (layoutParams.width) {
//            case ViewGroup.LayoutParams.MATCH_PARENT:
//                layoutParams.height = (int) (iv.getMeasuredWidth() * bitmap.getHeight() * 1f / bitmap.getWidth());
//                break;
//            case ViewGroup.LayoutParams.WRAP_CONTENT:
//                switch (layoutParams.height) {
//                    case ViewGroup.LayoutParams.MATCH_PARENT:
//                        layoutParams.width = (int) (iv.getMeasuredHeight() * bitmap.getWidth() * 1f / bitmap.getHeight());
//                        break;
//                    case ViewGroup.LayoutParams.WRAP_CONTENT:
//                        break;
//                    default:
//                        layoutParams.width = (int) (layoutParams.height * bitmap.getWidth() * 1f / bitmap.getHeight());
//                        break;
//                }
//                break;
//            default:
//                layoutParams.height = (int) (layoutParams.width * bitmap.getHeight() * 1f / bitmap.getWidth());
//                break;
//        }
//        iv.requestLayout();
    }

}
