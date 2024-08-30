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
 * 网上各种乱七八糟的解决方案，莫信，很多人不验证就发上去
 * 其实做好几件事，瀑布流应该就不会有严重问题了
 * 1.涉及到图片，必须对imageview 调用setLayoutParams设置高度
 * 2.上拉更多，必须用notifyItemRangeInserted，如果涉及到图片加载比较耗时的情况，会闪烁
 * 3.下拉刷新，必须notifydatasetchanged，否则间隔错乱
 * 4.间隔计算必须均分，否则GG
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
