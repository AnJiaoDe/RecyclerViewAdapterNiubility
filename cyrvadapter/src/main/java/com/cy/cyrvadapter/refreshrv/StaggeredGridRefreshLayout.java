package com.cy.cyrvadapter.refreshrv;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import androidx.recyclerview.widget.RecyclerView;

import com.cy.cyrvadapter.recyclerview.StaggeredGridRecyclerView;
import com.cy.cyrvadapter.refreshrv.BaseRVRefreshLayout;


/**
 * Created by cy on 2018/4/9.
 */

public class StaggeredGridRefreshLayout extends BaseRVRefreshLayout<StaggeredGridRecyclerView> {

    private StaggeredGridRecyclerView staggeredGridRecyclerView;

    public StaggeredGridRefreshLayout(Context context) {
        this(context, null);
    }

    public StaggeredGridRefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        staggeredGridRecyclerView = new StaggeredGridRecyclerView(context);
        setContentView(staggeredGridRecyclerView);
    }

    @Override
    public StaggeredGridRecyclerView getRecyclerView() {
        return staggeredGridRecyclerView;
    }
}
