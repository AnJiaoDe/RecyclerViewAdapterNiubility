package com.cy.recyclerviewadapter.activity.vr;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;

import com.cy.cyrvadapter.adapter.SimpleAdapter;
import com.cy.cyrvadapter.adapter.LinearItemDecoration;
import com.cy.cyrvadapter.recyclerview.VerticalRecyclerView;
import com.cy.cyrvadapter.adapter.BaseViewHolder;
import com.cy.cyrvadapter.refreshlayout.LogUtils;
import com.cy.recyclerviewadapter.BaseActivity;
import com.cy.recyclerviewadapter.R;
import com.cy.recyclerviewadapter.bean.VRBean;

import java.util.ArrayList;
import java.util.List;

public class VRActivity extends BaseActivity {

    private SimpleAdapter<VRBean> rvAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vr2);
        List<VRBean> list = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            list.add(new VRBean("内容" + i));
        }
        final VerticalRecyclerView verticalRecyclerView=findViewById(R.id.vr);
        rvAdapter = new SimpleAdapter<VRBean>() {
            @Override
            public void bindDataToView(final BaseViewHolder holder, int position, VRBean bean,boolean isSelected) {
                holder.setText(R.id.tv, bean.getStr());
                LogUtils.log("position",position);
            }

            @Override
            public int getItemLayoutID(int position, VRBean bean) {
                return R.layout.item_rv;
            }

            @Override
            public void onItemClick(BaseViewHolder holder, int position, VRBean bean) {
                showToast("点击" + position);
            }
        };
        ((VerticalRecyclerView) findViewById(R.id.vr)).addItemDecoration(
                new LinearItemDecoration(rvAdapter).setSpace_vertical(dpAdapt(10)).setSpace_horizontal(0));
        ((VerticalRecyclerView) findViewById(R.id.vr)).setAdapter(rvAdapter);
        rvAdapter.add(list);
    }

    @Override
    public void onClick(View v) {

    }
    /**
     * --------------------------------------------------------------------------------
     */
    public int dpAdapt(float dp) {
        return dpAdapt(dp, 360);
    }

    public int dpAdapt(float dp, float widthDpBase) {
        DisplayMetrics dm = getResources().getDisplayMetrics();
        int heightPixels = dm.heightPixels;//高的像素
        int widthPixels = dm.widthPixels;//宽的像素
        float density = dm.density;//density=dpi/160,密度比
        float heightDP = heightPixels / density;//高度的dp
        float widthDP = widthPixels / density;//宽度的dp
        float w = widthDP > heightDP ? heightDP : widthDP;
        return (int) (dp * w / widthDpBase * density + 0.5f);
    }
}
