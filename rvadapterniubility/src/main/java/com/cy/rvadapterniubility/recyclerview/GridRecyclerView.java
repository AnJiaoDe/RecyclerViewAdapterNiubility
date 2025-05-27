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

public class GridRecyclerView<T extends GridRecyclerView> extends DragSelectRecyclerView<T> {
    private int spanCount = 2;
    private SparseArray<Boolean> sparseArrayFullSpan;
    private GridItemDecoration gridItemDecoration;

    public GridRecyclerView(Context context) {
        this(context, null);
    }

    public GridRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        sparseArrayFullSpan = new SparseArray<>();
        addItemDecoration(new GridItemDecoration(ScreenUtils.dpAdapt(context, 10)));
    }

    public T setSpanCount(int spanCount) {
        this.spanCount = spanCount;
        return (T) this;
    }

    public int getSpanCount() {
        return spanCount;
    }
    public T putFullSpanPosition(int position) {
        sparseArrayFullSpan.put(position, true);
        return (T) this;
    }

    public T removeFullSpanPosition(int position) {
        sparseArrayFullSpan.remove(position);
        return (T) this;
    }

    public SparseArray<Boolean> getSparseArrayFullSpan() {
        return sparseArrayFullSpan;
    }

    public T addItemDecoration(GridItemDecoration gridItemDecoration) {
        if (this.gridItemDecoration != null)
            removeItemDecoration(this.gridItemDecoration);
        this.gridItemDecoration = gridItemDecoration;
        super.addItemDecoration(gridItemDecoration);
        return (T) this;
    }
//
//    public GridRecyclerView addItemDecoration(FullSpanGridItemDecoration gridItemDecoration) {
//        if (this.gridItemDecoration != null)
//            removeItemDecoration(this.gridItemDecoration.getGridItemDecoration());
//        this.gridItemDecoration = gridItemDecoration;
//        super.addItemDecoration(gridItemDecoration);
//        return this;
//    }

    public GridItemDecoration getGridItemDecoration() {
        return gridItemDecoration;
    }
}
