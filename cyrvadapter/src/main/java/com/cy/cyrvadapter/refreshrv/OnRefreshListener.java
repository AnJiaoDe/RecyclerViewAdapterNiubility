package com.cy.cyrvadapter.refreshrv;

import com.cy.cyrvadapter.refreshlayout.OnPullListener;

public abstract class OnRefreshListener extends OnPullListener {
    public abstract void onRefreshStart();

    public void onRefreshFinish() {
    }

    /**
     * 正在刷新时向上滑动屏幕，刷新被取消
     */
    public void onRefreshCancel() {
    }

    public  void onLoadMoreStart(){}

    public void onLoadMoreFinish() {
    }

    public void onLoadMoreCancel() {
    }
}
