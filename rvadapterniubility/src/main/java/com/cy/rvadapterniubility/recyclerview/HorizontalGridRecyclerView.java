package com.cy.rvadapterniubility.recyclerview;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.ConcatAdapter;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


/**
 * Created by cy on 2017/7/2.
 */

public class HorizontalGridRecyclerView extends GridRecyclerView<HorizontalGridRecyclerView> {

    public HorizontalGridRecyclerView(Context context) {
        this(context, null);
    }

    public HorizontalGridRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void setAdapter(@Nullable final Adapter adapter) {
        final GridLayoutManager layoutManager = new GridLayoutManager(getContext(), getSpanCount(), RecyclerView.HORIZONTAL, false);
        setSpanSizeLookup(layoutManager);
        setLayoutManager(layoutManager);
        super.setAdapter(adapter);
    }

    public void setAdapter(@Nullable final Adapter adapter, GridLayoutManager.SpanSizeLookup spanSizeLookup) {
        final GridLayoutManager layoutManager = new GridLayoutManager(getContext(), getSpanCount(), RecyclerView.HORIZONTAL, false);
        layoutManager.setSpanSizeLookup(spanSizeLookup);
        setLayoutManager(layoutManager);
        super.setAdapter(adapter);
    }
}