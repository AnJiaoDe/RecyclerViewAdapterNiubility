package com.cy.rvadapterniubility.refreshrv;

import android.content.Context;
import android.util.AttributeSet;

import com.cy.rvadapterniubility.recyclerview.OnStaggeredLoadMoreListener;
import com.cy.rvadapterniubility.recyclerview.StaggeredAdapter;
import com.cy.rvadapterniubility.recyclerview.StaggeredRecyclerView;


/**
 * Created by cy on 2018/4/9.
 */

public class StaggeredRefreshLayout extends BaseRVRefreshLayout<StaggeredRecyclerView> {

    private StaggeredRecyclerView staggeredGridRecyclerView;

    public StaggeredRefreshLayout(Context context) {
        this(context, null);
    }

    public StaggeredRefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        staggeredGridRecyclerView = new StaggeredRecyclerView(context);
        setContentView(staggeredGridRecyclerView);
    }

    @Override
    public StaggeredRecyclerView getRecyclerView() {
        return staggeredGridRecyclerView;
    }
    public StaggeredRefreshLayout setAdapter(StaggeredAdapter staggeredAdapter, OnRefreshListener onRefreshListener, OnStaggeredLoadMoreListener onStaggeredLoadMoreListener) {
        setEnableLoadMore(false);
        staggeredGridRecyclerView.setAdapter(staggeredAdapter);
        staggeredGridRecyclerView.addOnScrollListener(onStaggeredLoadMoreListener);
        setOnRefreshListener(onRefreshListener);
        return this;
    }
}
