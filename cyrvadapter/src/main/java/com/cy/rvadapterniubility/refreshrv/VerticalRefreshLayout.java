package com.cy.rvadapterniubility.refreshrv;

import android.content.Context;
import android.util.AttributeSet;

import com.cy.rvadapterniubility.recyclerview.VerticalRecyclerView;

/**
 * Created by lenovo on 2017/12/31.
 */

public class VerticalRefreshLayout extends BaseRVRefreshLayout<VerticalRecyclerView> {
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
}
