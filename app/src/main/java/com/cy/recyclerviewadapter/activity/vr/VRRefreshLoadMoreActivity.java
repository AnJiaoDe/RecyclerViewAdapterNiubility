package com.cy.recyclerviewadapter.activity.vr;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.cy.cyrvadapter.adapter.SimpleAdapter;
import com.cy.cyrvadapter.refreshlayout.LoadMoreFinishListener;
import com.cy.cyrvadapter.refreshlayout.LogUtils;
import com.cy.cyrvadapter.refreshlayout.OnPullListener;
import com.cy.cyrvadapter.refreshlayout.RefreshFinishListener;
import com.cy.cyrvadapter.refreshrv.VerticalRefreshLayout;
import com.cy.cyrvadapter.adapter.BaseViewHolder;
import com.cy.recyclerviewadapter.BaseActivity;
import com.cy.recyclerviewadapter.R;
import com.cy.recyclerviewadapter.bean.VRBean;

import java.util.ArrayList;
import java.util.List;

public class VRRefreshLoadMoreActivity extends BaseActivity {

    private SimpleAdapter<VRBean> rvAdapter;
    private VerticalRefreshLayout verticalRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vrrefresh);
        verticalRefreshLayout = findViewById(R.id.vrl);
        final List<VRBean> list = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            list.add(new VRBean("内容" + i));
        }

        rvAdapter = new SimpleAdapter<VRBean>() {
            @Override
            public void bindDataToView(BaseViewHolder holder, int position, VRBean bean,boolean isSelected) {
                holder.setText(R.id.tv, bean.getStr());
            }

            @Override
            public int getItemLayoutID(int position, VRBean bean) {
                return R.layout.item_rv;
            }

            @Override
            public void onItemClick(BaseViewHolder holder, int position, VRBean bean) {
                showToast("点击" + position);
            }
        };
        verticalRefreshLayout.setAdapter(rvAdapter, new OnPullListener() {
            @Override
            public void onRefreshStart() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        verticalRefreshLayout.finishRefresh();

//                        verticalRefreshLayout.finishRefresh(new RefreshFinishListener() {
//                            @Override
//                            public void onRefreshFinish(final FrameLayout headLayout) {
//                                for (int i = 0; i < 8; i++) {
//                                    rvAdapter.getAdapter().addToTopNoNotify(new VRBean("更新" + i));
//                                }
//                                rvAdapter.getAdapter().notifyDataSetChanged();
//
//                                final TextView textView = new TextView(headLayout.getContext());
//                                textView.setGravity(Gravity.CENTER);
//                                textView.setBackgroundColor(Color.WHITE);
//                                textView.setText("有8条更新");
//                                headLayout.addView(textView, new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
//
//                                new Handler().postDelayed(new Runnable() {
//                                    @Override
//                                    public void run() {
//                                        headLayout.removeView(textView);
//                                        verticalRefreshLayout.closeRefresh();
//                                    }
//                                }, 1000);
//                            }
//                        });
                    }
                }, 2000);

            }

            @Override
            public void onLoadMoreStart() {
                LogUtils.log("onLoadMoreStart");

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        verticalRefreshLayout.finishLoadMore(new LoadMoreFinishListener() {
                            @Override
                            public void onLoadMoreFinish(final FrameLayout footLayout) {
                                LogUtils.log("onLoadMoreFinish");

                                final TextView textView = new TextView(footLayout.getContext());
                                textView.setGravity(Gravity.CENTER);
                                textView.setBackgroundColor(Color.WHITE);
                                textView.setTextColor(Color.RED);
                                textView.setText("有8条更多");
                                footLayout.addView(textView, new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        footLayout.removeView(textView);
                                        for (int i = 0; i < 8; i++) {
                                            rvAdapter.getAdapter().addNoNotify(new VRBean("更多" + i));
                                        }
                                        rvAdapter.getAdapter().notifyDataSetChanged();
//                                                verticalRefreshLayout.getRecyclerView().smoothScrollBy(0,
//                                                        ScreenUtils.dpAdapt(VRRefreshLoadMoreActivity.this, 40) );
//                                        verticalRefreshLayout.closeLoadMore();

                                    }
                                }, 1000);

                            }
                        });
                    }
                }, 2000);
            }
        });
        rvAdapter.getAdapter().add(list);

    }

    @Override
    public void onClick(View v) {

    }
}
