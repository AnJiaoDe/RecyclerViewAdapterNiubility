package com.cy.recyclerviewadapter.activity.vr;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.cy.recyclerviewadapter.BaseActivity;
import com.cy.recyclerviewadapter.LogUtils;
import com.cy.recyclerviewadapter.R;
import com.cy.recyclerviewadapter.bean.VRBean;
import com.cy.rvadapterniubility.adapter.BaseViewHolder;
import com.cy.rvadapterniubility.adapter.ItemAnimCallback;
import com.cy.rvadapterniubility.adapter.SimpleAdapter;
import com.cy.rvadapterniubility.recyclerview.VerticalRecyclerView;

import java.util.ArrayList;
import java.util.List;

public class VRAnimActivity extends BaseActivity {

    private SimpleAdapter<VRBean> simpleAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vr2);
        List<VRBean> list = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            list.add(new VRBean("内容" + i));
        }
        final VerticalRecyclerView verticalRecyclerView= (VerticalRecyclerView) findViewById(R.id.vr);
        simpleAdapter = new SimpleAdapter<VRBean>() {
            @Override
            public void bindDataToView(BaseViewHolder holder, int position, VRBean bean, @NonNull List<Object> payloads) {
                holder.setText(R.id.tv, bean.getStr());
//                verticalRecyclerView.setDragTouchView(holder,holder.getView(R.id.btn_drag));
                LogUtils.log("position",position);
            }

            @Override
            public int getItemLayoutID(int position, VRBean bean) {
                return R.layout.item_rv_anim;
            }

            @Override
            public void onItemClick(BaseViewHolder holder, int position, VRBean bean) {
                showToast("点击" + position);
            }

//            @Override
//            public void onViewAttachedToWindow(@NonNull BaseViewHolder holder) {
//                super.onViewAttachedToWindow(holder);
//                startDefaultAttachedAnim(holder);
//            }

        };
        ((VerticalRecyclerView) findViewById(R.id.vr)).addItemTouchAnim(new ItemAnimCallback(simpleAdapter) {
            @Override
            public void onSelectedChanged__(@Nullable RecyclerView.ViewHolder viewHolder, int actionState) {
                super.onSelectedChanged__(viewHolder, actionState);
                    viewHolder.itemView.setBackgroundColor(0xffd9d9d9);
            }

            @Override
            public void clearView(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
                super.clearView(recyclerView, viewHolder);
                viewHolder.itemView.setBackgroundResource(R.drawable.line_bottom);
            }
        }).setAdapter(simpleAdapter);
        simpleAdapter.add(list);
    }

    @Override
    public void onClick(View v) {

    }
}
