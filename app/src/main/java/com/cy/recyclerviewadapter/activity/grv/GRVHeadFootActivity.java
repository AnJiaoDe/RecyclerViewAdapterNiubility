package com.cy.recyclerviewadapter.activity.grv;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;

import androidx.annotation.NonNull;

import com.cy.recyclerviewadapter.BaseActivity;
import com.cy.recyclerviewadapter.R;
import com.cy.rvadapterniubility.adapter.BaseViewHolder;
import com.cy.rvadapterniubility.adapter.SimpleAdapter;
import com.cy.rvadapterniubility.recyclerview.GridItemDecoration;
import com.cy.rvadapterniubility.recyclerview.VerticalGridRecyclerView;

import java.util.List;

public class GRVHeadFootActivity extends BaseActivity {
    private SimpleAdapter<Boolean> simpleAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grvhead_foot);

        simpleAdapter = new SimpleAdapter<Boolean>() {
            @Override
            public void bindDataToView(@NonNull BaseViewHolder holder, int position, Boolean bean, @NonNull List<Object> payloads, int indexFullSpan) {
                if (bean) {
                    holder.setText(R.id.tv, "tab" + position);
                    return;
                }
                holder.setImageResource(R.id.iv, R.drawable.pic3);
            }

            @Override
            public int getItemLayoutID(int position, Boolean bean) {
                if(bean)return R.layout.item_head_00;
                return R.layout.item_grv;
            }

            @Override
            public void onItemClick(@NonNull BaseViewHolder holder, int position, Boolean bean, int indexFullSpan) {
                showToast("点击" + position);
            }

            @Override
            public boolean isFullSpan(int itemLayoutID) {
                if(itemLayoutID==R.layout.item_head_00)return true;
                return super.isFullSpan(itemLayoutID);
            }

            @Override
            public void onViewAttachedToWindow(BaseViewHolder holder) {
                super.onViewAttachedToWindow(holder);
//                startDefaultAttachedAnim(holder);
            }
        };

        VerticalGridRecyclerView verticalGridRecyclerView = findViewById(R.id.grv);
        verticalGridRecyclerView.setSpanCount(4)
                .addItemDecoration(new GridItemDecoration(dpAdapt(10)))
                .setAdapter(simpleAdapter);

        for (int i = 0; i < 1000; i++) {
            simpleAdapter.addNoNotify(i % 15 == 0 );
        }
        simpleAdapter.notifyDataSetChanged();
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
