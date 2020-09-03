package com.cy.rvadapterniubility.refreshrv;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.cy.refreshlayoutniubility.OnPullListener;
import com.cy.refreshlayoutniubility.RefreshLayoutNiubility;
import com.cy.rvadapterniubility.adapter.MultiAdapter;
import com.cy.rvadapterniubility.adapter.SimpleAdapter;
import com.cy.rvadapterniubility.recyclerview.BaseRecyclerView;
import com.cy.rvadapterniubility.recyclerview.OnGridLoadMoreListener;
import com.cy.rvadapterniubility.recyclerview.OnRVLoadMoreListener;
import com.cy.rvadapterniubility.recyclerview.OnVerticalScrollListener;


/**
 * Created by lenovo on 2017/12/31.
 */

public abstract class BaseRVRefreshLayout<T extends BaseRecyclerView, V extends BaseRVRefreshLayout> extends RefreshLayoutNiubility {

    public BaseRVRefreshLayout(Context context) {
        this(context, null);
    }

    public BaseRVRefreshLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public abstract T getRecyclerView();

    /**
     *
     */
    public V addOnScrollListener() {
        addOnScrollListener(new OnVerticalScrollListener() {
            @Override
            public void onScrollArrivedBottom(RecyclerView recyclerView, PositionHolder positionHolder) {
                super.onScrollArrivedBottom(recyclerView, positionHolder);
                startLoadMore();
            }
        });
        return (V) this;
    }

    public V addOnScrollListener(OnVerticalScrollListener onVerticalScrollListener) {
        getRecyclerView().addOnScrollListener(onVerticalScrollListener.getOnScrollListener());
        return (V) this;
    }


    /**
     * @param onRefreshListener
     */
    public V setOnRefreshListener(final OnRefreshListener onRefreshListener) {
        setEnableLoadMore(false);
        setOnPullListener(new OnPullListener() {
            @Override
            public void onRefreshStart() {
                onRefreshListener.onRefreshStart();
            }

            @Override
            public void onLoadMoreStart() {

            }
        });
        return (V) this;
    }

    /**
     * @param onLoadMoreListener
     */
    public V setOnLoadMoreListener(final OnLoadMoreListener onLoadMoreListener) {
        setEnableRefresh(false);
        setOnPullListener(new OnPullListener() {
            @Override
            public void onRefreshStart() {
            }

            @Override
            public void onLoadMoreStart() {
                onLoadMoreListener.onLoadMoreStart();
            }
        });
        addOnScrollListener();
        return (V) this;
    }

    @Override
    public void setOnPullListener(OnPullListener onPullListener) {
        super.setOnPullListener(onPullListener);
        addOnScrollListener();
    }

    public V setAdapter(SimpleAdapter simpleAdapter) {
        BaseRecyclerView baseRecyclerView = getRecyclerView();
        baseRecyclerView.setAdapter(simpleAdapter);
        return (V) this;
    }
    public V setAdapter(SimpleAdapter simpleAdapter,OnRefreshListener onRefreshListener) {
        BaseRecyclerView baseRecyclerView = getRecyclerView();
        setOnRefreshListener(onRefreshListener);
        baseRecyclerView.setAdapter(simpleAdapter);
        return (V) this;
    }
    public V setAdapter(SimpleAdapter simpleAdapter,OnPullListener onPullListener) {
        BaseRecyclerView baseRecyclerView = getRecyclerView();
        setOnPullListener(onPullListener);
        baseRecyclerView.setAdapter(simpleAdapter);
        return (V) this;
    }
    public V setAdapter(MultiAdapter multiAdapter) {
        BaseRecyclerView baseRecyclerView = getRecyclerView();
        baseRecyclerView.setAdapter(multiAdapter.getMergeAdapter());
        return (V) this;
    }

    public V setAdapter(MultiAdapter multiAdapter, OnVerticalScrollListener onVerticalScrollListener) {
        BaseRecyclerView baseRecyclerView = getRecyclerView();
        baseRecyclerView.addOnScrollListener(onVerticalScrollListener);
        baseRecyclerView.setAdapter(multiAdapter.getMergeAdapter());
        return (V) this;
    }

    public V setAdapter(MultiAdapter multiAdapter, OnPullListener onPullListener) {
        BaseRecyclerView baseRecyclerView = getRecyclerView();
        setOnPullListener(onPullListener);
        baseRecyclerView.setAdapter(multiAdapter.getMergeAdapter());
        return (V) this;
    }

}
