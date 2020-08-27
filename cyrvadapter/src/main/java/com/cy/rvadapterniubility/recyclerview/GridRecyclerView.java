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

public class GridRecyclerView extends BaseRecyclerView<GridRecyclerView> implements IGridRecyclerView {
    private int spanCount = 2;
    private int orientation = RecyclerView.VERTICAL;
    private SparseArray<Boolean> arrayFullSpan;
    private GridItemDecoration gridItemDecoration;

    public GridRecyclerView(Context context) {
        this(context, null);
    }

    public GridRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        arrayFullSpan = new SparseArray<>();
        addItemDecoration(new GridItemDecoration(ScreenUtils.dpAdapt(context, 10)));
    }

    public GridRecyclerView setSpanCount(int spanCount) {
        this.spanCount = spanCount;
        return this;
    }

    public GridRecyclerView setOrientation(int orientation) {
        this.orientation = orientation;
        return this;
    }

    @Override
    public <T extends IGridRecyclerView> T addFullSpanPosition(int position) {
        arrayFullSpan.put(position, true);
        return (T) this;
    }

    @Override
    public <T extends IGridRecyclerView> T removeFullSpanPosition(int position) {
        arrayFullSpan.remove(position);
        return (T) this;
    }

    public SparseArray<Boolean> getArrayFullSpan() {
        return arrayFullSpan;
    }


    public void addItemDecoration(GridItemDecoration gridItemDecoration) {
        this.gridItemDecoration = gridItemDecoration;
        super.addItemDecoration(gridItemDecoration);
    }


    @Override
    public <T extends IGridItemDecoration> T getGridItemDecoration() {
        return (T) gridItemDecoration;
    }
    @Override
    public <T extends RecyclerView> T getRecyclerView() {
        return (T) this;
    }

    @Override
    public void setAdapter(final RecyclerView.Adapter adapter) {
        final GridLayoutManager layoutManager = new GridLayoutManager(getContext(), spanCount, orientation, false);
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
