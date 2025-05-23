package com.cy.recyclerviewadapter.activity.grv;

import static com.cy.recyclerviewadapter.ToastUtils.showToast;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;

import com.cy.androidview.ScreenUtils;
import com.cy.androidview.selectorview.ImageViewSelector;
import com.cy.recyclerviewadapter.R;
import com.cy.recyclerviewadapter.bean.HRVBean;
import com.cy.rvadapterniubility.LogUtils;
import com.cy.rvadapterniubility.adapter.BaseViewHolder;
import com.cy.rvadapterniubility.adapter.DragSelectorAdapter;
import com.cy.rvadapterniubility.adapter.SimpleAdapter;
import com.cy.rvadapterniubility.recyclerview.GridItemDecoration;
import com.cy.rvadapterniubility.recyclerview.VerticalGridRecyclerView;

import java.util.ArrayList;
import java.util.List;

public class GRVDragSelectorActivity extends AppCompatActivity {

    private DragSelectorAdapter<HRVBean> dragSelectorAdapter;

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
        dragSelectorAdapter = new DragSelectorAdapter<HRVBean>() {
            @Override
            public void bindDataToView(BaseViewHolder holder, int position, HRVBean bean, boolean isSelected) {
                LogUtils.log("bindDataToView",position+":"+isSelected);
                holder.setImageResource(R.id.iv, bean.getResID());
                holder.setVisibility(R.id.ivs, isUseDragSelect() ? View.VISIBLE : View.GONE);
                ImageViewSelector imageViewSelector = holder.getView(R.id.ivs);
                imageViewSelector.setChecked(isSelected);
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
                startDragSelect(position);
                getAdapter().postNotifyDataSetChanged();
            }
        };
        ((VerticalGridRecyclerView) findViewById(R.id.VerticalGridRecyclerView))
                .setSpanCount(4)
                .addItemDecoration(new GridItemDecoration(ScreenUtils.dpAdapt(this, 6)));
        ((VerticalGridRecyclerView) findViewById(R.id.VerticalGridRecyclerView)).addOnItemTouchListener(dragSelectorAdapter)
                .setAdapter(dragSelectorAdapter.getAdapter(), new GridLayoutManager.SpanSizeLookup() {
                    @Override
                    public int getSpanSize(int i) {
                        if (i == 4) return 1;
                        return 1;
                    }
                });
        dragSelectorAdapter.getAdapter().add(list);
    }
}
