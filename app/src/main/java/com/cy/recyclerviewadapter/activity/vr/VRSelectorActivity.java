package com.cy.recyclerviewadapter.activity.vr;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.cy.recyclerviewadapter.BaseActivity;
import com.cy.recyclerviewadapter.LogUtils;
import com.cy.recyclerviewadapter.R;
import com.cy.recyclerviewadapter.bean.VRBean;
import com.cy.rvadapterniubility.adapter.BaseViewHolder;
import com.cy.rvadapterniubility.adapter.SelectorAdapter;
import com.cy.rvadapterniubility.adapter.SimpleAdapter;
import com.cy.rvadapterniubility.recyclerview.BaseRecyclerView;
import com.cy.rvadapterniubility.recyclerview.LinearItemDecoration;
import com.cy.rvadapterniubility.recyclerview.OnSimpleScrollListener;
import com.cy.rvadapterniubility.recyclerview.PositionHolder;
import com.cy.rvadapterniubility.recyclerview.VerticalRecyclerView;

import java.util.ArrayList;
import java.util.List;

public class VRSelectorActivity extends BaseActivity {
    private SelectorAdapter<VRBean> selectorAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vrselector);

        List<VRBean> list = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            list.add(new VRBean("内容" + i));
        }
        final VerticalRecyclerView verticalRecyclerView=  findViewById(R.id.VerticalRecyclerView);
        selectorAdapter = new SelectorAdapter<VRBean>() {
            @Override
            public void bindDataToView(BaseViewHolder holder, int position, VRBean bean, boolean isSelected, @NonNull List<Object> payloads) {
                holder.setText(R.id.tv, bean.getStr());
                holder.setTextColor(R.id.tv,isSelected? Color.RED:Color.BLACK);
                LogUtils.log("bindDataToView",position);
            }

            @Override
            public int getItemLayoutID(int position, VRBean bean) {
                return R.layout.item_rv;
            }

            @Override
            public void onItemClick__(BaseViewHolder holder, int position, VRBean bean) {
                showToast("点击" + position);
            }
        };
        verticalRecyclerView.setAdapter(selectorAdapter);
        selectorAdapter.add(list);
    }

    @Override
    public void onClick(View v) {

    }
}