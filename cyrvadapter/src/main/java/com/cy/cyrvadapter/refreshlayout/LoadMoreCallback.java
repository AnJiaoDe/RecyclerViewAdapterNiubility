package com.cy.cyrvadapter.refreshlayout;

/**
 * Created by lcodecore on 2016/10/1.
 */

interface LoadMoreCallback {


    /**
     * 开始loadMore
     */
    public void onLoadMoreStart();


    public void onLoadMoreFinish();


    /**
     * 正在loadMore时向下滑动屏幕，loadMore被取消
     */
    public void onLoadMoreCancel();

//    void onStateChanged(int state);

}
