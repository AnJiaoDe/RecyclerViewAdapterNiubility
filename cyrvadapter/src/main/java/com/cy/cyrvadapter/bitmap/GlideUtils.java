package com.cy.cyrvadapter.bitmap;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

/**
 * Created by cy on 2016/10/9.
 */
public class GlideUtils {
    /*
    glide加载图片
     */
    public static void loadImageByGlide(Context context, String url, ImageView mImageView) {
        Glide.with(context)
                .load(url)
                .dontAnimate()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
//                .placeholder(R.drawable.default_pic)
                .into(mImageView);

    }
    /*
    glide加载图片
     */
    public static void loadImageByGlide(Context context, String url, ImageView mImageView,int default_res) {
        Glide.with(context)
                .load(url)
                .dontAnimate()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(default_res)
                .into(mImageView);

    }

    /*
    glide加载图片并压缩
     */
    public static void loadImageByGlide(Context context, String url, ImageView mImageView, int width, int height) {

        Glide.with(context)
                .load(url)
                .override(width, height)
                .dontAnimate()
//                .placeholder(R.drawable.default_pic)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(mImageView);
    }
    /*
    glide加载图片并压缩
     */
    public static void loadImageByGlide(Context context, String url, ImageView mImageView, int width, int height,int default_res) {

        Glide.with(context)
                .load(url)
                .override(width, height)
                .dontAnimate()
                .placeholder(default_res)

                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(mImageView);
    }


}
