package com.cy.rvadapterniubility.recyclerview;

import android.os.Handler;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cy.BaseAdapter.R;
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
     * 必须手动调用closeLoadMore()结束loadMore
     */
    public abstract void closeLoadMore() ;

    public abstract void closeLoadMoreDelay(T msg,int ms);
}
