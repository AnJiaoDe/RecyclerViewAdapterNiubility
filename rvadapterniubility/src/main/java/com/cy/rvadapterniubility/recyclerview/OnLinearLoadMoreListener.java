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

/**
 * @Description:仿头条LoadMore丝滑体验
 * @Author: cy
 * @CreateDate: 2020/6/16 15:12
 * @UpdateUser:
 * @UpdateDate: 2020/6/16 15:12
 * @UpdateRemark:
 * @Version:
 */
public abstract class OnLinearLoadMoreListener extends OnLoadMoreListener<String> {
    private SimpleAdapter<String> loadMoreAdapter;
    private MultiAdapter<SimpleAdapter> multiAdapter;
    private int count_remain = 0;
    private boolean isLoadMoreing = false;
    private int orientation = RecyclerView.VERTICAL;
    private int space_vertical;
    private int space_horizontal;
    private RecyclerView recyclerView;
    private OnCloseLoadMoreCallback onCloseLoadMoreCallback;
    private final String CLEAR = "_C_L_E_A_R_";

    public OnLinearLoadMoreListener(MultiAdapter<SimpleAdapter> multiAdapter) {
        this.multiAdapter = multiAdapter;
        loadMoreAdapter = new SimpleAdapter<String>() {
            @Override
            public void bindDataToView(BaseViewHolder holder, int position, String bean) {
                bindDataToLoadMore(holder, bean);
            }

            @Override
            public int getItemLayoutID(int position, String bean) {
                if (orientation == RecyclerView.VERTICAL)
                    return getVerticalLoadMoreLayoutID();
                return getHorizontalLoadMoreLayoutID();
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

    private void checkRecyclerView(RecyclerView recyclerView) {
        this.recyclerView = recyclerView;
        LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
        orientation = linearLayoutManager.getOrientation();
        space_vertical = 0;
        space_horizontal = 0;
        try {
            LinearItemDecoration linearItemDecoration = (LinearItemDecoration) recyclerView.getItemDecorationAt(0);
            space_vertical = linearItemDecoration.getSpace_vertical();
            space_horizontal = linearItemDecoration.getSpace_horizontal();
        } catch (Exception e) {
        }
    }


    /**
     * 在onDragging中添加loadMore布局，是因为如果item很少，recyclerView有很多剩余空间，就要禁用loadMore
     *
     * @param recyclerView
     * @param positionHolder
     */
    @Override
    public void onDragging(RecyclerView recyclerView, PositionHolder positionHolder, int offsetX, int offsetY) {
        super.onDragging(recyclerView, positionHolder, offsetX, offsetY);
        checkRecyclerView(recyclerView);
        for (int position : positionHolder.getLastVisibleItemPositions()) {
            RecyclerView.ViewHolder holder = recyclerView.findViewHolderForAdapterPosition(position);
            //说明recyclerView没有剩余空间，需要添加loadMore
            //此处产生BUG，因为clear后，recyclerView.findViewHolderForAdapterPosition(position)导致NULL,所以必须判断NULL
            if (orientation == RecyclerView.VERTICAL) {
                if (holder != null && holder.itemView.getBottom() + space_vertical >= recyclerView.getHeight()) {
                    if (loadMoreAdapter.getItemCount() == 0) {
                        loadMoreAdapter.add("");
                    }
                    return;
                }
            } else {
                if (holder != null && holder.itemView.getRight() + space_horizontal >= recyclerView.getWidth()) {
                    if (loadMoreAdapter.getItemCount() == 0) {
                        loadMoreAdapter.add("");
                    }
                    return;
                }
            }

        }
    }

    @Override
    public void onShouldResumePicLoad(RecyclerView recyclerView, PositionHolder positionHolder, int velocity_x, int velocity_y, int offsetX, int offsetY) {
        checkRecyclerView(recyclerView);
        for (int position : positionHolder.getLastVisibleItemPositions()) {
            RecyclerView.ViewHolder holder = recyclerView.findViewHolderForAdapterPosition(position);
            if (orientation == RecyclerView.VERTICAL) {
                if (holder != null && holder.itemView.getBottom() + space_vertical < recyclerView.getHeight())
                    continue;
            } else {
                if (holder != null && holder.itemView.getRight() + space_horizontal < recyclerView.getWidth())
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

    @Override
    public void onItemLoadMoreClick(BaseViewHolder holder) {
    }

    @Override
    public void bindDataToLoadMore(final BaseViewHolder holder, String bean) {
        IAnimationView animationView = holder.getView(R.id.animView);
        if (animationView == null) return;
//        if (animationView == null) {
//            animationView = holder.getView(R.id.animView);
//        } else {
//        animationView.getView().setId(R.id.animView);
//        FrameLayout root = (FrameLayout) holder.itemView;
//        root.addView(animationView.getView(), 0, new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
//        }
        animationView.stopLoadAnimation();
        animationView.getView().setVisibility(View.GONE);

        TextView tv = holder.getView(R.id.tv);
//        if (tv == null) {
//            tv = holder.getView(R.id.tv);
//        } else {
//        tv.setId(R.id.tv);
//        FrameLayout root = (FrameLayout) holder.itemView;
//        root.addView(tv, new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
//        }
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
                            if (onCloseLoadMoreCallback != null) onCloseLoadMoreCallback.onClosed();
                            //holder会被复用，所以动画还原到初始位置
                            holder.itemView.setAlpha(1);
                            holder.itemView.setTranslationX(0);
                            holder.itemView.setTranslationY(0);
                            setLoadMoreing(false);
                            loadMoreAdapter.clear();
                        }
                    });
                    animatorSet.start();
                    break;
                default:
                    animationView.stopLoadAnimation();
                    animationView.getView().setVisibility(View.GONE);
                    if(tv!=null){
                        tv.setText(bean);
                        tv.setVisibility(View.VISIBLE);
                    }
                    break;
            }
        } else {
            animationView.getView().setVisibility(View.VISIBLE);
            animationView.startLoadAnimation();
            if(tv!=null)tv.setVisibility(View.GONE);
        }
    }

    /**
     * 剩下多少个item还未显示，便开始loadMore，例如可以设置剩下2个item还未显示便开始loadMore，仿头条LoadMore丝滑体验
     */
    public int getCount_remain() {
        return count_remain;
    }

//    public IAnimationView setAnimationView() {
//        return null;
//    }

//    public TextView setTextViewToast() {
//        return null;
//    }
//
//    private void setLoadMoreText(String text) {
//        RecyclerView recyclerView = getRecyclerView();
//        if (recyclerView == null) return;
//        BaseViewHolder baseViewHolder = (BaseViewHolder) recyclerView.findViewHolderForAdapterPosition(getMultiAdapter().getMergeAdapter().getItemCount() - 1);
//        if (baseViewHolder == null) return;
//        IAnimationView animationView = baseViewHolder.getView(R.id.animView);
//        TextView tv = baseViewHolder.getView(R.id.tv);
//        if(animationView!=null)animationView.getView().setVisibility(View.GONE);
//        if(tv!=null){
//            baseViewHolder.setText(R.id.tv, text);
//            baseViewHolder.setVisible(R.id.tv);
//        }
//    }



    /**
     *
     */

//    public void closeLoadMoreNoData() {
//        String toast = getLoadMoreNoDataToast();
//        toast = (toast == null || TextUtils.isEmpty(toast)) ? "没有更多了哦~" : toast;
//        if (getLoadMoreAdapter().getItemCount() != 0) getLoadMoreAdapter().set(0, toast);
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                closeLoadMore(null);
//            }
//        }, 1000);
//    }

    /**
     * 必须手动调用closeLoadMore()结束loadMore
     */
    @Override
    public void closeLoadMore() {
        if (loadMoreAdapter.getItemCount() != 0) loadMoreAdapter.set(0, CLEAR);
    }

    @Override
    public void closeLoadMoreDelay(String msg, int ms) {
        closeLoadMoreDelay(msg,ms,null);
    }
    public void closeLoadMoreDelay(String msg) {
        closeLoadMoreDelay(msg,1000,null);
    }
    /**
     * 必须手动调用closeLoadMore()结束loadMore
     */
    public void closeLoadMore(OnCloseLoadMoreCallback onCloseLoadMoreCallback) {
        this.onCloseLoadMoreCallback = onCloseLoadMoreCallback;
        closeLoadMore();
    }

    public void closeLoadMoreDelay(String msg, int ms, final OnCloseLoadMoreCallback onCloseLoadMoreCallback) {
        if (loadMoreAdapter.getItemCount() != 0)loadMoreAdapter.set(0, msg);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                closeLoadMore(onCloseLoadMoreCallback);
            }
        }, ms);
    }
    public void closeLoadMoreDelay(String msg, final OnCloseLoadMoreCallback onCloseLoadMoreCallback) {
        closeLoadMoreDelay(msg,1000,onCloseLoadMoreCallback);
    }

    public SimpleAdapter<String> getLoadMoreAdapter() {
        return loadMoreAdapter;
    }

//    public RecyclerView getRecyclerView() {
//        return recyclerView;
//    }

//    public int getOrientation() {
//        return orientation;
//    }

    public boolean isLoadMoreing() {
        return isLoadMoreing;
    }

    public void setLoadMoreing(boolean loadMoreing) {
        isLoadMoreing = loadMoreing;
    }

//    public MultiAdapter<SimpleAdapter> getMultiAdapter() {
//        return multiAdapter;
//    }
}
