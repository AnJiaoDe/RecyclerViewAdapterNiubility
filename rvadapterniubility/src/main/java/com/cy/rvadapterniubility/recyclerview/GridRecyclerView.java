package com.cy.rvadapterniubility.recyclerview;

import android.content.Context;
import android.util.AttributeSet;
import android.util.SparseArray;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.ConcatAdapter;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cy.refreshlayoutniubility.ScreenUtils;
import com.cy.rvadapterniubility.adapter.HeadAdapter;
import com.cy.rvadapterniubility.adapter.SimpleAdapter;


/**
 * Created by cy on 2017/7/2.
 */

public class GridRecyclerView<T extends GridRecyclerView> extends DragSelectRecyclerView<T> {
    private int spanCount = 2;
    //    private SparseArray<Boolean> sparseArrayFullSpan;
    private GridItemDecoration gridItemDecoration;

    public GridRecyclerView(Context context) {
        this(context, null);
    }

    public GridRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
//        sparseArrayFullSpan = new SparseArray<>();
        addItemDecoration(new GridItemDecoration(ScreenUtils.dpAdapt(context, 10)));
    }

    public T setSpanCount(int spanCount) {
        this.spanCount = spanCount;
        return (T) this;
    }

    public int getSpanCount() {
        return spanCount;
    }

    protected void setSpanSizeLookup(final GridLayoutManager gridLayoutManager) {
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                RecyclerView.Adapter<?> adapter = getAdapter();

                if (adapter == null || position < 0 || position >= adapter.getItemCount()) {
                    return 1;
                }

                int spanCount = gridLayoutManager.getSpanCount();

                if (adapter instanceof SimpleAdapter) {
                    SimpleAdapter<?> simpleAdapter = (SimpleAdapter<?>) adapter;
                    return simpleAdapter.isFullSpan(simpleAdapter.getItemViewType(position))
                            ? spanCount : 1;

                } else if (adapter instanceof HeadAdapter) {
                    HeadAdapter<?, ?> headAdapter = (HeadAdapter<?, ?>) adapter;
                    return headAdapter.isFullSpan(position) ? spanCount : 1;

                } else if (adapter instanceof ConcatAdapter) {
                    ConcatAdapter concatAdapter = (ConcatAdapter) adapter;
                    int remainPosition = position;

                    for (RecyclerView.Adapter<?> child : concatAdapter.getAdapters()) {
                        int itemCount = child.getItemCount();

                        if (remainPosition < itemCount) {
                            if (child instanceof SimpleAdapter) {
                                SimpleAdapter<?> simpleAdapter = (SimpleAdapter<?>) child;
                                return simpleAdapter.isFullSpan(
                                        simpleAdapter.getItemViewType(remainPosition))
                                        ? spanCount : 1;

                            } else if (child instanceof HeadAdapter) {
                                HeadAdapter<?, ?> headAdapter = (HeadAdapter<?, ?>) child;
                                return headAdapter.isFullSpan(remainPosition)
                                        ? spanCount : 1;
                            }

                            return 1;
                        }

                        remainPosition -= itemCount;
                    }
                }

                return 1;
            }
        });
    }
//    public T putFullSpanPosition(int position) {
//        sparseArrayFullSpan.put(position, true);
//        return (T) this;
//    }
//
//    public T removeFullSpanPosition(int position) {
//        sparseArrayFullSpan.remove(position);
//        return (T) this;
//    }
//
//    public T clearFullSpanPositions() {
//        sparseArrayFullSpan.clear();
//        return (T) this;
//    }
//
//
//    public SparseArray<Boolean> getSparseArrayFullSpan() {
//        return sparseArrayFullSpan;
//    }

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
