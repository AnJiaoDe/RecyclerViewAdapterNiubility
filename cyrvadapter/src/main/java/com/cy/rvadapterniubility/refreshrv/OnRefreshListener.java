package com.cy.rvadapterniubility.refreshrv;


import android.view.View;
import android.widget.TextView;

import com.cy.BaseAdapter.R;
import com.cy.refreshlayoutniubility.OnPullListener;

public abstract class OnRefreshListener<T> extends OnPullListener<T> {
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
