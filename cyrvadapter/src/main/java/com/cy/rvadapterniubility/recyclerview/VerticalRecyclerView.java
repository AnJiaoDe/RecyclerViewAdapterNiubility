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

public class VerticalRecyclerView extends BaseRecyclerView<VerticalRecyclerView>{
    private LinearItemDecoration linearItemDecoration;
    public VerticalRecyclerView(Context context) {
        this(context, null);
    }

    public VerticalRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
//        setOverScrollMode(OVER_SCROLL_NEVER);
    }

    @Override
    public void setAdapter(RecyclerView.Adapter adapter) {
        setLayoutManager(new LinearLayoutManager(getContext()));
        super.setAdapter(adapter);
    }

    @Override
    public void addItemDecoration(@NonNull RecyclerView.ItemDecoration decor, int index) {
        try {
            this.linearItemDecoration = (LinearItemDecoration) decor;
        }catch (Exception e){
            throw new IllegalAccessError("You can only use LinearItemDecoration in VerticalRecyclerView or HorizontalRecyclerView");
        }
        super.addItemDecoration(decor, index);
    }

    @Override
    public void addItemDecoration(@NonNull RecyclerView.ItemDecoration decor) {
        try {
            this.linearItemDecoration = (LinearItemDecoration) decor;
        }catch (Exception e){
            throw new IllegalAccessError("You can only use LinearItemDecoration in VerticalRecyclerView or HorizontalRecyclerView");
        }
        super.addItemDecoration(decor);
    }

    @NonNull
    @Override
    public RecyclerView.ItemDecoration getItemDecorationAt(int index) {
        return linearItemDecoration;
    }

    public LinearItemDecoration getItemDecoration() {
        return linearItemDecoration;
    }


}
