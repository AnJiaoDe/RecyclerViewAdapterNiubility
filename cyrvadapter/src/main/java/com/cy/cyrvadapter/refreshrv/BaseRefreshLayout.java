package com.cy.cyrvadapter.refreshrv;

import android.content.Context;
import android.util.AttributeSet;

import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.lcodecore.tkrefreshlayout.footer.BallPulseView;
import com.lcodecore.tkrefreshlayout.header.progresslayout.ProgressLayout;

/**
 * Created by lenovo on 2017/12/31.
 */

public class BaseRefreshLayout extends TwinklingRefreshLayout {

    public BaseRefreshLayout(Context context) {
        this(context, null);
    }

    public BaseRefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);



    }


    public void setOnCYRefreshListener(Context context,final OnCYRefreshListener onCYRefreshListener,int color) {
        ProgressLayout progressLayout = new ProgressLayout(context);


        progressLayout.setColorSchemeColors(color);

        setEnableOverScroll(false);
        setEnableLoadmore(false);


        setHeaderView(progressLayout);

        setOnRefreshListener(new RefreshListenerAdapter() {
            @Override
            public void onRefresh(TwinklingRefreshLayout refreshLayout) {
                super.onRefresh(refreshLayout);
                onCYRefreshListener.onRefresh();
            }
        });

    }

    public void setOnCYLoadMoreLister(Context context,final OnCYLoadMoreLister onCYLoadMoreLister,int color) {
        BallPulseView ballPulseView = new BallPulseView(context);


        ballPulseView.setAnimatingColor(color);
        setEnableOverScroll(false);
        setEnableRefresh(false);


        setBottomView(ballPulseView);

        setOnRefreshListener(new RefreshListenerAdapter() {
            @Override
            public void onLoadMore(TwinklingRefreshLayout refreshLayout) {
                super.onLoadMore(refreshLayout);
                onCYLoadMoreLister.onLoadMore();
            }
        });
    }

    @Override
    public final void setOnRefreshListener(RefreshListenerAdapter refreshListener) {
        super.setOnRefreshListener(refreshListener);


    }
    public void setOnRefreshListener(Context context,RefreshListenerAdapter refreshListener,int color) {
        setOnRefreshListener(refreshListener);

        ProgressLayout progressLayout = new ProgressLayout(context);


        progressLayout.setColorSchemeColors(color);

        setEnableOverScroll(false);

        setAutoLoadMore(false);
        setHeaderView(progressLayout);

        BallPulseView ballPulseView = new BallPulseView(context);


        ballPulseView.setAnimatingColor(color);


        setBottomView(ballPulseView);

    }

    public interface OnCYRefreshListener {
        public void onRefresh();
    }

    public interface OnCYLoadMoreLister {

        public void onLoadMore();
    }
}
