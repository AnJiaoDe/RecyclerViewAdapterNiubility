package com.cy.recyclerviewadapter.activity.vr;

import android.os.Bundle;
import android.view.View;

import com.cy.cyrvadapter.adapter.SwipeRVAdapter;
import com.cy.cyrvadapter.recyclerview.SwipeRecyclerView;
import com.cy.recyclerviewadapter.BaseActivity;
import com.cy.recyclerviewadapter.R;
import com.cy.recyclerviewadapter.bean.VRBean;

import java.util.ArrayList;

public class VRSwipeActivity extends BaseActivity {
    private SwipeRVAdapter<VRBean> rvAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vrswipe);

        final ArrayList<VRBean> list_bean = new ArrayList<VRBean>();
        for (int i = 0; i < 100; i++) {
            list_bean.add(new VRBean("内容"+i));
        }


        rvAdapter = new SwipeRVAdapter<VRBean>(list_bean) {


            @Override
            public void bindSwipeDataToView(MyViewHolder holder, int position, VRBean bean, boolean isSelected) {
                holder.setText(R.id.tv, bean.getStr());


                holder.setOnClickListener(R.id.tv_zhiding, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        rvAdapter.closeOpenedSL();
                        showToast("点击置顶");

                    }
                });
                holder.setOnClickListener(R.id.tv_biaoweiweidu, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        rvAdapter.closeOpenedSL();

                        showToast("点击标为未读");

                    }
                });
                holder.setOnClickListener(R.id.layout_delete, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        rvAdapter.closeOpenedSL();

                        showToast("点击删除");

                    }
                });
            }

            @Override
            public int getItemLayoutID(int position, VRBean bean) {
                return R.layout.item_swipe;

            }


            @Override
            public void onItemClick(int position, VRBean bean) {
                showToast("点击内容");

            }


        };
        ((SwipeRecyclerView)findViewById(R.id.srv)).setAdapter(rvAdapter);
    }

    @Override
    public void onClick(View v) {

    }
}
