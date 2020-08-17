//package com.cy.cyrvadapter.recyclerview;
//
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.cy.cyrvadapter.adapter.MultiAdapter;
//import com.cy.cyrvadapter.adapter.SimpleAdapter;
//
///**
// * @Description:
// * @Author: cy
// * @CreateDate: 2020/7/15 14:18
// * @UpdateUser:
// * @UpdateDate: 2020/7/15 14:18
// * @UpdateRemark:
// * @Version:
// */
//public abstract class OnGridLoadMoreListener extends OnRVLoadMoreListener {
//    private GridRecyclerView gridRecyclerView;
//    public OnGridLoadMoreListener(MultiAdapter<SimpleAdapter> multiAdapter,GridRecyclerView gridRecyclerView) {
//        super(multiAdapter);
//        this.gridRecyclerView=gridRecyclerView;
//    }
//
//    public OnGridLoadMoreListener(MultiAdapter<SimpleAdapter> multiAdapter, int count_remain,GridRecyclerView gridRecyclerView) {
//        super(multiAdapter, count_remain);
//        this.gridRecyclerView=gridRecyclerView;
//    }
//
//    @Override
//    public void onFirstScrolled(RecyclerView recyclerView, PositionHolder positionHolder) {
//        super.onFirstScrolled(recyclerView, positionHolder);
//    }
//}
