package com.cy.cyrvadapter.adapter;

import android.graphics.Rect;
import android.util.SparseArray;
import android.view.View;
import android.widget.RelativeLayout;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.cy.cyrvadapter.recyclerview.GridRecyclerView;
import com.cy.cyrvadapter.refreshlayout.LogUtils;
import com.cy.cyrvadapter.refreshrv.StaggeredGridRefreshLayout;

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
    private int space;
    private GridRecyclerView gridRecyclerView;

    public GridItemDecoration(GridRecyclerView gridRecyclerView, int space) {
        this.space = space;
        this.gridRecyclerView = gridRecyclerView;
    }

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
//        if(!(layoutManager instanceof GridLayoutManager)&&!(layoutManager instanceof StaggeredGridLayoutManager))
//            throw new IllegalAccessError("You can only use GridItemDecoration in GridLayoutManager or StaggeredGridLayoutManager of RecyclerView");
        RecyclerView.ViewHolder viewHolder = parent.getChildViewHolder(view);
        int spanCount = 1;
        int orientation=RecyclerView.VERTICAL;
        if (layoutManager instanceof GridLayoutManager) {
            spanCount = ((GridLayoutManager) layoutManager).getSpanCount();
            orientation=((GridLayoutManager) layoutManager).getOrientation();
        } else if (layoutManager instanceof StaggeredGridLayoutManager) {
            spanCount = ((StaggeredGridLayoutManager) layoutManager).getSpanCount();
            orientation=((StaggeredGridLayoutManager) layoutManager).getOrientation();
        } else {
            throw new IllegalAccessError("You can only use GridItemDecoration in GridLayoutManager or StaggeredGridLayoutManager of RecyclerView");
        }
        int position = viewHolder.getBindingAdapterPosition();
        int perSpace = (int) (space * 1f / spanCount);

//        SparseArray<GridFullSpanBean> gridFullSpanBeanSparseArray=gridRecyclerView.getSparseArrayGridFullSpanBean();
//        GridFullSpanBean gridFullSpanBean=gridFullSpanBeanSparseArray.get(position);
//        LogUtils.log("getItemOffsetsposition",position);
//        if(gridFullSpanBean!=null){
//            LogUtils.log("gridFullSpanBean!=null");
//            outRect.left =gridFullSpanBean.getDecorationLeft();
//            outRect.top = gridFullSpanBean.getDecorationTop();
//            outRect.right = gridFullSpanBean.getDecorationRight();
//            outRect.bottom = gridFullSpanBean.getDecorationBottom();
//            return;
//        }

        int a = spanCount - position % spanCount;
        int b = 1 + position % spanCount;
        switch (orientation) {
            case RecyclerView.VERTICAL:
                outRect.left = a * perSpace;
                outRect.top = 0;
                outRect.right = b * perSpace;
                outRect.bottom = space;
                if (position < spanCount) outRect.top = space;
                break;
            //HORIZONTAL的其实就是VERTICAL翻转一下
            case RecyclerView.HORIZONTAL:
                outRect.left = 0;
                outRect.top = a * perSpace;
                outRect.right = space;
                outRect.bottom = b * perSpace;
                if (position < spanCount) outRect.left = space;
                break;
        }


//       论没有算法的if else
//        //顺数第一列
//        if (position % spanCount == 0) {
//            outRect.left = space;
//            outRect.top = space;
//            outRect.right = perSpanSpace;
//            outRect.bottom = 0;
//            //倒数第一列
//        } else if (position % spanCount == spanCount - 1) {
//            outRect.left = perSpanSpace;
//            outRect.top = space;
//            outRect.right = space;
//            outRect.bottom = 0;
//            //顺数第2列
//        } else if (position % spanCount == 1) {
//            outRect.left = (spanCount - 1) * perSpanSpace;
//            outRect.top = space;
//            outRect.right = (spanCount - 1) * perSpanSpace;
//            outRect.bottom = 0;
//            // 倒数第2列
//        } else if (position % spanCount == spanCount - 2) {
//            outRect.left = (spanCount - 1) * perSpanSpace;
//            outRect.top = space;
//            outRect.right = (spanCount - 1) * perSpanSpace;;
//            outRect.bottom = 0;
//        } else {
//            //中间列
//            outRect.left = (spanCount - 1) * perSpanSpace;
//            outRect.top = space;
//            outRect.right = perSpanSpace;
//            outRect.bottom = 0;
//        }
    }
}
