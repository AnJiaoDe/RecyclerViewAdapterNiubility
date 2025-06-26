package com.cy.rvadapterniubility.adapter;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.cy.rvadapterniubility.swipelayout.OnSwipeListener;
import com.cy.rvadapterniubility.swipelayout.SwipeLayout;

public abstract class SwipeAdapter<T> extends SimpleAdapter<T> {
    private SwipeLayout swipeLayout_opened;
    private SwipeLayout swipeLayout_scrolled;

    @Override
    public final void bindDataToView(BaseViewHolder holder, int position, T bean) {
        dealSwipe(holder, bean);
        bindDataToView__(holder, position, bean);
    }
    public abstract void bindDataToView__(BaseViewHolder holder, int position, T bean) ;

    private void dealSwipe(final BaseViewHolder holder, final T bean) {
        ((SwipeLayout) holder.itemView).getContentView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getBindingAdapterPosition();
                if(position<0||position>=getList_bean().size())return;
                onItemClick(holder, position, bean);
            }
        });
        ((SwipeLayout) holder.itemView).setOnSwipeListener(new OnSwipeListener() {
            @Override
            public void onScrolled(int dx) {
                swipeLayout_scrolled = (SwipeLayout) holder.itemView;
                int position = holder.getBindingAdapterPosition();
                if(position<0||position>=getList_bean().size())return;
                SwipeAdapter.this.onScrolled(holder,position, bean, dx);
            }

            @Override
            public void onOpened() {
                swipeLayout_opened = (SwipeLayout) holder.itemView;
                int position = holder.getBindingAdapterPosition();
                if(position<0||position>=getList_bean().size())return;
                SwipeAdapter.this.onOpened(holder, position, bean);
            }

            @Override
            public void onClosed() {
                swipeLayout_opened = null;
                int position = holder.getBindingAdapterPosition();
                if(position<0||position>=getList_bean().size())return;
                SwipeAdapter.this.onClosed(holder, position, bean);
            }
        });
    }


    public void onScrolled(BaseViewHolder holder, int position, T bean, int dx) {
    }

    public void onOpened(BaseViewHolder holder, int position, T bean) {
    }

    public void onClosed(BaseViewHolder holder, int position, T bean) {
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
}
