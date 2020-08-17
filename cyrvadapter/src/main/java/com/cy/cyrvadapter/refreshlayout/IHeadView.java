package com.cy.cyrvadapter.refreshlayout;

import android.widget.FrameLayout;

import java.net.PortUnreachableException;

/**
 * Created by lcodecore on 2016/10/1.
 */

public interface IHeadView {

    public <T extends FrameLayout> T getView();

    public int getHeightRefresh();

    public void setHeightRefresh(int heightRefresh);

    public int getHeightMax();

    public void setHeightMax(int heightMax);

    public void setAnimationView(IAnimationView animationView);

    public IAnimationView getAnimationView();

    public void addCallback(RefreshCallback callback);

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
     * 正在刷新时向上滑动屏幕，刷新被取消
     */
    public void refreshCancel();

    public boolean isRefreshing();

    public void refreshStart();

    public void refreshFinish();

    public void refreshFinish(RefreshFinishListener refreshFinishListener);

    public void open();

    public void close();

}
