package com.cy.recyclerviewadapter.activity.vr;

import android.os.Bundle;
import android.view.View;

import com.cy.recyclerviewadapter.BaseActivity;
import com.cy.recyclerviewadapter.R;
import com.cy.recyclerviewadapter.bean.VRBean;
import com.cy.rvadapterniubility.adapter.SimpleAdapter;

public class VRSelectActivity extends BaseActivity {
    private SimpleAdapter<VRBean> rvAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vrselect);
//        List<VRBean> list = new ArrayList<>();
//        for (int i=0;i<100;i++){
//            list.add(new VRBean("内容"+i));
//        }
//        rvAdapter = new BaseAdapter<VRBean>(list) {
//            @Override
//            public void bindDataToView(com.cy.BaseAdapter.viewholder.BaseViewHolder holder, int position, VRBean bean, boolean isSelected) {
//                holder.setText(R.id.tv, bean.getStr());
//
//                if (isSelected){
//                    holder.setBackgroundResource(R.id.tv,R.drawable.bg_shape);
//                }else {
//                    holder.getView(R.id.tv).setBackgroundColor(0x00000000);
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
//                showToast("点击" + position);
//
//            }
//        };
//        ((VerticalRecyclerView) findViewById(R.id.vr)).setAdapter(rvAdapter);
    }

    @Override
    public void onClick(View v) {

    }
}
