package com.cy.recyclerviewadapter.activity.sgrv;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.RequestManager;
import com.cy.recyclerviewadapter.BaseActivity;
import com.cy.recyclerviewadapter.GlideUtils;
import com.cy.recyclerviewadapter.LogUtils;
import com.cy.recyclerviewadapter.R;
import com.cy.recyclerviewadapter.bean.BingBean;
import com.cy.recyclerviewadapter.bean.HRVBean;
import com.cy.refreshlayoutniubility.IHeadView;
import com.cy.refreshlayoutniubility.OnSimpleRefreshListener;
import com.cy.rvadapterniubility.adapter.BaseViewHolder;
import com.cy.rvadapterniubility.adapter.MultiAdapter;
import com.cy.rvadapterniubility.adapter.SimpleAdapter;
import com.cy.rvadapterniubility.recyclerview.OnGridLoadMoreListener;
import com.cy.rvadapterniubility.recyclerview.OnStaggeredLoadMoreListener;
import com.cy.rvadapterniubility.refreshrv.GridRefreshLayout;
import com.cy.rvadapterniubility.refreshrv.StaggeredRefreshLayout;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class SGRVRefreshLoadMoreActivity extends BaseActivity {
    private SimpleAdapter<HRVBean> rvAdapter;
    private StaggeredRefreshLayout staggeredRefreshLayout;
    private MultiAdapter multiAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sgrvrefresh_load_more);

        staggeredRefreshLayout=findViewById(R.id.StaggeredRefreshLayout);
        List<HRVBean> list = new ArrayList<>();
        for (int i=0;i<100;i++){
            if (i%5==0){
                list.add(new HRVBean(R.drawable.pic7));
                continue;

            }
            list.add(new HRVBean(R.drawable.pic1));
        }
        rvAdapter=new SimpleAdapter<HRVBean>() {
            @Override
            public void bindDataToView(final BaseViewHolder holder, int position, final HRVBean bean) {
//                GlideUtils.getRequestManager(SGRVRefreshLoadMoreActivity.this, new GlideUtils.CallbackRequestManager() {
//                    @Override
//                    public void onRequestManagerGeted(RequestManager requestManager) {
//                        requestManager.load(bean.getResID()).into((ImageView) holder.getView(R.id.iv));
//                    }
//                });
                holder.setImageResource(R.id.iv,bean.getResID());
            }

            @Override
            public int getItemLayoutID(int position, HRVBean bean) {
                return R.layout.item_sgrv;
            }


            @Override
            public void onItemClick(BaseViewHolder holder,int position, HRVBean bean) {
                showToast("点击" + position);
            }
        };
        multiAdapter=new MultiAdapter().addAdapter(rvAdapter);
//        st.getRecyclerView().addItemDecoration(new FullSpanGridItemDecoration(dpAdapt(10)));
        staggeredRefreshLayout.setAdapter(multiAdapter, new OnSimpleRefreshListener() {
            @Override
            public void onRefreshStart(IHeadView headView) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        for (int i = 0; i < 8; i++) {
                            rvAdapter.addToTopNoNotify(new HRVBean(R.drawable.pic3));
                        }
                        rvAdapter.notifyDataSetChanged();
                        staggeredRefreshLayout.closeRefreshDelay("有8条更新",2000);
                    }
                }, 2000);
            }
        }, new OnStaggeredLoadMoreListener(multiAdapter) {
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
                                /**
                                 * 体现了MergeAdapter的强大所在，代码解耦合，position操作和单个Adapter一样，
                                 */
                                rvAdapter.notifyItemRangeInserted(multiAdapter.getAdapter(1).getItemCount() - 8, 8);
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
