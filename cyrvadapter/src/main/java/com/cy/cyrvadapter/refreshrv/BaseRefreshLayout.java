package com.cy.cyrvadapter.refreshrv;

import android.content.Context;
import android.util.AttributeSet;

import com.cy.cyrvadapter.R;
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.lcodecore.tkrefreshlayout.footer.BallPulseView;
import com.lcodecore.tkrefreshlayout.header.progresslayout.ProgressLayout;

/**
 * Created by lenovo on 2017/12/31.
 */

public class BaseRefreshLayout extends TwinklingRefreshLayout {
    private Context context;

    public BaseRefreshLayout(Context context) {
        this(context, null);
    }

    public BaseRefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);

        this.context = context;


    }


    public void setOnCYRefreshListener(final OnCYRefreshListener onCYRefreshListener) {
        ProgressLayout progressLayout = new ProgressLayout(context);


        progressLayout.setColorSchemeColors(getResources().getColor(R.color.colorPrimary));

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

    public void setOnCYLoadMoreLister(final OnCYLoadMoreLister onCYLoadMoreLister) {
        BallPulseView ballPulseView = new BallPulseView(context);


        ballPulseView.setAnimatingColor(getResources().getColor(R.color.colorPrimary));
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
    public void setOnRefreshListener(RefreshListenerAdapter refreshListener) {
        super.setOnRefreshListener(refreshListener);

        ProgressLayout progressLayout = new ProgressLayout(context);


        progressLayout.setColorSchemeColors(getResources().getColor(R.color.colorPrimary));

        setEnableOverScroll(false);

        setAutoLoadMore(false);
        setHeaderView(progressLayout);

        BallPulseView ballPulseView = new BallPulseView(context);


        ballPulseView.setAnimatingColor(getResources().getColor(R.color.colorPrimary));


        setBottomView(ballPulseView);

    }

    public interface OnCYRefreshListener {
        public void onRefresh();
    }

    public interface OnCYLoadMoreLister {

        public void onLoadMore();
    }
}
