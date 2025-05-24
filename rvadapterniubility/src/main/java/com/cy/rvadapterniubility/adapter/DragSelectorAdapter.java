package com.cy.rvadapterniubility.adapter;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.util.SparseArray;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.OverScroller;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.ViewCompat;
import androidx.core.widget.ScrollerCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.cy.rvadapterniubility.LogUtils;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by cy on 2018/3/29.类似策略模式,引入IAdapter接口，面向多态编程
 */

public abstract class DragSelectorAdapter<T> implements IAdapter<T, BaseViewHolder, SimpleAdapter>, RecyclerView.OnItemTouchListener {
    private SimpleAdapter<T> simpleAdapter;
    private Set<Integer> setSelector;
    private boolean useDragSelect = false;
    private int position_start = RecyclerView.NO_POSITION;
    private int position_end = RecyclerView.NO_POSITION;
    private int position_start_last = RecyclerView.NO_POSITION;
    private int position_end_last = RecyclerView.NO_POSITION;
    private boolean inTopScrollRange;
    private boolean inBottomScrollRange;
    private int topBoundFrom;
    private int topBoundTo;
    private int bottomBoundFrom;
    private int bottomBoundTo;
    private int scrollDistance;
    private float scrollSpeedFactor;
    private float x_last;
    private float y_last;
    private OverScroller overScroller;
    private RecyclerView recyclerView;
    private Runnable runnableScroll;
    private int maxScrollDistance = 32;

    public DragSelectorAdapter() {
        setSelector = new HashSet<>();
        simpleAdapter = new SimpleAdapter<T>() {
            @Override
            public void recycleData(@Nullable Object tag) {
                DragSelectorAdapter.this.recycleData(tag);
            }

            @Nullable
            @Override
            public Object setHolderTagPreBindData(BaseViewHolder holder, int position, T bean) {
                return DragSelectorAdapter.this.setHolderTagPreBindData(holder, position, bean);
            }

            @Override
            public void bindDataToView(final BaseViewHolder holder, int position, T bean) {
                DragSelectorAdapter.this.bindDataToView(holder, position, bean, setSelector.contains(position));
            }

            @Override
            public int getItemLayoutID(int position, T bean) {
                return DragSelectorAdapter.this.getItemLayoutID(position, bean);
            }

            @Override
            public void onItemClick(BaseViewHolder holder, int position, T bean) {
                DragSelectorAdapter.this.onItemClick(holder, position, bean);
            }

            @Override
            public void onItemLongClick(BaseViewHolder holder, int position, T bean) {
                DragSelectorAdapter.this.onItemLongClick(holder, position, bean);
            }

            @Override
            public void onItemMove(int fromPosition, int toPosition, BaseViewHolder srcHolder, BaseViewHolder targetHolder) {
                super.onItemMove(fromPosition, toPosition, srcHolder, targetHolder);
                DragSelectorAdapter.this.onItemMove(fromPosition, toPosition, srcHolder, targetHolder);
            }
        };
        runnableScroll = new Runnable() {
            @Override
            public void run() {
                if (overScroller != null && overScroller.computeScrollOffset()) {
                    scrollBy(scrollDistance);
                    ViewCompat.postOnAnimation(recyclerView, this);
                }
            }
        };
    }

    private void scrollBy(int distance) {
        int scrollDistance;
        if (distance > 0)
            scrollDistance = Math.min(distance, maxScrollDistance);
        else
            scrollDistance = Math.max(distance, -maxScrollDistance);
        recyclerView.scrollBy(0, scrollDistance);
        if (x_last != Float.MIN_VALUE && y_last != Float.MIN_VALUE)
            updateSelectedRange(x_last, y_last);
    }

    private void updateSelectedRange(float x, float y) {
        View child = recyclerView.findChildViewUnder(x, y);
        if (child != null) {
            int position = recyclerView.getChildAdapterPosition(child);
            if (position != RecyclerView.NO_POSITION && position_end != position) {
                position_end = position;

                int newStart, newEnd;
                newStart = Math.min(position_start, position_end);
                newEnd = Math.max(position_start, position_end);
                if (position_start_last == RecyclerView.NO_POSITION || position_end_last == RecyclerView.NO_POSITION) {
                    if (newEnd - newStart == 1)
                        toggleRange(newStart, newStart, true);
                    else
                        toggleRange(newStart, newEnd, true);
                } else {
                    if (newStart > position_start_last)
                        toggleRange(position_start_last, newStart - 1, false);
                    else if (newStart < position_start_last)
                        toggleRange(newStart, position_start_last - 1, true);
                    if (newEnd > position_end_last)
                        toggleRange(position_end_last + 1, newEnd, true);
                    else if (newEnd < position_end_last)
                        toggleRange(newEnd + 1, position_end_last, false);
                }
                position_start_last = newStart;
                position_end_last = newEnd;
            }
        }
    }

    private void reset() {
        useDragSelect = false;
        position_start = RecyclerView.NO_POSITION;
        position_end = RecyclerView.NO_POSITION;
        position_start_last = RecyclerView.NO_POSITION;
        position_end_last = RecyclerView.NO_POSITION;
        inTopScrollRange = false;
        inBottomScrollRange = false;
        x_last = Float.MIN_VALUE;
        y_last = Float.MIN_VALUE;
        stopScroll();
    }

