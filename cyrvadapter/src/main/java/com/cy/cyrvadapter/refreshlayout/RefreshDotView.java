package com.cy.cyrvadapter.refreshlayout;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

public class RefreshDotView extends LinearLayout implements IAnimationView {
    private Paint paint;
    private ValueAnimator[] valueAnimators;
    private RoundRectView[] roundRectViews;
    private int space = 2;
    private int height_dot_max = 16;
    private int height_dot_min = 4;
    private int width, height;

    public RefreshDotView(Context context) {
        this(context, null);
    }

    public RefreshDotView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setOrientation(HORIZONTAL);
        setGravity(Gravity.CENTER);

        space = ScreenUtils.dpAdapt(context, space);
        height_dot_max = ScreenUtils.dpAdapt(context, height_dot_max);
        height_dot_min = ScreenUtils.dpAdapt(context, height_dot_min);

        valueAnimators = new ValueAnimator[3];
        roundRectViews = new RoundRectView[3];
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);

        paint.setColor(Color.RED);

        for (int i = 0; i < roundRectViews.length; i++) {
            RoundRectView roundRectView = new RoundRectView(context);
            LayoutParams layoutParams = new LayoutParams(
                    LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            if (i != 0) layoutParams.leftMargin = space;
            layoutParams.gravity = Gravity.CENTER;
            addView(roundRectView, layoutParams);
            roundRectViews[i] = roundRectView;
            int height_start = height_dot_min;
            int height_end = height_dot_max;
            switch (i) {
                case 0:
                    int h = (int) (height_dot_max * 3f / 4);
                    roundRectView.setHeight(h);
                    height_start = h;
                    height_end = height_dot_min;
                    break;
                case 1:
                    roundRectView.setHeight(height_dot_min);
                    break;
                case 2:
                    int h_ = (int) (height_dot_max * 1f / 2);
                    roundRectView.setHeight(h_);
                    height_start = h_;
                    height_end = height_dot_min;
                    break;
            }
            ValueAnimator valueAnimator = ValueAnimator.ofInt(height_start, height_end);
            valueAnimator.setDuration(400);
            valueAnimator.setRepeatCount(ValueAnimator.INFINITE);
            valueAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
            valueAnimator.setRepeatMode(ValueAnimator.REVERSE);
            valueAnimator.addUpdateListener(new MyUpdateListener(roundRectView) {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    int value = (int) animation.getAnimatedValue();
                    getRoundRectView().setHeight(value).requestLayout();
                }
            });
            valueAnimators[i] = valueAnimator;
        }

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(width == 0 ? widthMeasureSpec : MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY),
                height == 0 ? heightMeasureSpec : MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

    }

    public RefreshDotView setWidth(int width) {
        this.width = width;
        return this;
    }

    public RefreshDotView setHeight(int height) {
        this.height = height;
        return this;
    }

    public RefreshDotView setSpace(int space) {
        this.space = space;
        return this;
    }

    public RefreshDotView setColor(int color) {
        paint.setColor(color);
        return this;
    }


    public RefreshDotView setHeight_dot_max(int height_dot_max) {
        this.height_dot_max = height_dot_max;
        return this;
    }

    public RefreshDotView setHeight_dot_min(int height_dot_min) {
        this.height_dot_min = height_dot_min;
        return this;
    }

    @Override
    public void startLoadAnimation() {
        //必须先取消原来的动画再开始新的动画，不然会变形
        cancelAllAnimation();
        for (int i = 0; i < valueAnimators.length; i++) {
            valueAnimators[i].start();
        }
    }

    @Override
    public void stopLoadAnimation() {
        for (int i = 0; i < valueAnimators.length; i++) {
            valueAnimators[i].cancel();
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
        for (int i = 0; i < valueAnimators.length; i++) {
            if (valueAnimators[i].isRunning()) return true;
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
    public View getView() {
        return this;
    }


    private static abstract class MyUpdateListener implements ValueAnimator.AnimatorUpdateListener {
        private RoundRectView roundRectView;

        public MyUpdateListener(RoundRectView roundRectView) {
            this.roundRectView = roundRectView;
        }

        public RoundRectView getRoundRectView() {
            return roundRectView;
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        cancelAllAnimation();
    }
}
