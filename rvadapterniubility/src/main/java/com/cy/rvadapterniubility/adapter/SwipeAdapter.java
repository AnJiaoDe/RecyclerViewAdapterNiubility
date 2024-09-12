package com.cy.rvadapterniubility.adapter;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cy.rvadapterniubility.swipelayout.OnSwipeListener;
import com.cy.rvadapterniubility.swipelayout.SwipeLayout;

/**
 * Created by cy on 2018/3/29.类似策略模式,引入IAdapter接口，面向多态编程
 */

public abstract class SwipeAdapter<T> implements IAdapter<T, BaseViewHolder, SimpleAdapter> {
    private SimpleAdapter<T> simpleAdapter;
    private SwipeLayout swipeLayout_opened;
    private SwipeLayout swipeLayout_scrolled;

    public SwipeAdapter() {
        simpleAdapter = new SimpleAdapter<T>() {
            @Override
            public void bindDataToView(final BaseViewHolder holder, int position, T bean) {
                dealSwipe(holder, bean);
                SwipeAdapter.this.bindDataToView(holder, position, bean);
            }

            @Override
            public void onViewRecycled(BaseViewHolder holder, int position, T bean) {
                super.onViewRecycled(holder, position, bean);
                SwipeAdapter.this.onViewRecycled(holder,position,bean);
            }

            @Override
            public int getItemLayoutID(int position, T bean) {
                return SwipeAdapter.this.getItemLayoutID(position, bean);
            }

            @Override
            public void onItemClick(BaseViewHolder holder, int position, T bean) {
                SwipeAdapter.this.onItemClick(holder, position, bean);
            }

            @Override
            public void onItemLongClick(BaseViewHolder holder, int position, T bean) {
                SwipeAdapter.this.onItemLongClick(holder, position, bean);
            }

            @Override
            public void onItemMove(int fromPosition, int toPosition, BaseViewHolder srcHolder, BaseViewHolder targetHolder) {
                super.onItemMove(fromPosition, toPosition, srcHolder, targetHolder);
                SwipeAdapter.this.onItemMove(fromPosition,toPosition,srcHolder,targetHolder);
            }

            @Override
            public void onViewAttachedToWindow(@NonNull BaseViewHolder holder) {
                super.onViewAttachedToWindow(holder);
                SwipeAdapter.this.onViewAttachedToWindow(holder);

            }
        };
    }

    private void dealSwipe(final BaseViewHolder holder, final T bean) {
        ((SwipeLayout) holder.itemView).getContentView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getBindingAdapterPosition();
                if(position<0||position>=simpleAdapter.getList_bean().size())return;
                simpleAdapter.onItemClick(holder, position, bean);
            }
        });
        ((SwipeLayout) holder.itemView).setOnSwipeListener(new OnSwipeListener() {
            @Override
            public void onScrolled(int dx) {
                swipeLayout_scrolled = (SwipeLayout) holder.itemView;
                int position = holder.getBindingAdapterPosition();
                if(position<0||position>=simpleAdapter.getList_bean().size())return;
                SwipeAdapter.this.onScrolled(holder,position, bean, dx);
            }

            @Override
            public void onOpened() {
                swipeLayout_opened = (SwipeLayout) holder.itemView;
                int position = holder.getBindingAdapterPosition();
                if(position<0||position>=simpleAdapter.getList_bean().size())return;
                SwipeAdapter.this.onOpened(holder, position, bean);
            }

            @Override
            public void onClosed() {
                swipeLayout_opened = null;
                int position = holder.getBindingAdapterPosition();
                if(position<0||position>=simpleAdapter.getList_bean().size())return;
                SwipeAdapter.this.onClosed(holder, position, bean);
            }
        });
    }

    @Override
    public void onViewRecycled(BaseViewHolder holder, int position, T bean) {

    }

    public void onScrolled(BaseViewHolder holder, int position, T bean, int dx) {
    }

    public void onOpened(BaseViewHolder holder, int position, T bean) {
    }

    public void onClosed(BaseViewHolder holder, int position, T bean) {
    }

    @Override
    public void onItemLongClick(BaseViewHolder holder, int position, T bean) {

    }

    @Override
    public void onItemMove(int fromPosition, int toPosition, BaseViewHolder srcHolder, BaseViewHolder targetHolder) {

    }

    /**
     * 再次彰显面向多态编程的威力，接口的强扩展
     *
     * @param holder
     */
    @Override
    public void onViewAttachedToWindow(BaseViewHolder holder) {

    }


    public SwipeLayout getOpened() {
        return swipeLayout_opened;
    }

    public void closeOpened() {
        swipeLayout_opened.close();
        swipeLayout_opened = null;
    }

    public void closeOpened(OnSwipeListener onSwipeListener) {
        swipeLayout_opened.close(onSwipeListener);
        swipeLayout_opened = null;
    }

    public SwipeLayout getScrolled() {
        return swipeLayout_scrolled;
    }

    public void closeScrolled() {
        swipeLayout_scrolled.close();
        swipeLayout_scrolled = null;
    }

    @Override
    public SimpleAdapter<T> getAdapter() {
        return simpleAdapter;
    }
}
