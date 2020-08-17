package com.cy.recyclerviewadapter.activity.vr;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import com.cy.cyrvadapter.adapter.BaseViewHolder;
import com.cy.cyrvadapter.adapter.MultiAdapter;
import com.cy.cyrvadapter.adapter.SimpleAdapter;
import com.cy.cyrvadapter.recyclerview.OnRVLoadMoreListener;
import com.cy.cyrvadapter.refreshrv.OnRefreshListener;
import com.cy.cyrvadapter.refreshrv.VerticalRefreshLayout;
import com.cy.recyclerviewadapter.BaseActivity;
import com.cy.recyclerviewadapter.R;
import com.cy.recyclerviewadapter.bean.VRBean;

import java.util.ArrayList;
import java.util.List;

public class VRRefreshLoadMoreSelfActivity extends BaseActivity {

    private MultiAdapter<SimpleAdapter> multiAdapter;
    private VerticalRefreshLayout verticalRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vrrefresh);
        verticalRefreshLayout = findViewById(R.id.vrl);

        multiAdapter = new MultiAdapter<SimpleAdapter>().addAdapter(new SimpleAdapter<String>() {
            @Override
            public void bindDataToView(BaseViewHolder holder, int position, String bean,boolean isSelected) {
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
        });

//        verticalRefreshLayout.setEnableLoadMore(false);
//        verticalRefreshLayout.getRecyclerView().setAdapter(multiAdapter.getMergeAdapter());
        verticalRefreshLayout.setAdapter(multiAdapter, new OnRefreshListener() {
            @Override
            public void onRefreshStart() {

            }
        }, new OnRVLoadMoreListener(multiAdapter, 6) {
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
//                                verticalRefreshLayout.closeLoadMore();
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
