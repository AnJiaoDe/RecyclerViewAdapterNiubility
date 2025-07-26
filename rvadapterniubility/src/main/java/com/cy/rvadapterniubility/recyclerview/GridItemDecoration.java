package com.cy.rvadapterniubility.recyclerview;

import android.graphics.Rect;
import android.view.View;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.cy.rvadapterniubility.adapter.BaseViewHolder;


/**
 * @Description:注意：使用ItemDecoration时，一定要小心，设置的item的宽度不能超过每列的最大限制，超过了就看不见space了
 * @Author: cy
 * @CreateDate: 2020/7/14 12:27
 * @UpdateUser:
 * @UpdateDate: 2020/7/14 12:27
 * @UpdateRemark:
 * @Version:
 */
public class GridItemDecoration extends RecyclerView.ItemDecoration {
    private float space;

    public GridItemDecoration(float space) {
        this.space = space;
    }


    public float getSpace() {
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
//        GridRecyclerView gridRecyclerView = null;
//        try {
        GridRecyclerView gridRecyclerView = (GridRecyclerView) parent;
//        } catch (Exception e) {
//            throw new IllegalAccessError("You can only use " + this.getClass().getName() + " in GridLayoutManager  for "
//                    + GridRecyclerView.class.getName() + "or "
//                    +VerticalGridRecyclerView.class.getName() + "or " + HorizontalGridRecyclerView.class.getName());
//        }
        final GridLayoutManager gridLayoutManager = (GridLayoutManager) gridRecyclerView.getLayoutManager();
        BaseViewHolder viewHolder = (BaseViewHolder) parent.getChildViewHolder(view);
//        int spanCount = 1;
//        int orientation = RecyclerView.VERTICAL;
//        if (layoutManager instanceof GridLayoutManager) {
        int spanCount = gridLayoutManager.getSpanCount();
        int orientation = gridLayoutManager.getOrientation();
//        } else {
//            throw new IllegalAccessError("You can only use " + this.getClass().getName() + " in GridLayoutManager  for " + VerticalGridRecyclerView.class.getName());
//        }
        GridLayoutManager.LayoutParams params = (GridLayoutManager.LayoutParams) view.getLayoutParams();
        // 获取item在span中的下标,假如2个span,index 永远是从0--1,//不能用getAbsoluteAdapterPosition，因为是grid
        //如果3个span,假如第一个span占了两列，那么是0,2,否则就是0,1,2
        //故而，左边第一个span 永远是0，最右边的span永远是spanCount-1
        int spanIndex = params.getSpanIndex();
//        LogUtils.log("spanIndex", spanIndex);

        int position = viewHolder.getAbsoluteAdapterPosition();
        float perSpace = space / spanCount;

        int a = spanCount - spanIndex;
        int b = gridRecyclerView.getSparseArrayFullSpan().get(position) != null ? spanCount : (1 + spanIndex % spanCount);
        boolean side = spanIndex == 0 || spanIndex == spanCount - 1;
        //必须四舍五入，否则，如果space很小，会导致间隔不均匀
        switch (orientation) {
            case RecyclerView.VERTICAL:
                outRect.left = Math.round(a * perSpace);
                outRect.top = Math.round(position >= 1 && gridRecyclerView.getSparseArrayFullSpan().get(position - spanIndex - 1) != null ?
                        0 : (position < spanCount ? space : 0));
                outRect.right = Math.round(b * perSpace);
                outRect.bottom = Math.round(space);
                break;
            //HORIZONTAL的其实就是VERTICAL翻转一下
            case RecyclerView.HORIZONTAL:
                outRect.left = Math.round(position >= 1 && gridRecyclerView.getSparseArrayFullSpan().get(position - spanIndex - 1) != null ?
                        0 : (position < spanCount ? space : 0));
                outRect.top = Math.round(a * perSpace);
                outRect.right = Math.round(space);
                outRect.bottom = Math.round(b * perSpace);
                break;
        }
    }
}
