package com.cy.recyclerviewadapter.activity.sgrv;

import android.os.Bundle;
import android.view.View;

import com.cy.recyclerviewadapter.BaseActivity;
import com.cy.recyclerviewadapter.LogUtils;
import com.cy.recyclerviewadapter.R;
import com.cy.recyclerviewadapter.bean.HRVBean;
import com.cy.rvadapterniubility.adapter.BaseViewHolder;
import com.cy.rvadapterniubility.adapter.SimpleAdapter;
import com.cy.rvadapterniubility.recyclerview.StaggeredAdapter;
import com.cy.rvadapterniubility.recyclerview.StaggeredRecyclerView;

import java.util.ArrayList;
import java.util.List;

public class SGRVActivity extends BaseActivity {
    private StaggeredAdapter<HRVBean> rvAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sgrv);

        rvAdapter = new StaggeredAdapter<HRVBean>() {
            @Override
            public void bindDataToView(BaseViewHolder holder, int position, HRVBean bean, boolean isSelected) {
                LogUtils.log("bindDataToView00000000000000",position+bean.getResID());
                holder.setImageResource(R.id.iv,bean.getResID());
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
        ((StaggeredRecyclerView) findViewById(R.id.sgrv)).setAdapter(rvAdapter);
        List<HRVBean> list = new ArrayList<>();
        for (int i=0;i<100;i++){
            if (i%5==0){
                list.add(new HRVBean(R.drawable.pic3));
                continue;
            }
            list.add(new HRVBean(R.drawable.pic1));
        }
        rvAdapter.getSimpleAdapter().addNoNotify(list);
//        rvAdapter.getSimpleAdapter().notifyItemInserted(0);
    }

    @Override
    public void onClick(View v) {
    }

}