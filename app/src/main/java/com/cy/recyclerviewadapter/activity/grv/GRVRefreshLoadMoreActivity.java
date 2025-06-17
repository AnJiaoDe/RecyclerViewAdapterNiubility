package com.cy.recyclerviewadapter.activity.grv;

import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.View;

import com.cy.recyclerviewadapter.BaseActivity;
import com.cy.recyclerviewadapter.LogUtils;
import com.cy.recyclerviewadapter.R;
import com.cy.recyclerviewadapter.bean.HRVBean;
import com.cy.refreshlayoutniubility.IHeadView;
import com.cy.refreshlayoutniubility.OnSimpleRefreshListener;
import com.cy.rvadapterniubility.adapter.BaseViewHolder;
import com.cy.rvadapterniubility.adapter.MultiAdapter;
import com.cy.rvadapterniubility.adapter.SimpleAdapter;
import com.cy.rvadapterniubility.recyclerview.GridItemDecoration;
import com.cy.rvadapterniubility.recyclerview.OnGridLoadMoreListener;
import com.cy.rvadapterniubility.refreshrv.GridRefreshLayout;

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


        gridRefreshLayout= (GridRefreshLayout) findViewById(R.id.grl);
        List<HRVBean> list = new ArrayList<>();
        for (int i=0;i<20;i++){
            if (i%5==0){
                list.add(new HRVBean(R.drawable.pic3));
                continue;
            }
            if (i%3==0){
                list.add(new HRVBean(R.drawable.pic2));
                continue;
            }
            list.add(new HRVBean(R.drawable.pic1));
        }
        rvAdapter=new SimpleAdapter<HRVBean>() {
            @Override
            public void bindDataToView(BaseViewHolder holder, int position, HRVBean bean) {
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
        gridRefreshLayout.getRecyclerView().addItemDecoration(new GridItemDecoration(dpAdapt(10)));
        gridRefreshLayout.setAdapter(multiAdapter, new OnSimpleRefreshListener() {
            @Override
            public void onRefreshStart(IHeadView headView) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        for (int i = 0; i < 8; i++) {
                            rvAdapter.addToTopNoNotify(new HRVBean(R.drawable.pic3));
                        }
                        rvAdapter.notifyDataSetChanged();
                        gridRefreshLayout.closeRefreshDelay("有8条更新",2000);
                    }
                }, 2000);
            }
        }, new OnGridLoadMoreListener(multiAdapter,10) {
            @Override
            public void onLoadMoreStart() {
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
                           rvAdapter.addNoNotify(new HRVBean(R.drawable.pic1));
                        }
                        closeLoadMoreDelay("有8条更多", 1000, new Callback() {
                            @Override
                            public void onClosed() {
                                LogUtils.log("onClosed");
//                                rvAdapter.notifyDataSetChanged();
                                /**
                                 * 体现了MergeAdapter的强大所在，代码解耦合，position操作和单个Adapter一样，
                                 */
                                rvAdapter.notifyItemRangeInserted(multiAdapter.getAdapter(0).getItemCount() - 8, 8);
                            }
                        });
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
