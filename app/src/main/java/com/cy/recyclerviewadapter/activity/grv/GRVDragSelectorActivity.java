package com.cy.recyclerviewadapter.activity.grv;

import static com.cy.recyclerviewadapter.ToastUtils.showToast;

import android.app.Service;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.cy.androidview.ScreenUtils;
import com.cy.androidview.selectorview.ImageViewSelector;
import com.cy.recyclerviewadapter.BaseActivity;
import com.cy.recyclerviewadapter.GlideUtils;
import com.cy.recyclerviewadapter.LogUtils;
import com.cy.recyclerviewadapter.R;
import com.cy.recyclerviewadapter.bean.HRVBean;
import com.cy.rvadapterniubility.adapter.BaseViewHolder;
import com.cy.rvadapterniubility.adapter.DragSelectorAdapter;
import com.cy.rvadapterniubility.recyclerview.GridItemDecoration;
import com.cy.rvadapterniubility.recyclerview.VerticalGridRecyclerView;

import java.util.ArrayList;
import java.util.List;

public class GRVDragSelectorActivity extends BaseActivity {

    private DragSelectorAdapter<HRVBean> dragSelectorAdapter;
    private View layout_menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grvdrag_selector);
        List<HRVBean> list = new ArrayList<>();
        for (int i = 0; i < 101; i++) {
            if (i % 5 == 0) {
                list.add(new HRVBean(R.drawable.pic3));
                continue;

            }
            list.add(new HRVBean(R.drawable.pic1));
        }
        layout_menu = findViewById(R.id.layout_menu);
        VerticalGridRecyclerView verticalGridRecyclerView = (VerticalGridRecyclerView) findViewById(R.id.VerticalGridRecyclerView);
        ImageViewSelector imageViewSelector = (ImageViewSelector) findViewById(R.id.ivs);
        TextView tv_count = (TextView) findViewById(R.id.tv_count);
        imageViewSelector.setOnCheckedChangeListener(new ImageViewSelector.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(ImageViewSelector iv, boolean isChecked) {
                LogUtils.log("onCheckedChangedALL", isChecked);
                dragSelectorAdapter.selectAll(isChecked);
            }
        });
        dragSelectorAdapter = new DragSelectorAdapter<HRVBean>() {
            @Override
            public void onSelectCountChanged(boolean isAllSelected, int count_selected) {
                imageViewSelector.setChecked(isAllSelected);
//                tv_count.setText("已选择"+getSelectedSize()+"项");
                //或者
                tv_count.setText("已选择" + count_selected + "项");

                // 降序遍历，如果要删除，从后往前删除
                for (int i = getSparseArraySelector().getSparseArray().size() - 1;
                     i >= 0; i--) {
                    LogUtils.log("onSelectCountChanged", getSparseArraySelector().getSparseArray().valueAt(i).getResID());
                }
            }

            @Override
            public void bindDataToView(BaseViewHolder holder, int position, HRVBean bean, boolean isSelected, @NonNull List<Object> payloads) {
                LogUtils.log("bindDataToView", position + ":" + isSelected + ":" + (!payloads.isEmpty() ? payloads.get(0) : ""));
                holder.setVisibility(R.id.layout_check, isUsingSelector() ? View.VISIBLE : View.GONE);
                ImageViewSelector imageViewSelector = holder.getView(R.id.ivs);
                imageViewSelector.setOnCheckedChangeListener(new ImageViewSelector.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(ImageViewSelector iv, boolean isChecked) {
                        LogUtils.log("bindDataToView onCheckedChanged", position+":"+isChecked);
                        //长按第一个ITEM后右滑然后左滑到第2个ITEM，第2个ITEM疯狂回调 导致疯狂闪烁
                        holder.setVisibility(R.id.view_mask, isChecked ? View.VISIBLE : View.GONE);
                        selectNoNotify(position, isChecked);
                    }
                });
                holder.setVisibility(R.id.view_mask, isSelected ? View.VISIBLE : View.GONE);
                //注意：setChecked必须在setOnCheckedChangeListener之后，否则VIEW复用导致position选择错乱
                imageViewSelector.setChecked(isSelected);

                //在bindDataToView 中，判断payloads是否有NOTIFY_START_DRA_SELECT 决定是否只更新item的选择框，不更新item的图片等较为耗时的操作
                if (!payloads.isEmpty() && NOTIFY_STATE_DRAG_SELECT.equals(payloads.get(0))) return;

                holder.setImageResource(R.id.iv,bean.getResID());
            }

            @Override
            public int getItemLayoutID(int position, HRVBean bean) {
                return R.layout.item_grv_drag_selector;
            }


            @Override
            public void onItemClick(BaseViewHolder holder, int position, HRVBean bean) {
                showToast("点击" + position);
            }

            @Override
            public void onItemLongClick__(BaseViewHolder holder, int position, HRVBean bean) {
                super.onItemLongClick(holder, position, bean);
                Vibrator vibrator = (Vibrator) getSystemService(Service.VIBRATOR_SERVICE);
                if (vibrator != null) vibrator.vibrate(50);
                layout_menu.setVisibility(View.VISIBLE);
                startDragSelect(position);
            }
        };
        verticalGridRecyclerView.setSpanCount(3)
                .addItemDecoration(new GridItemDecoration(ScreenUtils.dpAdapt(this, 20)));
        verticalGridRecyclerView.dragSelector(dragSelectorAdapter)
                .setAdapter(dragSelectorAdapter);
        dragSelectorAdapter.add(list);

        findViewById(R.id.iv_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layout_menu.setVisibility(View.GONE);
                dragSelectorAdapter.stopDragSelect();
            }
        });
    }

    @Override
    public void onClick(View v) {

    }
}
