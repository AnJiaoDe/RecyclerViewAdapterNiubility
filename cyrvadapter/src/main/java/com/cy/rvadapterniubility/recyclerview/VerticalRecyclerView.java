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

public class VerticalRecyclerView extends BaseRecyclerView{
    public VerticalRecyclerView(Context context) {
        this(context, null);
    }

    public VerticalRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void setAdapter(RecyclerView.Adapter adapter) {
        setLayoutManager(new LinearLayoutManager(getContext()));
        super.setAdapter(adapter);
    }

}
