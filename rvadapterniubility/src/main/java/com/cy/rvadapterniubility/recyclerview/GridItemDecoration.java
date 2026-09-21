package com.cy.rvadapterniubility.recyclerview;

import android.graphics.Rect;
import android.util.Log;
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
        GridRecyclerView gridRecyclerView = (GridRecyclerView) parent;

        final GridLayoutManager gridLayoutManager =
                (GridLayoutManager) gridRecyclerView.getLayoutManager();

        BaseViewHolder viewHolder =
                (BaseViewHolder) parent.getChildViewHolder(view);

        int spanCount = gridLayoutManager.getSpanCount();
        int orientation = gridLayoutManager.getOrientation();

        GridLayoutManager.LayoutParams params =
                (GridLayoutManager.LayoutParams) view.getLayoutParams();

        // 获取 item 在当前行中的 span 下标
        int spanIndex = params.getSpanIndex();

        int position = viewHolder.getAbsoluteAdapterPosition();

        float perSpace = space / (float) spanCount;

        /*
         * FullSpan 判断统一交给 SpanSizeLookup。
         *
         * 当前 item：
         * 直接从 LayoutParams 获取 spanSize。
         */
        boolean isFullSpan =
                params.getSpanSize() == spanCount;

        /*
         * 前一个相关 item 是否是 FullSpan。
         *
         * position - spanIndex - 1
         * 保持你原来的计算方式，不改变你的布局逻辑。
         */
        boolean previousIsFullSpan = false;

        if (position >= 1) {
            int previousPosition = position - spanIndex - 1;

            if (previousPosition >= 0) {
                GridLayoutManager.SpanSizeLookup spanSizeLookup =
                        gridLayoutManager.getSpanSizeLookup();

                previousIsFullSpan =
                        spanSizeLookup.getSpanSize(previousPosition) == spanCount;
            }
        }

        int a = spanCount - spanIndex;

        int b = isFullSpan
                ? spanCount
                : (1 + spanIndex % spanCount);

        // 必须四舍五入，否则，如果 space 很小，会导致间隔不均匀
        switch (orientation) {
            case RecyclerView.VERTICAL:

                outRect.left = Math.round(a * perSpace);

                outRect.top = Math.round(
                        previousIsFullSpan
                                ? 0
                                : (position < spanCount ? space : 0)
                );

                outRect.right = Math.round(b * perSpace);

                outRect.bottom = Math.round(space);

                break;

            // HORIZONTAL 的其实就是 VERTICAL 翻转一下
            case RecyclerView.HORIZONTAL:

                outRect.left = Math.round(
                        previousIsFullSpan
                                ? 0
                                : (position < spanCount ? space : 0)
                );

                outRect.top = Math.round(a * perSpace);

                outRect.right = Math.round(space);

                outRect.bottom = Math.round(b * perSpace);

                break;
        }
    }
}
