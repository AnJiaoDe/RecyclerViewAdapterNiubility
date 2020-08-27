package com.cy.recyclerviewadapter.activity.grv;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.cy.http.HttpUtils;
import com.cy.http.Imageloader;
import com.cy.http.StringCallbackImpl;
import com.cy.recyclerviewadapter.BaseActivity;
import com.cy.recyclerviewadapter.R;
import com.cy.recyclerviewadapter.bean.S360Bean;
import com.cy.refreshlayoutniubility.LoadMoreFinishListener;
import com.cy.refreshlayoutniubility.OnPullListener;
import com.cy.refreshlayoutniubility.RefreshFinishListener;
import com.cy.rvadapterniubility.LogUtils;
import com.cy.rvadapterniubility.adapter.BaseViewHolder;
import com.cy.rvadapterniubility.adapter.SimpleAdapter;
import com.cy.rvadapterniubility.refreshrv.GridRefreshLayout;
import com.google.gson.Gson;

public class GRVRefreshLoadMoreActivity extends BaseActivity {
    private SimpleAdapter<S360Bean.DataBean> rvAdapter;
    private int page = 0;
    private GridRefreshLayout gridRefreshLayout;
    private final int TYPE_FIRST = 0;
    private final int TYPE_REFRESH = 1;
    private final int TYPE_LOADMORE = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grvrefresh_load_more);


//        TextView textView=null;
//        textView.setText("dg");
        rvAdapter = new SimpleAdapter<S360Bean.DataBean>() {

            @Override
            public void onScrolling() {
                super.onScrolling();
                LogUtils.log("onScrolling");
                Imageloader.getInstance().cancelAllLoad();
            }

            @Override
            public void bindDataToView(BaseViewHolder holder, int position, S360Bean.DataBean bean, boolean isSelected) {
                holder.setImageResource(R.id.iv,R.drawable.default_pic);
                if (isScrolling()) return;
                String urlImage = bean.getUrl();
                Bitmap bitmap = Imageloader.getInstance().getBitmapFromMemoryCache(urlImage);
                if (bitmap != null) {
                    LogUtils.log("bitmap!=null");
//                    computeSize((ImageView) holder.getView(R.id.iv),bitmap);
                    holder.setImageBitmap(R.id.iv, bitmap);
                } else {
                    LogUtils.log("bitmap==null");
//                    holder.setImageResource(R.id.iv, R.drawable.default_pic);
//                    ((ImageView)holder.getView(R.id.iv)).setScaleType(ImageView.ScaleType.FIT_START);
                    Imageloader.getInstance().with(GRVRefreshLoadMoreActivity.this)
                            .url( bean.getUrl())
                            .tag(bean.getUrl())
                            .width(500)
                            .height(500)
                            .into((ImageView) holder.getView(R.id.iv))
                            .load();

//                    Glide.with(GRVRefreshLoadMoreActivity.this).load( bean.getUrl()).placeholder(R.drawable.default_pic)
//                            .into((ImageView) holder.getView(R.id.iv));


                }
            }

            @Override
            public int getItemLayoutID(int position, S360Bean.DataBean bean) {
                return R.layout.item_grv;
            }

            @Override
            public void onItemClick(BaseViewHolder holder, int position,S360Bean.DataBean bean) {

            }
        };

        gridRefreshLayout = ((GridRefreshLayout) findViewById(R.id.grl));
        gridRefreshLayout.setAdapter(rvAdapter,new OnPullListener() {
            @Override
            public void onRefreshStart() {
                call(page+=10, TYPE_REFRESH,10);
            }

            @Override
            public void onLoadMoreStart() {
                call(page+=10, TYPE_LOADMORE,10);
            }
        });

        call(page, TYPE_FIRST,10);
    }

    private void call(int page, final int type,int count) {

        HttpUtils.getInstance().get("http://wallpaper.apc.360.cn/index.php")
                .param("c", "WallPaper")
                .param("a", "getAppsByCategory")
                .param("cid", 12)
                .param("start", page)
                .param("count", count)
                .param("from", "360chrome")
                .enqueue(new StringCallbackImpl() {
                    @Override
                    public void onSuccess(String response) {
//                        LogUtils.log(response);
                        S360Bean s360Bean = null;
                        try {
                            s360Bean = new Gson().fromJson(response, S360Bean.class);
                        } catch (Exception e) {
                            LogUtils.log("Exception", e.getMessage());
                            return;
                        }
                        if(!s360Bean.getErrno().equals("0")){
                            LogUtils.log("Exception", s360Bean.getErrmsg());
                            return;
                        }
                        LogUtils.log("size", s360Bean.getData().size());

                        final S360Bean bean = s360Bean;

                        switch (type) {
                            case TYPE_FIRST:
                                rvAdapter.add(bean.getData());
                                break;
                            case TYPE_REFRESH:
                                gridRefreshLayout.finishRefresh(new RefreshFinishListener() {
                                    @Override
                                    public void onRefreshFinish(final FrameLayout headLayout) {
                                        rvAdapter.addToTop(bean.getData());

                                        final TextView textView = new TextView(headLayout.getContext());
                                        textView.setGravity(Gravity.CENTER);
                                        textView.setBackgroundColor(Color.WHITE);
                                        textView.setText("有" + bean.getData().size() + "条更新");
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
                                break;
                            case TYPE_LOADMORE:
                                gridRefreshLayout.finishLoadMore(new LoadMoreFinishListener() {
                                    @Override
                                    public void onLoadMoreFinish(final FrameLayout footLayout) {
                                        rvAdapter.add(bean.getData());

//                                        gridRefreshLayout.getRecyclerView().smoothScrollBy(0, 100);

                                        final TextView textView = new TextView(footLayout.getContext());
                                        textView.setGravity(Gravity.CENTER);
                                        textView.setBackgroundColor(Color.WHITE);
                                        textView.setTextColor(Color.RED);
                                        textView.setText("有" + bean.getData().size() + "条更新");
                                        footLayout.addView(textView, new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

                                        new Handler().postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                footLayout.removeView(textView);
//                                                ViewGroup.LayoutParams layoutParams = gridRefreshLayout.getFootView().getView().getLayoutParams();
//                                                layoutParams.height = 0;
//                                                gridRefreshLayout.requestLayout();
                                                //关闭loadmore的时候，有动画过程感觉有反弹的效果，不好看，体验差
                                                gridRefreshLayout.closeLoadMore();
                                            }
                                        }, 1000);
                                    }
                                });
                                break;
                        }

                    }

                    @Override
                    public void onLoading(Object readedPart, int percent, long current, long length) {

                    }

                    @Override
                    public void onCancel(Object readedPart, int percent, long current, long length) {

                    }

                    @Override
                    public void onFail(String errorMsg) {

                    }
                });
    }

    @Override
    public void onClick(View v) {

    }
}
