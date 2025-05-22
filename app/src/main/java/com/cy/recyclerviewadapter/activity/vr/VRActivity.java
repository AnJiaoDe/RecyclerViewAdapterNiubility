package com.cy.recyclerviewadapter.activity.vr;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cy.recyclerviewadapter.BaseActivity;
import com.cy.recyclerviewadapter.LogUtils;
import com.cy.recyclerviewadapter.R;
import com.cy.recyclerviewadapter.bean.VRBean;
import com.cy.rvadapterniubility.adapter.BaseViewHolder;
import com.cy.rvadapterniubility.adapter.SimpleAdapter;
import com.cy.rvadapterniubility.recyclerview.BaseRecyclerView;
import com.cy.rvadapterniubility.recyclerview.LinearItemDecoration;
import com.cy.rvadapterniubility.recyclerview.OnSimpleScrollListener;
import com.cy.rvadapterniubility.recyclerview.PositionHolder;
import com.cy.rvadapterniubility.recyclerview.VerticalRecyclerView;

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
            public void bindDataToView(final BaseViewHolder holder, int position, VRBean bean) {
                holder.setText(R.id.tv, bean.getStr());
                LogUtils.log("bindDataToView",position);
            }

            @Override
            public int getItemLayoutID(int position, VRBean bean) {
                return R.layout.item_rv;
            }

            @Override
            public void onItemClick(BaseViewHolder holder, int position, VRBean bean) {
                showToast("点击" + position);
                clear();
            }
        };
        ((VerticalRecyclerView) findViewById(R.id.vr)).addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                switch (newState) {
                    case RecyclerView.SCROLL_STATE_IDLE:
                        LogUtils.log("SCROLL_STATE_IDLE");
                        break;
                    case RecyclerView.SCROLL_STATE_DRAGGING:
                        LogUtils.log("SCROLL_STATE_DRAGGING");
                        break;
                    case RecyclerView.SCROLL_STATE_SETTLING:
                        LogUtils.log("SCROLL_STATE_SETTLING");
                        break;
                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LogUtils.log("SCROLL");
            }
        });
        ((VerticalRecyclerView) findViewById(R.id.vr)).addOnScrollListener(new OnSimpleScrollListener() {
            @Override
            public void onItemShow(BaseRecyclerView baseRecyclerView, int position, PositionHolder positionHolder) {
                super.onItemShow(baseRecyclerView, position, positionHolder);
                LogUtils.log("onItemShow",position);
            }
        });
        ((VerticalRecyclerView) findViewById(R.id.vr)).addItemDecoration(
                new LinearItemDecoration().setSpace_vertical(dpAdapt(10)).setSpace_horizontal(dpAdapt(10)));
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
