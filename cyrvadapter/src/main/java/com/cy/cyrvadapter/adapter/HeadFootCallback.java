//package com.cy.cyrvadapter.adapter;
//
//import androidx.recyclerview.widget.RecyclerView;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public abstract class HeadFootCallback {
//    private List<Object> list_bean = new ArrayList<>();//数据源
//
//    public int getItemCount() {
//        return list_bean.size();
//    }
//
//    public abstract void bindDataToView(BaseViewHolder holder, int position, Object bean);
//
//    public abstract int getItemLayoutID(int position, Object bean);
//
//    public abstract void onItemClick(BaseViewHolder holder, int position, Object bean);
//
//    public void onItemLongClick(BaseViewHolder holder, int position, Object bean) {
//    }
//
//    public void onViewAttachedToWindow(BaseViewHolder holder) {
//    }
//
//
//    /**
//     * ---------------------------------------------------------------------------------
//     */
//
//    public List<Object> getList_bean() {
//        return list_bean;
//    }
//
//    public HeadFootCallback setList_bean(List<Object> list_bean) {
//        this.list_bean = list_bean;
//        return this;
//    }
//
//    public HeadFootCallback remove(int position) {
//        list_bean.remove(position);
//        return this;
//    }
//
//    public HeadFootCallback add(int position, Object bean) {
//        list_bean.add(position, bean);
//        return this;
//    }
//
//    public HeadFootCallback add(Object bean) {
//        list_bean.add(bean);
//        return this;
//    }
//
//    public HeadFootCallback addToTop(Object bean) {
//        list_bean.add(0, bean);
//        return this;
//    }
//    public HeadFootCallback add(List<Object> beans) {
//        list_bean.addAll(beans);
//        return this;
//    }
//
//    public HeadFootCallback clear() {
//        list_bean.clear();
//        return this;
//    }
//    public HeadFootCallback clearAdd(List<Object> beans) {
//        clear();
//        list_bean.addAll(beans);
//        return this;
//    }
//    public HeadFootCallback addToTop(List<Object> beans) {
//        list_bean.addAll(0,beans);
//        return this;
//    }
//
//
//
//}
