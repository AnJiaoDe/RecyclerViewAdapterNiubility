package com.cy.recyclerviewadapter;

import android.content.Context;
import android.net.Uri;
import android.widget.ImageView;

import androidx.annotation.DrawableRes;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;

/**
 * @Description:
 * @Author: cy
 * @CreateDate: 2020/11/10 16:49
 * @UpdateUser:
 * @UpdateDate: 2020/11/10 16:49
 * @UpdateRemark:
 * @Version:
 */

public class GlideUtils {
    public static void getRequestManager(Context context, CallbackRequestManager callbackRequestManager) {
        RequestManager requestManager = null;
        try {
            requestManager = Glide.with(context);
        } catch (Exception e) {

        }
        if (requestManager != null)
            callbackRequestManager.onRequestManagerGeted(requestManager);
    }

    public static void load(Context context, @DrawableRes int idRes, final ImageView imageView) {
        GlideUtils.getRequestManager(context, new GlideUtils.CallbackRequestManager() {
            @Override
            public void onRequestManagerGeted(RequestManager requestManager) {
                requestManager.load(idRes).into(imageView);
            }
        });
    }

    public static void load(Context context, final String str, @DrawableRes int idResPlaceholder, final ImageView imageView) {
        GlideUtils.getRequestManager(context, new GlideUtils.CallbackRequestManager() {
            @Override
            public void onRequestManagerGeted(RequestManager requestManager) {
//                if (str.endsWith(".gif")) {
//                    requestManager.asGif().load(str)
//                            .placeholder(idResPlaceholder).into(imageView);
//                } else if (str.endsWith(".webp")) {
//                    // webp不能用target，否则无法显示，坑逼啊,CustomTarget imageviewtarget也不灵
//                    requestManager.load(str)
//                            .placeholder(idResPlaceholder).into(imageView);
//                } else {
                requestManager.load(str)
                        .placeholder(idResPlaceholder).into(imageView);
//                }
            }
        });
    }
    public static void load(Context context, final Uri uri, @DrawableRes int idResPlaceholder, final ImageView imageView) {
        GlideUtils.getRequestManager(context, new GlideUtils.CallbackRequestManager() {
            @Override
            public void onRequestManagerGeted(RequestManager requestManager) {
                requestManager.load(uri)
                        .placeholder(idResPlaceholder).into(imageView);
            }
        });
    }

    public static void load(Context context, final String str, final ImageView imageView) {
        load(context, str, 0, imageView);
    }
    public static void load(Context context, final Uri uri, final ImageView imageView) {
        load(context, uri, 0, imageView);
    }

    public static interface CallbackRequestManager {
        public void onRequestManagerGeted(RequestManager requestManager);
    }
}
