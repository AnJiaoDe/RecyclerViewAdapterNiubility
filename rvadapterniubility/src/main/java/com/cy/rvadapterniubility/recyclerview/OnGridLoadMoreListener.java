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

import com.cy.refreshlayoutniubility.IAnimationView;
import com.cy.rvadapterniubility.R;
import com.cy.rvadapterniubility.adapter.BaseViewHolder;
import com.cy.rvadapterniubility.adapter.MultiAdapter;
import com.cy.rvadapterniubility.adapter.SimpleAdapter;

import java.util.List;

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
    private float space;

    public OnGridLoadMoreListener(MultiAdapter<SimpleAdapter> multiAdapter) {
        this.multiAdapter = multiAdapter;
        loadMoreAdapter = new SimpleAdapter<String>() {
            @Override
            public void bindDataToView(BaseViewHolder holder, int position, String bean, @NonNull List<Object> payloads) {
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
     * 垂直布局时：下拉refresh时必须removeFullPostion，否则布局错乱，GG，故而滑动到顶部时必须removeFullPostion。
     *
     * @param baseRecyclerView
     * @param positionHolder
     * @param offsetX
     * @param offsetY
     */
    @Override
    public void onScrollArrivedTop(BaseRecyclerView baseRecyclerView, PositionHolder positionHolder, int offsetX, int offsetY) {
        super.onScrollArrivedTop(baseRecyclerView, positionHolder, offsetX, offsetY);
        if (loadMoreAdapter.getItemCount() > 0) {
            //loadMoreAdapter.set()不一定会回调bindDataToView，因为loadmore布局还不可见，故而必须手动removeFullSpanPosition
            gridRecyclerView.removeFullSpanPosition(multiAdapter.getMergeAdapter().getItemCount() - 1);
            isLoadMoreing = false;
            loadMoreAdapter.clear();
        }
    }

    /**
     * 水平布局时：
     *
     * @param baseRecyclerView
     * @param positionHolder
     * @param offsetX
     * @param offsetY
     */
    @Override
    public void onScrollArrivedLeft(BaseRecyclerView baseRecyclerView, PositionHolder positionHolder, int offsetX, int offsetY) {
        super.onScrollArrivedLeft(baseRecyclerView, positionHolder, offsetX, offsetY);
        if (loadMoreAdapter.getItemCount() > 0) {
            //loadMoreAdapter.set()不一定会回调bindDataToView，因为loadmore布局还不可见，故而必须手动removeFullSpanPosition
            gridRecyclerView.removeFullSpanPosition(multiAdapter.getMergeAdapter().getItemCount() - 1);
            isLoadMoreing = false;
            loadMoreAdapter.clear();
        }
    }

    /**
     * 要在滑动过程中就add loadmore的布局，否则idle的时候不会显示loadmore布局，贼鸡儿尴尬，注意下拉refresh时必须removeFullPostion，否则GG
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
            //itemcount为0时，postion为-1
            if (position < 0 ||
                    (baseRecyclerView != null && position >= baseRecyclerView.getAdapter().getItemCount()))
                continue;
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
            if (position >= multiAdapter.getMergeAdapter().getItemCount() - 1 - count_remain) {
                if (loadMoreAdapter.getItemCount() == 0) {
                    gridRecyclerView.putFullSpanPosition(multiAdapter.getMergeAdapter().getItemCount());
                    loadMoreAdapter.add("");
                }
                //防止频繁loadMore,而且布应该在onDragging触发onLoadMoreStart
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
                            gridRecyclerView.removeFullSpanPosition(multiAdapter.getMergeAdapter().getItemCount() - 1);
                            //holder会被复用，所以动画还原到初始位置
                            holder.itemView.setAlpha(1);
                            holder.itemView.setTranslationX(0);
                            holder.itemView.setTranslationY(0);
                            isLoadMoreing = false;
                            //千万不能notifydatasetchanged,否则整个列表都会被刷新，如果是比较耗时的加载图片，会闪烁
                            loadMoreAdapter.clearNoNotify();
                            loadMoreAdapter.notifyItemRemoved(0);

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
     *
     * @param callback
     */
    @Override
    public void closeLoadMore(@NonNull Callback callback) {
        this.callback = callback;
        if (loadMoreAdapter.getItemCount() != 0) loadMoreAdapter.set(0, CLEAR);
    }

    @Override
    public void closeLoadMoreDelay(String msg, int ms, @NonNull final Callback callback) {
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

}
