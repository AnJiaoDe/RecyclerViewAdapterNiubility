package com.cy.cyrvadapter.recyclerview;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.cy.BaseAdapter.R;
import com.cy.cyrvadapter.adapter.BaseViewHolder;
import com.cy.cyrvadapter.adapter.MultiAdapter;
import com.cy.cyrvadapter.adapter.SimpleAdapter;
import com.cy.cyrvadapter.refreshlayout.IAnimationView;
import com.cy.cyrvadapter.refreshlayout.LogUtils;

/**
 * @Description:仿头条LoadMore丝滑体验
 * @Author: cy
 * @CreateDate: 2020/6/16 15:12
 * @UpdateUser:
 * @UpdateDate: 2020/6/16 15:12
 * @UpdateRemark:
 * @Version:
 */
public abstract class OnRVLoadMoreListener extends OnVerticalScrollListener {
    private SimpleAdapter<String> loadMoreAdapter;
    private MultiAdapter<SimpleAdapter> multiAdapter;
    private int count_remain = 0;
    private boolean isLoadMoreing = false;
    private OnCloseLoadMoreCallback onCloseLoadMoreCallback;
    private final String CLEAR = "CLEAR";
    private RecyclerView recyclerView;

    public OnRVLoadMoreListener(MultiAdapter<SimpleAdapter> multiAdapter) {
        this.multiAdapter = multiAdapter;
        loadMoreAdapter = new SimpleAdapter<String>() {
            @Override
            public void bindDataToView(BaseViewHolder holder, int position, String bean,boolean isSelected) {
                OnRVLoadMoreListener.this.bindDataToLoadMore(holder, bean);
            }

            @Override
            public int getItemLayoutID(int position, String bean) {
                return OnRVLoadMoreListener.this.getLoadMoreLayoutID();
            }

            @Override
            public void onItemClick(BaseViewHolder holder, int position, String bean) {

            }
        };
        multiAdapter.addAdapter(multiAdapter.getAdapters().size(), loadMoreAdapter);
    }

    public OnRVLoadMoreListener(MultiAdapter<SimpleAdapter> multiAdapter, int count_remain) {
        this(multiAdapter);
        this.count_remain = count_remain;
    }

    /**
     * 在onDragging中添加loadMore布局，是因为如果item很少，recyclerView有很多剩余空间，就要禁用loadMore
     *
     * @param recyclerView
     * @param positionHolder
     */
    @Override
    public final void onDragging(RecyclerView recyclerView, PositionHolder positionHolder) {
        super.onDragging(recyclerView, positionHolder);
        this.recyclerView = recyclerView;
        for (int position : positionHolder.getLastVisibleItemPositions()) {
            RecyclerView.ViewHolder holder = recyclerView.findViewHolderForAdapterPosition(position);
//            GridRecyclerView gridRecyclerView=null;
//            try {
//                gridRecyclerView = (GridRecyclerView) recyclerView;
//            }catch (Exception e){
//                LogUtils.log("Exception",e.getMessage());
//            }
//            int space=gridRecyclerView!=null?gridRecyclerView.getGridItemDecoration().getSpace():0;
            //说明recyclerView没有剩余空间，需要添加loadMore
            //此处产生BUG，因为clear后，recyclerView.findViewHolderForAdapterPosition(position)导致NULL,所以必须判断NULL
            if (holder != null && holder.itemView.getBottom() >= recyclerView.getHeight()) {
                if (loadMoreAdapter.getItemCount() == 0) {
                    loadMoreAdapter.add("");
                    LogUtils.log("onLoadMoreAdded onDragging");
                    onLoadMoreAdded();
                }
                return;
            }
        }
    }

    @Override
    public void onIdle(RecyclerView recyclerView, PositionHolder positionHolder) {
        super.onIdle(recyclerView, positionHolder);
        this.recyclerView = recyclerView;
        for (int position : positionHolder.getLastVisibleItemPositions()) {
            BaseViewHolder baseViewHolder = (BaseViewHolder) recyclerView.findViewHolderForAdapterPosition(position);
//            GridRecyclerView gridRecyclerView=null;
//            try {
//                gridRecyclerView = (GridRecyclerView) recyclerView;
//            }catch (Exception e){
//                LogUtils.log("Exception",e.getMessage());
//            }
//            int space=gridRecyclerView!=null?gridRecyclerView.getGridItemDecoration().getSpace():0;
//            //数据太少，没有充满recyclerView,没有loadMore的必要
//            LogUtils.log("baseViewHolder",baseViewHolder.itemView.getBottom());
//            LogUtils.log("rect.height()",space);
//            LogUtils.log("recyclerView.getHeight()",recyclerView.getHeight());
            if (baseViewHolder.itemView.getBottom() < recyclerView.getHeight()) continue;
            LogUtils.log("baseViewHolder000000");
            //说明最后一个item-count_remain可见了，可以开始loadMore了
            if (position >= multiAdapter.getMergeAdapter().getItemCount() - 1 - getCount_remain()) {
                if (loadMoreAdapter.getItemCount() == 0) {
                    loadMoreAdapter.add("");
                    LogUtils.log("onLoadMoreAdded");
                    onLoadMoreAdded();
                }
                LogUtils.log("isLoadMoreing", isLoadMoreing);
                //防止频繁loadMore
                if (!isLoadMoreing) {
                    isLoadMoreing = true;
                    onLoadMoreStart();
                }
                return;
            }
        }
    }

