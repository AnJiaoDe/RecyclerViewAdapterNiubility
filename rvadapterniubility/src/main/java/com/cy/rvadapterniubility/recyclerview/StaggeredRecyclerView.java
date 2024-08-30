package com.cy.rvadapterniubility.recyclerview;

import android.content.Context;
import android.util.AttributeSet;
import android.util.SparseArray;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.cy.refreshlayoutniubility.ScreenUtils;


/**
 * Created by cy on 2017/7/2.
 */

public class StaggeredRecyclerView<T extends StaggeredRecyclerView> extends BaseRecyclerView<T> {
    private int spanCount = 2;
    private StaggeredItemDecoration staggeredItemDecoration;

    public StaggeredRecyclerView(Context context) {
        this(context, null);
    }

    public StaggeredRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        addItemDecoration(new StaggeredItemDecoration(ScreenUtils.dpAdapt(context, 10)));
//        addOnScrollListener(new OnScrollListener() {
//            @Override
//            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
//                super.onScrollStateChanged(recyclerView, newState);
//                //防止第一行到顶部有空白区域
//                StaggeredGridLayoutManager staggeredGridLayoutManager = (StaggeredGridLayoutManager) getLayoutManager();
//                if (staggeredGridLayoutManager != null)
//                    staggeredGridLayoutManager.invalidateSpanAssignments();
//            }
//        });
//        addOnScrollListener(new OnSimpleScrollListener() {
//            onscr
//            //orientation vertical
//            @Override
//            public void onScrollArrivedTop(BaseRecyclerView baseRecyclerView, PositionHolder positionHolder, int offsetX, int offsetY) {
//                super.onScrollArrivedTop(baseRecyclerView, positionHolder, offsetX, offsetY);
////                LogUtils.log("onScrollArrivedTop");
//                //解决滑动回顶部的时候，item错位还原动画，item decoration错乱  的  问题
//                baseRecyclerView.getAdapter().notifyDataSetChanged();
//
////                StaggeredGridLayoutManager staggeredGridLayoutManager = (StaggeredGridLayoutManager) getLayoutManager();
////                //这个根本不灵
//////                staggeredGridLayoutManager.invalidateSpanAssignments();
////                //这个虽然能解决滑动回顶部的时候，item decoration错乱  的  问题   但是不能解决item错位还原动画
////                Object result = ReflexUtils.invoke(ReflexUtils.getDeclaredMethod(StaggeredGridLayoutManager.class.getName(),
////                        "checkForGaps", new Class[]{}),
////                        staggeredGridLayoutManager,
////                        new Object[]{});
////                Boolean r = false;
////                try {
////                    r = (Boolean) result;
////                } catch (Exception e) {
////                    return;
////                }
////                if (r)
////                    ReflexUtils.invoke(ReflexUtils.getDeclaredMethod(RecyclerView.class.getName(),
////                            "markItemDecorInsetsDirty", new Class[]{}),
////                            baseRecyclerView,
////                            new Object[]{});
//            }
//            //orientation horinzontal
//            @Override
//            public void onScrollArrivedLeft(BaseRecyclerView baseRecyclerView, PositionHolder positionHolder, int offsetX, int offsetY) {
//                super.onScrollArrivedLeft(baseRecyclerView, positionHolder, offsetX, offsetY);
////                LogUtils.log("onScrollArrivedLeft");
//                //解决滑动回顶部的时候，item错位还原动画，item decoration错乱  的  问题
//                baseRecyclerView.getAdapter().notifyDataSetChanged();
//            }
//        });
    }

    public T setSpanCount(int spanCount) {
        this.spanCount = spanCount;
        return (T) this;
    }

    public int getSpanCount() {
        return spanCount;
    }


    public T addItemDecoration(StaggeredItemDecoration staggeredItemDecoration) {
        if (this.staggeredItemDecoration != null)
            removeItemDecoration(this.staggeredItemDecoration);
        this.staggeredItemDecoration = staggeredItemDecoration;
        super.addItemDecoration(staggeredItemDecoration);
        return (T) this;
    }

    public StaggeredItemDecoration getStaggeredItemDecoration() {
        return staggeredItemDecoration;
    }
}
