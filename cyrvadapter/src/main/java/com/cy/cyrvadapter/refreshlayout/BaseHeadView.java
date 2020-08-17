package com.cy.cyrvadapter.refreshlayout;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.IntEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.view.Gravity;
import android.view.ViewConfiguration;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;

public class BaseHeadView extends FrameLayout implements IHeadView {
    private Context context;
    private IAnimationView animationView;
    private RefreshCallback callback;
    private int heightMax;
    private int heightRefresh;
    private int velocity_y_limit = 1000;
    private float y_dragUp_ratio = 1f / 2;
    private float y_dragDown_ratio = 1f / 2;
    //    private float height_refresh_ratio ;
    private int duration_refresh_start = 100;

    private int duration_refresh_finish = 300;

    private boolean isRefreshing = false;

    public BaseHeadView(Context context) {
        super(context);
        this.context = context;
        LayoutParams layoutParams_child = new LayoutParams(ScreenUtils.dpAdapt(context, 24), ScreenUtils.dpAdapt(context, 24));
        layoutParams_child.gravity = Gravity.CENTER;
        animationView = new RotateLineCircleView(context);
        addView(animationView.getView(), layoutParams_child);
        heightMax = ScreenUtils.dpAdapt(context, 800);
        heightRefresh = ScreenUtils.dpAdapt(context, 80);
        velocity_y_limit = (int) (ViewConfiguration.get(context).getScaledMaximumFlingVelocity() * 0.7f);
    }


    public void setVelocity_y_limit(int velocity_y_limit) {
        this.velocity_y_limit = velocity_y_limit;
    }

    public void setY_dragUp_ratio(float y_dragUp_ratio) {
        this.y_dragUp_ratio = y_dragUp_ratio;
    }

    public void setY_dragDown_ratio(float y_dragDown_ratio) {
        this.y_dragDown_ratio = y_dragDown_ratio;
    }


    public void setDuration_refresh_start(int duration_refresh_start) {
        this.duration_refresh_start = duration_refresh_start;
    }

    public void setDuration_refresh_finish(int duration_refresh_finish) {
        this.duration_refresh_finish = duration_refresh_finish;
    }

    /**
     * ---------------------------------------------------------------
     */

    @Override
    public void setAnimationView(IAnimationView animationView) {
        removeView(this.animationView.getView());
        this.animationView = animationView;
        addView(animationView.getView());
    }


    @Override
    public IAnimationView getAnimationView() {
        return animationView;
    }

    @Override
    public void setHeightMax(int heightMax) {
        this.heightMax = heightMax;
    }

    @Override
    public int getHeightRefresh() {
        return heightRefresh;
    }

    @Override
    public int getHeightMax() {
        return heightMax;
    }

    @Override
    public void setHeightRefresh(int heightRefresh) {
        this.heightRefresh = heightRefresh;
    }

    @Override
    public boolean isRefreshing() {
        return isRefreshing;
    }

    @Override
    public void refreshCancel() {
        if (callback != null) callback.onRefreshCancel();
        close();
    }


    @Override
    public void addCallback(RefreshCallback callback) {
        this.callback = callback;
    }

    @Override
    public BaseHeadView getView() {
        return this;
    }

    @Override
    public void onDragingDown(int distance) {
        int value = Math.abs((int) (distance * y_dragDown_ratio));
        int height = getHeight() + value;
        height = height > heightMax ? heightMax : height;
        getLayoutParams().height = height;
        requestLayout();
        animationView.onDraging(height, heightRefresh, heightMax);
    }

    @Override
    public void onDragingUp(int distance) {
        int value = Math.abs((int) (distance * y_dragUp_ratio));
        int height = getHeight() - value;
        height = height < 0 ? 0 : height;
        getLayoutParams().height = height;
        requestLayout();
        animationView.onDraging(height, heightRefresh, heightMax);
    }

    @Override
    public void onDragRelease(int velocity_y) {
        if (velocity_y > velocity_y_limit || getHeight() >= heightRefresh) {
            if (!isRefreshing) {
                refreshStart();
            } else {
                open();
            }
            return;
        }
        if (-velocity_y > velocity_y_limit || getHeight() < heightRefresh) {
            if (isRefreshing) {
                refreshCancel();
                return;
            }
            close();
        }
    }

    private void open(AnimatorListenerAdapter animatorListenerAdapter) {
        final int height_head = getHeight();
        ValueAnimator valueAnimator = ValueAnimator.ofInt(0, height_head - heightRefresh);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int value = (int) animation.getAnimatedValue();
                int height = height_head - value;
                getLayoutParams().height = height;
                requestLayout();
                animationView.onDraging(height, heightRefresh, heightMax);
            }
        });
        valueAnimator.setDuration(duration_refresh_start);
        valueAnimator.setInterpolator(new DecelerateInterpolator());
        valueAnimator.setEvaluator(new IntEvaluator());
        if (animatorListenerAdapter != null) valueAnimator.addListener(animatorListenerAdapter);
        valueAnimator.start();
    }

    private void close(AnimatorListenerAdapter animatorListenerAdapter) {
        animationView.stopLoadAnimation();
        final int height_head = getHeight();
        ValueAnimator valueAnimator = ValueAnimator.ofInt(0, height_head);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int value = (int) animation.getAnimatedValue();
                int height = height_head - value;
                getLayoutParams().height = height;
                requestLayout();
                animationView.onDraging(height, heightRefresh, heightMax);
            }
        });
        valueAnimator.setDuration(duration_refresh_finish);
        valueAnimator.setInterpolator(new DecelerateInterpolator());
        valueAnimator.setEvaluator(new IntEvaluator());
        if (animatorListenerAdapter != null) valueAnimator.addListener(animatorListenerAdapter);
        valueAnimator.start();
    }

    @Override
    public void open() {
        open(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                if (callback != null) callback.onRefreshStart();
                animationView.startLoadAnimation();
                isRefreshing = true;
            }
        });
    }

    @Override
    public void close() {
        close(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                isRefreshing = false;
                animationView.onDragClosed();
            }
        });
    }


    @Override
    public void refreshStart() {
        open();
    }

    @Override
    public void refreshFinish() {
        LogUtils.log("refreshFinish");
        animationView.closeLoadAnimation(new IAnimationView.IAnimationViewCallback() {
            @Override
            public void onLoadOpened() {

            }

            @Override
            public void onLoadClosed() {
                LogUtils.log("onLoadClosed");
                close(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        isRefreshing = false;
                        animationView.onDragClosed();
                        if (callback != null) callback.onRefreshFinish();
                    }
                });

            }
        });

    }

    @Override
    public void refreshFinish(final RefreshFinishListener refreshFinishListener) {
        animationView.closeLoadAnimation(new IAnimationView.IAnimationViewCallback() {
            @Override
            public void onLoadOpened() {

            }

            @Override
            public void onLoadClosed() {
                if (callback != null) callback.onRefreshFinish();
                refreshFinishListener.onRefreshFinish(BaseHeadView.this);
            }
        });

    }
}
