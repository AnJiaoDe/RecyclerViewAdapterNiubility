package com.cy.cyrvadapter.refreshlayout;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Build;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.DecelerateInterpolator;

import androidx.annotation.Nullable;

/**
 * @Description:
 * @Author: cy
 * @CreateDate: 2020/6/17 17:25
 * @UpdateUser:
 * @UpdateDate: 2020/6/17 17:25
 * @UpdateRemark:
 * @Version:
 */
public class RotateLineCircleView extends View implements IAnimationView {
    private Paint paint_out;
    private Paint paint_in;
    private Context context;
    private int startAngle_out = 270;
    private int sweepAngle_out = 0;
    private int startAngle_in = 240;
    private int sweepAngle_in = 0;
    private ValueAnimator valueAnimator_load;
    //    private ValueAnimator valueAnimator_open;
    private ValueAnimator valueAnimator_close;
    private int color = 0xff2a83fc;

    public RotateLineCircleView(Context context) {
        this(context, null);
    }

    public RotateLineCircleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        paint_out = new Paint();
        paint_in = new Paint();

        paint_out.setColor(color);
        paint_out.setStyle(Paint.Style.STROKE);
        paint_out.setAntiAlias(true);
        paint_out.setStrokeWidth(dpAdapt(2));
        paint_out.setStrokeCap(Paint.Cap.ROUND);

        paint_in.setColor(color);
        paint_in.setStyle(Paint.Style.STROKE);
        paint_in.setAntiAlias(true);
        paint_in.setStrokeWidth(dpAdapt(2));
        paint_in.setStrokeCap(Paint.Cap.ROUND);


        valueAnimator_load = ValueAnimator.ofInt(110, 90, 70, 50, 30, 50, 70, 110);
        valueAnimator_load.setDuration(6000);
        valueAnimator_load.setRepeatCount(ValueAnimator.INFINITE);
        valueAnimator_load.setInterpolator(new AccelerateDecelerateInterpolator());
        valueAnimator_load.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int value = (int) animation.getAnimatedValue();
                sweepAngle_out = value;
                sweepAngle_in = 140 - value;
                invalidate();
                startAngle_out = (int) ((startAngle_out + 8) % 360);
                startAngle_in = (int) ((startAngle_in + 8) % 360);
            }

        });
        /**
         * -----------------------------------------------------
         */
//        valueAnimator_open = ValueAnimator.ofFloat(0, 1);
//        valueAnimator_open.setDuration(6000);
//        valueAnimator_open.setRepeatCount(ValueAnimator.INFINITE);
//        valueAnimator_open.setInterpolator(new DecelerateInterpolator());
//        valueAnimator_open.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
//            @Override
//            public void onAnimationUpdate(ValueAnimator animation) {
//                float value = (float) animation.getAnimatedValue();
//                sweepAngle_out -= value * 360;
//                invalidate();
//                if (sweepAngle_out <= 30) valueAnimator_open.cancel();
//            }
//        });
        /**
         * -----------------------------------------------------
         */
        valueAnimator_close = ValueAnimator.ofFloat(0, 1);
        valueAnimator_close.setDuration(10000);
        valueAnimator_close.setInterpolator(new DecelerateInterpolator());
        valueAnimator_close.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float) animation.getAnimatedValue();
                sweepAngle_out += value * 360;
                sweepAngle_in = 60;
                startAngle_in = (int) Math.min(420,startAngle_in+value * 360);
                invalidate();
                if (sweepAngle_out >= 360&&startAngle_in%360==60) valueAnimator_close.cancel();
            }
        });

    }

    /**
     * 恢复初始状态值
     */
    private void restoreParams() {
        startAngle_out = 270;
        sweepAngle_out = 0;
        startAngle_in = 240;
        sweepAngle_in = 0;
    }

    @Override
    public <T extends IAnimationView> T setColor(int color) {
        this.color = color;
        paint_out.setColor(color);
        paint_in.setColor(color);
        return (T) this;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            canvas.drawArc(paint_out.getStrokeWidth(), paint_out.getStrokeWidth(), getWidth() - paint_out.getStrokeWidth(),
                    getHeight() - paint_out.getStrokeWidth(), startAngle_out, sweepAngle_out, false, paint_out);
            canvas.drawArc(dpAdapt(6), dpAdapt(6), getWidth() - dpAdapt(6),
                    getHeight() - dpAdapt(6), startAngle_in, sweepAngle_in, false, paint_in);
        }
    }

    /**
     * --------------------------------------------------------------------------
     */
    public int getStartAngle_out() {
        return startAngle_out;
    }

    public RotateLineCircleView setStartAngle_out(int startAngle_out) {
        this.startAngle_out = startAngle_out;
        return this;
    }

    public int getSweepAngle_out() {
        return sweepAngle_out;
    }

    public RotateLineCircleView setSweepAngle_out(int sweepAngle_out) {
        this.sweepAngle_out = sweepAngle_out;
        return this;

    }

    public int getStartAngle_in() {
        return startAngle_in;
    }

    public RotateLineCircleView setStartAngle_in(int startAngle_in) {
        this.startAngle_in = startAngle_in;
        return this;

    }

    public int getSweepAngle_in() {
        return sweepAngle_in;
    }

    public RotateLineCircleView setSweepAngle_in(int sweepAngle_in) {
        this.sweepAngle_in = sweepAngle_in;
        return this;
    }
