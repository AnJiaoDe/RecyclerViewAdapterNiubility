package com.cy.rvadapterniubility.recyclerview;

import android.graphics.Rect;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

/**
 * @Description:注意：使用ItemDecoration时，一定要小心，设置的item的宽度不能超过每列的最大限制，超过了就看不见space了
 * @Author: cy
 * @CreateDate: 2020/7/14 12:27
 * @UpdateUser:
 * @UpdateDate: 2020/7/14 12:27
 * @UpdateRemark:
 * @Version:
 */
public class StaggeredItemDecoration extends RecyclerView.ItemDecoration implements IGridItemDecoration{
    private int space;

    public StaggeredItemDecoration(int space) {
        this.space = space;
    }

    @Override
    public int getSpace() {
        return space;
    }

    /**
     * 5个span,6个space,要想分的均匀，必须找到5,6的公约数(肯定是找最小公约数5*6=30)，将每个space分成30/6=5份
     * 每个span左右占据份数如下：
     * 5,1  4,2  3,3  2,4  1,5
     * 每个item左边分到的份数从5到1递减，
     * 每个item右边分到的份数从做到右递增。
     * 论数学思想与算法的威力,不要拿着需求就想着写if else 写个简单算法不香吗
     *
     * @param outRect
     * @param view
     * @param parent
     * @param state
     */
    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        final RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        RecyclerView.ViewHolder viewHolder = parent.getChildViewHolder(view);
        int spanCount = 1;
        int orientation = RecyclerView.VERTICAL;
        if (layoutManager instanceof StaggeredGridLayoutManager) {
            spanCount = ((StaggeredGridLayoutManager) layoutManager).getSpanCount();
            orientation = ((StaggeredGridLayoutManager) layoutManager).getOrientation();
        } else {
            throw new IllegalAccessError("You can only use "+this.getClass().getName()+" in  StaggeredGridLayoutManager for RecyclerView");
        }

        StaggeredGridLayoutManager.LayoutParams params = (StaggeredGridLayoutManager.LayoutParams) view.getLayoutParams();
        // 获取item在span中的下标
        int spanIndex = params.getSpanIndex();
//        int position = viewHolder.getBindingAdapterPosition();
        int perSpace = (int) (space * 1f / spanCount);

        int a = spanCount - spanIndex % spanCount;
        int b = 1 + spanIndex % spanCount;
        switch (orientation) {
            case RecyclerView.VERTICAL:
                outRect.left = a * perSpace;
                outRect.top = 0;
                outRect.right = b * perSpace;
                outRect.bottom = space;
                if (spanIndex < spanCount) outRect.top = space;
                break;
            //HORIZONTAL的其实就是VERTICAL翻转一下
            case RecyclerView.HORIZONTAL:
                outRect.left = 0;
                outRect.top = a * perSpace;
                outRect.right = space;
                outRect.bottom = b * perSpace;
                if (spanIndex < spanCount) outRect.left = space;
                break;
        }

    }
}
