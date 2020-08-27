package com.cy.recyclerviewadapter.activity.vr;

import android.os.Bundle;
import android.view.View;

import com.cy.recyclerviewadapter.BaseActivity;
import com.cy.recyclerviewadapter.R;
import com.cy.recyclerviewadapter.bean.HRVBean;
import com.cy.recyclerviewadapter.bean.VRHeadFootBean;
import com.cy.rvadapterniubility.adapter.BaseViewHolder;
import com.cy.rvadapterniubility.adapter.MultiAdapter;
import com.cy.rvadapterniubility.adapter.SimpleAdapter;
import com.cy.rvadapterniubility.recyclerview.VerticalRecyclerView;

import java.util.ArrayList;
import java.util.List;

public class VRHeadFootActivity extends BaseActivity {

    private MultiAdapter<SimpleAdapter> multiAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vr);

        multiAdapter = new MultiAdapter<SimpleAdapter>().addAdapter(new SimpleAdapter<String>() {
            @Override
            public void bindDataToView(BaseViewHolder holder, int position, String bean, boolean isSelected) {
                holder.setText(R.id.tv, "head" + position);
            }

            @Override
            public int getItemLayoutID(int position, String bean) {
                return R.layout.head;
            }

            @Override
            public void onItemClick(BaseViewHolder holder, int position, String bean) {
                showToast("点击head,删除head");
                remove(position);
            }
        }).addAdapter(new SimpleAdapter<VRHeadFootBean>() {

            @Override
            public void bindDataToView(BaseViewHolder holder, int position, VRHeadFootBean bean,boolean isSelected) {

                holder.setText(R.id.tv,bean);
            }

            @Override
            public int getItemLayoutID(int position, VRHeadFootBean bean) {
                return R.layout.item_rv;
            }


            @Override
            public void onItemClick(BaseViewHolder holder, int position, VRHeadFootBean bean) {
                showToast("点击" + position);
            }

            @Override
            public void onViewAttachedToWindow(BaseViewHolder holder) {
                super.onViewAttachedToWindow(holder);
                getAdapter().startDefaultAttachedAnim(holder);
            }

        }).addAdapter(new SimpleAdapter<String>() {
            @Override
            public void bindDataToView(BaseViewHolder holder, int position, String bean,boolean isSelected) {
                holder.setText(R.id.tv, "foot" + position);

            }

            @Override
            public int getItemLayoutID(int position, String bean) {
                return R.layout.foot;

            }

            @Override
            public void onItemClick(BaseViewHolder holder, int position, String bean) {
                showToast("点击foot,删除foot");
                remove(position);
            }
        });
        ((VerticalRecyclerView) findViewById(R.id.vr)).setAdapter(multiAdapter.getMergeAdapter());





        final List<String> list_head = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            list_head.add("head" + i);
        }


        List<VRHeadFootBean> list_content = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            list_content.add(new VRHeadFootBean("内容" + i));
        }

        final List<String> list_foot = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            list_foot.add("foot" + i);
        }
        multiAdapter.getAdapter(0).add(list_head);
        multiAdapter.getAdapter(1).add(list_content);
        multiAdapter.getAdapter(2).add(list_foot);
    }

    @Override
    public void onClick(View v) {

    }
}
