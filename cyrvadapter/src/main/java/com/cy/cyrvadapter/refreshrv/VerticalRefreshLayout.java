package com.cy.cyrvadapter.refreshrv;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.cy.cyrvadapter.recyclerview.OnRVLoadMoreScrollListener;
import com.cy.cyrvadapter.recyclerview.VerticalRecyclerView;
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;

/**
 * Created by lenovo on 2017/12/31.
 */

public class VerticalRefreshLayout extends BaseRefreshLayout {
    private VerticalRecyclerView verticalRecyclerView;

    public VerticalRefreshLayout(Context context) {
        super(context);
    }

    public VerticalRefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);

        verticalRecyclerView = new VerticalRecyclerView(context);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        verticalRecyclerView.setLayoutParams(layoutParams);
        addView(verticalRecyclerView);
    }

    public VerticalRecyclerView getRV() {
        return verticalRecyclerView;
    }

    public void setAdapter(RecyclerView.Adapter adapter) {
        verticalRecyclerView.setAdapter(adapter);
    }

    public void setAdapter(final Context context, RecyclerView.Adapter adapter, int color, RefreshListenerAdapter refreshListenerAdapter) {
        verticalRecyclerView.setAdapter(context, adapter, new OnRVLoadMoreScrollListener() {
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

    public void setAdapter(Context context, RecyclerView.Adapter adapter, int color, OnCYRefreshListener onCYRefreshListener) {
        verticalRecyclerView.setAdapter(context, adapter);
        setOnCYRefreshListener(context, onCYRefreshListener, color);
    }

    public void setAdapter(final Context context, RecyclerView.Adapter adapter, int color, OnCYLoadMoreLister onCYLoadMoreLister) {
        verticalRecyclerView.setAdapter(context, adapter, new OnRVLoadMoreScrollListener() {
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
        setOnCYLoadMoreLister(context,onCYLoadMoreLister, color);
    }
}
