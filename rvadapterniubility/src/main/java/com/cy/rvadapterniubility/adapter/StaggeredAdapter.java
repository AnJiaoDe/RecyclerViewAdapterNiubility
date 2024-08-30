package com.cy.rvadapterniubility.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
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
            public int getItemViewType(int position) {
                return super.getItemViewType(position);
            }


            @Override
            public int getItemLayoutID(int position, T bean) {
                return StaggeredAdapter.this.getItemLayoutID(position, bean);
            }

//            @Override
//            public long getItemId(int position) {
//                return StaggeredAdapter.this.getItemId(position);
//            }
//
//            @Override
//            public boolean hasStableIds_() {
//                return StaggeredAdapter.this.hasStableIds_();
//            }

            @Override
            public void bindDataToView(final BaseViewHolder holder, int position, T bean) {
                StaggeredAdapter.this.bindDataToView(holder, position, bean);
            }

            @Override
            public void onViewRecycled(BaseViewHolder holder, int position, T bean) {
                StaggeredAdapter.this.onViewRecycled(holder, position, bean);
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
            public void onViewAttachedToWindow(@NonNull BaseViewHolder holder) {
                super.onViewAttachedToWindow(holder);
                StaggeredAdapter.this.onViewAttachedToWindow(holder);
            }

        };
    }

//    @Override
//    public long getItemId(int position) {
//        return position;
//    }

//    @Override
//    public boolean hasStableIds_() {
//        return true;
//    }

    public  boolean isFullSpan(int itemLayoutID){
        return false;
    }


    @Override
    public void onItemLongClick(BaseViewHolder holder, int position, T bean) {

    }

    /**
     * 再次彰显面向多态编程的威力，接口的强扩展
     *
     * @param holder
     */
    @Override
    public void onViewAttachedToWindow(BaseViewHolder holder) {

    }

    @Override
    public void onViewRecycled(BaseViewHolder holder, int position, T bean) {

    }


    @Override
    public SimpleAdapter<T> getAdapter() {
        return simpleAdapter;
    }
}
