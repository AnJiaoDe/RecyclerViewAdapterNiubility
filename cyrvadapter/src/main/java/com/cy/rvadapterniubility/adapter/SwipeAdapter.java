package com.cy.rvadapterniubility.adapter;

import android.view.View;

import androidx.annotation.NonNull;

import com.cy.rvadapterniubility.swipelayout.SwipeLayout;

/**
 * Created by cy on 2018/3/29.类似策略模式,引入IAdapter接口，面向多态编程
 */

public abstract class SwipeAdapter<T> implements IAdapter<T, BaseViewHolder> {
    private IAdapter<T, BaseViewHolder> adapter;
    private SwipeLayout swipeLayout_opened;
    private SwipeLayout swipeLayout_scrolled;

    public SwipeAdapter() {
    }

    private void dealSwipe(final BaseViewHolder holder, final T bean) {
        ((SwipeLayout) holder.itemView).getContentView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.onItemClick(holder, holder.getAdapterPosition(), bean);
            }
        });
        ((SwipeLayout) holder.itemView).addOnSwipeListener(new SwipeLayout.OnSwipeListener() {
            @Override
            public void onScrolled(int dx) {
                swipeLayout_scrolled = (SwipeLayout) holder.itemView;
                SwipeAdapter.this.onScrolled(holder, holder.getAdapterPosition(), bean, dx);
            }

            @Override
            public void onOpened() {
                swipeLayout_opened = (SwipeLayout) holder.itemView;
                SwipeAdapter.this.onOpened(holder, holder.getAdapterPosition(), bean);
            }

            @Override
            public void onClosed() {
                swipeLayout_opened = null;
                SwipeAdapter.this.onClosed(holder, holder.getAdapterPosition(), bean);
            }
        });
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

    public void closeOpened(final OnSwipeCallback onSwipeCallback) {
        swipeLayout_opened.close(new SwipeLayout.OnSwipeListener() {
            @Override
            public void onScrolled(int dx) {

            }

            @Override
            public void onOpened() {

            }

            @Override
            public void onClosed() {
                onSwipeCallback.onClosed();
            }
        });
        swipeLayout_opened = null;
    }

    public SwipeLayout getScrolled() {
        return swipeLayout_scrolled;
    }

    public void closeScrolled() {
        swipeLayout_scrolled.close();
        swipeLayout_scrolled = null;
    }

    /**
     * 策略1
     *
     * @return
     */
    public SimpleAdapter<T> getSimpleAdapter() {
        if (adapter == null || !(adapter instanceof SimpleAdapter)) {
            adapter = null;
            adapter = new SimpleAdapter<T>() {
                @Override
                public void bindDataToView(final BaseViewHolder holder, int position, T bean, boolean isSelected) {
                    dealSwipe(holder, bean);
                    SwipeAdapter.this.bindDataToView(holder, position, bean,position==getPositionSelected());
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
                public void onViewAttachedToWindow(@NonNull BaseViewHolder holder) {
                    super.onViewAttachedToWindow(holder);
                    SwipeAdapter.this.onViewAttachedToWindow(holder);

                }
            };
        }
        return (SimpleAdapter<T>) adapter;
    }
//
//    /**
//     * 策略2
//     *
//     * @return
//     */
//    public HeadFootAdapter<T> getHeadFootAdapter() {
//        if (adapter == null || !(adapter instanceof HeadFootAdapter)) {
//            adapter = new HeadFootAdapter<T>() {
//                @Override
//                public void bindDataToView(BaseViewHolder holder, int position, T bean) {
//                    dealSwipe(holder, bean);
//                    SwipeAdapter.this.bindDataToView(holder, position, bean);
//                }
//
//                @Override
//                public int getItemLayoutID(int position, T bean) {
//                    return SwipeAdapter.this.getItemLayoutID(position, bean);
//                }
//
//                @Override
//                public void onItemClick(BaseViewHolder holder, int position, T bean) {
//                    SwipeAdapter.this.onItemClick(holder, position, bean);
//                }
//
//                @Override
//                public void onItemLongClick(BaseViewHolder holder, int position, T bean) {
//                    SwipeAdapter.this.onItemLongClick(holder, position, bean);
//                }
//
//                @Override
//                public void onViewAttachedToWindow(@NonNull BaseViewHolder holder) {
//                    super.onViewAttachedToWindow(holder);
//                    SwipeAdapter.this.onViewAttachedToWindow(holder);
//
//                }
//            };
//        }
//        return (HeadFootAdapter<T>) adapter;
//    }

    public static class OnSwipeCallback implements SwipeLayout.OnSwipeListener {

        @Override
        public void onScrolled(int dx) {

        }

        @Override
        public void onOpened() {

        }

        @Override
        public void onClosed() {

        }
    }
}
