package com.cy.recyclerviewadapter.activity.vr;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.cy.recyclerviewadapter.BaseActivity;
import com.cy.recyclerviewadapter.LogUtils;
import com.cy.recyclerviewadapter.R;
import com.cy.recyclerviewadapter.bean.VRBean;
import com.cy.refreshlayoutniubility.RefreshFinishListener;
import com.cy.rvadapterniubility.adapter.BaseViewHolder;
import com.cy.rvadapterniubility.adapter.MultiAdapter;
import com.cy.rvadapterniubility.adapter.SimpleAdapter;
import com.cy.rvadapterniubility.recyclerview.LinearItemDecoration;
import com.cy.rvadapterniubility.recyclerview.OnCloseLoadMoreCallback;
import com.cy.rvadapterniubility.recyclerview.OnLinearLoadMoreListener;
import com.cy.rvadapterniubility.recyclerview.OnSimpleLinearLoadMoreListener;
import com.cy.rvadapterniubility.recyclerview.PositionHolder;
import com.cy.rvadapterniubility.refreshrv.OnRefreshListener;
import com.cy.rvadapterniubility.refreshrv.LinearRefreshLayout;

import java.util.ArrayList;
import java.util.List;

public class VRRefreshLoadMoreSelfActivity extends BaseActivity {

    private MultiAdapter<SimpleAdapter> multiAdapter;
    private LinearRefreshLayout verticalRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vrrefresh);
        verticalRefreshLayout = findViewById(R.id.vrl);

        multiAdapter = new MultiAdapter<SimpleAdapter>().addAdapter(new SimpleAdapter<String>() {
            @Override
            public void bindDataToView(BaseViewHolder holder, int position, String bean, boolean isSelected) {
                holder.setText(R.id.tv, "head" + position);
            }

            @Override
            public int getItemLayoutID(int position, String bean) {
                return R.layout.head;
            }

            @Override
            public void onItemClick(BaseViewHolder holder, int position, String bean) {
                showToast("点击head,删除head");
                remove(position);
            }
        }).addAdapter(new SimpleAdapter<VRBean>() {
            @Override
            public void bindDataToView(BaseViewHolder holder, int position, VRBean bean, boolean isSelected) {
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
        });

//        verticalRefreshLayout.setEnableLoadMore(false);
//        verticalRefreshLayout.getRecyclerView().setAdapter(multiAdapter.getMergeAdapter());
//        verticalRefreshLayout.getRecyclerView().addItemDecoration(new LinearItemDecoration().setSpace_vertical(60));
        verticalRefreshLayout.setAdapter(multiAdapter, new OnRefreshListener() {

            @Override
            public void onRefreshFinish() {
                super.onRefreshFinish();
            }


            @Override
            public void onRefreshStart() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        verticalRefreshLayout.finishRefresh(new RefreshFinishListener() {
                            @Override
                            public void onRefreshFinish(final FrameLayout headLayout) {
                                for (int i = 0; i < 1; i++) {
                                    multiAdapter.getAdapters().get(1).addToTopNoNotify(new VRBean("更新" + i));
                                }
                                multiAdapter.getAdapters().get(1).notifyDataSetChanged();

                                final TextView textView = new TextView(headLayout.getContext());
                                textView.setGravity(Gravity.CENTER);
                                textView.setBackgroundColor(Color.WHITE);
                                textView.setText("有8条更新");
                                headLayout.addView(textView, new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        headLayout.removeView(textView);
                                        verticalRefreshLayout.closeRefresh();
                                    }
                                }, 1000);
                            }
                        });
                    }
                }, 2000);
            }
        }, new OnSimpleLinearLoadMoreListener(multiAdapter, 6) {

            @Override
            public void onLoadMoreStart() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        /**
                         * 模拟没有更多的场景
                         */
                        if (multiAdapter.getMergeAdapter().getItemCount() > 120) {
                            closeLoadMoreNoData();
                            return;
                        }
                        for (int i = 0; i < 8; i++) {
                            multiAdapter.getAdapter(1).addNoNotify(new VRBean("更多" + i));
                        }
                        setLoadMoreText("有8条更多");
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                /**
                                 * 体现了MergeAdapter的强大所在，代码解耦合，position操作和单个Adapter一样，
                                 */
                                closeLoadMore(new OnCloseLoadMoreCallback() {
                                    @Override
                                    public void onClosed() {
                                        multiAdapter.getAdapter(1).notifyItemRangeInserted(multiAdapter.getAdapter(1).getItemCount() - 8, 8);

                                    }
                                });
                            }
                        }, 1000);
                    }
                }, 2000);
            }
        });
        final List<String> list_head = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            list_head.add("head" + i);
        }

        final List<VRBean> list_content = new ArrayList<>();
        for (int i = 0; i < 17; i++) {
            list_content.add(new VRBean("内容" + i));
        }

        multiAdapter.getAdapter(0).add(list_head);
        multiAdapter.getAdapter(1).add(list_content);
    }

    @Override
    public void onClick(View v) {

    }
}
