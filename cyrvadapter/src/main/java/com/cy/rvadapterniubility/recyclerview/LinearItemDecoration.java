package com.cy.rvadapterniubility.recyclerview;

import android.graphics.Rect;
import android.view.View;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * @Description:
 * @Author: cy
 * @CreateDate: 2020/7/14 12:27
 * @UpdateUser:
 * @UpdateDate: 2020/7/14 12:27
 * @UpdateRemark:
 * @Version:
 */
public class LinearItemDecoration extends RecyclerView.ItemDecoration {
    private int space_vertical, space_horizontal;

    public LinearItemDecoration setSpace_vertical(int space_vertical) {
        this.space_vertical = space_vertical;
        return this;
    }

    public LinearItemDecoration setSpace_horizontal(int space_horizontal) {
        this.space_horizontal = space_horizontal;
        return this;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        final LinearLayoutManager layoutManager = (LinearLayoutManager) parent.getLayoutManager();
        RecyclerView.ViewHolder viewHolder = parent.getChildViewHolder(view);
        int orientation= layoutManager.getOrientation();
        int position = viewHolder.getBindingAdapterPosition();

        switch (orientation) {
            case RecyclerView.VERTICAL:
                outRect.left =space_horizontal;
                outRect.top = space_vertical;
                outRect.right =space_horizontal;
                outRect.bottom = position==parent.getAdapter().getItemCount()-1?space_vertical:0;
                break;
            //HORIZONTAL的其实就是VERTICAL翻转一下
            case RecyclerView.HORIZONTAL:
                outRect.left = space_horizontal;
                outRect.top =space_vertical;
                outRect.right =position==parent.getAdapter().getItemCount()-1?space_horizontal:0;
                outRect.bottom = space_vertical;
                break;
        }
    }

    public int getSpace_vertical() {
        return space_vertical;
    }

    public int getSpace_horizontal() {
        return space_horizontal;
    }
}
