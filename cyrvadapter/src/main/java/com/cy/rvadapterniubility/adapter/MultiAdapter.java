package com.cy.rvadapterniubility.adapter;

import androidx.recyclerview.widget.MergeAdapter;
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
    private MergeAdapter mergeAdapter;

    public MultiAdapter() {
        mergeAdapter = new MergeAdapter();
    }

    public MultiAdapter<T> addAdapter(T adapter) {
        mergeAdapter.addAdapter(adapter);
        return this;
    }

    public MultiAdapter<T> addAdapter(int index, T adapter) {
        mergeAdapter.addAdapter(index, adapter);
        return this;
    }

    public MultiAdapter<T> removeAdapter(T adapter) {
        mergeAdapter.removeAdapter(adapter);
        return this;
    }
    public MultiAdapter<T> removeAllAdapter() {
        getAdapters().clear();
        mergeAdapter.notifyDataSetChanged();
        return this;
    }

    public List<T> getAdapters() {
        return  (List<T>) mergeAdapter.getAdapters();
    }

    public  T  getAdapter(int index) {
        return (T) mergeAdapter.getAdapters().get(index);
    }

    public MergeAdapter getMergeAdapter() {
        return mergeAdapter;
    }
}
