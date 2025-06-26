package com.cy.recyclerviewadapter.activity.vr;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;

import com.cy.recyclerviewadapter.BaseActivity;
import com.cy.recyclerviewadapter.LogUtils;
import com.cy.recyclerviewadapter.R;
import com.cy.recyclerviewadapter.bean.VRBean;
import com.cy.refreshlayoutniubility.IAnimationView;
import com.cy.refreshlayoutniubility.IHeadView;
import com.cy.refreshlayoutniubility.OnSimpleRefreshListener;
import com.cy.rvadapterniubility.adapter.BaseViewHolder;
import com.cy.rvadapterniubility.adapter.MultiAdapter;
import com.cy.rvadapterniubility.adapter.SimpleAdapter;
import com.cy.rvadapterniubility.recyclerview.OnLinearLoadMoreListener;
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
        verticalRefreshLayout = (LinearRefreshLayout) findViewById(R.id.vrl);

        multiAdapter = new MultiAdapter<SimpleAdapter>().addAdapter(new SimpleAdapter<String>() {
            @Override
            public void bindDataToView(BaseViewHolder holder, int position, String bean, @NonNull List<Object> payloads) {
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
            public void bindDataToView(BaseViewHolder holder, int position, VRBean bean, @NonNull List<Object> payloads) {
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
//        verticalRefreshLayout.getRecyclerView().setAdapter(multiAdapter.getMergeAdapter());
//        verticalRefreshLayout.getRecyclerView().addItemDecoration(new LinearItemDecoration().setSpace_vertical(60));
        verticalRefreshLayout.setAdapter(multiAdapter, new OnSimpleRefreshListener() {
//            @Override
//            public void onRefreshFinish(IHeadView headView) {
//                super.onRefreshFinish(headView);
//            }
//            @Override
//            public void bindDataToRefreshFinishedLayout(View view, String msg) {
//                LogUtils.log("bindDataToRefreshFinishedLayout",msg);
//                TextView textView=view.findViewById(R.id.tv);
//                textView.setText(msg);
//            }
//            @Override
//            public int getRefreshFinishedLayoutID() {
//                LogUtils.log("getRefreshFinishedLayoutID");
//                return super.getRefreshFinishedLayoutID();
//            }
            @Override
            public void onRefreshStart(IHeadView headView) {
                LogUtils.log("onRefreshStart");
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        for (int i = 0; i < 8; i++) {
                            multiAdapter.getAdapters().get(1).addToTopNoNotify(new VRBean("更新" + i));
                        }
                        multiAdapter.getAdapters().get(1).notifyDataSetChanged();
                        verticalRefreshLayout.closeRefreshDelay("有8条更新",2000);
                    }
                }, 2000);
            }
        }, new OnLinearLoadMoreListener(multiAdapter, 0) {
            @Override
            public void bindDataToLoadMore(BaseViewHolder holder, String bean) {
                IAnimationView animationView=holder.getView(R.id.animView);
                animationView.setColor(Color.RED);
                super.bindDataToLoadMore(holder, bean);
            }

            @Override
            public void onLoadMoreStart() {
                LogUtils.log("onLoadMoreStart");
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        /**
                         * 模拟没有更多的场景
                         */
                        if (multiAdapter.getMergeAdapter().getItemCount() > 120) {
                            closeLoadMoreDelay("没有更多了哦~", 1000, new Callback() {
                                @Override
                                public void onClosed() {

                                }
                            });
                            return;
                        }
                        for (int i = 0; i < 8; i++) {
                            multiAdapter.getAdapter(1).addNoNotify(new VRBean("更多" + i));
                        }
//                        closeLoadMore(new Callback() {
//                            @Override
//                            public void onClosed() {
//                                multiAdapter.getAdapter(1).notifyDataSetChanged();
//                            }
//                        });
                        closeLoadMoreDelay("有8条更多", 1000, new Callback() {
                            @Override
                            public void onClosed() {
                                /**
                                 * 体现了MergeAdapter的强大所在，代码解耦合，position操作和单个Adapter一样，
                                 */
                                multiAdapter.getAdapter(1).notifyItemRangeInserted(multiAdapter.getAdapter(1).getItemCount() - 8, 8);
                            }
                        });
//                        new Handler().postDelayed(new Runnable() {
//                            @Override
//                            public void run() {
//
//                                closeLoadMore(new OnCloseLoadMoreCallback() {
//                                    @Override
//                                    public void onClosed() {
//                                        multiAdapter.getAdapter(1).notifyItemRangeInserted(multiAdapter.getAdapter(1).getItemCount() - 8, 8);
//                                    }
//                                });
//                            }
//                        }, 1000);
                    }
                }, 2000);
            }
        });
        final List<String> list_head = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            list_head.add("head" + i);
        }

        final List<VRBean> list_content = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            list_content.add(new VRBean("内容" + i));
        }

        multiAdapter.getAdapter(0).add(list_head);
        multiAdapter.getAdapter(1).add(list_content);
    }

    @Override
    public void onClick(View v) {

    }
}
