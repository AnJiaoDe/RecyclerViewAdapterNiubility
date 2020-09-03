//package com.cy.recyclerviewadapter.activity.sgrv;
//
//import android.os.Bundle;
//import android.view.View;
//
//import com.cy.recyclerviewadapter.BaseActivity;
//import com.cy.recyclerviewadapter.R;
//
//public class SGRVTypeActivity extends BaseActivity {
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_sgrvtype);
//        findViewById(R.id.btn_grv).setOnClickListener(this);
//        findViewById(R.id.btn_head).setOnClickListener(this);
//        findViewById(R.id.btn_refresh_loadmore).setOnClickListener(this);
//    }
//
//    @Override
//    public void onClick(View v) {
//
//        switch (v.getId()) {
//            case R.id.btn_grv:
//                startAppcompatActivity(SGRVActivity.class);
//                break;
//            case R.id.btn_head:
//                startAppcompatActivity(SGRVHeadFootActivity.class);
//
//                break;
//            case R.id.btn_refresh_loadmore:
//                startAppcompatActivity(SGRVRefreshLoadMoreActivity.class);
//
//                break;
//        }
//
//    }
//}
