package com.cy.rvadapterniubility.refreshrv;

import android.content.Context;
import android.util.AttributeSet;

import com.cy.rvadapterniubility.adapter.MultiAdapter;
import com.cy.rvadapterniubility.recyclerview.BaseRecyclerView;
import com.cy.rvadapterniubility.recyclerview.OnRVLoadMoreListener;
import com.cy.rvadapterniubility.recyclerview.OnVerticalScrollListener;
import com.cy.rvadapterniubility.recyclerview.VerticalRecyclerView;

/**
 * Created by lenovo on 2017/12/31.
 */

public class VerticalRefreshLayout extends BaseRVRefreshLayout<VerticalRecyclerView,VerticalRefreshLayout> {
    private VerticalRecyclerView verticalRecyclerView;
    public VerticalRefreshLayout(Context context) {
        this(context, null);
    }
    public VerticalRefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        verticalRecyclerView = new VerticalRecyclerView(context);
        setContentView(verticalRecyclerView);
    }

    @Override
    public VerticalRecyclerView getRecyclerView() {
        return verticalRecyclerView;
    }
    public VerticalRefreshLayout setAdapter(MultiAdapter multiAdapter, OnRVLoadMoreListener onRVLoadMoreListener) {
        setEnableRefresh(false);
        setEnableLoadMore(false);
        BaseRecyclerView baseRecyclerView = getRecyclerView();
        baseRecyclerView.addOnScrollListener(onRVLoadMoreListener);
        baseRecyclerView.setAdapter(multiAdapter.getMergeAdapter());
        return  this;
    }


    public VerticalRefreshLayout setAdapter(MultiAdapter multiAdapter, OnRefreshListener onRefreshListener, OnRVLoadMoreListener onRVLoadMoreListener) {
        setEnableLoadMore(false);
        BaseRecyclerView baseRecyclerView = getRecyclerView();
        baseRecyclerView.addOnScrollListener(onRVLoadMoreListener);
        setOnRefreshListener(onRefreshListener);
        baseRecyclerView.setAdapter(multiAdapter.getMergeAdapter());
        return  this;
    }

}
