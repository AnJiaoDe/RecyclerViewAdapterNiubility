package com.cy.cyrvadapter.refreshlayout;

public abstract class OnPullListener {
    public abstract void onRefreshStart();

    public void onRefreshFinish() {
    }

    /**
     * 正在刷新时向上滑动屏幕，刷新被取消
     */
    public void onRefreshCancel() {
    }

    public abstract void onLoadMoreStart();


    public void onLoadMoreFinish() {
    }

    public void onLoadMoreCancel() {
    }

//    public void onStateChanged(int state){
//
//    }
}
