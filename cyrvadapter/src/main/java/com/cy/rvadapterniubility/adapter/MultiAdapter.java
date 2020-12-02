package com.cy.rvadapterniubility.adapter;

import androidx.recyclerview.widget.ConcatAdapter;
import androidx.recyclerview.widget.RecyclerView.Adapter;

import java.util.List;

/**
 * @Description:
 * @Author: cy
 * @CreateDate: 2020/6/16 21:19
 * @UpdateUser:
 * @UpdateDate: 2020/6/16 21:19
 * @UpdateRemark:
 * @Version: 1.0
 */
public class MultiAdapter<T extends Adapter>  {
    private ConcatAdapter concatAdapter;

    public MultiAdapter() {
        concatAdapter = new ConcatAdapter();
    }

    public MultiAdapter<T> addAdapter(T adapter) {
        concatAdapter.addAdapter(adapter);
        return this;
    }

    public MultiAdapter<T> addAdapter(int index, T adapter) {
        concatAdapter.addAdapter(index, adapter);
        return this;
    }

    public MultiAdapter<T> removeAdapter(T adapter) {
        concatAdapter.removeAdapter(adapter);
        return this;
    }
    public MultiAdapter<T> removeAllAdapter() {
        getAdapters().clear();
        concatAdapter.notifyDataSetChanged();
        return this;
    }

    public List<T> getAdapters() {
        return  (List<T>) concatAdapter.getAdapters();
    }

    public  T  getAdapter(int index) {
        return (T) concatAdapter.getAdapters().get(index);
    }

    public ConcatAdapter getMergeAdapter() {
        return concatAdapter;
    }
}
