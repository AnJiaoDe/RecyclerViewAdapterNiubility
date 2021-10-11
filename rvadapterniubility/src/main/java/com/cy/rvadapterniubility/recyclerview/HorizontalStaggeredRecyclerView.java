package com.cy.rvadapterniubility.recyclerview;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

/**
 * Created by cy on 2017/7/2.
 */

public class HorizontalStaggeredRecyclerView extends StaggeredRecyclerView<HorizontalStaggeredRecyclerView>   {
    public HorizontalStaggeredRecyclerView(Context context) {
        this(context,null);
    }

    public HorizontalStaggeredRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void setAdapter(final RecyclerView.Adapter adapter) {
        final StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(getSpanCount(), RecyclerView.HORIZONTAL);
        setLayoutManager(layoutManager);
        super.setAdapter(adapter);
    }
}
