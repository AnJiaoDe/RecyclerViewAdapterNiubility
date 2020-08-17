package com.cy.cyrvadapter.recyclerview;

import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.SparseArray;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cy.cyrvadapter.adapter.GridFullSpanBean;
import com.cy.cyrvadapter.adapter.GridItemDecoration;
import com.cy.cyrvadapter.refreshlayout.LogUtils;
import com.cy.cyrvadapter.refreshlayout.ScreenUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cy on 2017/7/2.
 */

public class GridRecyclerView extends BaseRecyclerView<GridRecyclerView> {
    private int spanCount = 2;
    private int orientation = RecyclerView.VERTICAL;
    private Context context;
    private SparseArray<GridFullSpanBean> sparseArrayGridFullSpanBean = new SparseArray<GridFullSpanBean>();
    private GridItemDecoration gridItemDecoration;
    public GridRecyclerView(Context context) {
        this(context, null);
    }

    public GridRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    public GridRecyclerView setSpanCount(int spanCount) {
        this.spanCount = spanCount;
        return this;
    }

    public GridRecyclerView setOrientation(int orientation) {
        this.orientation = orientation;
        return this;
    }

    public GridRecyclerView addFullSpanPosition(int position,GridFullSpanBean gridFullSpanBean) {
        sparseArrayGridFullSpanBean.put(position,gridFullSpanBean);
        return this;
    }

    public SparseArray<GridFullSpanBean> getSparseArrayGridFullSpanBean() {
        return sparseArrayGridFullSpanBean;
    }

    public void addItemDecoration(GridItemDecoration gridItemDecoration) {
        this.gridItemDecoration=gridItemDecoration;
        super.addItemDecoration(gridItemDecoration);
    }

    public GridItemDecoration getGridItemDecoration() {
        return gridItemDecoration;
    }

    @Override
    public void setAdapter(final Adapter adapter) {
        final GridLayoutManager layoutManager = new GridLayoutManager(getContext(), spanCount, orientation, false);
        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (sparseArrayGridFullSpanBean.get(position) != null) return layoutManager.getSpanCount();
                return 1;
            }
        });
        setLayoutManager(layoutManager);
        super.setAdapter(adapter);
    }
}
