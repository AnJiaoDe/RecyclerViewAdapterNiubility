package com.cy.recyclerviewadapter.activity.grv;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.cy.androidview.selectorview.ImageViewSelector;
import com.cy.recyclerviewadapter.BaseActivity;
import com.cy.recyclerviewadapter.LogUtils;
import com.cy.recyclerviewadapter.R;
import com.cy.recyclerviewadapter.bean.HRVBean;
import com.cy.rvadapterniubility.adapter.BaseViewHolder;
import com.cy.rvadapterniubility.adapter.HeadAdapter;
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

        ImageViewSelector imageViewSelector = (ImageViewSelector) findViewById(R.id.ivs);
        imageViewSelector.setVisibility(View.GONE);
        imageViewSelector.setOnCheckedChangeListener(new ImageViewSelector.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(ImageViewSelector iv, boolean isChecked, boolean byTouch) {
                LogUtils.log("onCheckedChangedALL", isChecked);
                if (!byTouch) return;
                headAdapter.getSelector().selectAll(isChecked);
            }
        });
        TextView tv_count = findViewById(R.id.tv_count);
        findViewById(R.id.btn_delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                headAdapter.removeItemsNoNotify(headAdapter.getSelector().getSelectedList());
                headAdapter.getSelector().stopDragSelectNoNotify();
                headAdapter.notifyDataSetChanged();
                imageViewSelector.setVisibility(View.GONE);
            }
        });
        headAdapter = new HeadAdapter<String, HRVBean>() {
            @Override
            public void bindHeadToView(@NonNull BaseViewHolder holder, int adapterPosition, int headPosition, String bean, @NonNull List<Object> payloads) {
                holder.setText(R.id.tv, bean+ headPosition);
                ImageViewSelector imageViewSelector = holder.getView(R.id.ivs);
                imageViewSelector.setOnCheckedChangeListener(new ImageViewSelector.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(ImageViewSelector iv, boolean isChecked, boolean byTouch) {
                        if (!byTouch) return;
                        if (isChecked && getSelector().isOverMaxCountSelect() && !getSelector().isSelected(adapterPosition)) {
                            showToast("不能超过最大选择数量");
                            imageViewSelector.setChecked(false);
                            return;
                        }
                        LogUtils.log("selectRange onCheckedChanged:" + holder.getTag(), adapterPosition + ":" + isChecked);
                        //长按第一个ITEM后右滑然后左滑到第2个ITEM，第2个ITEM疯狂回调 导致疯狂闪烁
                        getSelector().selectHead(adapterPosition, isChecked);
                    }
                });
                holder.setVisibility(R.id.ivs, getSelector().isUsingSelector()? View.VISIBLE : View.GONE);

                //注意：setChecked必须在setOnCheckedChangeListener之后，否则VIEW复用导致position选择错乱
                imageViewSelector.setChecked(getSelector().isSelected(adapterPosition));
            }

            @Override
            public void bindItemToView(@NonNull BaseViewHolder holder, int adapterPosition, int itemPosition, HRVBean bean, @NonNull List<Object> payloads) {
                LogUtils.log("bindItemToView", itemPosition);
                ImageViewSelector imageViewSelector = holder.getView(R.id.ivs);
                imageViewSelector.setOnCheckedChangeListener(new ImageViewSelector.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(ImageViewSelector iv, boolean isChecked, boolean byTouch) {
                        if (!byTouch) return;
                        if (isChecked && getSelector().isOverMaxCountSelect() && !getSelector().isSelected(adapterPosition)) {
                            showToast("不能超过最大选择数量");
                            imageViewSelector.setChecked(false);
                            return;
                        }
                        LogUtils.log("selectRange onCheckedChanged:" + holder.getTag(), adapterPosition + ":" + isChecked);
                        //长按第一个ITEM后右滑然后左滑到第2个ITEM，第2个ITEM疯狂回调 导致疯狂闪烁
                        getSelector().selectNoNotify(adapterPosition, isChecked);
                    }
                });
                holder.setVisibility(R.id.layout_check, getSelector().isUsingSelector() ? View.VISIBLE : View.GONE);
                //注意：setChecked必须在setOnCheckedChangeListener之后，否则VIEW复用导致position选择错乱
                imageViewSelector.setChecked(getSelector().isSelected(adapterPosition));

                holder.setImageResource(R.id.iv, R.drawable.pic3);
            }

            @Override
            public int getHeadLayoutID(int adapterPosition, int headPosition, String bean) {
                return R.layout.item_head_selector;
            }

            @Override
            public int getItemLayoutID(int adapterPosition, int itemPosition, HRVBean bean) {
                return R.layout.item_grv_drag_selector;
            }

            @Override
            public void onHeadClick(@NonNull BaseViewHolder holder, int adapterPosition, int headPosition, String bean) {
                showToast("onHeadClick 点击" + headPosition);
                LogUtils.log("onHeadClick", headPosition);
            }

            @Override
            public void onItemClick(@NonNull BaseViewHolder holder, int adapterPosition, int itemPosition, HRVBean bean) {
                showToast("onItemClick 点击" + itemPosition);
                LogUtils.log("onItemClick", itemPosition);
            }

            @Override
            public void onHeadLongClick(@NonNull BaseViewHolder holder, int adapterPosition, int headPosition, String bean) {
                LogUtils.log("onHeadLongClick", headPosition);
            }

            @Override
            public void onItemLongClick(@NonNull BaseViewHolder holder, int adapterPosition, int itemPosition, HRVBean bean) {
                LogUtils.log("onItemLongClick", itemPosition);
                headAdapter.getSelector().startDragSelect(adapterPosition);
                imageViewSelector.setVisibility(View.VISIBLE);
            }

            @Override
            public void onSelectCountOverMax(int maxCount) {
                super.onSelectCountOverMax(maxCount);
            }

            @Override
            public void onSelectCountChanged(boolean isAllSelected, int countSelected, int countAllCanSelect) {
                super.onSelectCountChanged(isAllSelected, countSelected, countAllCanSelect);
                tv_count.setText(countSelected + "/" + countAllCanSelect);
                imageViewSelector.setChecked(isAllSelected);
            }
        };

        VerticalGridRecyclerView verticalGridRecyclerView = findViewById(R.id.grv);
        verticalGridRecyclerView.setSpanCount(4)
                .addItemDecoration(new GridItemDecoration(dpAdapt(10)))
                .setAdapter(headAdapter);

        for (int i = 0; i < 100; i++) {
//            if(i%15==0){
//                headAdapter.addHeadNoNotify(String.valueOf(i));
//            }else {
            headAdapter.addHeadNoNotify(String.valueOf(i));

            List<HRVBean> list = new ArrayList<>();
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
            headAdapter.addItemNoNotify(String.valueOf(i), list);
//            }
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