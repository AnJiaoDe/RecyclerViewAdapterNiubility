package com.cy.rvadapterniubility.adapter;

import android.view.View;

import androidx.annotation.NonNull;

import com.cy.rvadapterniubility.swipelayout.OnSwipeListener;
import com.cy.rvadapterniubility.swipelayout.SwipeLayout;

/**
 * Created by cy on 2018/3/29.类似策略模式,引入IAdapter接口，面向多态编程
 */

public abstract class SelectorAdapter<T> implements IAdapter<T, BaseViewHolder, SimpleAdapter> {
    private SimpleAdapter<T> simpleAdapter;
    private int positionSelectedLast = -1;
    private int positionSelected = 0;

    public SelectorAdapter() {
        simpleAdapter = new SimpleAdapter<T>() {
            @Override
            public void bindDataToView(final BaseViewHolder holder, int position, T bean) {
                SelectorAdapter.this.bindDataToView(holder, position, bean, position == getPositionSelected());
            }

            @Override
            public void onViewRecycled(BaseViewHolder holder, int position, T bean) {
                SelectorAdapter.this.onViewRecycled(holder, position, bean);
            }

            @Override
            public long getItemId(int position) {
                return SelectorAdapter.this.getItemId(position);
            }

            @Override
            public boolean hasStableIds_() {
                return SelectorAdapter.this.hasStableIds_();
            }

            @Override
            protected void handleClick(final BaseViewHolder holder) {
                /**
                 *
                 */
                //添加Item的点击事件
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int position = holder.getBindingAdapterPosition();
                        if (position < 0 || position >= getList_bean().size()) return;
                        //设置选中的item
                        if (positionSelectedLast != position) {
                            positionSelectedLast = positionSelected;
                            positionSelected = position; //选择的position赋值给参数，
                            notifyItemChanged(positionSelectedLast);
                            notifyItemChanged(positionSelected);

                            positionSelectedLast=positionSelected;
                        }
                        onItemClick(holder, position, getList_bean().get(position));
                    }
                });
                //添加Item的长按事件
                holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        int position = holder.getBindingAdapterPosition();
                        if (position < 0 || position >= getList_bean().size()) return false;
                        onItemLongClick(holder, position, getList_bean().get(position));
                        return true;
                        //返回true，那么长按监听只执行长按监听中执行的代码，返回false，还会继续响应其他监听中的事件。
                    }
                });
            }

            @Override
            public int getItemLayoutID(int position, T bean) {
                return SelectorAdapter.this.getItemLayoutID(position, bean);
            }

            @Override
            public void onItemClick(BaseViewHolder holder, int position, T bean) {
                SelectorAdapter.this.onItemClick(holder, position, bean);
            }

            @Override
            public void onItemLongClick(BaseViewHolder holder, int position, T bean) {
                SelectorAdapter.this.onItemLongClick(holder, position, bean);
            }

            @Override
            public void onViewAttachedToWindow(@NonNull BaseViewHolder holder) {
                super.onViewAttachedToWindow(holder);
                SelectorAdapter.this.onViewAttachedToWindow(holder);

            }
        };
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds_() {
        return true;
    }

    @Override
    public final void bindDataToView(BaseViewHolder holder, int position, T bean) {

    }

    public abstract void bindDataToView(BaseViewHolder holder, int position, T bean, boolean isSelected);

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

    public int getPositionSelectedLast() {
        return positionSelectedLast;
    }

    public void setPositionSelectedLast(int positionSelectedLast) {
        this.positionSelectedLast = positionSelectedLast;
    }

    public int getPositionSelected() {
        return positionSelected;
    }

    public void setPositionSelected(int positionSelected) {
        if (positionSelectedLast != positionSelected) {
            this.positionSelected = positionSelected; //选择的position赋值给参数，
            simpleAdapter.notifyItemChanged(positionSelectedLast);
            simpleAdapter.notifyItemChanged(positionSelected);

            positionSelectedLast=positionSelected;
        }
    }

    public void setPositionSelectedNoNotify(int positionSelected) {
        this.positionSelected = positionSelected;
    }

    public void setPositionSelectedNotifyAll(int positionSelected) {
        if (positionSelectedLast != positionSelected) {
            this.positionSelected = positionSelected;
            simpleAdapter.notifyDataSetChanged();
            positionSelectedLast = positionSelected;
        }
    }

    @Override
    public SimpleAdapter<T> getAdapter() {
        return simpleAdapter;
    }
}
