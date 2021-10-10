package com.cy.rvadapterniubility.refreshrv;
import android.content.Context;
import android.util.AttributeSet;

import com.cy.refreshlayoutniubility.OnRefreshListener;
import com.cy.rvadapterniubility.adapter.MultiAdapter;
import com.cy.rvadapterniubility.adapter.SimpleAdapter;
import com.cy.rvadapterniubility.recyclerview.OnGridLoadMoreListener;
import com.cy.rvadapterniubility.recyclerview.OnStaggeredLoadMoreListener;
import com.cy.rvadapterniubility.recyclerview.VerticalGridRecyclerView;
import com.cy.rvadapterniubility.recyclerview.VerticalStaggeredRecyclerView;
/**
 * Created by cy on 2018/4/9.
 */
public class StaggeredRefreshLayout extends BaseRVRefreshLayout<VerticalStaggeredRecyclerView> {
    private VerticalStaggeredRecyclerView verticalStaggeredRecyclerView;

    public StaggeredRefreshLayout(Context context) {
        this(context,null);
    }

    public StaggeredRefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        verticalStaggeredRecyclerView = new VerticalStaggeredRecyclerView(context);
        setContentView(verticalStaggeredRecyclerView);
    }

    @Override
    public <T extends BaseRVRefreshLayout> T setRecyclerView(VerticalStaggeredRecyclerView verticalStaggeredRecyclerView) {
        this.verticalStaggeredRecyclerView=verticalStaggeredRecyclerView;
        return setContentView(verticalStaggeredRecyclerView);
    }

    @Override
    public VerticalStaggeredRecyclerView getRecyclerView() {
        return verticalStaggeredRecyclerView;
    }

    public StaggeredRefreshLayout setAdapter(SimpleAdapter simpleAdapter, OnRefreshListener onRefreshListener, OnStaggeredLoadMoreListener onStaggeredLoadMoreListener) {
        verticalStaggeredRecyclerView.addOnScrollListener(onStaggeredLoadMoreListener);
        setOnRefreshListener(onRefreshListener);
        verticalStaggeredRecyclerView.setAdapter(simpleAdapter);
        return  this;
    }
    public StaggeredRefreshLayout setAdapter(MultiAdapter multiAdapter, OnRefreshListener onRefreshListener, OnStaggeredLoadMoreListener onStaggeredLoadMoreListener) {
        verticalStaggeredRecyclerView.addOnScrollListener(onStaggeredLoadMoreListener);
        setOnRefreshListener(onRefreshListener);
        verticalStaggeredRecyclerView.setAdapter(multiAdapter.getMergeAdapter());
        return  this;
    }
}
