package com.cy.rvadapterniubility.adapter;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.cy.rvadapterniubility.swipelayout.SwipeRecyclerView;

import java.util.Collections;

public  class ItemAnimCallback extends ItemTouchHelper.Callback {
    private SimpleAdapter simpleAdapter;
    private boolean isLongPressDragEnabled=true;
    public ItemAnimCallback(SimpleAdapter simpleAdapter) {
        this.simpleAdapter = simpleAdapter;
    }

    public void setLongPressDragEnabled(boolean longPressDragEnabled) {
        isLongPressDragEnabled = longPressDragEnabled;
    }

    @Override
    public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        final int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
        final int swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END;
        if(recyclerView instanceof SwipeRecyclerView)return makeMovementFlags(dragFlags, ItemTouchHelper.ACTION_STATE_IDLE);
        return makeMovementFlags(dragFlags, swipeFlags);
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        int position_from = viewHolder.getBindingAdapterPosition();
        int position_to = target.getBindingAdapterPosition();
        Collections.swap(simpleAdapter.getList_bean(), position_from, position_to);
        simpleAdapter.notifyItemMoved(position_from, position_to);
        return false;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        simpleAdapter.remove(viewHolder.getBindingAdapterPosition());
    }

    @Override
    public boolean isLongPressDragEnabled() {
        return isLongPressDragEnabled;
    }


}
