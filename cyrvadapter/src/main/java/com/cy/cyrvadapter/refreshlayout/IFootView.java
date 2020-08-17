package com.cy.cyrvadapter.refreshlayout;

import android.widget.FrameLayout;

/**
 * Created by lcodecore on 2016/10/1.
 */

public interface IFootView {

    public <T extends FrameLayout> T getView();

    public int getHeightLoadMore();

    public int getHeightMax();

    public void setHeightLoadMore(int heightLoadMore);

    public void setHeightMax(int heightMax);

    public void setAnimationView(IAnimationView animationView);

    public IAnimationView getAnimationView();

    public void addCallback(LoadMoreCallback callback);

    /**
     * 下拉中
     */
    public void onDragingDown(int distance);

    /**
     * 上拉中
     *
     * @param distance
     */
    public void onDragingUp(int distance);

    /**
     * 拖动松开
     */
    public void onDragRelease(int velocity_y);


    /**
     * 正在loadMore时向下滑动屏幕，loadMore被取消
     */
    public void loadMoreCancel();

    public boolean isLoadMoreIng();

    public void loadMoreStart();

    public void loadMoreFinish();

    public void loadMoreFinish(LoadMoreFinishListener loadMoreFinishListener);

    public void open();

    public void close();

}