    private void startScroll() {
        if (overScroller == null) overScroller = new OverScroller(recyclerView.getContext());
        if (overScroller.isFinished()) {
            recyclerView.removeCallbacks(runnableScroll);
            overScroller.startScroll(0, overScroller.getCurrY(), 0, 5000, 100000);
            ViewCompat.postOnAnimation(recyclerView, runnableScroll);
        }
    }


    private void stopScroll() {
        if (overScroller != null && !overScroller.isFinished()) {
            recyclerView.removeCallbacks(runnableScroll);
            overScroller.abortAnimation();
        }
    }

    @Override
    public boolean onInterceptTouchEvent(@NonNull RecyclerView recyclerView, @NonNull MotionEvent motionEvent) {
        this.recyclerView = recyclerView;
        if (!useDragSelect || getAdapter().getItemCount() == 0)
            return false;
        switch (motionEvent.getActionMasked()) {
            case MotionEvent.ACTION_POINTER_DOWN:
            case MotionEvent.ACTION_DOWN:
                reset();
                break;
        }
        return true;
    }

    @Override
    public void onTouchEvent(@NonNull RecyclerView recyclerView, @NonNull MotionEvent motionEvent) {
        if (!useDragSelect) return;
        LogUtils.log("onTouchEvent");
        switch (motionEvent.getActionMasked()) {
            case MotionEvent.ACTION_MOVE:
                updateSelectedRange(motionEvent.getX(), motionEvent.getY());
                int y = (int) motionEvent.getY();
                if (y >= topBoundFrom && y <= topBoundTo) {
                    x_last = motionEvent.getX();
                    y_last = motionEvent.getY();
                    scrollSpeedFactor = (((float) topBoundTo - (float) topBoundFrom) - ((float) y - (float) topBoundFrom))
                            / ((float) topBoundTo - (float) topBoundFrom);
                    scrollDistance = (int) ((float) maxScrollDistance * scrollSpeedFactor * -1f);
                    if (!inTopScrollRange) {
                        inTopScrollRange = true;
                        startScroll();
                    }
                } else if (y < topBoundFrom) {
                    x_last = motionEvent.getX();
                    y_last = motionEvent.getY();
                    scrollDistance = maxScrollDistance * -1;
                    if (!inTopScrollRange) {
                        inTopScrollRange = true;
                        startScroll();
                    }
                } else if (y >= bottomBoundFrom && y <= bottomBoundTo) {
                    x_last = motionEvent.getX();
                    y_last = motionEvent.getY();
                    scrollSpeedFactor = (((float) y - (float) bottomBoundFrom)) / ((float) bottomBoundTo - (float) bottomBoundFrom);
                    scrollDistance = (int) ((float) maxScrollDistance * scrollSpeedFactor);
                    if (!inBottomScrollRange) {
                        inBottomScrollRange = true;
                        startScroll();
                    }
                } else if (y > bottomBoundTo) {
                    x_last = motionEvent.getX();
                    y_last = motionEvent.getY();
                    scrollDistance = maxScrollDistance;
                    if (!inBottomScrollRange) {
                        inBottomScrollRange = true;
                        startScroll();
                    }
                } else {
                    inTopScrollRange = false;
                    inBottomScrollRange = false;
                    x_last = Float.MIN_VALUE;
                    y_last = Float.MIN_VALUE;
                    stopScroll();
                }
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:
                reset();
                break;
        }
    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

    }

    public boolean isUseDragSelect() {
        return useDragSelect;
    }

    public void startDragSelect(int position) {
        this.useDragSelect = true;
        position_start = position;
        position_end = position;
        position_start_last = position;
        position_end_last = position;
    }

    public void stopDragSelect() {
        useDragSelect = false;
    }

    public void toggle(final int position, boolean select) {
        if (select) {
            setSelector.add(position);
        } else {
            setSelector.remove(position);
        }
        //必须用handler，否则GG  Cannot call this method while RecyclerView is computing a layout or scrolling
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                simpleAdapter.notifyItemChanged(position);
            }
        });
    }

    public void toggleRange(final int start, final int end, boolean isSelected) {
        for (int i = start; i <= end; i++) {
            if (isSelected)
                setSelector.add(i);
            else
                setSelector.remove(i);
        }
        //必须用handler，否则GG  Cannot call this method while RecyclerView is computing a layout or scrolling
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                simpleAdapter.notifyItemRangeChanged(start, end - start + 1);
            }
        });
    }

    @Override
    public void recycleData(@Nullable Object tag) {

    }

    @Nullable
    @Override
    public Object setHolderTagPreBindData(BaseViewHolder holder, int position, T bean) {
        return null;
    }

    @Override
    public final void bindDataToView(BaseViewHolder holder, int position, T bean) {

    }

    public abstract void bindDataToView(BaseViewHolder holder, int position, T bean, boolean isSelected);

    @Override
    public void onItemLongClick(BaseViewHolder holder, int position, T bean) {

    }

    @Override
    public void onItemMove(int fromPosition, int toPosition, BaseViewHolder srcHolder, BaseViewHolder targetHolder) {

    }

    @Override
    public SimpleAdapter<T> getAdapter() {
        return simpleAdapter;
    }
}
