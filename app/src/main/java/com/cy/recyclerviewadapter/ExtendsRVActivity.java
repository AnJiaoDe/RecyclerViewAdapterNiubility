package com.cy.recyclerviewadapter;

import android.os.Bundle;
import android.view.View;

import com.cy.recyclerviewadapter.bean.VRBean;

import java.util.ArrayList;
import java.util.List;

public class ExtendsRVActivity extends BaseActivity {
//    private MyRVAdapter<VRBean> rvAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_extends_rv);
//        List<VRBean> list = new ArrayList<>();
//        for (int i=0;i<100;i++){
//            list.add(new VRBean("内容"+i));
//        }
//        rvAdapter = new MyRVAdapter<VRBean>(list) {
//
//
//            @Override
//            public void bindMyDataToView(MyViewHolder holder, int position, VRBean bean, boolean isSelected) {
//                holder.setText(R.id.tv, bean.getStr());
//
//                if (position==0||position==3){
//                    holder.setMyText(R.id.tv);
//                }
//            }
//
//            @Override
//            public int getItemLayoutID(int position, VRBean bean) {
//                return R.layout.item_rv;
//            }
//
//
//
//            @Override
//            public void onItemClick(int position, VRBean bean) {
//                showMyToast(ExtendsRVActivity.this);
//
//            }
//        };
//        ((VerticalRecyclerView) findViewById(R.id.vr)).setAdapter(rvAdapter);
    }

    @Override
    public void onClick(View v) {

    }
}
