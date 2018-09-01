package com.cy.cyrvadapter.bitmap;

import android.app.Activity;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

/**
 * Created by cy on 2016/10/9.
 */
public class GlideUtils {
//    ⑧退出Activity（即退出Fragment）：
//
//            07-05 11:28:17.679 16273-16273/com.mypractice E/----A Activity----: onPause
//    07-05 11:28:17.679 16273-16273/com.mypractice E/----A Fragment----: onPause
//    07-05 11:28:18.109 16273-16273/com.mypractice E/----A Activity----: onStop
//    07-05 11:28:18.110 16273-16273/com.mypractice E/----A Fragment----: onStop
//    07-05 11:28:18.110 16273-16273/com.mypractice E/----A Activity----: onDestroy
//    07-05 11:28:18.110 16273-16273/com.mypractice E/----A Fragment----: onDestroyView
//    07-05 11:28:18.111 16273-16273/com.mypractice E/----A Fragment----: onDestroy
//    07-05 11:28:18.111 16273-16273/com.mypractice E/----A Fragment----: onDetach
    /*
    glide加载图片,传入activity对象是为了防止内存泄漏，如果是activity+VP+Fragmnent:一定要传Activity对象,如果传getContext 或者fragment 会内存泄漏
     */
    public static void loadImageByGlide(Activity context, String url, ImageView mImageView) {
        if (context==null)return;
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
    public static void loadImageByGlide(Activity context, String url, ImageView mImageView,int default_res) {
        if (context==null)return;

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
    public static void loadImageByGlide(Activity context, String url, ImageView mImageView, int width, int height) {
        if (context==null)return;

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
    public static void loadImageByGlide(Activity context, String url, ImageView mImageView, int width, int height,int default_res) {
        if (context==null)return;

        Glide.with(context)
                .load(url)
                .override(width, height)
                .dontAnimate()
                .placeholder(default_res)

                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(mImageView);
    }


}
