package com.cy.recyclerviewadapter.activity.grv;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;

import androidx.annotation.NonNull;

import com.cy.recyclerviewadapter.BaseActivity;
import com.cy.recyclerviewadapter.LogUtils;
import com.cy.recyclerviewadapter.R;
import com.cy.recyclerviewadapter.bean.HRVBean;
import com.cy.rvadapterniubility.adapter.BaseViewHolder;
import com.cy.rvadapterniubility.adapter.MultiAdapter;
import com.cy.rvadapterniubility.adapter.SimpleAdapter;
import com.cy.rvadapterniubility.recyclerview.GridItemDecoration;
import com.cy.rvadapterniubility.recyclerview.VerticalGridRecyclerView;

import java.util.ArrayList;
import java.util.List;

public class GRVHeadFootActivity extends BaseActivity {
    private MultiAdapter<SimpleAdapter> multiAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grvhead_foot);


        multiAdapter = new MultiAdapter<SimpleAdapter>();

        VerticalGridRecyclerView verticalGridRecyclerView = findViewById(R.id.grv);
        verticalGridRecyclerView.setSpanCount(4)
                .addItemDecoration(new GridItemDecoration(dpAdapt(10)))
                .setAdapter(multiAdapter.getMergeAdapter());
//        final List<String> list_head = new ArrayList<>();
//        for (int i = 0; i < 1; i++) {
//            list_head.add("head" + i);
//        }

        int position_head = 0;
        for (int i = 0; i < 1000; i++) {
            SimpleAdapter<String> simpleAdapterFull = new SimpleAdapter<String>() {
                @Override
                public void bindDataToView(BaseViewHolder holder, int position, String bean, @NonNull List<Object> payloads) {
                    holder.setText(R.id.tv, "full" + position);
                }

                @Override
                public int getItemLayoutID(int position, String bean) {
                    return R.layout.foot;
                }

                @Override
                public void onItemClick(BaseViewHolder holder, int position, String bean) {
                }
            };
            simpleAdapterFull.add("full");
            multiAdapter.addAdapter(simpleAdapterFull);

            SimpleAdapter<HRVBean> simpleAdapter = new SimpleAdapter<HRVBean>() {
                @Override
                public void bindDataToView(BaseViewHolder holder, int position, HRVBean bean, @NonNull List<Object> payloads) {
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

                @Override
                public void onViewAttachedToWindow(BaseViewHolder holder) {
                    super.onViewAttachedToWindow(holder);
//                startDefaultAttachedAnim(holder);
                }
            };
            List<HRVBean> list_content = new ArrayList<>();
            final int max=i%5==0?16:13;
            for (int kkk = 0; kkk < max; kkk++) {
                list_content.add(new HRVBean(R.drawable.pic3));
            }
            simpleAdapter.add(list_content);
            verticalGridRecyclerView.putFullSpanPosition(position_head);
            position_head += list_content.size() + 1;
            multiAdapter.addAdapter(simpleAdapter);
        }

//        multiAdapter.addAdapter(new SimpleAdapter<String>() {
//            @Override
//            public void bindDataToView(BaseViewHolder holder, int position, String bean, @NonNull List<Object> payloads) {
//                holder.setText(R.id.tv, "foot" + position);
//            }
//
//            @Override
//            public int getItemLayoutID(int position, String bean) {
//                return R.layout.foot;
//
//            }
//
//
//            @Override
//            public void onItemClick(BaseViewHolder holder, int position, String bean) {
//            }
//        });
//        final List<String> list_foot = new ArrayList<>();
//        for (int i = 0; i < 1; i++) {
//            list_foot.add("foot" + i);
//        }
//        multiAdapter.getAdapter(0).add(list_head);
//        LogUtils.log("multiAdapter.getAdapter(0).add(list_head);");
//        multiAdapter.getAdapter(1).add(list_content);
//        LogUtils.log("multiAdapter.getAdapter(1).add(list_content);");
//        multiAdapter.getAdapter(2).add(list_foot);
//        LogUtils.log("multiAdapter.getAdapter(2).add(list_foot);");
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
