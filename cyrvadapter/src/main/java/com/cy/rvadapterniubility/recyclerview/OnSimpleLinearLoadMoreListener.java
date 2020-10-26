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
public abstract class OnSimpleLinearLoadMoreListener extends OnLinearLoadMoreListener {
    private OnCloseLoadMoreCallback onCloseLoadMoreCallback;
    private final String CLEAR = "CLEAR";

    public OnSimpleLinearLoadMoreListener(MultiAdapter<SimpleAdapter> multiAdapter) {
        super(multiAdapter);
    }

    public OnSimpleLinearLoadMoreListener(MultiAdapter<SimpleAdapter> multiAdapter, int count_remain) {
        super(multiAdapter, count_remain);
    }

    @Override
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
                    final ObjectAnimator objectAnimator_transY = getOrientation() == RecyclerView.VERTICAL ?
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
                            setLoadMoreing(false);
                            if (onCloseLoadMoreCallback != null) onCloseLoadMoreCallback.onClosed();
                            closeLoadMore();
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

    public int getVerticalLoadMoreLayoutID() {
        return R.layout.cy_loadmore_vertical_foot_default;
    }

    public int getHorizontalLoadMoreLayoutID() {
        return R.layout.cy_loadmore_horizontal_foot_default;
    }


    public IAnimationView setAnimationView() {
        return null;
    }

    public TextView setTextViewToast() {
        return null;
    }

    public void setLoadMoreText(String text) {
        RecyclerView recyclerView = getRecyclerView();
        if (recyclerView == null) return;
        BaseViewHolder baseViewHolder = (BaseViewHolder) recyclerView.findViewHolderForAdapterPosition(getMultiAdapter().getMergeAdapter().getItemCount() - 1);
        if (baseViewHolder == null) return;
        baseViewHolder.setGone(R.id.animView);
        baseViewHolder.setText(R.id.tv, text);
        baseViewHolder.setVisible(R.id.tv);
    }

    /**
     * 必须手动调用closeLoadMore()结束loadMore
     */
    public void closeLoadMore(OnCloseLoadMoreCallback onCloseLoadMoreCallback) {
        this.onCloseLoadMoreCallback = onCloseLoadMoreCallback;
        if (getLoadMoreAdapter().getItemCount() != 0) getLoadMoreAdapter().set(0, CLEAR);
    }

    /**
     *
     */

    public void closeLoadMoreNoData() {
        String toast = getLoadMoreNoDataToast();
        toast = (toast == null || TextUtils.isEmpty(toast)) ? "没有更多了哦~" : toast;
        if (getLoadMoreAdapter().getItemCount() != 0) getLoadMoreAdapter().set(0, toast);
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


}
