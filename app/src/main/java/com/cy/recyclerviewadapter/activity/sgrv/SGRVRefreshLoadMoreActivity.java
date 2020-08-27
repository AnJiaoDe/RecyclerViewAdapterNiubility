package com.cy.recyclerviewadapter.activity.sgrv;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import com.cy.http.BitmapCallbackImpl;
import com.cy.http.HttpUtils;
import com.cy.http.Imageloader;
import com.cy.http.StringCallbackImpl;
import com.cy.recyclerviewadapter.BaseActivity;
import com.cy.recyclerviewadapter.R;
import com.cy.recyclerviewadapter.bean.BingBean;
import com.cy.rvadapterniubility.LogUtils;
import com.cy.rvadapterniubility.adapter.BaseViewHolder;
import com.cy.rvadapterniubility.adapter.SimpleAdapter;
import com.cy.rvadapterniubility.refreshrv.StaggeredRefreshLayout;
import com.google.gson.Gson;

public class SGRVRefreshLoadMoreActivity extends BaseActivity {
    private SimpleAdapter<BingBean.ImagesBean> rvAdapter;

    //    private Map<String, Imageloader> map_imageLoader = new HashMap<>();
    private void computeSize(ImageView iv, Bitmap bitmap) {
        ViewGroup.LayoutParams layoutParams = iv.getLayoutParams();
        switch (layoutParams.width) {
            case ViewGroup.LayoutParams.MATCH_PARENT:
                layoutParams.height = (int) (iv.getMeasuredWidth() * bitmap.getHeight() * 1f / bitmap.getWidth());
                break;
            case ViewGroup.LayoutParams.WRAP_CONTENT:
                switch (layoutParams.height) {
                    case ViewGroup.LayoutParams.MATCH_PARENT:
                        layoutParams.width = (int) (iv.getMeasuredHeight() * bitmap.getWidth() * 1f / bitmap.getHeight());
                        break;
                    case ViewGroup.LayoutParams.WRAP_CONTENT:
                        break;
                    default:
                        layoutParams.width = (int) (layoutParams.height * bitmap.getWidth() * 1f / bitmap.getHeight());
                        break;
                }
                break;
            default:
                layoutParams.height = (int) (layoutParams.width * bitmap.getHeight() * 1f / bitmap.getWidth());
                break;
        }
//        iv.requestLayout();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sgrvrefresh_load_more);
        final StaggeredRefreshLayout staggeredGridRefreshLayout = ((StaggeredRefreshLayout) findViewById(R.id.sgrl));

        rvAdapter = new SimpleAdapter<BingBean.ImagesBean>() {
            @Override
            public void bindDataToView(BaseViewHolder holder, int position, BingBean.ImagesBean bean, boolean isSelected) {
                LogUtils.log("position", position);
                String urlImage = "http://cn.bing.com" + bean.getUrl();
                Bitmap bitmap = Imageloader.getInstance().getBitmapFromMemoryCache(urlImage);
                bitmap = null;
                if (bitmap != null) {
                    LogUtils.log("bitmap != null");
//                    computeSize((ImageView) holder.getView(R.id.iv),bitmap);
                    holder.setImageBitmap(R.id.iv, bitmap);
                } else {
                    LogUtils.log("bitmap == null");
//                    holder.setImageResource(R.id.iv, R.drawable.default_pic);
                    if (isScrolling()) return;
//                    ((ImageView)holder.getView(R.id.iv)).setScaleType(ImageView.ScaleType.FIT_START);
                    Imageloader.getInstance().with(SGRVRefreshLoadMoreActivity.this)
                            .url("http://cn.bing.com" + bean.getUrl())
                            .tag("http://cn.bing.com" + bean.getUrl())
                            .width(500)
                            .height(500)
                            .into((ImageView) holder.getView(R.id.iv))
//                            .load();
                            .load(new BitmapCallbackImpl(SGRVRefreshLoadMoreActivity.this) {
                                @Override
                                public void onSuccess(Bitmap response) {
                                    ImageView imageView =getImageParams().getImageView();
                                    RecyclerView.LayoutParams layoutParams= (RecyclerView.LayoutParams) imageView.getLayoutParams();
                                    int width=staggeredGridRefreshLayout.getRecyclerView().getWidth()-
                                           3*layoutParams.leftMargin;
                                    layoutParams.width=width/2;
                                    layoutParams.height=(int) (width/2*response.getHeight() * 1F / response.getWidth());
                                    LogUtils.log("width", width);

                                    imageView.setLayoutParams(layoutParams);

                                    imageView.setImageBitmap(response);


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

//                    Glide.with(SGRVRefreshLoadMoreActivity.this).load("http://cn.bing.com" + bean.getUrl()).into((ImageView) holder.getView(R.id.iv));
                    /**
                     * W/RecyclerView: Cannot call this method in a scroll callback. Scroll callbacks mightbe run during a measure & layout pass where you cannot change theRecyclerView data. Any method call that might change the structureof the RecyclerView or the adapter contents should be postponed tothe next frame.
                     *     java.lang.IllegalStateException:
                     */
//                            rvAdapter.notifyItemChanged(ii);

//                                    BaseViewHolder holder = (BaseViewHolder) staggeredGridRefreshLayout.getRecyclerView().findViewHolderForAdapterPosition(ii);
                    //有些图片加载出来后，holder空间被撑大，有些holder不可见了，所以需要判断null
//                                    holder.setImageBitmap(R.id.iv, response);
                }
            }

            @Override
            public int getItemLayoutID(int position, BingBean.ImagesBean bean) {
                return R.layout.item_sgrv;
            }


            @Override
            public void onItemClick(BaseViewHolder holder, int position, BingBean.ImagesBean bean) {

            }
        };
        LogUtils.log("rvAdapter____", rvAdapter.hashCode());

        staggeredGridRefreshLayout.setAdapter(rvAdapter);
//        staggeredGridRefreshLayout.setOnPullListener(new OnPullListener() {
//            @Override
//            public void onRefreshStart() {
//                new Handler().postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        staggeredGridRefreshLayout.finishRefresh(new RefreshFinishListener() {
//                            @Override
//                            public void onRefreshFinish(final FrameLayout headLayout) {
//                                for (int i = 0; i < 8; i++) {
//                                    rvAdapter.addToHeadNoNotify(new HRVBean(R.drawable.pic3));
//                                }
//                                rvAdapter.notifyDataSetChanged();
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
//                                        staggeredGridRefreshLayout.closeRefresh();
//                                    }
//                                }, 2000);
//                            }
//                        });
//                    }
//                }, 3000);
//
//            }
//
//            @Override
//            public void onLoadMoreStart() {
//                new Handler().postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        staggeredGridRefreshLayout.finishLoadMore(new LoadMoreFinishListener() {
//                            @Override
//                            public void onLoadMoreFinish(FrameLayout footLayout) {
//                                staggeredGridRefreshLayout.finishLoadMore(new LoadMoreFinishListener() {
//                                    @Override
//                                    public void onLoadMoreFinish(final FrameLayout footLayout) {
//
//                                        for (int i = 0; i < 8; i++) {
//                                            rvAdapter.add(new HRVBean(R.drawable.pic3));
//                                        }
//                                        rvAdapter.notifyDataSetChanged();
//
//                                        staggeredGridRefreshLayout.getRecyclerView().smoothScrollBy(0,100);
//
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
//                                                staggeredGridRefreshLayout.closeLoadMore();
//                                            }
//                                        }, 2000);
//                                    }
//
//                                });
//                            }
//                        });
//                    }
//                }, 3000);
//
//            }
//        });

        HttpUtils.getInstance().get("http://cn.bing.com/HPImageArchive.aspx")
                .param("format", "js")
                .param("ids", 0)
                .param("n", 20)
                .enqueue(new StringCallbackImpl() {
                    @Override
                    public void onSuccess(String response) {

//                        LogUtils.log(response);
                        BingBean bingBean = null;
                        try {
                            bingBean = new Gson().fromJson(response, BingBean.class);
                        } catch (Exception e) {
                            LogUtils.log("Exception", e.getMessage());
                        }
                        rvAdapter.add(bingBean.getImages());
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

//    private int getTargetHeight(ImageView view) {
//        int verticalPadding = view.getPaddingTop() + view.getPaddingBottom();
//        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
//        int layoutParamSize = layoutParams != null ? layoutParams.height : 0;
//        return getTargetDimen(view,view.getHeight(), layoutParamSize, verticalPadding);
//    }
//
//    private int getTargetWidth(ImageView view) {
//        int horizontalPadding = view.getPaddingLeft() + view.getPaddingRight();
//        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
//        int layoutParamSize = layoutParams != null ? layoutParams.width : 0;
//        return getTargetDimen(view,view.getWidth(), layoutParamSize, horizontalPadding);
//    }
//
//    private int getTargetDimen(ImageView view,int viewSize, int paramSize, int paddingSize) {
//        // We consider the View state as valid if the View has non-null layout params and a non-zero
//        // layout params width and height. This is imperfect. We're making an assumption that View
//        // parents will obey their child's layout parameters, which isn't always the case.
//        int adjustedParamSize = paramSize - paddingSize;
//        if (adjustedParamSize > 0) {
//            return adjustedParamSize;
//        }
//
//        // Since we always prefer layout parameters with fixed sizes, even if waitForLayout is true,
//        // we might as well ignore it and just return the layout parameters above if we have them.
//        // Otherwise we should wait for a layout pass before checking the View's dimensions.
//        if ( view.isLayoutRequested()) {
//            return 0;
//        }
//
//        // We also consider the View state valid if the View has a non-zero width and height. This
//        // means that the View has gone through at least one layout pass. It does not mean the Views
//        // width and height are from the current layout pass. For example, if a View is re-used in
//        // RecyclerView or ListView, this width/height may be from an old position. In some cases
//        // the dimensions of the View at the old position may be different than the dimensions of the
//        // View in the new position because the LayoutManager/ViewParent can arbitrarily decide to
//        // change them. Nevertheless, in most cases this should be a reasonable choice.
//        int adjustedViewSize = viewSize - paddingSize;
//        if (adjustedViewSize > 0) {
//            return adjustedViewSize;
//        }
//
//        // Finally we consider the view valid if the layout parameter size is set to wrap_content.
//        // It's difficult for Glide to figure out what to do here. Although Target.SIZE_ORIGINAL is a
//        // coherent choice, it's extremely dangerous because original images may be much too large to
//        // fit in memory or so large that only a couple can fit in memory, causing OOMs. If users want
//        // the original image, they can always use .override(Target.SIZE_ORIGINAL). Since wrap_content
//        // may never resolve to a real size unless we load something, we aim for a square whose length
//        // is the largest screen size. That way we're loading something and that something has some
//        // hope of being downsampled to a size that the device can support. We also log a warning that
//        // tries to explain what Glide is doing and why some alternatives are preferable.
//        // Since WRAP_CONTENT is sometimes used as a default layout parameter, we always wait for
//        // layout to complete before using this fallback parameter (ConstraintLayout among others).
//        if (!view.isLayoutRequested() && paramSize == ViewGroup.LayoutParams.WRAP_CONTENT) {
////            if (Log.isLoggable(TAG, Log.INFO)) {
////                Log.i(
////                        TAG,
////                        "Glide treats LayoutParams.WRAP_CONTENT as a request for an image the size of this"
////                                + " device's screen dimensions. If you want to load the original image and are"
////                                + " ok with the corresponding memory cost and OOMs (depending on the input size),"
////                                + " use override(Target.SIZE_ORIGINAL). Otherwise, use LayoutParams.MATCH_PARENT,"
////                                + " set layout_width and layout_height to fixed dimension, or use .override()"
////                                + " with fixed dimensions.");
////            }
//            return getMaxDisplayLength(view.getContext());
//        }
//        return 0;
//    }

//    private int getMaxDisplayLength(@NonNull Context context) {
//        int maxDisplayLength;
//        WindowManager windowManager =
//                (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
//        Display display = Preconditions.checkNotNull(windowManager).getDefaultDisplay();
//        Point displayDimensions = new Point();
//        display.getSize(displayDimensions);
//        maxDisplayLength = Math.max(displayDimensions.x, displayDimensions.y);
//
//        return maxDisplayLength;
//    }

    @Override
    public void onClick(View v) {

    }
}
