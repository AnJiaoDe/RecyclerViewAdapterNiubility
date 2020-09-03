package com.cy.rvadapterniubility.recyclerview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;

import com.cy.rvadapterniubility.adapter.ItemAnimCallback;

/**
 * Created by cy on 2017/7/2.
 */

public class BaseRecyclerView<T extends BaseRecyclerView> extends RecyclerView {
    //永远<=0
    private int offsetX = 0;
    private int offsetY = 0;

    private ItemTouchHelper itemTouchHelper;
    private ItemAnimCallback itemAnimCallback;

    public BaseRecyclerView(Context context) {
        this(context, null);
    }

    public BaseRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setOverScrollMode(OVER_SCROLL_NEVER);
        setEnableAnimDefault(false);
        addOnScrollListener(new OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                offsetX -= dx;
                offsetY -= dy;
            }
        });
    }

    public T setEnableAnimDefault(boolean enable) {
        SimpleItemAnimator simpleItemAnimator = (SimpleItemAnimator) getItemAnimator();
        //去除难看的默认闪烁动画
        if (simpleItemAnimator != null) simpleItemAnimator.setSupportsChangeAnimations(enable);
        return (T) this;
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
        scrollBy(offsetX - x, offsetY - y);
    }

    public int getOffsetX() {
        return offsetX;
    }

    public int getOffsetY() {
        return offsetY;
    }

    public T setOffsetX(int offsetX) {
        this.offsetX = offsetX;
        return (T) this;
    }

    public T setOffsetY(int offsetY) {
        this.offsetY = offsetY;
        return (T) this;
    }

    public T addOnScrollListener(OnVerticalScrollListener onSimpleScrollListener) {
        super.addOnScrollListener(onSimpleScrollListener.getOnScrollListener());
        return (T) this;
    }

    public T addItemTouchAnim(final ItemAnimCallback itemAnimCallback) {
        this.itemAnimCallback = itemAnimCallback;
        itemTouchHelper = new ItemTouchHelper(itemAnimCallback);
        itemTouchHelper.attachToRecyclerView(this);
        return (T) this;
    }

    public T setDragTouchView(final ViewHolder holder, View view) {
        if (itemAnimCallback == null) return (T) this;
        itemAnimCallback.setLongPressDragEnabled(false);
        view.setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                itemTouchHelper.startDrag(holder);
                return false;
            }
        });
        return (T) this;
    }

    public ItemTouchHelper getItemTouchHelper() {
        return itemTouchHelper;
    }

    public T clear() {
        itemTouchHelper = null;
        return (T) this;
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        clear();
    }
}
