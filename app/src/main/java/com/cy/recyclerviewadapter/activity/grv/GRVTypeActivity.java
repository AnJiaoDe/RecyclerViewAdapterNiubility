package com.cy.recyclerviewadapter.activity.grv;

import android.os.Bundle;
import android.view.View;

import com.cy.recyclerviewadapter.BaseActivity;
import com.cy.recyclerviewadapter.R;

public class GRVTypeActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grvtype);

        findViewById(R.id.btn_grv).setOnClickListener(this);
        findViewById(R.id.btn_grv_h).setOnClickListener(this);
        findViewById(R.id.btn_head).setOnClickListener(this);
        findViewById(R.id.btn_refresh_loadmore).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btn_grv:
                startAppcompatActivity(GRVActivity.class);
                break;
            case R.id.btn_grv_h:
                startAppcompatActivity(GRVHorinzotalActivity.class);
                break;
            case R.id.btn_head:
                startAppcompatActivity(GRVHeadFootActivity.class);

                break;
            case R.id.btn_refresh_loadmore:
                startAppcompatActivity(GRVRefreshLoadMoreActivity.class);

                break;
        }

    }
}
