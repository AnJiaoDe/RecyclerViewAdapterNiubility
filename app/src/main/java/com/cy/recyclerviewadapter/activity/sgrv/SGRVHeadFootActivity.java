package com.cy.recyclerviewadapter.activity.sgrv;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.cy.recyclerviewadapter.BaseActivity;
import com.cy.recyclerviewadapter.LogUtils;
import com.cy.recyclerviewadapter.R;
import com.cy.recyclerviewadapter.bean.HRVBean;
import com.cy.rvadapterniubility.adapter.BaseViewHolder;
import com.cy.rvadapterniubility.adapter.MultiAdapter;
import com.cy.rvadapterniubility.adapter.SimpleAdapter;
import com.cy.rvadapterniubility.adapter.StaggeredAdapter;
import com.cy.rvadapterniubility.recyclerview.StaggeredItemDecoration;
import com.cy.rvadapterniubility.recyclerview.VerticalStaggeredRecyclerView;

import java.util.ArrayList;
import java.util.List;

public class SGRVHeadFootActivity extends BaseActivity {
    private MultiAdapter<SimpleAdapter> multiAdapter;
    private VerticalStaggeredRecyclerView verticalStaggeredRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sgrvhead_foot);


        multiAdapter = new MultiAdapter<SimpleAdapter>().addAdapter(new StaggeredAdapter<String>() {
            @Override
            public void bindDataToView(BaseViewHolder holder, int position, String bean, @NonNull List<Object> payloads) {
                holder.setText(R.id.tv, "head" + position);
            }

            @Nullable
            @Override
            public Object setHolderTagPreBindData(BaseViewHolder holder, int position, String bean) {
                return null;
            }

            @Override
            public int getItemLayoutID(int position, String bean) {
                return R.layout.foot;
            }

            @Override
            public boolean isFullSpan(int itemLayoutID) {
                return true;
            }

            @Override
            public void onItemClick(BaseViewHolder holder, int position, String bean) {
            }
        }).addAdapter(new SimpleAdapter<HRVBean>() {
            @Override
            public void bindDataToView(BaseViewHolder holder, int position, HRVBean bean, @NonNull List<Object> payloads) {
                holder.setImageResource(R.id.iv, bean.getResID());
            }

            @Override
            public int getItemLayoutID(int position, HRVBean bean) {
                return R.layout.item_sgrv;
            }


            @Override
            public void onItemClick(BaseViewHolder holder, int position, HRVBean bean) {
                showToast("点击" + position);
            }

        }).addAdapter(new StaggeredAdapter<String>() {
            @Override
            public void bindDataToView(BaseViewHolder holder, int position, String bean, @NonNull List<Object> payloads) {
                holder.setText(R.id.tv, "foot" + position);
            }

            @Nullable
            @Override
            public Object setHolderTagPreBindData(BaseViewHolder holder, int position, String bean) {
                return null;
            }

            @Override
            public int getItemLayoutID(int position, String bean) {
                return R.layout.foot;
            }

            @Override
            public boolean isFullSpan(int itemLayoutID) {
                return true;
            }

            @Override
            public void onItemClick(BaseViewHolder holder, int position, String bean) {
            }
        });
        verticalStaggeredRecyclerView = (VerticalStaggeredRecyclerView) findViewById(R.id.VerticalStaggeredRecyclerView);

        verticalStaggeredRecyclerView.setSpanCount(2)
                .addItemDecoration(new StaggeredItemDecoration(dpAdapt(10)))
                .setAdapter(multiAdapter.getMergeAdapter());

        final List<String> list_head = new ArrayList<>();
        for (int i = 0; i < 1; i++) {
            list_head.add("head" + i);
        }

        List<HRVBean> list_content = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            if (i % 5 == 0) {
                list_content.add(new HRVBean(R.drawable.pic7));
                continue;

            }
            list_content.add(new HRVBean(R.drawable.pic1));
        }

        final List<String> list_foot = new ArrayList<>();
        for (int i = 0; i < 1; i++) {
            list_foot.add("foot" + i);
        }
        multiAdapter.getAdapter(0).add(list_head);
        LogUtils.log("multiAdapter.getAdapter(0).add(list_head);");
        multiAdapter.getAdapter(1).add(list_content);
        LogUtils.log("multiAdapter.getAdapter(1).add(list_content);");
        multiAdapter.getAdapter(2).add(list_foot);
        LogUtils.log("multiAdapter.getAdapter(2).add(list_foot);");

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
