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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cy.BaseAdapter.R;
import com.cy.refreshlayoutniubility.IAnimationView;
import com.cy.rvadapterniubility.adapter.BaseViewHolder;
import com.cy.rvadapterniubility.adapter.MultiAdapter;
import com.cy.rvadapterniubility.adapter.SimpleAdapter;

import java.io.File;

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
    private Callback callback;
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
                if (holder != null && holder.itemView.getBottom() + space_vertical >= baseRecyclerView.getHeight()) {
                    if (loadMoreAdapter.getItemCount() == 0) {
                        loadMoreAdapter.add("");
                    }
                    return;
                }
            } else {
                if (holder != null && holder.itemView.getRight() + space_horizontal >= baseRecyclerView.getWidth()) {
                    if (loadMoreAdapter.getItemCount() == 0) {
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
//            itemcount为0时，postion为-1
            if (position < 0 ||
                    (baseRecyclerView.getAdapter() != null && position >= baseRecyclerView.getAdapter().getItemCount()))
                continue;
            RecyclerView.ViewHolder holder = baseRecyclerView.findViewHolderForAdapterPosition(position);
            if (orientation == RecyclerView.VERTICAL) {
                if (holder != null && holder.itemView.getBottom() + space_vertical < baseRecyclerView.getHeight())
                    continue;
            } else {
                if (holder != null && holder.itemView.getRight() + space_horizontal < baseRecyclerView.getWidth())
                    continue;
            }
            //说明最后一个item-count_remain可见了，可以开始loadMore了
            if (position >= multiAdapter.getMergeAdapter().getItemCount() - 1 - count_remain) {
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
        animationView.stopLoadAnimation();
        animationView.getView().setVisibility(View.GONE);

        final TextView tv = holder.getView(R.id.tv);
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
                            //瀑布流会出现防止莫名奇妙地再显示一次没有更多字样才消失，原因不明，grid不知道会不会出现
                            if (tv != null) tv.setVisibility(View.GONE);
                            //holder会被复用，所以动画还原到初始位置
                            holder.itemView.setAlpha(1);
                            holder.itemView.setTranslationX(0);
                            holder.itemView.setTranslationY(0);
                            isLoadMoreing = false;
                            //千万不能notifydatasetchanged,否则整个列表都会被刷新，如果是比较耗时的加载图片，会闪烁
                            loadMoreAdapter.getAdapter().clearNoNotify();
                            loadMoreAdapter.getAdapter().notifyItemRemoved(0);

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
        this.callback = callback;
        if (loadMoreAdapter.getItemCount() != 0) loadMoreAdapter.set(0, CLEAR);
    }

    @Override
    public void closeLoadMoreDelay(String msg, int ms, final @NonNull Callback callback) {
        if (loadMoreAdapter.getItemCount() != 0) loadMoreAdapter.set(0, msg);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                closeLoadMore(callback);
            }
        }, ms);
    }

    public SimpleAdapter<String> getLoadMoreAdapter() {
        return loadMoreAdapter;
    }


    public boolean isLoadMoreing() {
        return isLoadMoreing;
    }

//    public void setLoadMoreing(boolean loadMoreing) {
//        isLoadMoreing = loadMoreing;
//    }

}