    public abstract void onLoadMoreStart();

    public void bindDataToLoadMore(final BaseViewHolder holder, String bean) {
        IAnimationView animationView = setAnimationView();
        if (animationView == null) {
            animationView = holder.getView(R.id.animView);
        } else {
            animationView.getView().setId(R.id.animView);
            FrameLayout root = (FrameLayout) holder.itemView;
            root.addView(animationView.getView(), 0, new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
        }
        holder.setVisible(R.id.animView);
        animationView.startLoadAnimation();
        TextView tv = setTextViewToast();
        if (tv == null) {
            tv = holder.getView(R.id.tv);
        } else {
            tv.setId(R.id.tv);
            FrameLayout root = (FrameLayout) holder.itemView;
            root.addView(tv, new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
        }
        if (bean != null && !bean.isEmpty()) {
            switch (bean) {
                case CLEAR:
                    final ObjectAnimator objectAnimator_alpha = ObjectAnimator.ofFloat(holder.itemView, "alpha", 1, 0);
                    final ObjectAnimator objectAnimator_transY = ObjectAnimator.ofFloat(holder.itemView, "translationY", 0, holder.itemView.getHeight());

                    final AnimatorSet animatorSet = new AnimatorSet();
                    animatorSet.setDuration(500);
                    animatorSet.playTogether(objectAnimator_alpha, objectAnimator_transY);
                    animatorSet.setInterpolator(new AccelerateDecelerateInterpolator());
                    animatorSet.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            //holder会被复用，所以动画还原到初始位置
                            holder.itemView.setAlpha(1);
                            holder.itemView.setTranslationY(0);
                            if (onCloseLoadMoreCallback != null) onCloseLoadMoreCallback.onClosed();
                            loadMoreAdapter.clear();
                        }
                    });
                    animatorSet.start();
                    break;
                default:
                    tv.setText(bean);
                    tv.setVisibility(View.VISIBLE);
                    break;
            }
        } else {
            tv.setVisibility(View.GONE);
        }
    }

    public void onLoadMoreAdded() {
    }

    public int getLoadMoreLayoutID() {
        return R.layout.cyrvadapter_loadmore_foot_default;
    }

    /**
     * 剩下多少个item还未显示，便开始loadMore，例如可以设置剩下2个item还未显示便开始loadMore，仿头条LoadMore丝滑体验
     */
    public int getCount_remain() {
        return count_remain;
    }

    public IAnimationView setAnimationView() {
        return null;
    }

    public TextView setTextViewToast() {
        return null;
    }

    public void setLoadMoreText(String text) {
        if (recyclerView == null) return;
        BaseViewHolder baseViewHolder = (BaseViewHolder) recyclerView.findViewHolderForAdapterPosition(multiAdapter.getMergeAdapter().getItemCount() - 1);
        baseViewHolder.setGone(R.id.animView);
        baseViewHolder.setText(R.id.tv, text);
        baseViewHolder.setVisible(R.id.tv);
    }

    /**
     * 必须手动调用closeLoadMore()结束loadMore
     */
    public void closeLoadMore(OnCloseLoadMoreCallback onCloseLoadMoreCallback) {
        isLoadMoreing = false;
        this.onCloseLoadMoreCallback = onCloseLoadMoreCallback;
        if (loadMoreAdapter.getItemCount() != 0) loadMoreAdapter.set(0, CLEAR);
    }

    /**
     *
     */

    public void closeLoadMoreNoData() {
        closeLoadMoreNoData(null);
    }


    public void closeLoadMoreNoData(String toast) {
        toast = (toast == null || TextUtils.isEmpty(toast)) ? "没有更多了哦~" : toast;
        if (loadMoreAdapter.getItemCount() != 0) loadMoreAdapter.set(0, toast);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                closeLoadMore(null);
            }
        }, 1000);
    }

    public SimpleAdapter<String> getLoadMoreAdapter() {
        return loadMoreAdapter;
    }

    public static interface OnCloseLoadMoreCallback {
        public void onClosed();
    }
}