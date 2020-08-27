package com.cy.rvadapterniubility.recyclerview;

import com.cy.rvadapterniubility.adapter.BaseViewHolder;
import com.cy.rvadapterniubility.adapter.IAdapter;
import com.cy.rvadapterniubility.adapter.SimpleAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cy on 2018/3/29.类似策略模式,引入IAdapter接口，面向多态编程
 */

public abstract class StaggeredAdapter<T> implements IAdapter<T, BaseViewHolder> {
    private List<SimpleAdapter> listAdapter;

    public StaggeredAdapter() {
        listAdapter = new ArrayList<SimpleAdapter>();
    }

    List<SimpleAdapter> getListAdapter() {
        return listAdapter;
    }
    public SimpleAdapter getSimpleAdapter(){
        return listAdapter.get(0);
    }
    @Override
    public void onItemLongClick(BaseViewHolder holder, int position, T bean) {

    }

    /**
     * 再次彰显面向多态编程的威力，接口的强扩展
     *
     * @param holder
     */
    @Override
    public void onViewAttachedToWindow(BaseViewHolder holder) {

    }
}
