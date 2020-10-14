package com.cy.rvadapterniubility.recyclerview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewParent;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cy.rvadapterniubility.adapter.MultiAdapter;

/**
 * Created by cy on 2017/7/2.
 */

public class HorizontalRecyclerView extends BaseRecyclerView<HorizontalRecyclerView> {
    private int downX; // 按下时 X轴坐标值
    private int downY; // 按下时 Y 轴坐标值

    public HorizontalRecyclerView(Context context) {
        this(context, null);
    }

    public HorizontalRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void setAdapter(RecyclerView.Adapter adapter) {
        setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        super.setAdapter(adapter);
    }

    public HorizontalRecyclerView setAdapter(MultiAdapter multiAdapter, OnLinearLoadMoreListener onRVLoadMoreListener) {
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

                int dx = moveX - downX;
                int dy = moveY - downY;
                downX = moveX;
                downY = moveY;
                if (Math.abs(dx) > Math.abs(dy) &&
                        ((dx > 0 && canScrollHorizontally(-1)) || (dx < 0 && canScrollHorizontally(1)))) {
                    requestDisallowInterceptTouch(true);
                }
                break;
        }
        return super.onInterceptTouchEvent(ev);
    }

    private void requestDisallowInterceptTouch(boolean disallowInterceptTouchEvent) {
        final ViewParent parent = getParent();
        if (parent != null) parent.requestDisallowInterceptTouchEvent(disallowInterceptTouchEvent);
    }

}
