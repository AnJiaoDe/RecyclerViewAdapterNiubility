package com.cy.recyclerviewadapter.activity.grv;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.cy.cyrvadapter.adapter.RVAdapter;
import com.cy.cyrvadapter.recyclerview.GridRecyclerView;
import com.cy.recyclerviewadapter.BaseActivity;
import com.cy.recyclerviewadapter.R;
import com.cy.recyclerviewadapter.bean.HRVBean;

import java.util.ArrayList;
import java.util.List;

public class GRVActivity extends BaseActivity {

    private RVAdapter<HRVBean> rvAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grv);
        List<HRVBean> list = new ArrayList<>();
        for (int i=0;i<100;i++){
            if (i%5==0){
                list.add(new HRVBean(R.drawable.pic3));
                continue;

            }
            list.add(new HRVBean(R.drawable.pic1));
        }
        rvAdapter=new RVAdapter<HRVBean>(list) {
            @Override
            public void bindDataToView(RVViewHolder holder, int position, HRVBean bean, boolean isSelected) {


                holder.setImageResource(R.id.iv,bean.getResID());
            }

            @Override
            public int getItemLayoutID(int position, HRVBean bean) {
                return R.layout.item_grv;
            }


            @Override
            public void onItemClick(int position, HRVBean bean) {

            }
        };
        ((GridRecyclerView)findViewById(R.id.grv)).setAdapter(rvAdapter,3, RecyclerView.VERTICAL,false,false);
    }

    @Override
    public void onClick(View v) {

    }
}
