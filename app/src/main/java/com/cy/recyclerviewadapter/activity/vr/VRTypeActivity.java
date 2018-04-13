package com.cy.recyclerviewadapter.activity.vr;

import android.os.Bundle;
import android.view.View;

import com.cy.recyclerviewadapter.BaseActivity;
import com.cy.recyclerviewadapter.R;

public class VRTypeActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vrtype);

        findViewById(R.id.btn_vr).setOnClickListener(this);
        findViewById(R.id.btn_vr_multi).setOnClickListener(this);
        findViewById(R.id.btn_vr_head_foot).setOnClickListener(this);
        findViewById(R.id.btn_vr_refreshloadmore).setOnClickListener(this);
        findViewById(R.id.btn_vr_refresh).setOnClickListener(this);
        findViewById(R.id.btn_vr_more).setOnClickListener(this);
        findViewById(R.id.btn_swipe).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_vr:

                startAppcompatActivity(VRActivity.class);
                break;
            case R.id.btn_vr_multi:

                startAppcompatActivity(VRMultiActivity.class);
                break;
            case R.id.btn_vr_head_foot:

                startAppcompatActivity(VRHeadFootActivity.class);
                break;
            case R.id.btn_vr_refreshloadmore:

                startAppcompatActivity(VRRefreshLoadMoreActivity.class);
                break;
            case R.id.btn_vr_refresh:

                startAppcompatActivity(VRRefreshActivity.class);
                break;
            case R.id.btn_vr_more:

                startAppcompatActivity(VRLoadMoreActivity.class);
                break;
            case R.id.btn_swipe:
                startAppcompatActivity(VRSwipeActivity.class);
                break;
        }
    }
}
