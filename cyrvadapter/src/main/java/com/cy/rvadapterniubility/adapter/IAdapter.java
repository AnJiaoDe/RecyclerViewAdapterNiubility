package com.cy.rvadapterniubility.adapter;


import androidx.recyclerview.widget.RecyclerView;

public interface IAdapter<T, V extends RecyclerView.ViewHolder> {

    public void bindDataToView(V holder, int position, T bean, boolean isSelected);

    public int getItemLayoutID(int position, T bean);

    public void onItemClick(V holder, int position, T bean);

    public void onItemLongClick(V holder, int position, T bean);

    public void onViewAttachedToWindow(V holder);

}
