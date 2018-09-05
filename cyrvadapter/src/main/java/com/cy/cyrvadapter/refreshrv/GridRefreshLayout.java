package com.cy.cyrvadapter.refreshrv;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.cy.cyrvadapter.recyclerview.GridRecyclerView;
import com.cy.cyrvadapter.recyclerview.OnRVLoadMoreScrollListener;
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;

/**
 * Created by lenovo on 2017/12/31.
 */

public class GridRefreshLayout extends BaseRefreshLayout {
    private GridRecyclerView gridRecyclerView;

    public GridRefreshLayout(Context context) {
        super(context);
    }

    public GridRefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);

        gridRecyclerView = new GridRecyclerView(context);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        gridRecyclerView.setLayoutParams(layoutParams);
        addView(gridRecyclerView);
    }

    public GridRecyclerView getRV() {
        return gridRecyclerView;
    }


    public void setAdapter(final Context context, RecyclerView.Adapter adapter, int spanCount, int orientation, int color, RefreshListenerAdapter refreshListenerAdapter) {
        gridRecyclerView.setAdapter(context, adapter, spanCount, orientation, new OnRVLoadMoreScrollListener() {
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

        setOnRefreshListener(context,refreshListenerAdapter, color);
    }

    public void setAdapter(Context context,RecyclerView.Adapter adapter, int spanCount, int orientation, int color, OnCYRefreshListener onCYRefreshListener) {
        gridRecyclerView.setAdapter(context,adapter, spanCount, orientation);

        setOnCYRefreshListener(context,onCYRefreshListener, color);

    }

    public void setAdapter(final  Context context,RecyclerView.Adapter adapter, int spanCount, int orientation, int color, OnCYLoadMoreLister onCYLoadMoreLister) {
        gridRecyclerView.setAdapter(context,adapter, spanCount, orientation, new OnRVLoadMoreScrollListener() {
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

    public void setAdapter(final Context context, RecyclerView.Adapter adapter, int spanCount, int orientation, boolean head, boolean foot, int color, RefreshListenerAdapter refreshListenerAdapter) {
        gridRecyclerView.setAdapter(context,adapter, spanCount, orientation, head, foot, new OnRVLoadMoreScrollListener() {
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

        setOnRefreshListener(context,refreshListenerAdapter, color);
    }

    public void setAdapter(Context context,RecyclerView.Adapter adapter, int spanCount, int orientation, boolean head, boolean foot, int color, OnCYRefreshListener onCYRefreshListener) {
        gridRecyclerView.setAdapter(context,adapter, spanCount, orientation, head, foot);

        setOnCYRefreshListener(context,onCYRefreshListener, color);
    }

    public void setAdapter(final Context context, RecyclerView.Adapter adapter, int spanCount, int orientation, boolean head, boolean foot, int color, OnCYLoadMoreLister onCYLoadMoreLister) {
        gridRecyclerView.setAdapter(context,adapter, spanCount, orientation, head, foot, new OnRVLoadMoreScrollListener() {
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
