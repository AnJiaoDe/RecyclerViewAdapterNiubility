package com.cy.cyrvadapter.refreshrv;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import com.cy.cyrvadapter.recyclerview.OnRVScrollListener;
import com.cy.cyrvadapter.recyclerview.StaggeredGridRecyclerView;
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;

/**
 * Created by cy on 2018/4/9.
 */

public class StaggeredGridRefreshLayout extends BaseRefreshLayout {
    private Context context;

    private StaggeredGridRecyclerView staggeredGridRecyclerView;

    public StaggeredGridRefreshLayout(Context context) {
        this(context, null);
    }

    public StaggeredGridRefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        staggeredGridRecyclerView = new StaggeredGridRecyclerView(context);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        staggeredGridRecyclerView.setLayoutParams(layoutParams);
        addView(staggeredGridRecyclerView);
    }

    public StaggeredGridRecyclerView getStaggeredGridRecyclerView() {
        return staggeredGridRecyclerView;
    }

    public void setAdapter(RecyclerView.Adapter adapter, int spanCount, int orientation) {
        staggeredGridRecyclerView.setAdapter(adapter, spanCount, orientation);

    }

    public void setAdapter(RecyclerView.Adapter adapter, int spanCount, int orientation,int color, RefreshListenerAdapter refreshListenerAdapter) {
        staggeredGridRecyclerView.setAdapter(adapter, spanCount, orientation, new OnRVScrollListener() {
            @Override
            public void rvStartLoadMore() {
                startLoadMore();
            }
        });

        setOnRefreshListener(refreshListenerAdapter,color);
    }

    public void setAdapter(RecyclerView.Adapter adapter, int spanCount, int orientation, int color,OnCYRefreshListener onCYRefreshListener) {
        staggeredGridRecyclerView.setAdapter(adapter, spanCount, orientation);

        setOnCYRefreshListener(onCYRefreshListener,color);
    }

    public void setAdapter(RecyclerView.Adapter adapter, int spanCount, int orientation, int color,OnCYLoadMoreLister onCYLoadMoreLister) {
        staggeredGridRecyclerView.setAdapter(adapter, spanCount, orientation, new OnRVScrollListener() {
            @Override
            public void rvStartLoadMore() {
                startLoadMore();
            }
        });

        setOnCYLoadMoreLister(onCYLoadMoreLister, color);
    }
}
