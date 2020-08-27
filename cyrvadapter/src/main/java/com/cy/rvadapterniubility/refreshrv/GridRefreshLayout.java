package com.cy.rvadapterniubility.refreshrv;

import android.content.Context;
import android.util.AttributeSet;

import com.cy.rvadapterniubility.recyclerview.GridRecyclerView;

/**
 * Created by lenovo on 2017/12/31.
 */

public class GridRefreshLayout extends BaseRVRefreshLayout<GridRecyclerView> {
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
}