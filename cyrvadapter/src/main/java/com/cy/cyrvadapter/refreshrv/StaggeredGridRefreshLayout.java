package com.cy.cyrvadapter.refreshrv;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.cy.cyrvadapter.recyclerview.OnRVLoadMoreScrollListener;
import com.cy.cyrvadapter.recyclerview.StaggeredGridRecyclerView;
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;

/**
 * Created by cy on 2018/4/9.
 */

public class StaggeredGridRefreshLayout extends BaseRefreshLayout {

    private StaggeredGridRecyclerView staggeredGridRecyclerView;

    public StaggeredGridRefreshLayout(Context context) {
        this(context, null);
    }

    public StaggeredGridRefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        staggeredGridRecyclerView = new StaggeredGridRecyclerView(context);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        staggeredGridRecyclerView.setLayoutParams(layoutParams);
        addView(staggeredGridRecyclerView);
    }

    public StaggeredGridRecyclerView getStaggeredGridRecyclerView() {
        return staggeredGridRecyclerView;
    }

    public void setAdapter(Context context, RecyclerView.Adapter adapter, int spanCount, int orientation) {
        staggeredGridRecyclerView.setAdapter(context, adapter, spanCount, orientation);

    }

    public void setAdapter(final Context context, RecyclerView.Adapter adapter, int spanCount, int orientation, int color, RefreshListenerAdapter refreshListenerAdapter) {
        staggeredGridRecyclerView.setAdapter(context, adapter, spanCount, orientation, new OnRVLoadMoreScrollListener() {
            @Override
            public void rvStartLoadMore() {
                startLoadMore();
            }

            @Override
            public void onGlideShouldPauseRequests() {
                Glide.with(context).pauseRequests();

            }

            @Override
            public void onGlideShouldResumeRequests() {
                Glide.with(context).resumeRequests();

            }
        });

        setOnRefreshListener(context, refreshListenerAdapter, color);
    }

    public void setAdapter(Context context, RecyclerView.Adapter adapter, int spanCount, int orientation, int color, OnCYRefreshListener onCYRefreshListener) {
        staggeredGridRecyclerView.setAdapter(context, adapter, spanCount, orientation);

        setOnCYRefreshListener(context, onCYRefreshListener, color);
    }

    public void setAdapter(final Context context, RecyclerView.Adapter adapter, int spanCount, int orientation, int color, OnCYLoadMoreLister onCYLoadMoreLister) {
        staggeredGridRecyclerView.setAdapter(context, adapter, spanCount, orientation, new OnRVLoadMoreScrollListener() {
            @Override
            public void rvStartLoadMore() {
                startLoadMore();
            }

            @Override
            public void onGlideShouldPauseRequests() {
                Glide.with(context).pauseRequests();

            }

            @Override
            public void onGlideShouldResumeRequests() {
                Glide.with(context).resumeRequests();

            }
        });

        setOnCYLoadMoreLister(context, onCYLoadMoreLister, color);
    }
}
