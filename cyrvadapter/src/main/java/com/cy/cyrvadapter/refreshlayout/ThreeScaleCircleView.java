package com.cy.cyrvadapter.refreshlayout;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

public class ThreeScaleCircleView extends LinearLayout implements IAnimationView{
    private Paint paint;
    private AnimatorSet[] animatorSets;
    private CircleView[] circleViews;
    private int space = 16;
    private int radius_max = 6;
    private int radius_min = 2;
    private int width, height;

    public ThreeScaleCircleView(Context context) {
        this(context,null);
    }

    public ThreeScaleCircleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setOrientation(HORIZONTAL);
        setGravity(Gravity.CENTER);


        space=ScreenUtils.dpAdapt(context,space);
        radius_max=ScreenUtils.dpAdapt(context,radius_max);
        radius_min=ScreenUtils.dpAdapt(context,radius_min);

        animatorSets = new AnimatorSet[3];
        circleViews = new CircleView[3];
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);

        paint.setColor(Color.RED);

        for (int i = 0; i < circleViews.length; i++) {
            final CircleView circleView = new CircleView(context);
            circleViews[i] = circleView;
            float scale_start = 1f;
            float scale_end = radius_max * 1f / radius_min;
            float alpha_start=1f;
            float alpah_end=0.4f;
            switch (i) {
                case 0:
                    circleView.setRadius(radius_min);
                    break;
                case 1:
                    circleView.setRadius(radius_max);
                    scale_end = radius_min * 1f / radius_max;
                    alpha_start=0.2f;
                    alpah_end=1f;
                    break;
                case 2:
                    circleView.setRadius(radius_max*3f/4);
                    scale_end=1f/4;
                    alpha_start=0.6f;
                    break;
            }
            final ObjectAnimator objectAnimator_scaleX = ObjectAnimator.ofFloat(circleView, "scaleX", scale_start, scale_end, scale_start);
            objectAnimator_scaleX.setRepeatCount(Animation.INFINITE);

            final ObjectAnimator objectAnimator_scaleY = ObjectAnimator.ofFloat(circleView, "scaleY", scale_start, scale_end, scale_start);
            objectAnimator_scaleY.setRepeatCount(Animation.INFINITE);

            final ObjectAnimator objectAnimator_alpha = ObjectAnimator.ofFloat(circleView, "alpha", alpha_start,alpah_end,alpha_start);
            objectAnimator_alpha.setRepeatCount(Animation.INFINITE);

            AnimatorSet animatorSet = new AnimatorSet();
            animatorSet.setDuration(1400);
            animatorSet.playTogether(objectAnimator_scaleX, objectAnimator_scaleY,objectAnimator_alpha);
            animatorSet.setInterpolator(new AccelerateDecelerateInterpolator());

            animatorSets[i] = animatorSet;

            LayoutParams layoutParams = new LayoutParams(
                    LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            if (i != 0) layoutParams.leftMargin = space;
            layoutParams.gravity = Gravity.CENTER;
            addView(circleView, layoutParams);
        }

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(width == 0 ? widthMeasureSpec
                        : MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY),
                height == 0 ? heightMeasureSpec
                        : MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    public ThreeScaleCircleView setWidth(int width) {
        this.width = width;
        return this;
    }

    public ThreeScaleCircleView setHeight(int height) {
        this.height = height;
        return this;
    }

    public ThreeScaleCircleView setSpace(int space) {
        this.space = space;
        return this;
    }

    @Override
    public <T extends IAnimationView> T setColor(int color) {
        paint.setColor(color);
        return (T) this;
    }

    public ThreeScaleCircleView setRadius_max(int radius_max) {
        this.radius_max = radius_max;
        return this;
    }

    public ThreeScaleCircleView setRadius_min(int radius_min) {
        this.radius_min = radius_min;
        return this;
    }

    @Override
    public View getView() {
        return this;
    }

    @Override
    public void startLoadAnimation() {
        //必须先取消原来的动画再开始新的动画，不然会变形
        cancelAllAnimation();
        for (int i = 0; i < animatorSets.length; i++) {
            animatorSets[i].start();
        }
    }

    @Override
    public void stopLoadAnimation() {
        for (int i = 0; i < animatorSets.length; i++) {
            animatorSets[i].cancel();
        }
    }

    @Override
    public void openLoadAnimation(IAnimationViewCallback animationViewCallback) {

    }

    @Override
    public void closeLoadAnimation(IAnimationViewCallback animationViewCallback) {

    }

    @Override
    public void cancelAllAnimation() {
        stopLoadAnimation();
    }

    @Override
    public boolean isRunning() {
        for (int i = 0; i < animatorSets.length; i++) {
            if (animatorSets[i].isRunning()) return true;
        }
        return false;
    }

    @Override
    public void onDraging(int height_current, int height_load, int height_max) {

    }

    @Override
    public void onDragClosed() {

    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        cancelAllAnimation();
    }
}
