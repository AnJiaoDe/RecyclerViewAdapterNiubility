package com.cy.recyclerviewadapter.activity.vr;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import com.cy.recyclerviewadapter.BaseActivity;
import com.cy.recyclerviewadapter.R;
import com.cy.recyclerviewadapter.bean.VRBean;
import com.cy.rvadapterniubility.adapter.BaseViewHolder;
import com.cy.rvadapterniubility.adapter.SimpleAdapter;
import com.cy.rvadapterniubility.refreshrv.OnLoadMoreListener;
import com.cy.rvadapterniubility.refreshrv.VerticalRefreshLayout;

import java.util.ArrayList;
import java.util.List;

public class VRLoadMoreActivity extends BaseActivity {
    private SimpleAdapter<VRBean> rvAdapter;
    private VerticalRefreshLayout verticalRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vrload_more);


        verticalRefreshLayout = (VerticalRefreshLayout) findViewById(R.id.vrl);
        List<VRBean> list = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            list.add(new VRBean("内容" + i));
        }
        rvAdapter = new SimpleAdapter<VRBean>() {
            @Override
            public void bindDataToView(BaseViewHolder holder, int position, VRBean bean, boolean isSelected) {
                holder.setText(R.id.tv, bean.getStr());
            }

            @Override
            public int getItemLayoutID(int position, VRBean bean) {
                return R.layout.item_rv;
            }


            @Override
            public void onItemClick(BaseViewHolder holder,int position, VRBean bean) {
                showToast("点击" + position);

            }
        };
        verticalRefreshLayout.setAdapter(rvAdapter);
        rvAdapter.add(list);
        verticalRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMoreStart() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
//                        verticalRefreshLayout.finishLoadMore(new LoadMoreFinishListener() {
//                            @Override
//                            public void onLoadMoreFinish(FrameLayout footLayout) {
//                                verticalRefreshLayout.finishLoadMore(new LoadMoreFinishListener() {
//                                    @Override
//                                    public void onLoadMoreFinish(final FrameLayout footLayout) {
//                                        final TextView textView = new TextView(footLayout.getContext());
//                                        textView.setGravity(Gravity.CENTER);
//                                        textView.setBackgroundColor(Color.WHITE);
//                                        textView.setTextColor(Color.RED);
//                                        textView.setText("有8条更新");
//                                        footLayout.addView(textView, new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
//
//                                        new Handler().postDelayed(new Runnable() {
//                                            @Override
//                                            public void run() {
//                                                footLayout.removeView(textView);
//                                                verticalRefreshLayout.closeLoadMore();
//                                            }
//                                        }, 2000);
//                                    }
//
//                                });
//                            }
//                        });
                        verticalRefreshLayout.finishLoadMore();
                    }
                }, 3000);
            }
        });
    }

    @Override
    public void onClick(View v) {

    }
}
