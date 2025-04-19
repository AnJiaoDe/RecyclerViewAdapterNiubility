package com.cy.recyclerviewadapter.activity.grv;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;

import androidx.recyclerview.widget.GridLayoutManager;

import com.cy.androidview.ScreenUtils;
import com.cy.recyclerviewadapter.BaseActivity;
import com.cy.recyclerviewadapter.LogUtils;
import com.cy.recyclerviewadapter.R;
import com.cy.recyclerviewadapter.bean.HRVBean;
import com.cy.rvadapterniubility.adapter.BaseViewHolder;
import com.cy.rvadapterniubility.adapter.SimpleAdapter;
import com.cy.rvadapterniubility.recyclerview.GridItemDecoration;
import com.cy.rvadapterniubility.recyclerview.VerticalGridRecyclerView;

import java.util.ArrayList;
import java.util.List;

public class GRVActivity extends BaseActivity {

    private SimpleAdapter<HRVBean> rvAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grv);
        List<HRVBean> list = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            if (i % 5 == 0) {
                list.add(new HRVBean(R.drawable.pic3));
                continue;

            }
            list.add(new HRVBean(R.drawable.pic1));
        }
        rvAdapter = new SimpleAdapter<HRVBean>() {
            @Override
            public void bindDataToView(BaseViewHolder holder, int position, HRVBean bean) {
                holder.setImageResource(R.id.iv, bean.getResID());
            }

            @Override
            public int getItemLayoutID(int position, HRVBean bean) {
                return R.layout.item_grv;
            }


            @Override
            public void onItemClick(BaseViewHolder holder, int position, HRVBean bean) {
                showToast("点击" + position);
            }
        };
        ((VerticalGridRecyclerView) findViewById(R.id.grv))
                .setSpanCount(3)
                .addItemDecoration(new GridItemDecoration(ScreenUtils.dpAdapt(this, 6)));
        ((VerticalGridRecyclerView) findViewById(R.id.grv)).setAdapter(rvAdapter, new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int i) {
                if (i == 4) return 1;
                return 1;
            }
        });
        rvAdapter.add(list);
    }

    @Override
    public void onClick(View v) {
    }

}
