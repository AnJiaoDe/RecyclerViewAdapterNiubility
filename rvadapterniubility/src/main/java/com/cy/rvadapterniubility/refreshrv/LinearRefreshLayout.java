package com.cy.rvadapterniubility.refreshrv;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import com.cy.refreshlayoutniubility.OnRefreshListener;
import com.cy.refreshlayoutniubility.RefreshLayoutNiubility;
import com.cy.rvadapterniubility.adapter.MultiAdapter;
import com.cy.rvadapterniubility.recyclerview.BaseRecyclerView;
import com.cy.rvadapterniubility.recyclerview.OnLinearLoadMoreListener;
import com.cy.rvadapterniubility.recyclerview.OnLoadMoreListener;
import com.cy.rvadapterniubility.recyclerview.VerticalGridRecyclerView;
import com.cy.rvadapterniubility.recyclerview.VerticalRecyclerView;

/**
 * Created by lenovo on 2017/12/31.
 */

public class LinearRefreshLayout extends BaseRVRefreshLayout<VerticalRecyclerView,OnLinearLoadMoreListener> {
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
    public <T extends BaseRVRefreshLayout<VerticalRecyclerView, OnLinearLoadMoreListener>> T setRecyclerView(VerticalRecyclerView recyclerView) {
        this.verticalRecyclerView=recyclerView;
        return setContentView(verticalRecyclerView);
    }

    @Override
    public VerticalRecyclerView getRecyclerView() {
        return verticalRecyclerView;
    }

}
