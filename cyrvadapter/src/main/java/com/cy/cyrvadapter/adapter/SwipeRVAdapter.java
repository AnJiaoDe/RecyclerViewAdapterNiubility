package com.cy.cyrvadapter.adapter;

import android.view.View;

import com.cy.cyrvadapter.recyclerview.SwipeLayout;

import java.util.List;

/**
 * Created by cy on 2018/3/29.
 */

public abstract class SwipeRVAdapter<T> extends RVAdapter<T> {
    private SwipeLayout sl_opened;

    public SwipeRVAdapter(List<T> list_bean, boolean haveHeadView) {
        super(list_bean, haveHeadView);
    }

    public SwipeRVAdapter(List<T> list_bean, boolean haveHeadView, boolean haveFootView) {
        super(list_bean, haveHeadView, haveFootView);
    }

    public SwipeRVAdapter(List<T> list) {
        super(list);
    }

    @Override
    public final void bindDataToView(MyViewHolder holder, final int position, final T bean, boolean isSelected) {

        ((SwipeLayout) holder.itemView).getContentView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClick(position, bean);
            }
        });
        ((SwipeLayout) holder.itemView).setOnSweepListener(new SwipeLayout.OnSweepListener() {
            @Override
            public void onSweepChanged(SwipeLayout view, boolean isOpened) {


                if (isOpened) {


                    if (sl_opened != null) {

                        closeOpenedSL();
                    }


                    sl_opened = view;


                } else {

                    sl_opened = null;
                }


            }
        });
        bindSwipeDataToView(holder, position, bean, isSelected);
    }

    //填充数据
    public abstract void bindSwipeDataToView(MyViewHolder holder, int position, T bean, boolean isSelected);


    public SwipeLayout getSl_opened() {
        return sl_opened;
    }

    public void setSl_opened(SwipeLayout sl_opened) {
        this.sl_opened = sl_opened;
    }

    public void closeOpenedSL() {
        if (sl_opened != null) {

            sl_opened.close();
            sl_opened = null;
        }
    }
}
