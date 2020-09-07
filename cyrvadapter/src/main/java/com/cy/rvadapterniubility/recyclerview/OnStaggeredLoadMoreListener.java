//package com.cy.rvadapterniubility.recyclerview;
//
//import android.animation.Animator;
//import android.animation.AnimatorListenerAdapter;
//import android.animation.AnimatorSet;
//import android.animation.ObjectAnimator;
//import android.content.Context;
//import android.os.Handler;
//import android.text.TextUtils;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.view.animation.AccelerateDecelerateInterpolator;
//import android.widget.FrameLayout;
//import android.widget.TextView;
//
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.cy.BaseAdapter.R;
//import com.cy.refreshlayoutniubility.IAnimationView;
//import com.cy.refreshlayoutniubility.ScreenUtils;
//
///**
// * @Description:仿头条LoadMore丝滑体验
// * @Author: cy
// * @CreateDate: 2020/6/16 15:12
// * @UpdateUser:
// * @UpdateDate: 2020/6/16 15:12
// * @UpdateRemark:
// * @Version:
// */
//public abstract class OnStaggeredLoadMoreListener extends OnVerticalScrollListener {
//    private boolean isLoadMoreing = false;
//    private OnCloseLoadMoreCallback onCloseLoadMoreCallback;
//    private final String CLEAR = "CLEAR";
//    private StaggeredRecyclerView staggeredRecyclerView;
//    private int count_remain = 0;
//    private View viewLoadMore;
//    private String bean = "";
//    public OnStaggeredLoadMoreListener() {
//    }
//
//    public OnStaggeredLoadMoreListener(int count_remain) {
//        this.count_remain = count_remain;
//    }
//
//    private void checkRecyclerView(RecyclerView recyclerView) {
////        try {
////            this.staggeredRecyclerView = (StaggeredRecyclerView) recyclerView;
////        } catch (Exception e) {
////            throw new IllegalArgumentException("You must use " + getClass().getName() + " in " + StaggeredRecyclerView.class.getName());
////        }
//    }
//
//    private View getViewLoadMore(Context context) {
//        if (viewLoadMore == null)
//            viewLoadMore = LayoutInflater.from(context).inflate(getLoadMoreLayoutID(), staggeredRecyclerView.getContentView(), false);
//        return viewLoadMore;
//    }
//
//    private void addLoadMoreView(Context context) {
//        removeLoadMoreView(context);
//        staggeredRecyclerView.getContentView().addView(getViewLoadMore(context),
//                new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ScreenUtils.dpAdapt(context,40)));
//        bindDataToLoadMore();
//    }
//
//    private void removeLoadMoreView(Context context) {
//        staggeredRecyclerView.getContentView().removeView(getViewLoadMore(context));
//    }
//
//    /**
//     * 在onDragging中添加loadMore布局，是因为如果item很少，recyclerView有很多剩余空间，就要禁用loadMore
//     *
//     * @param recyclerView
//     * @param positionHolder
//     */
//    @Override
//    public final void onDragging(RecyclerView recyclerView, PositionHolder positionHolder) {
//        super.onDragging(recyclerView, positionHolder);
//        checkRecyclerView(recyclerView);
//        for (ILinearRecyclerView linearRecyclerView : staggeredRecyclerView.getListRV()) {
//            LinearLayoutManager linearLayoutManager = (LinearLayoutManager) linearRecyclerView.getRecyclerView().getLayoutManager();
//            int lastVisibleItemPosition = linearLayoutManager.findLastVisibleItemPosition();
//            RecyclerView.ViewHolder viewHolder = linearRecyclerView.getRecyclerView().findViewHolderForAdapterPosition(lastVisibleItemPosition);
//            //说明recyclerView没有剩余空间，需要添加loadMore
//            //此处产生BUG，因为clear后，recyclerView.findViewHolderForAdapterPosition(position)导致NULL,所以必须判断NULL
//            if (viewHolder != null && viewHolder.itemView.getBottom() >= recyclerView.getHeight()) {
//                addLoadMoreView(recyclerView.getContext());
//                return;
//            }
//        }
//    }
//
//    @Override
//    public void onIdle(RecyclerView recyclerView, PositionHolder positionHolder) {
//        super.onIdle(recyclerView, positionHolder);
//        checkRecyclerView(recyclerView);
//        for (ILinearRecyclerView linearRecyclerView : staggeredRecyclerView.getListRV()) {
//            LinearLayoutManager linearLayoutManager = (LinearLayoutManager) linearRecyclerView.getRecyclerView().getLayoutManager();
//            int lastVisibleItemPosition = linearLayoutManager.findLastVisibleItemPosition();
//            RecyclerView.ViewHolder viewHolder = linearRecyclerView.getRecyclerView().findViewHolderForAdapterPosition(lastVisibleItemPosition);
//            //说明recyclerView没有剩余空间，需要添加loadMore
//            //此处产生BUG，因为clear后，recyclerView.findViewHolderForAdapterPosition(position)导致NULL,所以必须判断NULL
//            if (viewHolder.itemView.getBottom() < recyclerView.getHeight()) continue;
//            //说明最后一个item-count_remain可见了，可以开始loadMore了
//            if (lastVisibleItemPosition >= linearRecyclerView.getRecyclerView().getAdapter().getItemCount() - 1 - getCount_remain()) {
//                addLoadMoreView(recyclerView.getContext());
//                //防止频繁loadMore
//                if (!isLoadMoreing) {
//                    isLoadMoreing = true;
//                    onLoadMoreStart();
//                }
//                return;
//            }
//        }
//    }
//
//    /**
//     * 剩下多少个item还未显示，便开始loadMore，例如可以设置剩下2个item还未显示便开始loadMore，仿头条LoadMore丝滑体验
//     */
//    public int getCount_remain() {
//        return count_remain;
//    }
//
//    public abstract void onLoadMoreStart();
//
//    public void bindDataToLoadMore() {
//        IAnimationView animationView = setAnimationView();
//        if (animationView == null) {
//            animationView = viewLoadMore.findViewById(R.id.animView);
//        } else {
//            animationView.getView().setId(R.id.animView);
//            FrameLayout root = (FrameLayout) viewLoadMore;
//            root.addView(animationView.getView(), 0, new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
//        }
//        animationView.stopLoadAnimation();
//        animationView.getView().setVisibility(View.GONE);
//
//        TextView tv = setTextViewToast();
//        if (tv == null) {
//            tv = viewLoadMore.findViewById(R.id.tv);
//        } else {
//            tv.setId(R.id.tv);
//            FrameLayout root = (FrameLayout) viewLoadMore;
//            root.addView(tv, new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
//        }
//        if (bean != null && !bean.isEmpty()) {
//            switch (bean) {
//                case CLEAR:
//                    final ObjectAnimator objectAnimator_alpha = ObjectAnimator.ofFloat(viewLoadMore, "alpha", 1, 0);
//                    final ObjectAnimator objectAnimator_transY = ObjectAnimator.ofFloat(viewLoadMore, "translationY", 0, viewLoadMore.getHeight());
//                    final AnimatorSet animatorSet = new AnimatorSet();
//                    animatorSet.setDuration(500);
//                    animatorSet.playTogether(objectAnimator_alpha, objectAnimator_transY);
//                    animatorSet.setInterpolator(new AccelerateDecelerateInterpolator());
//                    animatorSet.addListener(new AnimatorListenerAdapter() {
//                        @Override
//                        public void onAnimationEnd(Animator animation) {
//                            super.onAnimationEnd(animation);
//                            //holder会被复用，所以动画还原到初始位置
//                            viewLoadMore.setAlpha(1);
//                            viewLoadMore.setTranslationY(0);
//                            removeLoadMoreView(viewLoadMore.getContext());
//                            bean="";
//                            if (onCloseLoadMoreCallback != null) onCloseLoadMoreCallback.onClosed();
//                        }
//                    });
//                    animatorSet.start();
//                    break;
//                default:
//                    animationView.stopLoadAnimation();
//                    animationView.getView().setVisibility(View.GONE);
//                    tv.setText(bean);
//                    tv.setVisibility(View.VISIBLE);
//                    break;
//            }
//        } else {
//            animationView.getView().setVisibility(View.VISIBLE);
//            animationView.startLoadAnimation();
//            tv.setVisibility(View.GONE);
//        }
//    }
//
//    public int getLoadMoreLayoutID() {
//        return R.layout.cy_loadmore_vertical_foot_default;
//    }
//
//
//    public IAnimationView setAnimationView() {
//        return null;
//    }
//
//    public TextView setTextViewToast() {
//        return null;
//    }
//
//    public void setLoadMoreText(String text) {
//        TextView tv = viewLoadMore.findViewById(R.id.tv);
//        tv.setText(text);
//    }
//    public String getLoadMoreNoDataToast() {
//        return "";
//    }
//
//    /**
//     * 必须手动调用closeLoadMore()结束loadMore
//     */
//    public void closeLoadMore(final OnCloseLoadMoreCallback onCloseLoadMoreCallback) {
//        isLoadMoreing = false;
//        this.onCloseLoadMoreCallback = onCloseLoadMoreCallback;
//
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                removeLoadMoreView(viewLoadMore.getContext());
//                if (onCloseLoadMoreCallback != null) onCloseLoadMoreCallback.onClosed();
//            }
//        }, 1000);
//        bean = CLEAR;
//    }
//
//    public void closeLoadMoreNoData() {
//        String toast = getLoadMoreNoDataToast();
//        toast = (toast == null || TextUtils.isEmpty(toast)) ? "没有更多了哦~" : toast;
//        bean = toast;
//        removeLoadMoreView(viewLoadMore.getContext());
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                closeLoadMore(null);
//            }
//        }, 1000);
//    }
//}
