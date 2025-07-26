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
    private float space_vertical, space_horizontal;

    public LinearItemDecoration setSpace_vertical(float space_vertical) {
        this.space_vertical = space_vertical;
        return this;
    }

    public LinearItemDecoration setSpace_horizontal(float space_horizontal) {
        this.space_horizontal = space_horizontal;
        return this;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) parent.getLayoutManager();
        RecyclerView.ViewHolder viewHolder = parent.getChildViewHolder(view);
        int orientation= linearLayoutManager.getOrientation();
        int position = viewHolder.getAbsoluteAdapterPosition();

        switch (orientation) {
            case RecyclerView.VERTICAL:
                outRect.left =Math.round(space_horizontal);
                outRect.top = Math.round(space_vertical);
                outRect.right =Math.round(space_horizontal);
                outRect.bottom = Math.round(position==parent.getAdapter().getItemCount()-1?space_vertical:0);
                break;
            //HORIZONTAL的其实就是VERTICAL翻转一下
            case RecyclerView.HORIZONTAL:
                outRect.left = Math.round(space_horizontal);
                outRect.top =Math.round(space_vertical);
                outRect.right =Math.round(position==parent.getAdapter().getItemCount()-1?space_horizontal:0);
                outRect.bottom = Math.round(space_vertical);
                break;
        }
    }

    public float getSpace_vertical() {
        return space_vertical;
    }

    public float getSpace_horizontal() {
        return space_horizontal;
    }
}
