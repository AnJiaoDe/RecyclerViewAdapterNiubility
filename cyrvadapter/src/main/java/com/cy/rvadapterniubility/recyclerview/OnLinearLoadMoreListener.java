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

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cy.BaseAdapter.R;
import com.cy.refreshlayoutniubility.IAnimationView;
import com.cy.rvadapterniubility.adapter.BaseViewHolder;
import com.cy.rvadapterniubility.adapter.MultiAdapter;
import com.cy.rvadapterniubility.adapter.SimpleAdapter;
import com.cy.rvadapterniubility.refreshrv.OnRefreshListener;

/**
 * @Description:仿头条LoadMore丝滑体验
 * @Author: cy
 * @CreateDate: 2020/6/16 15:12
 * @UpdateUser:
 * @UpdateDate: 2020/6/16 15:12
 * @UpdateRemark:
 * @Version:
 */
public abstract class OnLinearLoadMoreListener extends OnSimpleScrollListener {
    private SimpleAdapter<String> loadMoreAdapter;
    private MultiAdapter<SimpleAdapter> multiAdapter;
    private int count_remain = 0;
    private boolean isLoadMoreing = false;
    private OnCloseLoadMoreCallback onCloseLoadMoreCallback;
    private final String CLEAR = "CLEAR";
    private RecyclerView recyclerView;
    private int orientation = RecyclerView.VERTICAL;
    private int space_vertical;
    private int space_horizontal;

    public OnLinearLoadMoreListener(MultiAdapter<SimpleAdapter> multiAdapter) {
        this.multiAdapter = multiAdapter;
        loadMoreAdapter = new SimpleAdapter<String>() {
            @Override
            public void bindDataToView(BaseViewHolder holder, int position, String bean, boolean isSelected) {
                OnLinearLoadMoreListener.this.bindDataToLoadMore(holder, bean);
            }

            @Override
            public int getItemLayoutID(int position, String bean) {
                if (orientation == RecyclerView.VERTICAL)
                    return OnLinearLoadMoreListener.this.getVerticalLoadMoreLayoutID();
                return OnLinearLoadMoreListener.this.getHorizontalLoadMoreLayoutID();
            }

            @Override
            public void onItemClick(BaseViewHolder holder, int position, String bean) {
                onItemLoadMoreClick(holder);
            }
        };
        multiAdapter.addAdapter(multiAdapter.getAdapters().size(), loadMoreAdapter);
    }

    public OnLinearLoadMoreListener(MultiAdapter<SimpleAdapter> multiAdapter, int count_remain) {
        this(multiAdapter);
        this.count_remain = count_remain;
    }
    private void checkRecyclerView(){
        this.recyclerView = recyclerView;
        LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
        orientation = linearLayoutManager.getOrientation();
        space_vertical = 0;
        space_horizontal = 0;
        try {
            LinearItemDecoration linearItemDecoration= (LinearItemDecoration) recyclerView.getItemDecorationAt(0);
            space_vertical =linearItemDecoration.getSpace_vertical();
            space_horizontal =linearItemDecoration.getSpace_horizontal();
        }catch (Exception e){
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

        for (int position : positionHolder.getLastVisibleItemPositions()) {
            RecyclerView.ViewHolder holder = recyclerView.findViewHolderForAdapterPosition(position);
            //说明recyclerView没有剩余空间，需要添加loadMore
            //此处产生BUG，因为clear后，recyclerView.findViewHolderForAdapterPosition(position)导致NULL,所以必须判断NULL

            if (orientation == RecyclerView.VERTICAL) {
                if (holder != null && holder.itemView.getBottom() + 2 * space_vertical>= recyclerView.getHeight()) {
                    if (loadMoreAdapter.getItemCount() == 0) {
                        loadMoreAdapter.add("");
                    }
                    return;
                }
            } else {
                if (holder != null && holder.itemView.getRight()+ 2 * space_horizontal >= recyclerView.getWidth()) {
                    if (loadMoreAdapter.getItemCount() == 0) {
                        loadMoreAdapter.add("");
                    }
                    return;
                }
            }

        }
    }

    @Override
    public void onIdle(RecyclerView recyclerView, PositionHolder positionHolder) {
        super.onIdle(recyclerView, positionHolder);
        for (int position : positionHolder.getLastVisibleItemPositions()) {
            RecyclerView.ViewHolder holder = recyclerView.findViewHolderForAdapterPosition(position);
            if (orientation == RecyclerView.VERTICAL) {
                if (holder != null && holder.itemView.getBottom()+2 * space_vertical < recyclerView.getHeight())
                    continue;
            } else {
                if (holder != null && holder.itemView.getRight()+2 * space_horizontal < recyclerView.getWidth())
                    continue;
            }
            //说明最后一个item-count_remain可见了，可以开始loadMore了
            if (position >= multiAdapter.getMergeAdapter().getItemCount() - 1 - getCount_remain()) {
                if (loadMoreAdapter.getItemCount() == 0) {
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

    public void onItemLoadMoreClick(BaseViewHolder holder) {
    }

    public void bindDataToLoadMore(final BaseViewHolder holder, String bean) {
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
                    final ObjectAnimator objectAnimator_transY = orientation == RecyclerView.VERTICAL ?
                            ObjectAnimator.ofFloat(holder.itemView, "translationY", 0, holder.itemView.getHeight())
                            : ObjectAnimator.ofFloat(holder.itemView, "translationX", 0, holder.itemView.getWidth());
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
                            holder.itemView.setTranslationX(0);
                            holder.itemView.setTranslationY(0);
                            if (onCloseLoadMoreCallback != null) onCloseLoadMoreCallback.onClosed();
                            loadMoreAdapter.clear();
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

//    public void onLoadMoreAdded() {
//    }

    public int getVerticalLoadMoreLayoutID() {
        return R.layout.cy_loadmore_vertical_foot_default;
    }

    public int getHorizontalLoadMoreLayoutID() {
        return R.layout.cy_loadmore_horizontal_foot_default;
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
        if (baseViewHolder == null) return;
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
        String toast = getLoadMoreNoDataToast();
        toast = (toast == null || TextUtils.isEmpty(toast)) ? "没有更多了哦~" : toast;
        if (loadMoreAdapter.getItemCount() != 0) loadMoreAdapter.set(0, toast);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                closeLoadMore(null);
            }
        }, 1000);
    }

    public String getLoadMoreNoDataToast() {
        return "";
    }


    public SimpleAdapter<String> getLoadMoreAdapter() {
        return loadMoreAdapter;
    }

}
