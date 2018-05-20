package com.cy.cyrvadapter.adapter;

import android.view.View;

import com.cy.cyrvadapter.recyclerview.SwipeLayout;

import java.util.List;

/**
 * Created by cy on 2018/3/29.
 */

public abstract class SwipeRVAdapter<T> extends RVAdapter<T> {
    private SwipeLayout sl_opened;

    //构造方法
    public SwipeRVAdapter(List<T> list_bean) {
        super(list_bean);
    }

    public SwipeRVAdapter(List<T> list, boolean isStaggeredGrid) {
        super(list, isStaggeredGrid);
    }

    public SwipeRVAdapter(List<T> list_bean, boolean haveHeadView, boolean haveFootView) {
        super(list_bean, haveHeadView, haveFootView);
    }

    public SwipeRVAdapter(List<T> list_bean, boolean isStaggeredGrid, boolean haveHeadView, boolean haveFootView) {
        super(list_bean, isStaggeredGrid, haveHeadView, haveFootView);
    }

    @Override
    public final   void bindDataToView(RVViewHolder holder, final int position, final T bean, boolean isSelected) {

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

    //填充数据 , isSelected:整个RV做单选，点击到哪个，哪个就是选中状态

    public abstract void bindSwipeDataToView(RVViewHolder holder, int position, T bean, boolean isSelected);


    //返回当前打开的Item
    public SwipeLayout getSl_opened() {
        return sl_opened;
    }
    //设置当前打开的Item

    public void setSl_opened(SwipeLayout sl_opened) {
        this.sl_opened = sl_opened;
    }

    //关闭打开的Item
    public void closeOpenedSL() {
        if (sl_opened != null) {

            sl_opened.close();
            sl_opened = null;
        }
    }
}
