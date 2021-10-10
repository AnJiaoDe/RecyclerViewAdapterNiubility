package com.cy.rvadapterniubility.recyclerview;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.cy.refreshlayoutniubility.ScreenUtils;
import com.cy.rvadapterniubility.LogUtils;
import com.cy.rvadapterniubility.ReflexUtils;
import com.cy.rvadapterniubility.adapter.SimpleAdapter;

import java.lang.reflect.Method;
import java.security.spec.ECField;


/**
 * Created by cy on 2017/7/2.
 */

public class VerticalStaggeredRecyclerView extends StaggeredRecyclerView<VerticalStaggeredRecyclerView> {
    public VerticalStaggeredRecyclerView(Context context) {
        this(context, null);
    }

    public VerticalStaggeredRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void setAdapter(final RecyclerView.Adapter adapter) {
        final StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(getSpanCount(), RecyclerView.VERTICAL);
        setLayoutManager(layoutManager);
        super.setAdapter(adapter);
    }
}
