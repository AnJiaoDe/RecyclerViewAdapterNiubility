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

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
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
public abstract class OnGridLoadMoreListener extends OnLoadMoreListener<String> {
    private SimpleAdapter<String> loadMoreAdapter;
    private MultiAdapter<SimpleAdapter> multiAdapter;
    private int count_remain = 0;
    private boolean isLoadMoreing = false;
    private Callback callback;
    private final String CLEAR = "CLEAR";
    private GridRecyclerView gridRecyclerView;
    private int orientation = RecyclerView.VERTICAL;
    private int space;

    public OnGridLoadMoreListener(MultiAdapter<SimpleAdapter> multiAdapter) {
        this.multiAdapter = multiAdapter;
        loadMoreAdapter = new SimpleAdapter<String>() {
            @Override
            public void bindDataToView(BaseViewHolder holder, int position, String bean) {
                OnGridLoadMoreListener.this.bindDataToLoadMore(holder, bean);
            }

            @Override
            public int getItemLayoutID(int position, String bean) {
                if (orientation == RecyclerView.VERTICAL)
                    return OnGridLoadMoreListener.this.getVerticalLoadMoreLayoutID();
                return OnGridLoadMoreListener.this.getHorizontalLoadMoreLayoutID();
            }

            @Override
            public void onItemClick(BaseViewHolder holder, int position, String bean) {
                onItemLoadMoreClick(holder);
            }
        };
        multiAdapter.addAdapter(multiAdapter.getAdapters().size(), loadMoreAdapter);
    }

    public OnGridLoadMoreListener(MultiAdapter<SimpleAdapter> multiAdapter, int count_remain) {
        this(multiAdapter);
        this.count_remain = count_remain;
    }

    @Override
    public void onItemLoadMoreClick(BaseViewHolder holder) {
    }

    private void checkRecyclerView(RecyclerView recyclerView) {
        try {
            this.gridRecyclerView = (GridRecyclerView) recyclerView;
            GridLayoutManager gridLayoutManager = (GridLayoutManager) recyclerView.getLayoutManager();
            orientation = gridLayoutManager.getOrientation();
        } catch (Exception e) {
            throw new IllegalArgumentException(
                    "You can only use " + getClass().getName() + " in " + GridRecyclerView.class.getName() + "or its subclass!");
        }
        space = gridRecyclerView != null ? gridRecyclerView.getGridItemDecoration().getSpace() : 0;
    }


    /**
     * 在onDragging中添加loadMore布局，是因为如果item很少，recyclerView有很多剩余空间，就要禁用loadMore
     *
     * @param baseRecyclerView
     * @param positionHolder
     */
    @Override
    public void onDragging(BaseRecyclerView baseRecyclerView, PositionHolder positionHolder, int velocity_x, int velocity_y, int offsetX, int offsetY) {
        super.onDragging(baseRecyclerView, positionHolder, velocity_x, velocity_y, offsetX, offsetY);
        checkRecyclerView(baseRecyclerView);
        for (int position : positionHolder.getLastVisibleItemPositions()) {
            RecyclerView.ViewHolder holder = baseRecyclerView.findViewHolderForAdapterPosition(position);
            //说明recyclerView没有剩余空间，需要添加loadMore
            //此处产生BUG，因为clear后，recyclerView.findViewHolderForAdapterPosition(position)导致NULL,所以必须判断NULL
            if (orientation == RecyclerView.VERTICAL) {
                if (holder != null && holder.itemView.getBottom() + space >= baseRecyclerView.getHeight()) {
                    if (loadMoreAdapter.getItemCount() == 0) {
                        gridRecyclerView.putFullSpanPosition(multiAdapter.getMergeAdapter().getItemCount());
                        loadMoreAdapter.add("");
                    }
                    return;
                }
            } else {
                if (holder != null && holder.itemView.getRight() + space >= baseRecyclerView.getWidth()) {
                    if (loadMoreAdapter.getItemCount() == 0) {
                        gridRecyclerView.putFullSpanPosition(multiAdapter.getMergeAdapter().getItemCount());
                        loadMoreAdapter.add("");
                    }
                    return;
                }
            }

        }
    }

