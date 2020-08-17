//package com.cy.cyrvadapter.adapter;
//
//import androidx.recyclerview.widget.StaggeredGridLayoutManager;
//
//public abstract class StaggeredGridHeadFootCallback<T> extends HeadFootCallback<T> {
//    @Override
//    public void bindDataToView(BaseViewHolder holder, int position, T bean) {
//        // 获取cardview的布局属性，记住这里要是布局的最外层的控件的布局属性，如果是里层的会报cast错误
//        StaggeredGridLayoutManager.LayoutParams layoutParams = (StaggeredGridLayoutManager.LayoutParams) holder.itemView.getLayoutParams();
//        //最最关键一步，设置当前view占满列数，这样就可以占据两列实现头部了
//        layoutParams.setFullSpan(true);
//    }
//}
