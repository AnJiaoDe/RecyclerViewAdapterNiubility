package com.cy.rvadapterniubility.recyclerview;

import android.os.Handler;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cy.rvadapterniubility.R;

import com.cy.rvadapterniubility.adapter.BaseViewHolder;
import com.cy.rvadapterniubility.adapter.MultiAdapter;
import com.cy.rvadapterniubility.adapter.SimpleAdapter;

/**
 * @Description:仿头条LoadMore丝滑体验
 * @Author: cy
 * @CreateDate: 2020/6/16 15:12
 * @UpdateUser:
 * @UpdateDate: 2020/6/16 15:12
 * @UpdateRemark:
 * @Version:
 */
public abstract class OnLoadMoreListener<T> extends OnSimpleScrollListener {

    public abstract void onLoadMoreStart();

    public abstract void onItemLoadMoreClick(BaseViewHolder holder);

    public abstract void bindDataToLoadMore(final BaseViewHolder holder, T bean);

    public int getVerticalLoadMoreLayoutID() {
        return R.layout.cy_loadmore_vertical_foot_default;
    }

    public int getHorizontalLoadMoreLayoutID() {
        return R.layout.cy_loadmore_horizontal_foot_default;
    }

    /**
     * 必须要有回调，必须 loadmore完全关闭后才能notify data，否则会导致上一次的loadMore动画没有停止，也没有被remove
     *
     * @param callback
     */
    public abstract void closeLoadMore(@NonNull Callback callback);

    public abstract void closeLoadMoreDelay(T msg, int ms, @NonNull Callback callback);

    public abstract SimpleAdapter<T> getLoadMoreAdapter();

    public static interface Callback {
        public void onClosed();
    }
}
