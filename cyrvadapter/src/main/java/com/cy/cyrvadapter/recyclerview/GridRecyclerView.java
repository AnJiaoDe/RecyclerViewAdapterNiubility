package com.cy.cyrvadapter.recyclerview;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

/**
 * Created by cy on 2017/7/2.
 */

public class GridRecyclerView extends RecyclerView {
    private Context context;

    public GridRecyclerView(Context context) {
        this(context, null);
    }


    public GridRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        setOverScrollMode(OVER_SCROLL_NEVER);

    }

    public void setAdapter(final Adapter adapter, int spanCount,int orientation, boolean head, final boolean foot) {
        final GridLayoutManager layoutManager = new GridLayoutManager(context, spanCount,orientation,false);
        setLayoutManager(layoutManager);

        if (head) {
             /*
        *设置SpanSizeLookup，它将决定view会横跨多少列。这个方法是为RecyclerView添加Header和Footer的关键。
        *当判断position指向的View为Header或者Footer时候，返回总列数（ lm.getSpanCount()）,即可让其独占一行。
        */

            layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {

                    if (foot && position == adapter.getItemCount() - 1) {
                        return layoutManager.getSpanCount();

                    }
                    return position == 0 ? layoutManager.getSpanCount() : 1;
                }
            });
        }else if(foot){

            layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    return position == adapter.getItemCount() - 1 ? layoutManager.getSpanCount() : 1;
                }
            });

        }
        setAdapter(adapter);

    }
    public void setAdapter(final Adapter adapter, int spanCount,int orientation) {
        final GridLayoutManager layoutManager = new GridLayoutManager(context, spanCount,orientation,false);
        setLayoutManager(layoutManager);

        setAdapter(adapter);

    }

    public void setAdapter(Adapter adapter, int spanCount,int orientation, boolean head, boolean foot, OnRVScrollListener onRVScrollListener) {
        setAdapter(adapter, spanCount, orientation,head, foot);

        setOnScrollListener(onRVScrollListener);
    }
    public void setAdapter(Adapter adapter, int spanCount,int orientation, OnRVScrollListener onRVScrollListener) {
        setAdapter(adapter, spanCount, orientation);

        setOnScrollListener(onRVScrollListener);
    }




}
