package com.cy.recyclerviewadapter;

import android.os.Bundle;
import android.view.View;

import com.cy.recyclerviewadapter.activity.grv.GRVTypeActivity;
import com.cy.recyclerviewadapter.activity.hr.HRVActivity;
import com.cy.recyclerviewadapter.activity.sgrv.SGRVTypeActivity;
import com.cy.recyclerviewadapter.activity.vr.VRTypeActivity;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.btn_vertical).setOnClickListener(this);
        findViewById(R.id.btn_horizontal).setOnClickListener(this);
        findViewById(R.id.btn_grid).setOnClickListener(this);
        findViewById(R.id.btn_staggeredGrid).setOnClickListener(this);
        findViewById(R.id.btn_extendsRV).setOnClickListener(this);
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
