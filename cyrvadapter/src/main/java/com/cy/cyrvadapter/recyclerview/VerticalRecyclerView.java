package com.cy.cyrvadapter.recyclerview;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cy.cyrvadapter.adapter.LinearItemDecoration;
import com.cy.cyrvadapter.adapter.SimpleAdapter;


/**
 * Created by cy on 2017/7/2.
 */

public class VerticalRecyclerView extends BaseRecyclerView<VerticalRecyclerView> {
    private LinearItemDecoration linearItemDecoration;
    public VerticalRecyclerView(Context context) {
        this(context, null);
    }

    public VerticalRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
//        setOverScrollMode(OVER_SCROLL_NEVER);
    }

    @Override
    public void setAdapter(Adapter adapter) {
        setLayoutManager(new LinearLayoutManager(getContext()));
        super.setAdapter(adapter);
    }

    @Override
    public void addItemDecoration(@NonNull ItemDecoration decor, int index) {
        try {
            this.linearItemDecoration = (LinearItemDecoration) decor;
        }catch (Exception e){
            throw new IllegalAccessError("You can only use LinearItemDecoration in VerticalRecyclerView or HorizontalRecyclerView");
        }
        super.addItemDecoration(decor, index);
    }

    @Override
    public void addItemDecoration(@NonNull ItemDecoration decor) {
        try {
            this.linearItemDecoration = (LinearItemDecoration) decor;
        }catch (Exception e){
            throw new IllegalAccessError("You can only use LinearItemDecoration in VerticalRecyclerView or HorizontalRecyclerView");
        }
        super.addItemDecoration(decor);
    }

    @NonNull
    @Override
    public ItemDecoration getItemDecorationAt(int index) {
        return linearItemDecoration;
    }

    public LinearItemDecoration getItemDecoration() {
        return linearItemDecoration;
    }

}
