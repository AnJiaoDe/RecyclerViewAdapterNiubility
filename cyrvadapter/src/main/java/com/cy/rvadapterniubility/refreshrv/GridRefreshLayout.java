package com.cy.rvadapterniubility.refreshrv;

import android.content.Context;
import android.util.AttributeSet;

import com.cy.rvadapterniubility.adapter.MultiAdapter;
import com.cy.rvadapterniubility.adapter.SimpleAdapter;
import com.cy.rvadapterniubility.recyclerview.BaseRecyclerView;
import com.cy.rvadapterniubility.recyclerview.GridRecyclerView;
import com.cy.rvadapterniubility.recyclerview.OnGridLoadMoreListener;

/**
 * Created by lenovo on 2017/12/31.
 */

public class GridRefreshLayout extends BaseRVRefreshLayout<GridRecyclerView,GridRefreshLayout> {
    private GridRecyclerView gridRecyclerView;

    public GridRefreshLayout(Context context) {
        this(context,null);
    }

    public GridRefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        gridRecyclerView = new GridRecyclerView(context);
        setContentView(gridRecyclerView);
    }

    @Override
    public GridRecyclerView getRecyclerView() {
        return gridRecyclerView;
    }
    public GridRefreshLayout setAdapter(SimpleAdapter simpleAdapter, OnRefreshListener onRefreshListener, OnGridLoadMoreListener onGridLoadMoreListener) {
        setEnableLoadMore(false);
        BaseRecyclerView baseRecyclerView = getRecyclerView();
        baseRecyclerView.addOnScrollListener(onGridLoadMoreListener);
        setOnRefreshListener(onRefreshListener);
        baseRecyclerView.setAdapter(simpleAdapter);
        return  this;
    }
    public GridRefreshLayout setAdapter(MultiAdapter multiAdapter, OnRefreshListener onRefreshListener, OnGridLoadMoreListener onGridLoadMoreListener) {
        setEnableLoadMore(false);
        BaseRecyclerView baseRecyclerView = getRecyclerView();
        baseRecyclerView.addOnScrollListener(onGridLoadMoreListener);
        setOnRefreshListener(onRefreshListener);
        baseRecyclerView.setAdapter(multiAdapter.getMergeAdapter());
        return  this;
    }
}
