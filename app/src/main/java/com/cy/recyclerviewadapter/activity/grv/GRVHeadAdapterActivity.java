package com.cy.recyclerviewadapter.activity.grv;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.cy.recyclerviewadapter.BaseActivity;
import com.cy.recyclerviewadapter.R;
import com.cy.recyclerviewadapter.bean.HRVBean;
import com.cy.rvadapterniubility.adapter.BaseViewHolder;
import com.cy.rvadapterniubility.adapter.HeadAdapter;
import com.cy.rvadapterniubility.adapter.SimpleAdapter;
import com.cy.rvadapterniubility.recyclerview.GridItemDecoration;
import com.cy.rvadapterniubility.recyclerview.VerticalGridRecyclerView;

import java.util.ArrayList;
import java.util.List;

public class GRVHeadAdapterActivity extends BaseActivity {
    private HeadAdapter<String, HRVBean> headAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grvhead_adapter);

        headAdapter = new HeadAdapter<String, HRVBean>() {
            @Override
            public void bindDataToHeadView(@NonNull BaseViewHolder holder, int position, String bean, @NonNull List<Object> payloads) {
                holder.setText(R.id.tv, "indexFullSpan:" +position);
            }

            @Override
            public void bindDataToView(@NonNull BaseViewHolder holder, int position, HRVBean bean, @NonNull List<Object> payloads) {
                holder.setImageResource(R.id.iv, R.drawable.pic3);
            }

            @Override
            public int getHeadLayoutID(int position, String bean) {
                return R.layout.item_head_00;
            }

            @Override
            public int getItemLayoutID(int position, HRVBean bean) {
                return R.layout.item_grv;
            }

            @Override
            public void onHeadClick(@NonNull BaseViewHolder holder, int position, String bean) {
                showToast("点击" + bean);
            }

            @Override
            public void onItemClick(@NonNull BaseViewHolder holder, int position, HRVBean bean) {
                showToast("点击" + position);
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
                .setAdapter(headAdapter);

        for (int i = 0; i < 1000; i++) {
            if(i%15==0){
                headAdapter.addHeadNoNotify(String.valueOf(i));
            }else {
                List<HRVBean> list=new ArrayList<>();
                list.add(new HRVBean(R.drawable.pic3));
                list.add(new HRVBean(R.drawable.pic3));
                list.add(new HRVBean(R.drawable.pic3));
                list.add(new HRVBean(R.drawable.pic3));
                list.add(new HRVBean(R.drawable.pic3));
                list.add(new HRVBean(R.drawable.pic3));
                list.add(new HRVBean(R.drawable.pic3));
                list.add(new HRVBean(R.drawable.pic3));
                list.add(new HRVBean(R.drawable.pic3));
                list.add(new HRVBean(R.drawable.pic3));
                headAdapter.addItemNoNotify(String.valueOf(i),list);
            }
        }
        headAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {

    }
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