package com.cy.recyclerviewadapter.activity.grv;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.cy.recyclerviewadapter.BaseActivity;
import com.cy.recyclerviewadapter.R;
import com.cy.recyclerviewadapter.bean.HRVBean;
import com.cy.recyclerviewadapter.bean.S360Bean;
import com.cy.recyclerviewadapter.bean.VRBean;
import com.cy.refreshlayoutniubility.LoadMoreFinishListener;
import com.cy.refreshlayoutniubility.OnPullListener;
import com.cy.refreshlayoutniubility.RefreshFinishListener;
import com.cy.rvadapterniubility.LogUtils;
import com.cy.rvadapterniubility.adapter.BaseViewHolder;
import com.cy.rvadapterniubility.adapter.MultiAdapter;
import com.cy.rvadapterniubility.adapter.SimpleAdapter;
import com.cy.rvadapterniubility.recyclerview.FullSpanGridItemDecoration;
import com.cy.rvadapterniubility.recyclerview.GridItemDecoration;
import com.cy.rvadapterniubility.recyclerview.GridRecyclerView;
import com.cy.rvadapterniubility.recyclerview.OnCloseLoadMoreCallback;
import com.cy.rvadapterniubility.recyclerview.OnGridLoadMoreListener;
import com.cy.rvadapterniubility.refreshrv.GridRefreshLayout;
import com.cy.rvadapterniubility.refreshrv.OnRefreshListener;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class GRVRefreshLoadMoreActivity extends BaseActivity {
    private SimpleAdapter<HRVBean> rvAdapter;
    private GridRefreshLayout gridRefreshLayout;
    private MultiAdapter multiAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grvrefresh_load_more);


        gridRefreshLayout=findViewById(R.id.grl);
        List<HRVBean> list = new ArrayList<>();
        for (int i=0;i<100;i++){
            if (i%5==0){
                list.add(new HRVBean(R.drawable.pic3));
                continue;

            }
            list.add(new HRVBean(R.drawable.pic1));
        }
        rvAdapter=new SimpleAdapter<HRVBean>() {
            @Override
            public void bindDataToView(BaseViewHolder holder, int position, HRVBean bean, boolean isSelected) {
                holder.setImageResource(R.id.iv,bean.getResID());
            }

            @Override
            public int getItemLayoutID(int position, HRVBean bean) {
                return R.layout.item_grv;
            }


            @Override
            public void onItemClick(BaseViewHolder holder,int position, HRVBean bean) {
                showToast("点击" + position);
            }
        };
        multiAdapter=new MultiAdapter().addAdapter(rvAdapter);
        gridRefreshLayout.getRecyclerView().addItemDecoration(new FullSpanGridItemDecoration(dpAdapt(10)));
        gridRefreshLayout.setAdapter(multiAdapter, new OnRefreshListener() {
            @Override
            public void onRefreshStart() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        gridRefreshLayout.finishRefresh(new RefreshFinishListener() {
                            @Override
                            public void onRefreshFinish(final FrameLayout headLayout) {
                                for (int i = 0; i < 8; i++) {
                                    rvAdapter.addToTopNoNotify(new HRVBean(R.drawable.pic3));
                                }
                                rvAdapter.notifyDataSetChanged();

                                final TextView textView = new TextView(headLayout.getContext());
                                textView.setGravity(Gravity.CENTER);
                                textView.setBackgroundColor(Color.WHITE);
                                textView.setText("有8条更新");
                                headLayout.addView(textView, new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        headLayout.removeView(textView);
                                        gridRefreshLayout.closeRefresh();
                                    }
                                }, 1000);
                            }
                        });
                    }
                }, 2000);
            }
        }, new OnGridLoadMoreListener(multiAdapter) {
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
                           rvAdapter.addNoNotify(new HRVBean(R.drawable.pic1));
                        }
//                        rvAdapter.notifyDataSetChanged();
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
                                        rvAdapter.notifyItemRangeInserted(multiAdapter.getAdapter(1).getItemCount() - 8, 8);
                                    }
                                });
                            }
                        }, 1000);
                    }
                }, 2000);
            }
        });
        rvAdapter.add(list);
    }

    @Override
    public void onClick(View v) {

    }
    /**
     * --------------------------------------------------------------------------------
     */
    public int dpAdapt(float dp) {
        return dpAdapt(dp, 360);
    }

    public int dpAdapt(float dp, float widthDpBase) {
        DisplayMetrics dm = getResources().getDisplayMetrics();
        int heightPixels = dm.heightPixels;//高的像素
        int widthPixels = dm.widthPixels;//宽的像素
        float density = dm.density;//density=dpi/160,密度比
        float heightDP = heightPixels / density;//高度的dp
        float widthDP = widthPixels / density;//宽度的dp
        float w = widthDP > heightDP ? heightDP : widthDP;
        return (int) (dp * w / widthDpBase * density + 0.5f);
    }
}
