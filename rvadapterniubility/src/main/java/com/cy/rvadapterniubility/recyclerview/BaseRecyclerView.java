package com.cy.rvadapterniubility.recyclerview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.MotionEventCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;

import com.cy.rvadapterniubility.LogUtils;
import com.cy.rvadapterniubility.adapter.BaseViewHolder;
import com.cy.rvadapterniubility.adapter.DragSelectFrameLayout;
import com.cy.rvadapterniubility.adapter.DragSelectorAdapter;
import com.cy.rvadapterniubility.adapter.ItemAnimCallback;
import com.cy.rvadapterniubility.adapter.SimpleAdapter;

import java.util.Objects;

/**
 * Created by cy on 2017/7/2.
 */

public class BaseRecyclerView<T extends BaseRecyclerView> extends RecyclerView {
    //永远<=0
    private int offsetX = 0;
    private int offsetY = 0;
    private int velocity_x;
    private int velocity_y;
    private ItemTouchHelper itemTouchHelper;
    private ItemAnimCallback itemAnimCallback;
    private VelocityTracker velocityTracker;
    private OnItemTouchListener onItemTouchListener;
    private GestureDetector gestureDetector;
    private boolean isLongPress = false;
    private float downX;
    private float downY;
    private float touchSlop;

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

        gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                return true;
            }

            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                LogUtils.log("onSingleTapUp");
                return super.onSingleTapUp(e);
            }

            @Override
            public void onLongPress(MotionEvent e) {
                super.onLongPress(e);
                LogUtils.log("onLongPress");

                isLongPress = true;

//                if (dragSelectorAdapter == null) return;
//                dragSelectorAdapter.setHaveChildLongPress(true);
//                int position = recyclerView.getChildAdapterPosition(DragSelectFrameLayout.this);
//                if (position < 0 || position >= dragSelectorAdapter.getAdapter().getList_bean().size())
//                    return;
//                dragSelectorAdapter.onItemLongClick((BaseViewHolder) recyclerView.findViewHolderForAdapterPosition(position),
//                        position, dragSelectorAdapter.getAdapter().getList_bean().get(position));
            }

        });

        final ViewConfiguration viewConfiguration = ViewConfiguration.get(context);
        touchSlop = viewConfiguration.getScaledTouchSlop();
    }

    public T addOnItemTouchListener(final DragSelectorAdapter<?> dragSelectorAdapter) {
        if (onItemTouchListener != null) removeOnItemTouchListener(onItemTouchListener);
        addOnItemTouchListener(onItemTouchListener = dragSelectorAdapter);
        return (T) this;
    }

    public T addOnScrollListener(OnSimpleScrollListener onSimpleScrollListener) {
        super.addOnScrollListener(onSimpleScrollListener.getOnScrollListener());
        return (T) this;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        gestureDetector.onTouchEvent(event);
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_POINTER_DOWN:
            case MotionEvent.ACTION_DOWN:
                LogUtils.log("dispatchTouchEvent  ACTION_DOWN");
                isLongPress = false;
                downX = event.getX();
                downY = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                LogUtils.log("dispatchTouchEvent  ACTION_MOVE");
                float moveX = event.getX();
                float moveY = event.getY();
                float dy = Math.abs(moveY - downY);
                boolean moveV = dy > touchSlop && dy >= Math.abs(moveX - downX);
                downX = moveX;
                downY = moveY;
                if (!moveV) {
                    return true;
                }
                if (isLongPress) return true;
                break;
            case MotionEvent.ACTION_CANCEL:
                LogUtils.log("dispatchTouchEvent  ACTION_CANCEL");
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:
                LogUtils.log("dispatchTouchEvent  ACTION_UP");
                isLongPress = false;
                break;
        }
        return super.dispatchTouchEvent(event);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_POINTER_DOWN:
            case MotionEvent.ACTION_DOWN:
                LogUtils.log("onInterceptTouchEvent  ACTION_DOWN");
                break;
            case MotionEvent.ACTION_MOVE:
                LogUtils.log("onInterceptTouchEvent  ACTION_MOVE");
                break;
            case MotionEvent.ACTION_CANCEL:
                LogUtils.log("onInterceptTouchEvent  ACTION_CANCEL");
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:
                LogUtils.log("onInterceptTouchEvent  ACTION_UP");
                break;
        }
        return super.onInterceptTouchEvent(event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (velocityTracker == null) velocityTracker = VelocityTracker.obtain();
        velocityTracker.addMovement(ev);
        switch (ev.getAction()) {
            case MotionEvent.ACTION_UP:
                velocityTracker.computeCurrentVelocity(1000);
                velocity_x = (int) velocityTracker.getXVelocity();
                velocity_y = (int) velocityTracker.getYVelocity();
//                LogUtils.log("velocity_x", velocity_x);
//                LogUtils.log("velocity_y", velocity_y);
                if (velocityTracker != null) {
                    velocityTracker.recycle();
                    velocityTracker = null;
                }
                break;
            case MotionEvent.ACTION_CANCEL:
                if (velocityTracker != null) {
                    velocityTracker.recycle();
                    velocityTracker = null;
                }
                break;
        }
        return super.onTouchEvent(ev);
    }

    public int getVelocity_x() {
        return velocity_x;
    }

    public int getVelocity_y() {
        return velocity_y;
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
