package com.cy.cyrvadapter.refreshlayout;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

/**
 * Created by Administrator on 2018/11/20 0020.
 */

public class ScreenUtils {

    public static int getStatusBarHeight(Context context) {
        Resources resources = context.getResources();
        int resourceId = resources.getIdentifier("status_bar_height", "dimen", "android");
        return resources.getDimensionPixelSize(resourceId);
    }

    public static int getNavigationBarHeight(Context context) {
        Resources resources = context.getResources();
        int resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android");
        return resources.getDimensionPixelSize(resourceId);
    }

    public static int getScreenWidth(Context context) {

        DisplayMetrics displayMetrics = new DisplayMetrics();

        WindowManager wm = (WindowManager) context.getSystemService("window");
        if (Build.VERSION.SDK_INT <= 17) {
            wm.getDefaultDisplay().getMetrics(displayMetrics);
        } else {
            wm.getDefaultDisplay().getRealMetrics(displayMetrics);
        }
        return displayMetrics.widthPixels;
    }

    public static int getScreenHeight(Context context) {
        DisplayMetrics displayMetrics = new DisplayMetrics();

        WindowManager wm = (WindowManager) context.getSystemService("window");
        if (Build.VERSION.SDK_INT <= 17) {
            wm.getDefaultDisplay().getMetrics(displayMetrics);
        } else {
            wm.getDefaultDisplay().getRealMetrics(displayMetrics);
        }
        return displayMetrics.heightPixels;
    }

    public static int setYStart(Context context, float y) {

//        if (isGroove(context)) {

        return (int) (y * getScreenHeight(context)) + getStatusBarHeight(context);
//        } else {
//            return (int) (y * getScreenHeight(context));
//
//
//        }
    }

    /**
     * 获取当前界面可视区域的高度
     *
     * @param activity
     * @return
     */
    public static int getVisibleFrameHeight(Activity activity) {
        Rect r = new Rect();
        //获取当前界面可视部分
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(r);

//        if (isGroove(activity)) {

//            return r.bottom - r.top - getStatusBarHeight(activity);
//        } else {
        return r.bottom - r.top;
//
//        }
    }

    /**
     * 获取当前界面可视区域的宽度
     *
     * @param activity
     * @return
     */
    public static int getVisibleFrameWidth(Activity activity) {
        Rect r = new Rect();
        //获取当前界面可视部分
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(r);
        return r.right - r.left;
    }


    /**
     * 将px值转换为dip或dp值，保证尺寸大小不变
     *
     * @param pxValue
     * @param scale   （DisplayMetrics类中属性density）
     * @return
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 将dp值转换为px值，保证尺寸大小不变
     *
     * @return
     */
    public static int dpAdapt(Context context, float dp) {

        return dpAdapt(context,dp,360);
    }
    public static int dpAdapt(Context context, float dp, float widthDpBase) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        int heightPixels = dm.heightPixels;//高的像素
        int widthPixels = dm.widthPixels;//宽的像素
//        int densityDpi = dm.densityDpi;//dpi
//        float xdpi = dm.xdpi;//xdpi
//        float ydpi = dm.ydpi;//ydpi
        float density = dm.density;//density=dpi/160,密度比
//        float scaledDensity = dm.scaledDensity;//scaledDensity=dpi/160 字体缩放密度比
        float heightDP = heightPixels / density;//高度的dp
        float widthDP = widthPixels / density;//宽度的dp
        float w = widthDP > heightDP ? heightDP : widthDP;
//        final float scale = activity.getResources().getDisplayMetrics().density;
        return (int) (dp * w / widthDpBase * density + 0.5f);
    }

    /**
     * 将sp值转换为px值，保证尺寸大小不变
     *
     * @return
     */
    public static int spAdapt(Context context, float sp) {

        return spAdapt(context,sp,360);
    }
    public static int spAdapt(Context context, float sp, float widthDpBase) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        int heightPixels = dm.heightPixels;//高的像素
        int widthPixels = dm.widthPixels;//宽的像素
//        int densityDpi = dm.densityDpi;//dpi
//        float xdpi = dm.xdpi;//xdpi
//        float ydpi = dm.ydpi;//ydpi
        float density = dm.density;//density=dpi/160,密度比
//        float scaledDensity = dm.scaledDensity;//scaledDensity=dpi/160 字体缩放密度比
        float heightDP = heightPixels / density;//高度的dp
        float widthDP = widthPixels / density;//宽度的dp
        float w = widthDP > heightDP ? heightDP : widthDP;
//        final float scale = activity.getResources().getDisplayMetrics().density;
        return (int) (sp * w / widthDpBase + 0.5f);
    }
//    public static int dp2px(Context context,int dimen_resID) {
//        final float scale = context.getResources().getDisplayMetrics().density;
//        return (int) (context.getResources().getDimension(dimen_resID) * scale + 0.5f);
//    }

    /**
     * 将px值转换为sp值，保证文字大小不变
     *
     * @param pxValue
     * @param fontScale （DisplayMetrics类中属性scaledDensity）
     * @return
     */
    public static int px2sp(Context context, float pxValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

    /**
     * 计算指定的 View 在屏幕中的坐标。
     */
    public static int[] getViewScreenLocation(View view) {
        int[] location = new int[2];
        // 获取控件在屏幕中的位置，返回的数组分别为控件左顶点的 x、y 的值
        view.getLocationOnScreen(location);
        return location;
    }

    /**
     * 计算指定的 View 在屏幕中的范围。
     */
    public static RectF getViewScreenRectF(View view) {
        int[] location = new int[2];
        // 获取控件在屏幕中的位置，返回的数组分别为控件左顶点的 x、y 的值
        view.getLocationOnScreen(location);
        return new RectF(location[0], location[1], location[0] + view.getWidth(),
                location[1] + view.getHeight());
    }

    /**
     * 判断触摸点是否在控件内
     */
    public static boolean isInViewRange(View view, MotionEvent event) {

        // MotionEvent event;
        // event.getX(); 获取相对于控件自身左上角的 x 坐标值
        // event.getY(); 获取相对于控件自身左上角的 y 坐标值
        float x = event.getRawX(); // 获取相对于屏幕左上角的 x 坐标值
        float y = event.getRawY(); // 获取相对于屏幕左上角的 y 坐标值

        // View view;
        RectF rect = getViewScreenRectF(view);
        return rect.contains(x, y);
    }


    public static float getDPWidth(Context context) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        if (Build.VERSION.SDK_INT <= 17) {
            wm.getDefaultDisplay().getMetrics(displayMetrics);
        } else {
            wm.getDefaultDisplay().getRealMetrics(displayMetrics);
        }
        int widthPixels = displayMetrics.widthPixels;//宽的像素
        float density = displayMetrics.density;//density=dpi/160,密度比
        float widthDP = widthPixels / density;//宽度的dp

        return widthDP;
    }

    public static float getDPHeight(Context context) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        if (Build.VERSION.SDK_INT <= 17) {
            wm.getDefaultDisplay().getMetrics(displayMetrics);
        } else {
            wm.getDefaultDisplay().getRealMetrics(displayMetrics);
        }
        int heightPixels = displayMetrics.heightPixels;//高的像素
        float density = displayMetrics.density;//density=dpi/160,密度比
        float heightDP = heightPixels / density;//高度的dp
        return heightDP;
    }


}

