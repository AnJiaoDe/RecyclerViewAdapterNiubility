package com.cy.cyrvadapter.refreshrv;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.cy.cyrvadapter.recyclerview.GridRecyclerView;
import com.cy.cyrvadapter.recyclerview.OnRVScrollListener;
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;

/**
 * Created by lenovo on 2017/12/31.
 */

public class GridRefreshLayout extends BaseRefreshLayout {
    private Context context;
    private GridRecyclerView gridRecyclerView;

    public GridRefreshLayout(Context context) {
        super(context);
    }

    public GridRefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;

        gridRecyclerView = new GridRecyclerView(context);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        gridRecyclerView.setLayoutParams(layoutParams);
        addView(gridRecyclerView);
    }

    public GridRecyclerView getRV() {
        return gridRecyclerView;
    }




    public void setAdapter(RecyclerView.Adapter adapter, int spanCount,int orientation,int color, RefreshListenerAdapter refreshListenerAdapter) {
        gridRecyclerView.setAdapter(adapter, spanCount,orientation, new OnRVScrollListener() {
            @Override
            public void rvStartLoadMore() {
                startLoadMore();
            }

            @Override
            public void onGlideShouldPauseRequests() {
                if (context!=null)  Glide.with(context).pauseRequests();

            }

            @Override
            public void onGlideShouldResumeRequests() {
                if (context!=null)  Glide.with(context).resumeRequests();

            }
        });

        setOnRefreshListener(refreshListenerAdapter,color);
    }
    public void setAdapter(RecyclerView.Adapter adapter, int spanCount,int orientation, int color,OnCYRefreshListener onCYRefreshListener) {
        gridRecyclerView.setAdapter(adapter, spanCount,orientation);

        setOnCYRefreshListener(onCYRefreshListener,color);
    }
    public void setAdapter(RecyclerView.Adapter adapter, int spanCount,int orientation,int color, OnCYLoadMoreLister onCYLoadMoreLister) {
        gridRecyclerView.setAdapter(adapter, spanCount,orientation, new OnRVScrollListener() {
            @Override
            public void rvStartLoadMore() {
                startLoadMore();
            }

            @Override
            public void onGlideShouldPauseRequests() {

                if (context!=null){

                    if (context!=null)  Glide.with(context).pauseRequests();
                }

            }

            @Override
            public void onGlideShouldResumeRequests() {
                if (context!=null)  Glide.with(context).resumeRequests();

            }
        });

        setOnCYLoadMoreLister(onCYLoadMoreLister, color);
    }
    public void setAdapter(RecyclerView.Adapter adapter, int spanCount,int orientation,boolean head,boolean foot,int color, RefreshListenerAdapter refreshListenerAdapter) {
        gridRecyclerView.setAdapter(adapter, spanCount,orientation,head,foot, new OnRVScrollListener() {
            @Override
            public void rvStartLoadMore() {
                startLoadMore();
            }

            @Override
            public void onGlideShouldPauseRequests() {
                if (context!=null)  Glide.with(context).pauseRequests();

            }

            @Override
            public void onGlideShouldResumeRequests() {
                if (context!=null)  Glide.with(context).resumeRequests();

            }
        });

        setOnRefreshListener(refreshListenerAdapter,color);
    }
    public void setAdapter(RecyclerView.Adapter adapter, int spanCount,int orientation,boolean head,boolean foot, int color,OnCYRefreshListener onCYRefreshListener) {
        gridRecyclerView.setAdapter(adapter, spanCount,orientation,head,foot);

        setOnCYRefreshListener(onCYRefreshListener,color);
    }
    public void setAdapter(RecyclerView.Adapter adapter, int spanCount,int orientation,boolean head,boolean foot,int color, OnCYLoadMoreLister onCYLoadMoreLister) {
        gridRecyclerView.setAdapter(adapter, spanCount,orientation,head,foot, new OnRVScrollListener() {
            @Override
            public void rvStartLoadMore() {
                startLoadMore();
            }

            @Override
            public void onGlideShouldPauseRequests() {

                if (context!=null){

                    if (context!=null)  Glide.with(context).pauseRequests();
                }

            }

            @Override
            public void onGlideShouldResumeRequests() {
                if (context!=null)  Glide.with(context).resumeRequests();

            }
        });

        setOnCYLoadMoreLister(onCYLoadMoreLister, color);
    }
}
