//package com.cy.recyclerviewadapter;
//
//import android.content.Context;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.TextView;
//
//import com.cy.BaseAdapter.adapter.BaseAdapter;
//
//import java.util.List;
//
///**
// * 自定义全局RVAdapter继承自库中RVAdapter
// */
//
//public abstract class MyRVAdapter<T> extends BaseAdapter<T> {
//
//
//    //复写构造方法，可根据需要选择，但至少复写一个
//
//    public MyRVAdapter(List list_bean) {
//        super(list_bean);
//    }
//
//    public MyRVAdapter(List list, boolean isStaggeredGrid) {
//        super(list, isStaggeredGrid);
//    }
//
//    public MyRVAdapter(List list_bean, boolean haveHeadView, boolean haveFootView) {
//        super(list_bean, haveHeadView, haveFootView);
//    }
//
//    public MyRVAdapter(List list_bean, boolean isStaggeredGrid, boolean haveHeadView, boolean haveFootView) {
//        super(list_bean, isStaggeredGrid, haveHeadView, haveFootView);
//    }
//
//    //???????????????????????????????????????????????????????????????
//    //如果想在ViewHolder添加方法,复写父类方法，返回自定义的ViewHolder
//    @Override
//    public com.cy.BaseAdapter.viewholder.BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false));
//
//    }
//
//    //如果想在ViewHolder添加方法,并且在实现或者复写的父类方法中使用自定义ViewHolder,父类ViewHolder必须强转为自定义的ViewHolder
//    @Override
//    public  void bindDataToView(com.cy.BaseAdapter.viewholder.BaseViewHolder holder, int position, T bean, boolean isSelected) {
//        bindMyDataToView((MyViewHolder) holder, position, bean, isSelected);
//    }
//
//    @Override
//    public  void bindDataToHeadView(com.cy.BaseAdapter.viewholder.BaseViewHolder holder) {
//        super.bindDataToHeadView(holder);
//
//        bindMyDataToHeadView((MyViewHolder) holder);
//    }
//
//    @Override
//    public  void bindDataToFootView(com.cy.BaseAdapter.viewholder.BaseViewHolder holder) {
//        super.bindDataToFootView(holder);
//
//        bindMyDataToFootView((MyViewHolder) holder);
//    }
//
//    //???????????????????????????????????????????????????????????????????????
//    //填充数据，isSelected:整个RV做单选，点击到哪个，哪个就是选中状态
//    public abstract void bindMyDataToView(MyViewHolder holder, int position, T bean, boolean isSelected);
//
//    //添加头部 填充数据
//    public void bindMyDataToHeadView(MyViewHolder holder) {
//    }
//
//    //添加尾部 填充数据
//    public void bindMyDataToFootView(MyViewHolder holder) {
//    }
//    //???????????????????????????????????????????????????????????????????????
//
//    //自己添加任意方法
//    public void showMyToast(Context context) {
//        ToastUtils.showToast(context, "自定义MyRVAdapter中的方法");
//    }
//
//    //操作父类属性,List
//    public void operate() {
//        getList_bean().get(0);
//    }
//
//    /**
//     * 如果想在ViewHolder添加方法,首先继承RVAdapter,然后继承此类，并且实现其构造方法
//     */
//    public static class MyViewHolder extends BaseAdapter.BaseViewHolder {
//        public MyViewHolder(View itemView) {
//            super(itemView);
//        }
//        //自己添加任意方法
//        //设置TextView 的Text
//
//        public MyViewHolder setMyText(int tv_id) {
//            TextView tv = getView(tv_id);
//
//
//            tv.setText("自定义MyViewHolder中的方法");
//
//            return this;
//        }
//
//    }
//}
