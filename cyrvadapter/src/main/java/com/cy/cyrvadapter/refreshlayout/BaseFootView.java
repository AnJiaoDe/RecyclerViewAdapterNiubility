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

public class BaseFootView extends FrameLayout implements IFootView {
    private Context context;
    private IAnimationView animationView;
    private LoadMoreCallback callback;
    private int heightMax;
    private int heightLoadMore;
    private int velocity_y_limit = 1000;
    private float y_dragUp_ratio = 1f / 2;
    private float y_dragDown_ratio = 1f / 2;
    private int duration_loadMore_start = 100;
    private int duration_loadMore_finish = 300;
    private boolean isLoadMoreing = false;

    public BaseFootView(Context context) {
        super(context);
        this.context = context;
        LayoutParams layoutParams_child = new LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        layoutParams_child.gravity = Gravity.CENTER;
        animationView = new ThreeScaleCircleView(context);
        addView(animationView.getView(), layoutParams_child);
        heightMax = ScreenUtils.dpAdapt(context, 400);
        heightLoadMore = ScreenUtils.dpAdapt(context, 60);
        velocity_y_limit = (int) (ViewConfiguration.get(context).getScaledMaximumFlingVelocity() * 0.7f);
    }

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
    public int getHeightMax() {
        return heightMax;
    }

    @Override

    public void setHeightLoadMore(int heightLoadMore) {
        this.heightLoadMore = heightLoadMore;
    }

    @Override
    public int getHeightLoadMore() {
        return heightLoadMore;
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

    public void setDuration_loadMore_start(int duration_loadMore_start) {
        this.duration_loadMore_start = duration_loadMore_start;
    }

    public void setDuration_loadMore_finish(int duration_loadMore_finish) {
        this.duration_loadMore_finish = duration_loadMore_finish;
    }

    public void scrollAnimation() {
        //刷新动画和拖动动画不能同时执行，不然会变形
        if (animationView.isRunning()) return;
        float scale = getHeight() * 1f / heightLoadMore;
        animationView.getView().setScaleX(scale);
        animationView.getView().setScaleY(scale);
    }

    @Override
    public void addCallback(LoadMoreCallback callback) {
        this.callback = callback;
    }

    @Override
    public BaseFootView getView() {
        return this;
    }

    @Override
    public void onDragingDown(int distance) {
        int value = Math.abs((int) (distance * y_dragUp_ratio));
        int height = getHeight() - value;
        height = height < 0 ? 0 : height;
        getLayoutParams().height = height;
        requestLayout();
        scrollAnimation();
    }

    @Override
    public void onDragingUp(int distance) {
        int value = Math.abs((int) (distance * y_dragDown_ratio));
        int height = getHeight() + value;
        height = height > heightMax ? heightMax : height;
        getLayoutParams().height = height;
        requestLayout();
        scrollAnimation();
    }

    @Override
    public void onDragRelease(int velocity_y) {
        if (-velocity_y > velocity_y_limit || getHeight() >= heightLoadMore) {
            if (!isLoadMoreing) {
                loadMoreStart();
            } else {
                open();
            }
            return;
        }
        if (velocity_y > velocity_y_limit || getHeight() < heightLoadMore) {
            if (isLoadMoreing) {
                loadMoreCancel();
                return;
            }
            close();
        }
    }

    @Override
    public void loadMoreCancel() {
        if (callback != null) callback.onLoadMoreCancel();
        close();
    }

    private void open(AnimatorListenerAdapter animatorListenerAdapter) {
        final int height_foot = getHeight();
        ValueAnimator valueAnimator = ValueAnimator.ofInt(0, height_foot - heightLoadMore);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int value = (int) animation.getAnimatedValue();
                int height = height_foot - value;
                getLayoutParams().height = height;
                requestLayout();
                scrollAnimation();
            }
        });
        valueAnimator.setDuration(duration_loadMore_start);
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
                if (callback != null) callback.onLoadMoreStart();
                animationView.startLoadAnimation();
                isLoadMoreing = true;
            }
        });
    }

    private void close(AnimatorListenerAdapter animatorListenerAdapter) {
        final int height_foot = getHeight();
        ValueAnimator valueAnimator = ValueAnimator.ofInt(0, height_foot);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int value = (int) animation.getAnimatedValue();
                int height = height_foot - value;
                getLayoutParams().height = height;
                requestLayout();
                scrollAnimation();
            }
        });
        valueAnimator.setDuration(duration_loadMore_finish);
        valueAnimator.setInterpolator(new DecelerateInterpolator());
        valueAnimator.setEvaluator(new IntEvaluator());
        if (animatorListenerAdapter != null) valueAnimator.addListener(animatorListenerAdapter);
        valueAnimator.start();
    }

    @Override
    public void close() {
        close(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                isLoadMoreing = false;
            }
        });
    }

    @Override
    public boolean isLoadMoreIng() {
        return isLoadMoreing;
    }

    @Override
    public void loadMoreStart() {
        open();
    }

    @Override
    public void loadMoreFinish() {
        animationView.stopLoadAnimation();
        close(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                isLoadMoreing = false;
                if (callback != null) callback.onLoadMoreFinish();
            }
        });
    }

    @Override
    public void loadMoreFinish(final LoadMoreFinishListener loadMoreFinishListener) {
        animationView.stopLoadAnimation();
        if (callback != null) callback.onLoadMoreFinish();
        loadMoreFinishListener.onLoadMoreFinish(BaseFootView.this);
    }

}
