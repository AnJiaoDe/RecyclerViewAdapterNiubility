package com.cy.recyclerviewadapter.activity.sgrv;

import android.os.Bundle;
import android.view.View;

import com.cy.recyclerviewadapter.BaseActivity;
import com.cy.recyclerviewadapter.R;
import com.cy.recyclerviewadapter.bean.HRVBean;
import com.cy.rvadapterniubility.adapter.BaseViewHolder;
import com.cy.rvadapterniubility.adapter.SimpleAdapter;
import com.cy.rvadapterniubility.recyclerview.StaggeredRecyclerView;

import java.util.ArrayList;
import java.util.List;

public class SGRVActivity extends BaseActivity {
    private SimpleAdapter<HRVBean> rvAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sgrv);


        rvAdapter = new SimpleAdapter<HRVBean>() {
            @Override
            public void bindDataToView(BaseViewHolder holder, int position, HRVBean bean, boolean isSelected) {
                holder.setBackgroundResource(R.id.iv,bean.getResID());
            }

            @Override
            public int getItemLayoutID(int position, HRVBean bean) {
                return R.layout.item_sgrv;
            }


            @Override
            public void onItemClick(BaseViewHolder holder,int position, HRVBean bean) {

                showToast("点击" + position);
            }
        };
        ((StaggeredRecyclerView) findViewById(R.id.grv)).setAdapter(rvAdapter);
        List<HRVBean> list = new ArrayList<>();
        for (int i=0;i<300;i++){
            if (i%5==0){
                list.add(new HRVBean(R.drawable.pic3));
                continue;

            }
            list.add(new HRVBean(R.drawable.pic1));
        }
        rvAdapter.add(list);
    }

    @Override
    public void onClick(View v) {

    }
}