package com.cy.recyclerviewadapter.activity.hr;

import android.os.Bundle;
import android.view.View;

import com.cy.cyrvadapter.adapter.SimpleAdapter;
import com.cy.cyrvadapter.recyclerview.HorizontalRecyclerView;
import com.cy.cyrvadapter.adapter.BaseViewHolder;
import com.cy.recyclerviewadapter.BaseActivity;
import com.cy.recyclerviewadapter.R;
import com.cy.recyclerviewadapter.bean.HRVBean;

import java.util.ArrayList;
import java.util.List;

public class HRVActivity extends BaseActivity {

    private SimpleAdapter<HRVBean> rvAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hrv);
        List<HRVBean> list = new ArrayList<>();
        for (int i=0;i<10;i++){
            if (i%5==0){
                list.add(new HRVBean(R.drawable.pic3));
                continue;

            }
            list.add(new HRVBean(R.drawable.pic1));
        }
        rvAdapter = new SimpleAdapter<HRVBean>() {
            @Override
            public void bindDataToView(BaseViewHolder holder, int position, HRVBean bean,boolean isSelected) {

                holder.setImageResource(R.id.iv,bean.getResID());

            }

            @Override
            public int getItemLayoutID(int position, HRVBean bean) {
                return R.layout.item_hrv;
            }

            @Override
            public void onItemClick(BaseViewHolder holder,int position, HRVBean bean) {
                showToast("点击" + position);
            }
        };
        ((HorizontalRecyclerView)findViewById(R.id.hrv)).setAdapter(rvAdapter);
        rvAdapter.add(list);
    }

    @Override
    public void onClick(View v) {

    }
}
