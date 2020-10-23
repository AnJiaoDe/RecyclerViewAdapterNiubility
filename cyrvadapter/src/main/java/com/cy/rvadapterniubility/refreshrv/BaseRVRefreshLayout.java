package com.cy.rvadapterniubility.refreshrv;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.cy.refreshlayoutniubility.OnPullListener;
import com.cy.refreshlayoutniubility.RefreshLayoutNiubility;
import com.cy.rvadapterniubility.adapter.MultiAdapter;
import com.cy.rvadapterniubility.adapter.SimpleAdapter;
import com.cy.rvadapterniubility.recyclerview.BaseRecyclerView;
import com.cy.rvadapterniubility.recyclerview.OnSimpleScrollListener;
import com.cy.rvadapterniubility.recyclerview.PositionHolder;


/**
 * Created by lenovo on 2017/12/31.
 */

public abstract class BaseRVRefreshLayout<V extends BaseRecyclerView> extends RefreshLayoutNiubility {

    public BaseRVRefreshLayout(Context context) {
        this(context, null);
    }

    public BaseRVRefreshLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public abstract V getRecyclerView();

    public  abstract <T extends BaseRVRefreshLayout> T setRecyclerView(V recyclerView);


    /**
     *
     */
    public <T extends BaseRVRefreshLayout> T addOnScrollListener() {
        addOnScrollListener(new OnSimpleScrollListener() {
            @Override
            public void onScrollArrivedBottom(RecyclerView recyclerView, PositionHolder positionHolder, int offsetX, int offsetY) {
                super.onScrollArrivedBottom(recyclerView, positionHolder, offsetX, offsetY);
                startLoadMore();
            }

            @Override
            public void onSettlingShouldPausePicLoad(RecyclerView recyclerView, PositionHolder positionHolder, int velocity_x, int velocity_y, int offsetX, int offsetY) {

            }

            @Override
            public void onIdleShouldResumePicLoad(RecyclerView recyclerView, PositionHolder positionHolder, int velocity_x, int velocity_y, int offsetX, int offsetY) {

            }
        });
        return (T) this;
    }

    public <T extends BaseRVRefreshLayout> T addOnScrollListener(OnSimpleScrollListener onVerticalScrollListener) {
        getRecyclerView().addOnScrollListener(onVerticalScrollListener.getOnScrollListener());
        return (T) this;
    }


    /**
     * @param onRefreshListener
     */
    public <T extends BaseRVRefreshLayout> T setOnRefreshListener(final OnRefreshListener onRefreshListener) {
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
        return (T) this;
    }

    /**
     * @param onLoadMoreListener
     */
    public <T extends BaseRVRefreshLayout> T setOnLoadMoreListener(final OnLoadMoreListener onLoadMoreListener) {
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
        return (T) this;
    }

    @Override
    public void setOnPullListener(OnPullListener onPullListener) {
        super.setOnPullListener(onPullListener);
        addOnScrollListener();
    }

    public <T extends BaseRVRefreshLayout> T setAdapter(SimpleAdapter simpleAdapter) {
        BaseRecyclerView baseRecyclerView = getRecyclerView();
        baseRecyclerView.setAdapter(simpleAdapter);
        return (T) this;
    }
    public <T extends BaseRVRefreshLayout> T setAdapter(SimpleAdapter simpleAdapter,OnRefreshListener onRefreshListener) {
        BaseRecyclerView baseRecyclerView = getRecyclerView();
        setOnRefreshListener(onRefreshListener);
        baseRecyclerView.setAdapter(simpleAdapter);
        return (T) this;
    }
    public <T extends BaseRVRefreshLayout> T setAdapter(SimpleAdapter simpleAdapter,OnPullListener onPullListener) {
        BaseRecyclerView baseRecyclerView = getRecyclerView();
        setOnPullListener(onPullListener);
        baseRecyclerView.setAdapter(simpleAdapter);
        return (T) this;
    }
    public <T extends BaseRVRefreshLayout> T setAdapter(MultiAdapter multiAdapter) {
        BaseRecyclerView baseRecyclerView = getRecyclerView();
        baseRecyclerView.setAdapter(multiAdapter.getMergeAdapter());
        return (T) this;
    }

    public <T extends BaseRVRefreshLayout> T setAdapter(MultiAdapter multiAdapter, OnSimpleScrollListener onVerticalScrollListener) {
        BaseRecyclerView baseRecyclerView = getRecyclerView();
        baseRecyclerView.addOnScrollListener(onVerticalScrollListener);
        baseRecyclerView.setAdapter(multiAdapter.getMergeAdapter());
        return (T) this;
    }

    public <T extends BaseRVRefreshLayout> T setAdapter(MultiAdapter multiAdapter, OnPullListener onPullListener) {
        BaseRecyclerView baseRecyclerView = getRecyclerView();
        setOnPullListener(onPullListener);
        baseRecyclerView.setAdapter(multiAdapter.getMergeAdapter());
        return (T) this;
    }

}
