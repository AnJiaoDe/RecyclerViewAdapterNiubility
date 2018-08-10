package com.cy.recyclerviewadapter;

import android.os.Bundle;
import android.view.View;

import com.cy.cyrvadapter.adapter.RVAdapter;
import com.cy.cyrvadapter.recyclerview.GridRecyclerView;
import com.cy.recyclerviewadapter.activity.grv.GRVTypeActivity;
import com.cy.recyclerviewadapter.activity.hr.HRVActivity;
import com.cy.recyclerviewadapter.activity.sgrv.SGRVTypeActivity;
import com.cy.recyclerviewadapter.activity.vr.VRTypeActivity;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends BaseActivity {
    private RVAdapter<String> rvAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.btn_vertical).setOnClickListener(this);
        findViewById(R.id.btn_horizontal).setOnClickListener(this);
        findViewById(R.id.btn_grid).setOnClickListener(this);
        findViewById(R.id.btn_staggeredGrid).setOnClickListener(this);
        findViewById(R.id.btn_extendsRV).setOnClickListener(this);


        List<String> list=new ArrayList<>();
        rvAdapter=new RVAdapter<String>(list,false,true) {
            @Override
            public void bindDataToView(RVViewHolder holder, int position, String bean, boolean isSelected) {

            }

            @Override
            public int getItemLayoutID(int position, String bean) {
               if (position==getItemCount()-1){
                   return R.layout.foot_main;
               }
                return R.layout.item_rv;
            }

            @Override
            public void onItemClick(int position, String bean) {

            }
        };

        ((GridRecyclerView)findViewById(R.id.grv)).setAdapter(rvAdapter,3,false,true);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btn_vertical:
                startAppcompatActivity(VRTypeActivity.class);
                break;
            case R.id.btn_horizontal:
                startAppcompatActivity(HRVActivity.class);

                break;
            case R.id.btn_grid:
                startAppcompatActivity(GRVTypeActivity.class);

                break;
            case R.id.btn_staggeredGrid:
                startAppcompatActivity(SGRVTypeActivity.class);

                break;
            case R.id.btn_extendsRV:
                startAppcompatActivity(ExtendsRVActivity.class);

                break;
        }
    }
}
