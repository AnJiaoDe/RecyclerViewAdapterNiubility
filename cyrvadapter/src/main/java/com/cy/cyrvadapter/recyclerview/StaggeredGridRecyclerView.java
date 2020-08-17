package com.cy.cyrvadapter.recyclerview;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;


/**
 * Created by cy on 2018/4/8.
 */

public class StaggeredGridRecyclerView extends BaseRecyclerView<StaggeredGridRecyclerView> {
    private int spanCount = 2;
    private int orientation = RecyclerView.VERTICAL;

    public StaggeredGridRecyclerView(Context context) {
        this(context, null);
    }

    public StaggeredGridRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public StaggeredGridRecyclerView setSpanCount(int spanCount) {
        this.spanCount = spanCount;
        return this;
    }

    public StaggeredGridRecyclerView setOrientation(int orientation) {
        this.orientation = orientation;
        return this;
    }

    @Override
    public void setAdapter(@Nullable Adapter adapter) {
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(spanCount, orientation){
            @Override
            public boolean isAutoMeasureEnabled() {
                return true;
            }

            @Override
            public void setMeasuredDimension(Rect childrenBounds, int wSpec, int hSpec) {
                super.setMeasuredDimension(childrenBounds, wSpec, hSpec);
            }
        };
        layoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_NONE);
        //取消动画，防止item复用导致的闪烁
        setItemAnimator(null);
        setLayoutManager(layoutManager);
        super.setAdapter(adapter);
    }
}
