package com.cy.recyclerviewadapter.activity.tabrv;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description:
 * @Author: cy
 * @CreateDate: 2020/6/13 16:20
 * @UpdateUser:
 * @UpdateDate: 2020/6/13 16:20
 * @UpdateRemark:
 * @Version:
 */
public abstract class FragmentPageAdapter extends FragmentStateAdapter {
    private List<String> list_bean = new ArrayList<>();

    public FragmentPageAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    public FragmentPageAdapter(@NonNull Fragment fragment) {
        super(fragment);
    }

    public FragmentPageAdapter add(String bean) {
        list_bean.add(bean);
        return this;
    }

    public FragmentPageAdapter add(List<String> list_bean) {
        list_bean.addAll(list_bean);
        return this;
    }

    public List<String> getList_bean() {
        return list_bean;
    }

    @Override
    public int getItemCount() {
        return list_bean.size();
    }
}
