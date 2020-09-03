package com.cy.rvadapterniubility.recyclerview;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by cy on 2017/7/2.
 */

public class HorizontalRecyclerView extends BaseRecyclerView<HorizontalRecyclerView> {

    public HorizontalRecyclerView(Context context) {
        this(context, null);
    }

    public HorizontalRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void setAdapter(RecyclerView.Adapter adapter) {
        setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL,false));
        super.setAdapter(adapter);
    }
}