//    public void open(AnimatorListenerAdapter animatorListenerAdapter) {
//        stopAnimation();
//        valueAnimator_open.start();
//    }

    private void close(AnimatorListenerAdapter animatorListenerAdapter) {
        //防止多次回调
        valueAnimator_close.removeAllListeners();
        valueAnimator_close.addListener(animatorListenerAdapter);
        valueAnimator_close.start();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        cancelAllAnimation();
    }

    /**
     * --------------------------------------------------------------------------
     */
    @Override
    public View getView() {
        return this;
    }

    @Override
    public boolean isRunning() {
        if (valueAnimator_load.isRunning() || valueAnimator_close.isRunning())
            return true;
        return false;
    }

    @Override
    public void openLoadAnimation(IAnimationViewCallback animationViewCallback) {
//        cancelAllAnimation();
//        valueAnimator_open.start();
    }

    @Override
    public void closeLoadAnimation(final IAnimationViewCallback animationViewCallback) {
        LogUtils.log("closeLoadAnimation");
        cancelAllAnimation();
        close(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                LogUtils.log("closeLoadAnimation onAnimationEnd");
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        animationViewCallback.onLoadClosed();
                    }
                }, 500);
            }
        });
    }

    @Override
    public void startLoadAnimation() {
        LogUtils.log("startLoadAnimation------------------");
        cancelAllAnimation();
        startAngle_out = 270;
        sweepAngle_out = 0;
        startAngle_in = 120;
        sweepAngle_in = 0;
        valueAnimator_load.start();
    }

    @Override
    public void stopLoadAnimation() {
        cancelAllAnimation();
    }

    @Override
    public void cancelAllAnimation() {
        valueAnimator_load.cancel();
        valueAnimator_close.cancel();
    }

    @Override
    public void onDraging(int height_current, int height_load, int height_max) {
        startAngle_in = (int) ((240 + 180 * height_current * 1f / height_load) % 360);
        sweepAngle_out = (int) (360 * height_current * 1f / height_load);
        int k = height_current / height_load % 2 == 0 ? 1 : -1;
        //不能为0，如果为0，转动慢的时候，会画成一个圈，具体原因，小编不明，故+4
        if (k == 1) {
            sweepAngle_in = (int) (60 * 1f / height_load * (height_current % height_load)) + 4;
        } else {
            sweepAngle_in = (int) (60 * 1f / height_load * (height_load - height_current % height_load)) + 4;
        }
        invalidate();
    }


    @Override
    public void onDragClosed() {
        restoreParams();
    }

    /**
     * --------------------------------------------------------------------------------
     */
    public int dpAdapt(float dp) {
        return dpAdapt(dp, 360);
    }

    public int dpAdapt(float dp, float widthDpBase) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        int heightPixels = dm.heightPixels;//高的像素
        int widthPixels = dm.widthPixels;//宽的像素
        float density = dm.density;//density=dpi/160,密度比
        float heightDP = heightPixels / density;//高度的dp
        float widthDP = widthPixels / density;//宽度的dp
        float w = widthDP > heightDP ? heightDP : widthDP;
        return (int) (dp * w / widthDpBase * density + 0.5f);
    }
}