    @Override
    public void onIdle(BaseRecyclerView baseRecyclerView, PositionHolder positionHolder, int velocity_x, int velocity_y, int offsetX, int offsetY) {
        super.onIdle(baseRecyclerView, positionHolder, velocity_x, velocity_y, offsetX, offsetY);
        checkRecyclerView(baseRecyclerView);
        for (int position : positionHolder.getLastVisibleItemPositions()) {
            RecyclerView.ViewHolder holder = baseRecyclerView.findViewHolderForAdapterPosition(position);
//            //数据太少，没有充满recyclerView,没有loadMore的必要
            if (orientation == RecyclerView.VERTICAL) {
                if (holder != null && holder.itemView.getBottom() + space < baseRecyclerView.getHeight())
                    continue;
            } else {
                if (holder != null && holder.itemView.getRight() + space < baseRecyclerView.getWidth())
                    continue;
            }

            //说明最后一个item-count_remain可见了，可以开始loadMore了
            if (position >= multiAdapter.getMergeAdapter().getItemCount() - 1 - getCount_remain()) {
                if (loadMoreAdapter.getItemCount() == 0) {
                    gridRecyclerView.putFullSpanPosition(multiAdapter.getMergeAdapter().getItemCount());
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
    public void bindDataToLoadMore(final BaseViewHolder holder, String bean) {
        IAnimationView animationView = holder.getView(R.id.animView);
        if (animationView == null) return;
        animationView.stopLoadAnimation();
        animationView.getView().setVisibility(View.GONE);

        TextView tv = holder.getView(R.id.tv);
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
                            isLoadMoreing = false;
                            loadMoreAdapter.clear();

                            if (callback != null) callback.onClosed();
                        }
                    });
                    animatorSet.start();
                    break;
                default:
                    animationView.stopLoadAnimation();
                    animationView.getView().setVisibility(View.GONE);
                    if (tv != null) {
                        tv.setText(bean);
                        tv.setVisibility(View.VISIBLE);
                    }
                    break;
            }
        } else {
            animationView.getView().setVisibility(View.VISIBLE);
            animationView.startLoadAnimation();
            if (tv != null) tv.setVisibility(View.GONE);
        }
    }

    /**
     * 剩下多少个item还未显示，便开始loadMore，例如可以设置剩下2个item还未显示便开始loadMore，仿头条LoadMore丝滑体验
     */
    public int getCount_remain() {
        return count_remain;
    }
    /**
     * 必须要有回调，必须 loadmore完全关闭后才能notify data，否则会导致上一次的loadMore动画没有停止，也没有被remove
     * @param callback
     */
    @Override
    public void closeLoadMore(@NonNull Callback callback) {
        this.callback=callback;
        if (loadMoreAdapter.getItemCount() != 0){
            gridRecyclerView.removeFullSpanPosition(multiAdapter.getMergeAdapter().getItemCount() - 1);
            loadMoreAdapter.set(0, CLEAR);
        }
    }

    @Override
    public void closeLoadMoreDelay(String msg, int ms, @NonNull final Callback callback) {
        if (loadMoreAdapter.getItemCount() != 0) {
            gridRecyclerView.removeFullSpanPosition(multiAdapter.getMergeAdapter().getItemCount() - 1);
            loadMoreAdapter.set(0, msg);
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                closeLoadMore(callback);
            }
        }, ms);
    }

//    public IAnimationView setAnimationView() {
//        return null;
//    }
//
//    public TextView setTextViewToast() {
//        return null;
//    }
//
//    public void setLoadMoreText(String text) {
//        if (gridRecyclerView == null) return;
//        BaseViewHolder baseViewHolder = (BaseViewHolder) gridRecyclerView.findViewHolderForAdapterPosition(multiAdapter.getMergeAdapter().getItemCount() - 1);
//        if(baseViewHolder==null)return;
//        baseViewHolder.setGone(R.id.animView);
//        baseViewHolder.setText(R.id.tv, text);
//        baseViewHolder.setVisible(R.id.tv);
//    }

    /**
     * 必须手动调用closeLoadMore()结束loadMore
     */
//    public void closeLoadMore(OnCloseLoadMoreCallback onCloseLoadMoreCallback) {
//        this.onCloseLoadMoreCallback = onCloseLoadMoreCallback;
//        if (loadMoreAdapter.getItemCount() != 0) {
//            loadMoreAdapter.set(0, CLEAR);
//        }
//    }

    /**
     *
     */

//    public void closeLoadMoreNoData() {
//        closeLoadMoreNoData(null);
//    }
//
//
//    public void closeLoadMoreNoData(String toast) {
//        toast = (toast == null || TextUtils.isEmpty(toast)) ? "没有更多了哦~" : toast;
//        if (loadMoreAdapter.getItemCount() != 0) loadMoreAdapter.set(0, toast);
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                closeLoadMore(null);
//            }
//        }, 1000);
//    }
    public SimpleAdapter<String> getLoadMoreAdapter() {
        return loadMoreAdapter;
    }

}
