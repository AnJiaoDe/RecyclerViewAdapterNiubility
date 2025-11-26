package com.cy.rvadapterniubility.refreshrv;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.cy.refreshlayoutniubility.OnRefreshListener;
import com.cy.refreshlayoutniubility.RefreshLayoutNiubility;
import com.cy.rvadapterniubility.adapter.MultiAdapter;
import com.cy.rvadapterniubility.adapter.SimpleAdapter;
import com.cy.rvadapterniubility.recyclerview.BaseRecyclerView;
import com.cy.rvadapterniubility.recyclerview.OnLoadMoreListener;
import com.cy.rvadapterniubility.recyclerview.OnSimpleScrollListener;
import com.cy.rvadapterniubility.recyclerview.PositionHolder;


/**
 * Created by lenovo on 2017/12/31.
 */

public abstract class BaseRVRefreshLayout<V extends BaseRecyclerView, O extends OnLoadMoreListener> extends RefreshLayoutNiubility {

    public BaseRVRefreshLayout(Context context) {
        this(context, null);
    }

    public BaseRVRefreshLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public abstract V getRecyclerView();

    public abstract <T extends BaseRVRefreshLayout<V, O>> T setRecyclerView(V recyclerView);

    /**
     *
     */
    public <T extends BaseRVRefreshLayout<V, O>> T addOnScrollListener() {
        addOnScrollListener(new OnSimpleScrollListener());
        return (T) this;
    }

    public <T extends BaseRVRefreshLayout<V, O>> T addOnScrollListener(OnSimpleScrollListener onSimpleScrollListener) {
        getRecyclerView().addOnScrollListener(onSimpleScrollListener.getOnScrollListener());
        return (T) this;
    }


    public <T extends BaseRVRefreshLayout<V, O>> T setAdapter(SimpleAdapter simpleAdapter) {
        BaseRecyclerView baseRecyclerView = getRecyclerView();
        baseRecyclerView.setAdapter(simpleAdapter);
        return (T) this;
    }

    public <T extends BaseRVRefreshLayout<V, O>> T setAdapter(SimpleAdapter simpleAdapter, OnRefreshListener onRefreshListener) {
        setOnRefreshListener(onRefreshListener);
        setAdapter(simpleAdapter);
        return (T) this;
    }

    public <T extends BaseRVRefreshLayout<V, O>> T setAdapter(MultiAdapter multiAdapter) {
        BaseRecyclerView baseRecyclerView = getRecyclerView();
        baseRecyclerView.setAdapter(multiAdapter.getMergeAdapter());
        return (T) this;
    }

    public <T extends BaseRVRefreshLayout<V, O>> T setAdapter(MultiAdapter multiAdapter, O onLoadMoreListener) {
        addOnScrollListener(onLoadMoreListener);
        setAdapter(multiAdapter);
        return (T) this;
    }

    public <T extends BaseRVRefreshLayout<V, O>> T setAdapter(MultiAdapter multiAdapter, OnRefreshListener onRefreshListener) {
        setOnRefreshListener(onRefreshListener);
        setAdapter(multiAdapter);
        return (T) this;
    }

    public <T extends BaseRVRefreshLayout<V, O>> T setAdapter(MultiAdapter multiAdapter,
                                                              OnRefreshListener onRefreshListener, O onLoadMoreListener) {
        onLoadMoreListener.setCallbackState(new OnLoadMoreListener.CallbackState() {
            @Override
            public void onStateChanged(boolean isLoadMoreing) {
                //防止上拉加载和刷新同时进行，导致RV崩溃
                setEnableRefresh(!isLoadMoreing);
            }
        });
        setOnRefreshListener(onRefreshListener);
        addOnScrollListener(onLoadMoreListener);
        setAdapter(multiAdapter);
        return (T) this;
    }

}
