package com.cy.rvadapterniubility.refreshrv;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.cy.refreshlayoutniubility.OnPullListener;
import com.cy.refreshlayoutniubility.RefreshLayoutNiubility;
import com.cy.rvadapterniubility.LogUtils;
import com.cy.rvadapterniubility.adapter.IScrollState;
import com.cy.rvadapterniubility.adapter.MultiAdapter;
import com.cy.rvadapterniubility.adapter.SimpleAdapter;
import com.cy.rvadapterniubility.recyclerview.BaseRecyclerView;
import com.cy.rvadapterniubility.recyclerview.OnGridLoadMoreListener;
import com.cy.rvadapterniubility.recyclerview.OnRVLoadMoreListener;
import com.cy.rvadapterniubility.recyclerview.OnVerticalScrollListener;


/**
 * Created by lenovo on 2017/12/31.
 */

public abstract class BaseRVRefreshLayout<T extends BaseRecyclerView> extends RefreshLayoutNiubility {

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
    public BaseRVRefreshLayout addOnScrollListener() {
        addOnScrollListener(new OnVerticalScrollListener() {
            @Override
            public void onScrollArrivedBottom(RecyclerView recyclerView, PositionHolder positionHolder) {
                super.onScrollArrivedBottom(recyclerView,positionHolder);
                startLoadMore();
            }
        });
        return this;
    }

    public BaseRVRefreshLayout addOnScrollListener(OnVerticalScrollListener onSimpleScrollListener) {
        getRecyclerView().addOnScrollListener(onSimpleScrollListener.getOnScrollListener());
        return this;
    }


    /**
     * @param onRefreshListener
     */
    public BaseRVRefreshLayout setOnRefreshListener(final OnRefreshListener onRefreshListener) {
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
        return this;
    }

    /**
     * @param onLoadMoreListener
     */
    public BaseRVRefreshLayout setOnLoadMoreListener(final OnLoadMoreListener onLoadMoreListener) {
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
        return this;
    }

    @Override
    public void setOnPullListener(OnPullListener onPullListener) {
        super.setOnPullListener(onPullListener);
        addOnScrollListener();
    }
//    /**
//     * @param adapter
//     */
//
//    public void setAdapter(RecyclerView.Adapter adapter) {
//        getRecyclerView().setAdapter(adapter);
//    }
//
//    /**
//     * @param adapter
//     * @param onPullListener
//     */
//    public void setAdapter(RecyclerView.Adapter adapter, OnPullListener onPullListener) {
//        setAdapter(adapter);
//        setOnPullListener(onPullListener);
//    }
//
//    /**
//     * @param adapter
//     * @param onRefreshListener
//     */
//    public void setAdapter(RecyclerView.Adapter adapter, OnRefreshListener onRefreshListener) {
//        setAdapter(adapter);
//        setOnRefreshListener(onRefreshListener);
//    }
//
//    /**
//     * @param adapter
//     * @param onLoadMoreListener
//     */
//    public void setAdapter(RecyclerView.Adapter adapter, OnLoadMoreListener onLoadMoreListener) {
//        setAdapter(adapter);
//        setOnLoadMoreListener(onLoadMoreListener);
//    }

    public BaseRVRefreshLayout setAdapter(IScrollState scrollState) {
        BaseRecyclerView baseRecyclerView = getRecyclerView();
        baseRecyclerView.addOnScrollHelper(scrollState);
        baseRecyclerView.setAdapter(scrollState.getAdapter());
        return this;
    }
    public BaseRVRefreshLayout setAdapter(MultiAdapter multiAdapter) {
        BaseRecyclerView baseRecyclerView = getRecyclerView();
        baseRecyclerView.setAdapter(multiAdapter.getMergeAdapter());
        return this;
    }
    public BaseRVRefreshLayout setAdapter(MultiAdapter multiAdapter, OnRVLoadMoreListener onRVLoadMoreListener) {
        setEnableRefresh(false);
        setEnableLoadMore(false);
        BaseRecyclerView baseRecyclerView = getRecyclerView();
        baseRecyclerView.addOnScrollListener(onRVLoadMoreListener);
        baseRecyclerView.setAdapter(multiAdapter.getMergeAdapter());
        return this;
    }
    public BaseRVRefreshLayout setAdapter(MultiAdapter multiAdapter, OnRefreshListener onRefreshListener, OnRVLoadMoreListener onRVLoadMoreListener) {
        setEnableLoadMore(false);
        BaseRecyclerView baseRecyclerView = getRecyclerView();
        baseRecyclerView.addOnScrollListener(onRVLoadMoreListener);
        setOnRefreshListener(onRefreshListener);
        baseRecyclerView.setAdapter(multiAdapter.getMergeAdapter());
        return this;
    }

    public BaseRVRefreshLayout setAdapter(MultiAdapter multiAdapter, OnRefreshListener onRefreshListener, OnGridLoadMoreListener onGridLoadMoreListener) {
        setEnableLoadMore(false);
        BaseRecyclerView baseRecyclerView = getRecyclerView();
        baseRecyclerView.addOnScrollListener(onGridLoadMoreListener);
        setOnRefreshListener(onRefreshListener);
        baseRecyclerView.setAdapter(multiAdapter.getMergeAdapter());
        return this;
    }
    public BaseRVRefreshLayout setAdapter(SimpleAdapter simpleAdapter, OnRefreshListener onRefreshListener, OnGridLoadMoreListener onGridLoadMoreListener) {
        setEnableLoadMore(false);
        BaseRecyclerView baseRecyclerView = getRecyclerView();
        baseRecyclerView.addOnScrollListener(onGridLoadMoreListener);
        setOnRefreshListener(onRefreshListener);
        baseRecyclerView.setAdapter(simpleAdapter);
        return this;
    }

    public BaseRVRefreshLayout setAdapter(MultiAdapter multiAdapter, OnVerticalScrollListener onSimpleScrollListener) {
        BaseRecyclerView baseRecyclerView = getRecyclerView();
        baseRecyclerView.addOnScrollListener(onSimpleScrollListener);
        baseRecyclerView.setAdapter(multiAdapter.getMergeAdapter());
        return this;
    }
    public BaseRVRefreshLayout setAdapter(MultiAdapter multiAdapter, OnPullListener onPullListener) {
        LogUtils.log("setAdapter");
        BaseRecyclerView baseRecyclerView = getRecyclerView();
        setOnPullListener(onPullListener);
        baseRecyclerView.setAdapter(multiAdapter.getMergeAdapter());
        return this;
    }
    public BaseRVRefreshLayout setAdapter(IScrollState scrollState, OnPullListener onPullListener) {
        LogUtils.log("setAdapter");
        setAdapter(scrollState);
        setOnPullListener(onPullListener);
        return this;
    }


    public BaseRVRefreshLayout setAdapter(IScrollState scrollState, OnRefreshListener onRefreshListener) {
        setAdapter(scrollState);
        setOnRefreshListener(onRefreshListener);
        return this;
    }

    public BaseRVRefreshLayout setAdapter(IScrollState scrollState, OnLoadMoreListener onLoadMoreListener) {
        setAdapter(scrollState);
        setOnLoadMoreListener(onLoadMoreListener);
        return this;
    }

    public BaseRVRefreshLayout setAdapter(IScrollState scrollState, OnVerticalScrollListener onSimpleScrollListener) {
        setAdapter(scrollState);
        addOnScrollListener(onSimpleScrollListener);
        return this;
    }
}
