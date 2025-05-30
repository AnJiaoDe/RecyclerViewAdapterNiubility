package com.cy.recyclerviewadapter.activity.grv;

import static com.cy.recyclerviewadapter.ToastUtils.showToast;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.cy.androidview.ScreenUtils;
import com.cy.androidview.selectorview.ImageViewSelector;
import com.cy.recyclerviewadapter.R;
import com.cy.recyclerviewadapter.bean.HRVBean;
import com.cy.rvadapterniubility.LogUtils;
import com.cy.rvadapterniubility.adapter.BaseViewHolder;
import com.cy.rvadapterniubility.adapter.DragSelectFrameLayout;
import com.cy.rvadapterniubility.adapter.DragSelectorAdapter;
import com.cy.rvadapterniubility.recyclerview.GridItemDecoration;
import com.cy.rvadapterniubility.recyclerview.VerticalGridRecyclerView;

import java.util.ArrayList;
import java.util.List;

public class GRVDragSelectorActivity extends AppCompatActivity {

    private DragSelectorAdapter<HRVBean> dragSelectorAdapter;
    private View layout_menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grvdrag_selector);
        List<HRVBean> list = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            if (i % 5 == 0) {
                list.add(new HRVBean(R.drawable.pic3));
                continue;

            }
            list.add(new HRVBean(R.drawable.pic1));
        }
        layout_menu = findViewById(R.id.layout_menu);
        ImageViewSelector imageViewSelector = findViewById(R.id.ivs);
        VerticalGridRecyclerView verticalGridRecyclerView = findViewById(R.id.VerticalGridRecyclerView);
        dragSelectorAdapter = new DragSelectorAdapter<HRVBean>() {
            @Override
            public void isAllSelected(boolean selectedAll) {
                imageViewSelector.setChecked(selectedAll);
            }

            @Override
            public void bindDataToView(BaseViewHolder holder, int position, HRVBean bean, boolean isSelected) {
                LogUtils.log("bindDataToView", position + ":" + isSelected);
                DragSelectFrameLayout dragSelectFrameLayout = (DragSelectFrameLayout) holder.itemView;
                dragSelectFrameLayout.with(verticalGridRecyclerView, this);

                holder.setImageResource(R.id.iv, bean.getResID());
                holder.setVisibility(R.id.ivs, isUsingSelector() ? View.VISIBLE : View.GONE);
                holder.setImageResource(R.id.ivs, isSelected ? R.drawable.cb_selected_rect_blue : R.drawable.cb_unselected_rect_white);
//                ImageViewSelector imageViewSelector = holder.getView(R.id.ivs);
//                imageViewSelector.setChecked(isSelected);
//                imageViewSelector.setOnCheckedChangeListener(new ImageViewSelector.OnCheckedChangeListener() {
//                    @Override
//                    public void onCheckedChanged(ImageViewSelector iv, boolean isChecked) {
//                        LogUtils.log("onCheckedChanged", isChecked);
//                        toggle(position, isChecked);
//                    }
//                });
            }

            @Override
            public int getItemLayoutID(int position, HRVBean bean) {
                return R.layout.item_grv_drag_selector;
            }


            @Override
            public void onItemClick(BaseViewHolder holder, int position, HRVBean bean) {
                showToast(GRVDragSelectorActivity.this, "点击" + position);
            }

            @Override
            public void onItemLongClick(BaseViewHolder holder, int position, HRVBean bean) {
                super.onItemLongClick(holder, position, bean);
                layout_menu.setVisibility(View.VISIBLE);
                startDragSelect(position);
            }
        };
        verticalGridRecyclerView.setSpanCount(3)
                .addItemDecoration(new GridItemDecoration(ScreenUtils.dpAdapt(this, 6)));
        verticalGridRecyclerView.dragSelector(dragSelectorAdapter)
                .setAdapter(dragSelectorAdapter.getAdapter());
        dragSelectorAdapter.getAdapter().add(list);

        findViewById(R.id.iv_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layout_menu.setVisibility(View.GONE);
                dragSelectorAdapter.stopDragSelect();
            }
        });
        imageViewSelector.setOnCheckedChangeListener(new ImageViewSelector.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(ImageViewSelector iv, boolean isChecked) {
                dragSelectorAdapter.selectAll(isChecked);
            }
        });
    }
}
