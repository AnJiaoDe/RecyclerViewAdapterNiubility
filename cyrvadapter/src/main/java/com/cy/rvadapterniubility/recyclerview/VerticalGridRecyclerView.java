package com.cy.rvadapterniubility.recyclerview;

import android.content.Context;
import android.util.AttributeSet;
import android.util.SparseArray;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cy.refreshlayoutniubility.ScreenUtils;


/**
 * Created by cy on 2017/7/2.
 */

public class VerticalGridRecyclerView extends BaseRecyclerView {
    private int spanCount = 2;
    private SparseArray<Boolean> arrayFullSpan;
    private IGridItemDecoration gridItemDecoration;

    public VerticalGridRecyclerView(Context context) {
        this(context, null);
    }

    public VerticalGridRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        arrayFullSpan = new SparseArray<>();
        addItemDecoration(new GridItemDecoration(ScreenUtils.dpAdapt(context, 10)));
    }

    public VerticalGridRecyclerView setSpanCount(int spanCount) {
        this.spanCount = spanCount;
        return this;
    }


    public VerticalGridRecyclerView addFullSpanPosition(int position) {
        arrayFullSpan.put(position, true);
        return this;
    }

    public VerticalGridRecyclerView removeFullSpanPosition(int position) {
        arrayFullSpan.remove(position);
        return this;
    }

    public SparseArray<Boolean> getArrayFullSpan() {
        return arrayFullSpan;
    }


    public VerticalGridRecyclerView addItemDecoration(GridItemDecoration gridItemDecoration) {
        if(this.gridItemDecoration!=null)removeItemDecoration(this.gridItemDecoration.getGridItemDecoration());
        this.gridItemDecoration = gridItemDecoration;
        super.addItemDecoration(gridItemDecoration);
        return this;
    }
    public VerticalGridRecyclerView addItemDecoration(FullSpanGridItemDecoration gridItemDecoration) {
        if(this.gridItemDecoration!=null)removeItemDecoration(this.gridItemDecoration.getGridItemDecoration());
        this.gridItemDecoration = gridItemDecoration;
        super.addItemDecoration(gridItemDecoration);
        return this;
    }

    public IGridItemDecoration getGridItemDecoration() {
        return gridItemDecoration;
    }

    @Override
    public void setAdapter(final RecyclerView.Adapter adapter) {
        final GridLayoutManager layoutManager = new GridLayoutManager(getContext(), spanCount, RecyclerView.VERTICAL, false);
        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (arrayFullSpan.get(position) != null) return layoutManager.getSpanCount();
                return 1;
            }
        });
        setLayoutManager(layoutManager);
        super.setAdapter(adapter);
    }
}
