package com.cy.recyclerviewadapter.activity.grv;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.cy.androidview.selectorview.ImageViewSelector;
import com.cy.recyclerviewadapter.BaseActivity;
import com.cy.recyclerviewadapter.LogUtils;
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
            public void bindHeadToView(@NonNull BaseViewHolder holder, int position, String head, @NonNull List<Object> payloads) {
                holder.setText(R.id.tv, "indexFullSpan:" +position);
            }

            @Override
            public void bindItemToView(@NonNull BaseViewHolder holder, int position, HRVBean item, @NonNull List<Object> payloads) {
                LogUtils.log("bindItemToView",position);
                ImageViewSelector imageViewSelector = holder.getView(R.id.ivs);
                imageViewSelector.setOnCheckedChangeListener(new ImageViewSelector.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(ImageViewSelector iv, boolean isChecked) {
                        if (isChecked && getSelector().isOverMaxCountSelect() && !getSelector().isSelected(position)) {
                            showToast("不能超过最大选择数量");
                            imageViewSelector.setChecked(false);
                            return;
                        }
                        LogUtils.log("selectRange onCheckedChanged:" + holder.getTag(), position + ":" + isChecked);
                        //长按第一个ITEM后右滑然后左滑到第2个ITEM，第2个ITEM疯狂回调 导致疯狂闪烁
                        holder.setVisibility(R.id.view_mask, isChecked ? View.VISIBLE : View.GONE);
                        getSelector().selectNoNotify(position, isChecked);
                    }
                });
                holder.setVisibility(R.id.view_mask, getSelector().isSelected(position) ? View.VISIBLE : View.GONE);
                //注意：setChecked必须在setOnCheckedChangeListener之后，否则VIEW复用导致position选择错乱
                imageViewSelector.setChecked(getSelector().isSelected(position));

                holder.setImageResource(R.id.iv, R.drawable.pic3);
            }

            @Override
            public int getHeadLayoutID(int position, String bean) {
                return R.layout.item_head_selector;
            }

            @Override
            public int getItemLayoutID(int position, HRVBean bean) {
                return R.layout.item_grv_drag_selector;
            }

            @Override
            public void onHeadClick(@NonNull BaseViewHolder holder, int position, String bean) {
                showToast("onHeadClick 点击" + bean);
                LogUtils.log("onHeadClick",position);
            }

            @Override
            public void onItemClick(@NonNull BaseViewHolder holder, int position, HRVBean bean) {
                showToast("onItemClick 点击" + position);
                LogUtils.log("onItemClick",position);
            }

            @Override
            public void onHeadLongClick(@NonNull BaseViewHolder holder, int position, String head) {
                super.onHeadLongClick(holder, position, head);
                LogUtils.log("onHeadLongClick",position);
            }

            @Override
            public void onItemLongClick(@NonNull BaseViewHolder holder, int position, HRVBean item) {
                super.onItemLongClick(holder, position, item);
                LogUtils.log("onItemLongClick",position);
            }

            @Override
            public void onSelectCountOverMax(int maxCount) {
                super.onSelectCountOverMax(maxCount);
            }

            @Override
            public void onSelectCountChanged(boolean isAllSelected, int countSelected, int countAllCanSelect) {
                super.onSelectCountChanged(isAllSelected, countSelected, countAllCanSelect);
            }
        };

        VerticalGridRecyclerView verticalGridRecyclerView = findViewById(R.id.grv);
        verticalGridRecyclerView.setSpanCount(4)
                .addItemDecoration(new GridItemDecoration(dpAdapt(10)))
                .dragSelector(headAdapter.getSelector())
                .setAdapter(headAdapter);
        headAdapter.getSelector().startDragSelect();

        for (int i = 0; i < 100; i++) {
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