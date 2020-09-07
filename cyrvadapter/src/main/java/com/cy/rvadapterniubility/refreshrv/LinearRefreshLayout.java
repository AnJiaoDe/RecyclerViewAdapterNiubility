package com.cy.rvadapterniubility.refreshrv;

import android.content.Context;
import android.util.AttributeSet;

import com.cy.rvadapterniubility.adapter.MultiAdapter;
import com.cy.rvadapterniubility.recyclerview.BaseRecyclerView;
import com.cy.rvadapterniubility.recyclerview.OnLinearLoadMoreListener;
import com.cy.rvadapterniubility.recyclerview.VerticalRecyclerView;

/**
 * Created by lenovo on 2017/12/31.
 */

public class LinearRefreshLayout extends BaseRVRefreshLayout<VerticalRecyclerView, LinearRefreshLayout> {
    private VerticalRecyclerView verticalRecyclerView;
    public LinearRefreshLayout(Context context) {
        this(context, null);
    }
    public LinearRefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        verticalRecyclerView = new VerticalRecyclerView(context);
        setContentView(verticalRecyclerView);
    }

    @Override
    public VerticalRecyclerView getRecyclerView() {
        return verticalRecyclerView;
    }
    public LinearRefreshLayout setAdapter(MultiAdapter multiAdapter, OnLinearLoadMoreListener onRVLoadMoreListener) {
        setEnableRefresh(false);
        setEnableLoadMore(false);
        BaseRecyclerView baseRecyclerView = getRecyclerView();
        baseRecyclerView.addOnScrollListener(onRVLoadMoreListener);
        baseRecyclerView.setAdapter(multiAdapter.getMergeAdapter());
        return  this;
    }


    public LinearRefreshLayout setAdapter(MultiAdapter multiAdapter, OnRefreshListener onRefreshListener, OnLinearLoadMoreListener onRVLoadMoreListener) {
        setEnableLoadMore(false);
        BaseRecyclerView baseRecyclerView = getRecyclerView();
        baseRecyclerView.addOnScrollListener(onRVLoadMoreListener);
        setOnRefreshListener(onRefreshListener);
        baseRecyclerView.setAdapter(multiAdapter.getMergeAdapter());
        return  this;
    }

}
