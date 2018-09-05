package com.cy.recyclerviewadapter.activity.sgrv;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.cy.cyrvadapter.adapter.RVAdapter;
import com.cy.cyrvadapter.recyclerview.StaggeredGridRecyclerView;
import com.cy.recyclerviewadapter.BaseActivity;
import com.cy.recyclerviewadapter.R;
import com.cy.recyclerviewadapter.bean.HRVBean;

import java.util.ArrayList;
import java.util.List;

public class SGRVHeadFootActivity extends BaseActivity {
    private RVAdapter<HRVBean> rvAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sgrvhead_foot);


        List<HRVBean> list = new ArrayList<>();
        for (int i = 0; i < 99; i++) {
            if (i % 5 == 0) {
                list.add(new HRVBean(R.drawable.pic3));
                continue;

            }
            list.add(new HRVBean(R.drawable.pic7));
        }
        rvAdapter = new RVAdapter<HRVBean>(list, true,true,true) {
            @Override
            public void bindDataToHeadView(RVViewHolder holder) {
                super.bindDataToHeadView(holder);
            }
            @Override
            public void bindDataToFootView(RVViewHolder holder) {
                super.bindDataToFootView(holder);
            }

            @Override
            public void bindDataToView(RVViewHolder holder, int position, HRVBean bean, boolean isSelected) {



                holder.setImageResource(R.id.iv, bean.getResID());
            }

            @Override
            public int getItemLayoutID(int position, HRVBean bean) {
                if (position == 0) {
                    return R.layout.head;
                }
                if (position == getItemCount() - 1) {
                    return R.layout.foot;
                }
                return R.layout.item_grv;
            }


            @Override
            public void onItemClick(int position, HRVBean bean) {

                showToast("点击"+position);

            }

            @Override
            public void onHeadClick() {
            }

            @Override
            public void onFootClick() {
                super.onFootClick();
            }
        };
        ((StaggeredGridRecyclerView) findViewById(R.id.grv)).setAdapter(getApplicationContext(),rvAdapter, 3, RecyclerView.VERTICAL);
    }

    @Override
    public void onClick(View v) {

    }
}

