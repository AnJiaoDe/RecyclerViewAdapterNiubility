//package com.cy.recyclerviewadapter.activity.sgrv;
//
//import android.os.Bundle;
//import android.view.View;
//
//import com.cy.recyclerviewadapter.BaseActivity;
//import com.cy.recyclerviewadapter.R;
//import com.cy.recyclerviewadapter.bean.HRVBean;
//import com.cy.rvadapterniubility.adapter.BaseViewHolder;
//import com.cy.rvadapterniubility.adapter.MultiAdapter;
//import com.cy.rvadapterniubility.adapter.SimpleAdapter;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class SGRVHeadFootActivity extends BaseActivity {
//    private MultiAdapter<SimpleAdapter> multiAdapter;
//
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_sgrvhead_foot);
//
//
//
//        multiAdapter = new MultiAdapter<SimpleAdapter>().addAdapter(new SimpleAdapter<String>() {
//            @Override
//            public void bindDataToView(BaseViewHolder holder, int position, String bean, boolean isSelected) {
//                holder.setText(R.id.tv, "head" + position);
//            }
//
//            @Override
//            public int getItemLayoutID(int position, String bean) {
//                return R.layout.head;
//            }
//
//            @Override
//            public void onItemClick(BaseViewHolder holder, int position, String bean) {
//                showToast("点击head,删除head");
//                remove(position);
//            }
//        }).addAdapter(new SimpleAdapter<HRVBean>() {
//
//            @Override
//            public void bindDataToView(BaseViewHolder holder, int position, HRVBean bean,boolean isSelected) {
//
//                holder.setImageResource(R.id.iv, bean.getResID());
//            }
//
//            @Override
//            public int getItemLayoutID(int position, HRVBean bean) {
//                return R.layout.item_grv;
//            }
//
//
//            @Override
//            public void onItemClick(BaseViewHolder holder, int position, HRVBean bean) {
//                showToast("点击" + position);
//            }
//
//            @Override
//            public void onViewAttachedToWindow(BaseViewHolder holder) {
//                super.onViewAttachedToWindow(holder);
//                getAdapter().startDefaultAttachedAnim(holder);
//            }
//
//        }).addAdapter(new SimpleAdapter<String>() {
//            @Override
//            public void bindDataToView(BaseViewHolder holder, int position, String bean,boolean isSelected) {
//                holder.setText(R.id.tv, "foot" + position);
//
//            }
//
//            @Override
//            public int getItemLayoutID(int position, String bean) {
//                return R.layout.foot;
//
//            }
//
//            @Override
//            public void onItemClick(BaseViewHolder holder, int position, String bean) {
//                showToast("点击foot,删除foot");
//                remove(position);
//            }
//        });
//
////        ((StaggeredGridRefreshLayout) findViewById(R.id.sgrv)).setAdapter(multiAdapter.getMergeAdapter());
//
//
//
//        List<HRVBean> list = new ArrayList<>();
//        for (int i = 0; i < 99; i++) {
//            if (i % 5 == 0) {
//                list.add(new HRVBean(R.drawable.pic3));
//                continue;
//
//            }
//            list.add(new HRVBean(R.drawable.pic7));
//        }
//    }
//
//    @Override
//    public void onClick(View v) {
//
//    }
//}
//
