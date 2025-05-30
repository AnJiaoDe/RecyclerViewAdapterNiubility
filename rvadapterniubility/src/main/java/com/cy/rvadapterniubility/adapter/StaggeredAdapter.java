package com.cy.rvadapterniubility.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

/**
 * Created by cy on 2018/3/29.类似策略模式,引入IAdapter接口，面向多态编程
 */

public abstract class StaggeredAdapter<T> implements IAdapter<T, BaseViewHolder, SimpleAdapter> {
    private SimpleAdapter<T> simpleAdapter;

    public StaggeredAdapter() {
        simpleAdapter = new SimpleAdapter<T>() {
            @NonNull
            @Override
            public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                BaseViewHolder baseViewHolder = new BaseViewHolder(LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false));
                if (isFullSpan(viewType)) {
                    baseViewHolder.setFullSpan(true);
                    ((StaggeredGridLayoutManager.LayoutParams) baseViewHolder.itemView.getLayoutParams()).setFullSpan(true);
                }
                return baseViewHolder;
            }

            @Override
            public int getItemLayoutID(int position, T bean) {
                return StaggeredAdapter.this.getItemLayoutID(position, bean);
            }

            @Override
            public void recycleData(@Nullable Object tag) {
                StaggeredAdapter.this.recycleData(tag);
            }

            @Nullable
            @Override
            public Object setHolderTagPreBindData(BaseViewHolder holder, int position, T bean) {
                return StaggeredAdapter.this.setHolderTagPreBindData(holder, position, bean);
            }

            @Override
            public void bindDataToView(final BaseViewHolder holder, int position, T bean) {
                StaggeredAdapter.this.bindDataToView(holder, position, bean);
            }


            @Override
            public void onItemClick(BaseViewHolder holder, int position, T bean) {
                StaggeredAdapter.this.onItemClick(holder, position, bean);
            }

            @Override
            public void onItemLongClick(BaseViewHolder holder, int position, T bean) {
                StaggeredAdapter.this.onItemLongClick(holder, position, bean);
            }

            @Override
            public void onItemMove(int fromPosition, int toPosition, BaseViewHolder srcHolder, BaseViewHolder targetHolder) {
                super.onItemMove(fromPosition, toPosition, srcHolder, targetHolder);
                StaggeredAdapter.this.onItemMove(fromPosition, toPosition, srcHolder, targetHolder);
            }

        };
    }

    public boolean isFullSpan(int itemLayoutID) {
        return false;
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
