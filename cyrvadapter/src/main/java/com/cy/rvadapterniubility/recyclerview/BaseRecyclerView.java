package com.cy.rvadapterniubility.recyclerview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;

import com.cy.rvadapterniubility.adapter.IScrollState;
import com.cy.rvadapterniubility.adapter.ItemAnimCallback;

/**
 * Created by cy on 2017/7/2.
 */

public class BaseRecyclerView<T extends BaseRecyclerView> extends RecyclerView {
    //永远<=0
    private int offsetX = 0;

    private ItemTouchHelper itemTouchHelper;
    private ItemAnimCallback itemAnimCallback;

    public BaseRecyclerView(Context context) {
        this(context, null);
    }

    public BaseRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setOverScrollMode(OVER_SCROLL_NEVER);
        //去除难看的默认闪烁动画
        SimpleItemAnimator simpleItemAnimator = (SimpleItemAnimator) getItemAnimator();
        if (simpleItemAnimator != null) simpleItemAnimator.setSupportsChangeAnimations(false);
        addOnScrollListener(new OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                offsetX -= dx;
            }
        });
    }
    /**
     * x为正，表示手指往左滑,x为负，表示手指往右滑
     *
     * @param x
     * @param y
     */
    @Override
    public void scrollBy(int x, int y) {
        super.scrollBy(x, y);
    }

    /**
     * x<=0
     * 比如 x=0,表示滑动到RecyclerView最左边，完全显示第一个item,
     * 比如 x=-100,表示RecyclerView左边100像素的界面被隐藏
     *
     * @param x
     * @param y
     */
    @Override
    public void scrollTo(int x, int y) {
        scrollBy(offsetX - x, y);
    }

    public int getOffsetX() {
        return offsetX;
    }

    public T addOnScrollListener(OnVerticalScrollListener onSimpleScrollListener) {
        super.addOnScrollListener(onSimpleScrollListener.getOnScrollListener());
        return (T) this;
    }


    public T addOnScrollHelper(final IScrollState scrollState) {
        addOnScrollListener(new OnVerticalScrollListener() {
            @Override
            public void onFirstScrolled(RecyclerView recyclerView, PositionHolder positionHolder) {
                super.onFirstScrolled(recyclerView, positionHolder);
                scrollState.onFirstOnScrolled(positionHolder);
            }

            @Override
            public void onScrollArrivedTop(RecyclerView recyclerView, PositionHolder positionHolder) {
                super.onScrollArrivedTop(recyclerView, positionHolder);
                scrollState.onScrollArrivedTop(positionHolder);
            }

            @Override
            public void onDragging(RecyclerView recyclerView, PositionHolder positionHolder) {
                super.onDragging(recyclerView, positionHolder);
                scrollState.onDragging(positionHolder);
            }

            @Override
            public void onIdle(RecyclerView recyclerView, PositionHolder positionHolder) {
                super.onIdle(recyclerView, positionHolder);
                scrollState.onIdle(positionHolder);
            }

            @Override
            public void onScrollingDown(RecyclerView recyclerView, int dy) {
                super.onScrollingDown(recyclerView, dy);
                scrollState.onScrollingDown(dy);
            }

            @Override
            public void onScrollingUp(RecyclerView recyclerView, int dy) {
                super.onScrollingUp(recyclerView, dy);
                scrollState.onScrollingUp(dy);
            }

            @Override
            public void onScrollArrivedBottom(RecyclerView recyclerView, PositionHolder positionHolder) {
                super.onScrollArrivedBottom(recyclerView, positionHolder);
                scrollState.onScrollArrivedBottom(positionHolder);
            }

            @Override
            public void onSettling(RecyclerView recyclerView, PositionHolder positionHolder) {
                super.onSettling(recyclerView, positionHolder);
                scrollState.onSettling(positionHolder);
            }
        }.getOnScrollListener());
        return (T) this;
    }

    public T addItemTouchAnim(final ItemAnimCallback itemAnimCallback) {
        this.itemAnimCallback = itemAnimCallback;
        itemTouchHelper = new ItemTouchHelper(itemAnimCallback);
        itemTouchHelper.attachToRecyclerView(this);
        return (T) this;
    }

    public void setDragTouchView(final ViewHolder holder, View view) {
        if (itemAnimCallback == null) return;
        itemAnimCallback.setLongPressDragEnabled(false);
        view.setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                itemTouchHelper.startDrag(holder);
                return false;
            }
        });
    }

    public ItemTouchHelper getItemTouchHelper() {
        return itemTouchHelper;
    }

    public void clear() {
        itemTouchHelper = null;
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        clear();
    }
}
