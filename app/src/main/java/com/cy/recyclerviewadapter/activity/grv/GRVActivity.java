package com.cy.recyclerviewadapter.activity.grv;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;

import com.cy.cyrvadapter.adapter.GridItemDecoration;
import com.cy.cyrvadapter.adapter.SimpleAdapter;
import com.cy.cyrvadapter.recyclerview.GridRecyclerView;
import com.cy.cyrvadapter.adapter.BaseViewHolder;
import com.cy.cyrvadapter.refreshlayout.LogUtils;
import com.cy.recyclerviewadapter.BaseActivity;
import com.cy.recyclerviewadapter.R;
import com.cy.recyclerviewadapter.bean.HRVBean;

import java.util.ArrayList;
import java.util.List;

public class GRVActivity extends BaseActivity {

    private SimpleAdapter<HRVBean> rvAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grv);
        LogUtils.log("GRVActivity");
        List<HRVBean> list = new ArrayList<>();
        for (int i=0;i<300;i++){
            if (i%5==0){
                list.add(new HRVBean(R.drawable.pic3));
                continue;

            }
            list.add(new HRVBean(R.drawable.pic1));
        }
        rvAdapter=new SimpleAdapter<HRVBean>() {
            @Override
            public void bindDataToView(BaseViewHolder holder, int position, HRVBean bean,boolean isSelected) {
                holder.setImageResource(R.id.iv,bean.getResID());
            }

            @Override
            public int getItemLayoutID(int position, HRVBean bean) {
                return R.layout.item_grv;
            }


            @Override
            public void onItemClick(BaseViewHolder holder,int position, HRVBean bean) {
                showToast("点击" + position);
            }
        };
        ((GridRecyclerView)findViewById(R.id.grv))
                .setSpanCount(4)
                .addItemDecoration(new GridItemDecoration((GridRecyclerView)findViewById(R.id.grv),dpAdapt(10)));
        ((GridRecyclerView)findViewById(R.id.grv)).setAdapter(rvAdapter);
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
