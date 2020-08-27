package com.cy.rvadapterniubility.recyclerview;

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
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.cy.BaseAdapter.R;
import com.cy.refreshlayoutniubility.IAnimationView;
import com.cy.rvadapterniubility.adapter.BaseViewHolder;
import com.cy.rvadapterniubility.adapter.MultiAdapter;
import com.cy.rvadapterniubility.adapter.SimpleAdapter;

/**
 * @Description:仿头条LoadMore丝滑体验
 * @Author: cy
 * @CreateDate: 2020/6/16 15:12
 * @UpdateUser:
 * @UpdateDate: 2020/6/16 15:12
 * @UpdateRemark:
 * @Version:
 */
public abstract class OnGridLoadMoreListener extends OnVerticalScrollListener {
    private SimpleAdapter<String> loadMoreAdapter;
    private MultiAdapter<SimpleAdapter> multiAdapter;
    private int count_remain = 0;
    private boolean isLoadMoreing = false;
    private OnCloseLoadMoreCallback onCloseLoadMoreCallback;
    private final String CLEAR = "CLEAR";
    private IGridRecyclerView gridRecyclerView;

    public OnGridLoadMoreListener(MultiAdapter<SimpleAdapter> multiAdapter) {
        this.multiAdapter = multiAdapter;
        loadMoreAdapter = new SimpleAdapter<String>() {
            @Override
            public void bindDataToView(BaseViewHolder holder, int position, String bean, boolean isSelected) {
                OnGridLoadMoreListener.this.bindDataToLoadMore(holder, bean);
            }

            @Override
            public int getItemLayoutID(int position, String bean) {
                return OnGridLoadMoreListener.this.getLoadMoreLayoutID();
            }

            @Override
            public void onItemClick(BaseViewHolder holder, int position, String bean) {

            }
        };
        multiAdapter.addAdapter(multiAdapter.getAdapters().size(), loadMoreAdapter);
    }

    public OnGridLoadMoreListener(MultiAdapter<SimpleAdapter> multiAdapter, int count_remain) {
        this(multiAdapter);
        this.count_remain = count_remain;
    }

    private void checkRecyclerView(RecyclerView recyclerView) {
        try {
            this.gridRecyclerView = (IGridRecyclerView) recyclerView;
        } catch (Exception e) {
            throw new IllegalArgumentException(
                    "You can only use " + getClass().getName() + " in " + GridRecyclerView.class.getName() + " or " + StaggeredRecyclerView.class.getName());
        }
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
        checkRecyclerView(recyclerView);
        for (int position : positionHolder.getLastVisibleItemPositions()) {
            RecyclerView.ViewHolder holder = recyclerView.findViewHolderForAdapterPosition(position);
            int space = gridRecyclerView != null ? gridRecyclerView.getGridItemDecoration().getSpace() : 0;
            //说明recyclerView没有剩余空间，需要添加loadMore
            //此处产生BUG，因为clear后，recyclerView.findViewHolderForAdapterPosition(position)导致NULL,所以必须判断NULL
            if (holder != null && holder.itemView.getBottom() + 2 * space >= recyclerView.getHeight()) {
                if (loadMoreAdapter.getItemCount() == 0) {
                    gridRecyclerView.addFullSpanPosition(multiAdapter.getMergeAdapter().getItemCount());
                    loadMoreAdapter.add("");
                }
                return;
            }
        }
    }

    @Override
    public void onIdle(RecyclerView recyclerView, PositionHolder positionHolder) {
        super.onIdle(recyclerView, positionHolder);
        checkRecyclerView(recyclerView);
        for (int position : positionHolder.getLastVisibleItemPositions()) {
            RecyclerView.ViewHolder holder = recyclerView.findViewHolderForAdapterPosition(position);
            int space = gridRecyclerView != null ? gridRecyclerView.getGridItemDecoration().getSpace() : 0;
//            //数据太少，没有充满recyclerView,没有loadMore的必要
            if (holder.itemView.getBottom() + 2 * space < recyclerView.getHeight())
                continue;
            //说明最后一个item-count_remain可见了，可以开始loadMore了
            if (position >= multiAdapter.getMergeAdapter().getItemCount() - 1 - getCount_remain()) {
                if (loadMoreAdapter.getItemCount() == 0) {
                    gridRecyclerView.addFullSpanPosition(multiAdapter.getMergeAdapter().getItemCount());
                    loadMoreAdapter.add("");
                }
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

        RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) holder.itemView.getLayoutParams();
        if (layoutParams instanceof StaggeredGridLayoutManager.LayoutParams)
            ((StaggeredGridLayoutManager.LayoutParams) layoutParams).setFullSpan(true);
        layoutParams.rightMargin = gridRecyclerView.getGridItemDecoration().getSpace();
        IAnimationView animationView = setAnimationView();
        if (animationView == null) {
            animationView = holder.getView(R.id.animView);
        } else {
            animationView.getView().setId(R.id.animView);
            FrameLayout root = (FrameLayout) holder.itemView;
            root.addView(animationView.getView(), 0, new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
        }
        animationView.stopLoadAnimation();
        animationView.getView().setVisibility(View.GONE);
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
                            loadMoreAdapter.clear();
                            gridRecyclerView.removeFullSpanPosition(multiAdapter.getMergeAdapter().getItemCount());
                            if (onCloseLoadMoreCallback != null) onCloseLoadMoreCallback.onClosed();
                        }
                    });
                    animatorSet.start();
                    break;
                default:
                    animationView.stopLoadAnimation();
                    animationView.getView().setVisibility(View.GONE);
                    tv.setText(bean);
                    tv.setVisibility(View.VISIBLE);
                    break;
            }
        } else {
            animationView.getView().setVisibility(View.VISIBLE);
            animationView.startLoadAnimation();
            tv.setVisibility(View.GONE);
        }
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
        if (gridRecyclerView == null) return;
        BaseViewHolder baseViewHolder = (BaseViewHolder) gridRecyclerView.getRecyclerView().findViewHolderForAdapterPosition(multiAdapter.getMergeAdapter().getItemCount() - 1);
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
        if (loadMoreAdapter.getItemCount() != 0) {
            loadMoreAdapter.set(0, CLEAR);
        }
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

}
