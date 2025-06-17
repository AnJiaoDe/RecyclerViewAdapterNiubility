package com.cy.rvadapterniubility.swipelayout;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.IntEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.animation.DecelerateInterpolator;
import android.widget.LinearLayout;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by cy on 11/24/14.
 */
public class SwipeLayout extends LinearLayout {
    private View contentView;
    private View sideView;
    private int dragDistance;
    private boolean isOpened = false;
    private int downX, downY;
    private VelocityTracker velocityTracker;
    private float velocity_x;
    private float velocity_x_limit;
    private int duration_open = 200;
    private int duration_close = 200;
    private OnSwipeListener onSwipeListener;

    public SwipeLayout(Context context) {
        this(context, null);
    }

    public SwipeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOrientation(HORIZONTAL);
        velocity_x_limit = (int) (ViewConfiguration.get(context).getScaledMaximumFlingVelocity() * 0.2f);

        contentView = new View(context);
        sideView = new View(context);
        setContentView(contentView);
        setSideView(sideView);

    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        if (getChildCount() > 4)
            throw new RuntimeException("Exception:You can add only one contentView and one sideView in " + getClass().getName());
        View view = getChildAt(2);
        if (view != null) {
            contentView = view;
            setContentView(contentView);
        }
        View view_ = getChildAt(3);
        if (view_ != null) {
            sideView = view_;
            setSideView(sideView);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //说明SwipeLayout在XML中定义的宽度为Wrap_content
        switch (MeasureSpec.getMode(heightMeasureSpec)) {
            case MeasureSpec.AT_MOST:
                /**
                 * 对应：
                 * <com.cy.swipelayout.SwipeLayout xmlns:android="http://schemas.android.com/apk/res/android"
                 *     android:id="@+id/sl"
                 *     android:layout_width="match_parent"
                 *     android:background="#eee"
                 *     android:layout_height="wrap_content">
                 */
                break;
            case MeasureSpec.UNSPECIFIED:
                break;
            case MeasureSpec.EXACTLY:
                /**
                 * 对应：
                 * <com.cy.swipelayout.SwipeLayout xmlns:android="http://schemas.android.com/apk/res/android"
                 *     android:id="@+id/sl"
                 *     android:layout_width="match_parent"
                 *     android:background="#eee"
                 *     android:layout_height="match_parent">
                 * 或者：
                 * <com.cy.swipelayout.SwipeLayout xmlns:android="http://schemas.android.com/apk/res/android"
                 *     android:id="@+id/sl"
                 *     android:layout_width="match_parent"
                 *     android:background="#eee"
                 *     android:layout_height="100dp">
                 */
                break;
            default:
                break;

        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int size_width = MeasureSpec.getSize(widthMeasureSpec);
        int size_height = MeasureSpec.getSize(heightMeasureSpec);
        contentView.measure(MeasureSpec.makeMeasureSpec(size_width, MeasureSpec.EXACTLY),
                MeasureSpec.makeMeasureSpec(size_height, MeasureSpec.EXACTLY));
        sideView.measure(getChildMeasureSpec(widthMeasureSpec, 0, sideView.getLayoutParams().width),
                MeasureSpec.makeMeasureSpec(size_height, MeasureSpec.EXACTLY));
        dragDistance = sideView.getMeasuredWidth();
    }


    public void setContentView(View view) {
        removeView(contentView);
//        LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        this.contentView = view;
        addView(contentView, 0);
    }

    public void setSideView(View view) {
        removeView(sideView);
        this.sideView = view;
        addView(sideView, 1);
    }

    public void setVelocity_x_limit(float velocity_x_limit) {
        this.velocity_x_limit = velocity_x_limit;
    }

    public void setDuration_open(int duration_open) {
        this.duration_open = duration_open;
    }

    public void setDuration_close(int duration_close) {
        this.duration_close = duration_close;
    }

    public View getContentView() {
        return contentView;
    }

    public View getSideView() {
        return sideView;
    }

    public void onOpening(float rate) {

    }

    public void onClosing(float rate) {

    }

    public void open() {
        final int left_content = contentView.getLeft();
        final int right_content = contentView.getRight();
        final int left_side = sideView.getLeft();
        final int right_side = sideView.getRight();
        ValueAnimator valueAnimator = ValueAnimator.ofInt(0, -dragDistance - left_content);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int value = (int) animation.getAnimatedValue();

                contentView.setLeft(left_content + value);
                contentView.setRight(right_content + value);
                sideView.setLeft(left_side + value);
                sideView.setRight(right_side + value);

                onOpening(animation.getAnimatedFraction());
            }
        });
        valueAnimator.setDuration(duration_open);
        valueAnimator.setInterpolator(new DecelerateInterpolator());
        valueAnimator.setEvaluator(new IntEvaluator());
        valueAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                isOpened = true;
                if (onSwipeListener != null) onSwipeListener.onOpened();
            }
        });
        valueAnimator.start();
    }

    public void close(final OnSwipeListener onSwipeListener) {
        final int left_content = contentView.getLeft();
        final int right_content = contentView.getRight();
        final int left_side = sideView.getLeft();
        final int right_side = sideView.getRight();
        ValueAnimator valueAnimator = ValueAnimator.ofInt(0, -left_content);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int value = (int) animation.getAnimatedValue();

                contentView.setLeft(left_content + value);
                contentView.setRight(right_content + value);
                sideView.setLeft(left_side + value);
                sideView.setRight(right_side + value);
                onClosing(animation.getAnimatedFraction());
            }
        });
        valueAnimator.setDuration(duration_close);
        valueAnimator.setInterpolator(new DecelerateInterpolator());
        valueAnimator.setEvaluator(new IntEvaluator());
        valueAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                isOpened = false;
                if (onSwipeListener != null) onSwipeListener.onClosed();
            }
        });
        valueAnimator.start();
    }

    public void close() {
        close(null);
    }

    public boolean isOpened() {
        return isOpened;
    }

    private void updateLayout(int distanceX) {
        contentView.setLeft(contentView.getLeft() + distanceX);
        contentView.setRight(contentView.getRight() + distanceX);
        sideView.setLeft(sideView.getLeft() + distanceX);
        sideView.setRight(sideView.getRight() + distanceX);
        if (onSwipeListener != null) onSwipeListener.onScrolled(distanceX);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downX = (int) ev.getX();
                downY = (int) ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:

                int moveX = (int) ev.getX();
                int moveY = (int) ev.getY();
                int distanceX = moveX - downX;
                int distanceY = moveY - downY;
                if (Math.abs(distanceX) > Math.abs(distanceY)) {
                    return true;
                }
                break;
        }
        return false;
    }


    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (velocityTracker == null) velocityTracker = VelocityTracker.obtain();
        velocityTracker.addMovement(ev);
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downX = (int) ev.getX();
                break;
            case MotionEvent.ACTION_MOVE:
                int moveX = (int) ev.getX();
                int distanceX = moveX - downX;
                downX = moveX;
                //右滑
                if (distanceX > 0 && contentView.getRight() != getWidth()) {
                    distanceX = Math.min(distanceX, getWidth() - contentView.getRight());

                    updateLayout(distanceX);

                    break;
                }
                //左滑
                if (distanceX < 0 && contentView.getRight() != getWidth() - dragDistance) {
                    distanceX = Math.max(distanceX, -dragDistance - contentView.getLeft());

                    updateLayout(distanceX);

                    break;
                }
                break;
            case MotionEvent.ACTION_UP:
                velocityTracker.computeCurrentVelocity(1000);
                velocity_x = velocityTracker.getXVelocity();

                //右滑
                if (velocity_x > velocity_x_limit) {
                    close();
                } else if (-velocity_x > velocity_x_limit) {
                    open();
                } else if (contentView.getLeft() >= -dragDistance * 1f / 2) {
                    close();
                } else if (contentView.getLeft() < -dragDistance * 1f / 2) {
                    open();
                }

                if (velocityTracker != null) {
                    velocityTracker.recycle();
                    velocityTracker = null;
                }
                break;
            case MotionEvent.ACTION_CANCEL:
                if (velocityTracker != null) {
                    velocityTracker.recycle();
                    velocityTracker = null;
                }
                break;
        }
        return true;
    }


    public void setOnSwipeListener(OnSwipeListener onSwipeListener) {
        this.onSwipeListener = onSwipeListener;
    }
}
