package com.cy.rvadapterniubility.adapter;


import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

public interface IAdapter<T, V extends RecyclerView.ViewHolder, A extends RecyclerView.Adapter> {

    @Nullable
    public Object setHolderTagPreBindData(V holder, int position, T bean);

    public void bindDataToView(V holder, int position, T bean);

    public void recycleData(@Nullable Object tag);

//    public void onViewRecycled(V holder, int position, T bean);

    public int getItemLayoutID(int position, T bean);

    public void onItemClick(V holder, int position, T bean);

    public void onItemLongClick(V holder, int position, T bean);

    public void onItemMove(int fromPosition, int toPosition, V srcHolder, V targetHolder);

//    public void onViewAttachedToWindow(V holder);

    public A getAdapter();

}
