package com.cy.cyrvadapter.recyclerview;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;

/**
 * Created by cy on 2018/4/8.
 */

public class StaggeredGridRecyclerView extends RecyclerView {

    public StaggeredGridRecyclerView(Context context) {
        this(context,null);
    }

    public StaggeredGridRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setOverScrollMode(OVER_SCROLL_NEVER);

    }

    public void setAdapter(Context context,final Adapter adapter, int spanCount, int orientation) {
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(spanCount, orientation);

        //取消动画，防止item复用导致的闪烁
        setItemAnimator(null);

        setLayoutManager(layoutManager);
        setAdapter(adapter);

        addOnScrollListener(new OnRVScrollListener(context));


    }




    public void setAdapter(Context context,Adapter adapter, int spanCount, int orientation, OnRVLoadMoreScrollListener onRVLoadMoreScrollListener) {
        setAdapter(context,adapter, spanCount,orientation);

        addOnScrollListener(onRVLoadMoreScrollListener);

    }

}
