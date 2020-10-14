package com.cy.rvadapterniubility.recyclerview;

import android.content.Context;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.MotionEvent;
import android.view.ViewParent;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cy.refreshlayoutniubility.ScreenUtils;
import com.cy.rvadapterniubility.adapter.MultiAdapter;


/**
 * Created by cy on 2017/7/2.
 */

public class HorizontalGridRecyclerView extends BaseRecyclerView<HorizontalGridRecyclerView> {
    private int spanCount = 2;
    private SparseArray<Boolean> arrayFullSpan;
    private IGridItemDecoration gridItemDecoration;

    private int downX; // 按下时 X轴坐标值
    private int downY; // 按下时 Y 轴坐标值

    public HorizontalGridRecyclerView(Context context) {
        this(context, null);
    }

    public HorizontalGridRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        arrayFullSpan = new SparseArray<>();
        addItemDecoration(new GridItemDecoration(ScreenUtils.dpAdapt(context, 10)));
    }

    public HorizontalGridRecyclerView setSpanCount(int spanCount) {
        this.spanCount = spanCount;
        return this;
    }


    public HorizontalGridRecyclerView addFullSpanPosition(int position) {
        arrayFullSpan.put(position, true);
        return this;
    }

    public HorizontalGridRecyclerView removeFullSpanPosition(int position) {
        arrayFullSpan.remove(position);
        return this;
    }

    public SparseArray<Boolean> getArrayFullSpan() {
        return arrayFullSpan;
    }


    public HorizontalGridRecyclerView addItemDecoration(GridItemDecoration gridItemDecoration) {
        if(this.gridItemDecoration!=null)removeItemDecoration(this.gridItemDecoration.getGridItemDecoration());
        this.gridItemDecoration = gridItemDecoration;
        super.addItemDecoration(gridItemDecoration);
        return this;
    }
    public HorizontalGridRecyclerView addItemDecoration(FullSpanGridItemDecoration gridItemDecoration) {
        if(this.gridItemDecoration!=null)removeItemDecoration(this.gridItemDecoration.getGridItemDecoration());
        this.gridItemDecoration = gridItemDecoration;
        super.addItemDecoration(gridItemDecoration);
        return this;
    }

    public IGridItemDecoration getGridItemDecoration() {
        return gridItemDecoration;
    }

    @Override
    public void setAdapter(final Adapter adapter) {
        final GridLayoutManager layoutManager = new GridLayoutManager(getContext(), spanCount, RecyclerView.HORIZONTAL, false);
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
    public HorizontalGridRecyclerView setAdapter(MultiAdapter multiAdapter, OnGridLoadMoreListener onRVLoadMoreListener) {
        addOnScrollListener(onRVLoadMoreListener);
        setAdapter(multiAdapter.getMergeAdapter());
        return this;
    }
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downX = (int) ev.getX();
                downY = (int) ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                int moveX = (int) ev.getX();
                int moveY = (int) ev.getY();
                if (Math.abs(moveX - downX) > Math.abs(moveY - downY)) {
                    requestDisallowInterceptTouchEvent();
                }
                downX = moveX;
                downY = moveY;
        }
        return super.onInterceptTouchEvent(ev);
    }
    private void requestDisallowInterceptTouchEvent() {
        final ViewParent parent = getParent();
        if (parent != null) parent.requestDisallowInterceptTouchEvent(true);
    }
}
