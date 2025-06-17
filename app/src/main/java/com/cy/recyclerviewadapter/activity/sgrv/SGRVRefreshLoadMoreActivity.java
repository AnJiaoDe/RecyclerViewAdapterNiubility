package com.cy.recyclerviewadapter.activity.sgrv;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.cy.recyclerviewadapter.BaseActivity;
import com.cy.recyclerviewadapter.GlideUtils;
import com.cy.recyclerviewadapter.LogUtils;
import com.cy.recyclerviewadapter.R;
import com.cy.recyclerviewadapter.bean.BingBean;
import com.cy.recyclerviewadapter.bean.HRVBean;
import com.cy.refreshlayoutniubility.IHeadView;
import com.cy.refreshlayoutniubility.OnSimpleRefreshListener;
import com.cy.refreshlayoutniubility.ScreenUtils;
import com.cy.rvadapterniubility.adapter.BaseViewHolder;
import com.cy.rvadapterniubility.adapter.MultiAdapter;
import com.cy.rvadapterniubility.adapter.SimpleAdapter;
import com.cy.rvadapterniubility.adapter.StaggeredAdapter;
import com.cy.rvadapterniubility.recyclerview.OnGridLoadMoreListener;
import com.cy.rvadapterniubility.recyclerview.OnStaggeredLoadMoreListener;
import com.cy.rvadapterniubility.refreshrv.GridRefreshLayout;
import com.cy.rvadapterniubility.refreshrv.StaggeredRefreshLayout;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class SGRVRefreshLoadMoreActivity extends BaseActivity {
    private StaggeredAdapter<HRVBean> staggeredAdapter;
    private StaggeredRefreshLayout staggeredRefreshLayout;
    private MultiAdapter multiAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sgrvrefresh_load_more);

        staggeredRefreshLayout = (StaggeredRefreshLayout) findViewById(R.id.StaggeredRefreshLayout);
        List<HRVBean> list = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            if (i % 5 == 0) {
                list.add(new HRVBean(R.drawable.pic7));
                continue;
            }
            list.add(new HRVBean(R.drawable.pic1));
        }
        staggeredAdapter = new StaggeredAdapter<HRVBean>() {
            @Override
            public void bindDataToView(final BaseViewHolder holder, int position, final HRVBean bean) {
//                GlideUtils.getRequestManager(SGRVRefreshLoadMoreActivity.this, new GlideUtils.CallbackRequestManager() {
//                    @Override
//                    public void onRequestManagerGeted(RequestManager requestManager) {
//                        requestManager.load(bean.getResID()).into((ImageView) holder.getView(R.id.iv));
//                    }
//                });
                // 在加载图片之前设定好图片的宽高，防止出现item错乱及闪烁
                ImageView iv = holder.getView(R.id.iv);
                ViewGroup.LayoutParams layoutParams = iv.getLayoutParams();
                int width_item = (int) ((ScreenUtils.getScreenWidth(SGRVRefreshLoadMoreActivity.this)
                        - 3 * ScreenUtils.dpAdapt(SGRVRefreshLoadMoreActivity.this, 10)) * 0.5f);
                switch (bean.getResID()) {
                    case R.drawable.pic1:
                        layoutParams.height = (int) (640.f / 771 * width_item);
                        break;
                    case R.drawable.pic3:
                        layoutParams.height = (int) (640.f / 959 * width_item);
                        break;
                    case R.drawable.pic7:
                        layoutParams.height = (int) (919.f / 640 * width_item);
                        break;
                }
                iv.setLayoutParams(layoutParams);

                //防止先展示别的图片再展示自己的，贼丑
                holder.setImageBitmap(R.id.iv, null);
                holder.getView(R.id.iv).setTag(bean.getResID());
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (holder.getView(R.id.iv).getTag().equals(bean.getResID())) {
                          // 使用 Glide 加载图片并应用渐变动画，下拉刷新看起来就没那么闪烁，然而：快速滑动列表，看得眼花头晕，故而不用
//                            Glide.with(SGRVRefreshLoadMoreActivity.this)
//                                    .load(bean.getResID())
//                                    .transition(DrawableTransitionOptions.withCrossFade(1000)) // 设置渐变效果
//                                    .into((ImageView) holder.getView(R.id.iv));
                            holder.setImageResource(R.id.iv,bean.getResID());
                        }
                    }
                }, 100);
            }

            @Nullable
            @Override
            public Object setHolderTagPreBindData(BaseViewHolder holder, int position, HRVBean bean) {
                return null;
            }

            @Override
            public int getItemLayoutID(int position, HRVBean bean) {
                return R.layout.item_sgrv;
            }


            @Override
            public void onItemClick(BaseViewHolder holder, int position, HRVBean bean) {
                showToast("点击" + position);
            }
        };
        multiAdapter = new MultiAdapter().addAdapter(staggeredAdapter.getAdapter());
//        st.getRecyclerView().addItemDecoration(new FullSpanGridItemDecoration(dpAdapt(10)));
        staggeredRefreshLayout.setAdapter(multiAdapter, new OnSimpleRefreshListener() {
            @Override
            public void onRefreshStart(IHeadView headView) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        List<HRVBean> list=new ArrayList<>();
                        for (int i = 0; i < 6; i++) {
                            if (i % 5 == 0) {
                                list.add(new HRVBean(R.drawable.pic7));
                                continue;
                            }
                            list.add(new HRVBean(R.drawable.pic3));
//                            staggeredAdapter.getAdapter().addToTopNoNotify(new HRVBean(R.drawable.pic3));
                        }
                        staggeredAdapter.getAdapter().addToTop(list);
                        staggeredRefreshLayout.getRecyclerView().scrollToPosition(0);
                        staggeredRefreshLayout.closeRefreshDelay("有8条更新", 2000);
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
//                            closeLoadMore(new Callback() {
//                                @Override
//                                public void onClosed() {
//
//                                }
//                            });
                            return;
                        }
                        for (int i = 0; i < 8; i++) {
                            staggeredAdapter.getAdapter().addNoNotify(new HRVBean(R.drawable.pic1));
                        }
                        closeLoadMoreDelay("有8条更多", 1000, new Callback() {
                            @Override
                            public void onClosed() {
                                /**
                                 * 体现了MergeAdapter的强大所在，代码解耦合，position操作和单个Adapter一样，
                                 */
                                staggeredAdapter.getAdapter().notifyItemRangeInserted(multiAdapter.getAdapter(0).getItemCount() - 8, 8);
                            }
                        });
                    }
                }, 2000);
            }
        });
        staggeredAdapter.getAdapter().add(list);
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
