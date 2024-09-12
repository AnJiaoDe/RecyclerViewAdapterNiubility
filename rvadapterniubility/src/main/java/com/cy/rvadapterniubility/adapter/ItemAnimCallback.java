package com.cy.rvadapterniubility.adapter;


import android.graphics.Canvas;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cy.rvadapterniubility.swipelayout.SwipeRecyclerView;

import java.util.Collections;

public class ItemAnimCallback extends ItemTouchHelper.Callback {
    private SimpleAdapter simpleAdapter;
    private boolean isLongPressDragEnabled = true;
    private boolean isItemViewSwipeEnabled = true;

    public ItemAnimCallback(SimpleAdapter simpleAdapter) {
        this.simpleAdapter = simpleAdapter;
    }

    public void setLongPressDragEnabled(boolean longPressDragEnabled) {
        isLongPressDragEnabled = longPressDragEnabled;
    }

    public void setItemViewSwipeEnabled(boolean itemViewSwipeEnabled) {
        isItemViewSwipeEnabled = itemViewSwipeEnabled;
    }

    @Override
    public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            LinearLayoutManager linearLayoutManager = (LinearLayoutManager) layoutManager;
            if (linearLayoutManager.getOrientation() == LinearLayoutManager.HORIZONTAL) {
                int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.LEFT |
                        ItemTouchHelper.RIGHT;
                int swipeFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
                return makeMovementFlags(dragFlags, swipeFlags);
            } else {
                int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.LEFT |
                        ItemTouchHelper.RIGHT;
                int swipeFlags = ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
                return makeMovementFlags(dragFlags, swipeFlags);
            }
        } else if (layoutManager instanceof LinearLayoutManager) {
            LinearLayoutManager linearLayoutManager = (LinearLayoutManager) layoutManager;
            if (linearLayoutManager.getOrientation() == LinearLayoutManager.HORIZONTAL) {
                int dragFlags = ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
                int swipeFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
                return makeMovementFlags(dragFlags, swipeFlags);
            } else {
                int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
                int swipeFlags = ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
                return makeMovementFlags(dragFlags, swipeFlags);
            }
        }
        return makeMovementFlags(0, 0);
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder srcHolder,
                          @NonNull RecyclerView.ViewHolder targetHolder) {
        // 不同的ViewType不能拖拽换位置。
        if (srcHolder.getItemViewType() != targetHolder.getItemViewType()) return false;

        int fromPosition = srcHolder.getAbsoluteAdapterPosition();
        int toPosition = targetHolder.getAbsoluteAdapterPosition();

        simpleAdapter.swap( fromPosition, toPosition);
        simpleAdapter.notifyItemMoved(fromPosition, toPosition);
        simpleAdapter.onItemMove__(fromPosition,toPosition,srcHolder,targetHolder);
        return true;// 返回true表示处理了并可以换位置，返回false表示你没有处理并不能换位置。
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        simpleAdapter.remove(viewHolder.getBindingAdapterPosition());
    }
    @Override
    public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        // 判断当前是否是swipe方式：侧滑。
        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
            //1.ItemView--ViewHolder; 2.侧滑条目的透明度程度关联谁？dX(delta增量，范围：当前条目-width~width)。
            RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
            float alpha = 1;
            if (layoutManager instanceof LinearLayoutManager) {
                int orientation = ((LinearLayoutManager)layoutManager).getOrientation();
                if (orientation == LinearLayoutManager.HORIZONTAL) {
                    alpha = 1 - Math.abs(dY) / viewHolder.itemView.getHeight();
                } else if (orientation == LinearLayoutManager.VERTICAL) {
                    alpha = 1 - Math.abs(dX) / viewHolder.itemView.getWidth();
                }
            }
            viewHolder.itemView.setAlpha(alpha);//1~0
        }
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
    }

    @Override
    public boolean isLongPressDragEnabled() {
        return isLongPressDragEnabled;
    }

    @Override
    public boolean isItemViewSwipeEnabled() {
        return isItemViewSwipeEnabled;
    }

    @Override
    public final void onSelectedChanged(@Nullable RecyclerView.ViewHolder viewHolder, int actionState) {
        super.onSelectedChanged(viewHolder, actionState);
        if (actionState != ItemTouchHelper.ACTION_STATE_IDLE)
            onSelectedChanged__(viewHolder, actionState);
    }

    public void onSelectedChanged__(@Nullable RecyclerView.ViewHolder viewHolder, int actionState) {
    }

}
