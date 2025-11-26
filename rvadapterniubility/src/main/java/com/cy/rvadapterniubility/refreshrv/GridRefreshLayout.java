package com.cy.rvadapterniubility.refreshrv;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import com.cy.refreshlayoutniubility.OnRefreshListener;
import com.cy.refreshlayoutniubility.RefreshLayoutNiubility;
import com.cy.rvadapterniubility.adapter.MultiAdapter;
import com.cy.rvadapterniubility.adapter.SimpleAdapter;
import com.cy.rvadapterniubility.recyclerview.BaseRecyclerView;
import com.cy.rvadapterniubility.recyclerview.VerticalGridRecyclerView;
import com.cy.rvadapterniubility.recyclerview.OnGridLoadMoreListener;

/**
 * Created by lenovo on 2017/12/31.
 */

public class GridRefreshLayout extends BaseRVRefreshLayout<VerticalGridRecyclerView,OnGridLoadMoreListener> {
    private VerticalGridRecyclerView verticalGridRecyclerView;

    public GridRefreshLayout(Context context) {
        this(context,null);
    }

    public GridRefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        verticalGridRecyclerView = new VerticalGridRecyclerView(context);
        setContentView(verticalGridRecyclerView);
    }

    @Override
    public <T extends BaseRVRefreshLayout<VerticalGridRecyclerView, OnGridLoadMoreListener>> T setRecyclerView(VerticalGridRecyclerView recyclerView) {
        this.verticalGridRecyclerView=recyclerView;
        return setContentView(verticalGridRecyclerView);
    }

    @Override
    public VerticalGridRecyclerView getRecyclerView() {
        return verticalGridRecyclerView;
    }
}
