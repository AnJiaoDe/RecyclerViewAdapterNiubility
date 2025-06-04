package com.cy.rvadapterniubility.recyclerview;


import android.view.ViewConfiguration;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;


/**
 * Created by lenovo on 2017/12/31.
 */

public  class OnSimpleScrollListener {
    protected boolean firstCallOnScrolled = true;

    protected static enum LAYOUT_MANAGER_TYPE {
        LINEAR,
        GRID,
        STAGGERED_GRID
    }

    /**
     * layoutManager的类型（枚举）
     */
    protected LAYOUT_MANAGER_TYPE layoutManagerType;


    protected RecyclerView.OnScrollListener onScrollListener;
    private PositionHolder positionHolder;
    private PositionHolder positionHolder_last;
    private int orientation = RecyclerView.VERTICAL;

    public OnSimpleScrollListener() {
        positionHolder = new PositionHolder(new int[1], new int[1], new int[1], new int[1]);
        onScrollListener = new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                BaseRecyclerView baseRecyclerView = checkRecyclerView(recyclerView);

                if (firstCallOnScrolled) {
                    positionHolder = computPosition(recyclerView);
                    onFirstScrolled(baseRecyclerView, positionHolder, baseRecyclerView.getOffsetX(), baseRecyclerView.getOffsetY());
                    firstCallOnScrolled = false;

                    int[] firstVisibleItemPositions = positionHolder.getFirstVisibleItemPositions();
                    int[] lastVisibleItemPositions = positionHolder.getLastVisibleItemPositions();
                    if (firstVisibleItemPositions[0] >= 0 && lastVisibleItemPositions[0] >= 0)
                        for (int p = positionHolder.getFirstVisibleItemPositions()[0]; p <= lastVisibleItemPositions[lastVisibleItemPositions.length - 1]; p++) {
                            onItemShow(baseRecyclerView, p, positionHolder);
                        }
//                    positionHolder_last = new PositionHolder(new int[1], new int[1], new int[1], new int[1]);
//                    positionHolder_last.setFirstCompletelyVisibleItemPositions(positionHolder.getFirstCompletelyVisibleItemPositions());
//                    positionHolder_last.setFirstVisibleItemPositions(positionHolder.getFirstVisibleItemPositions());
//                    positionHolder_last.setLastCompletelyVisibleItemPositions(positionHolder.getLastCompletelyVisibleItemPositions());
//                    positionHolder_last.setLastVisibleItemPositions(positionHolder.getLastVisibleItemPositions());
                    positionHolder_last = positionHolder;
                }

                if (dy < 0) { // 当前处于上滑状态
                    onScrollingFingerToBottom(baseRecyclerView, positionHolder, dy, baseRecyclerView.getOffsetX(), baseRecyclerView.getOffsetY());
                    return;
                }
                if (dy > 0) { // 当前处于下滑状态
                    onScrollingFingerToTop(baseRecyclerView, positionHolder, dy, baseRecyclerView.getOffsetX(), baseRecyclerView.getOffsetY());
                    return;
                }
                if (dx < 0) { // 当前处于上滑状态
                    onScrollingFingerToRight(baseRecyclerView, positionHolder, dy, baseRecyclerView.getOffsetX(), baseRecyclerView.getOffsetY());
                    return;
                }
                if (dx > 0) { // 当前处于下滑状态
                    onScrollingFingerToLeft(baseRecyclerView, positionHolder, dy, baseRecyclerView.getOffsetX(), baseRecyclerView.getOffsetY());
                    return;
                }
            }

            /**
             * 手指滑动屏幕，会产生下面2种调用，哪怕recyclerView本身已经不能滑动
             * 手指离开屏幕，有速度，快速滑动时，drag->setting->idle或者手指离开屏幕，没速度，drag->idle
             * @param recyclerView
             * @param newState
             */
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                positionHolder = computPosition(recyclerView);
                BaseRecyclerView baseRecyclerView = checkRecyclerView(recyclerView);

                if (positionHolder_last != null) {
                    int[] firstVisibleItemPositions_last = positionHolder_last.getFirstVisibleItemPositions();
                    int[] firstVisibleItemPositions = positionHolder.getFirstVisibleItemPositions();

                    int[] lastVisibleItemPositions_last = positionHolder_last.getLastVisibleItemPositions();
                    int[] lastVisibleItemPositions = positionHolder.getLastVisibleItemPositions();

//                LogUtils.log("firstVisibleItemPositions_last", firstVisibleItemPositions_last[0]);
//                LogUtils.log("firstVisibleItemPositions", firstVisibleItemPositions[0]);
//                LogUtils.log("lastVisibleItemPositions_last", lastVisibleItemPositions_last[0]);
//                LogUtils.log("lastVisibleItemPositions", lastVisibleItemPositions[0]);

                    //onScrollingFingerToBottom
                    if (firstVisibleItemPositions[0] >= 0 && (firstVisibleItemPositions_last[0] > firstVisibleItemPositions[0])) {
//                    LogUtils.log("onScrollingFingerToBottom", firstVisibleItemPositions[0] + "  " + firstVisibleItemPositions_last[0]);
                        for (int p = firstVisibleItemPositions_last[0] - 1; p >= firstVisibleItemPositions[0]; p--) {
                            onItemShow(baseRecyclerView, p, positionHolder);
                        }
                    } else if (lastVisibleItemPositions[0] >= 0 &&
                            (lastVisibleItemPositions[lastVisibleItemPositions.length - 1] > lastVisibleItemPositions_last[lastVisibleItemPositions_last.length - 1])) {
                        //onScrollingFingerToTop
//                    LogUtils.log("onScrollingFingerToTop", lastVisibleItemPositions[0] + "  " + lastVisibleItemPositions_last[0]);
                        for (int p = lastVisibleItemPositions_last[lastVisibleItemPositions_last.length - 1] + 1;
                             p <= lastVisibleItemPositions[lastVisibleItemPositions.length - 1]; p++) {
                            onItemShow(baseRecyclerView, p, positionHolder);
                        }
                    }
                }
                positionHolder_last = positionHolder;

                switch (newState) {
                    case RecyclerView.SCROLL_STATE_IDLE:
                        onIdle(baseRecyclerView, positionHolder, baseRecyclerView.getVelocity_x(), baseRecyclerView.getVelocity_y(),
                                baseRecyclerView.getOffsetX(), baseRecyclerView.getOffsetY());
                        if (orientation == RecyclerView.VERTICAL) {
                            if (!recyclerView.canScrollVertically(1)) {
                                onScrollArrivedBottom(baseRecyclerView, positionHolder, baseRecyclerView.getOffsetX(), baseRecyclerView.getOffsetY());
                                return;
                            }
                            if (!recyclerView.canScrollVertically(-1)) {
                                onScrollArrivedTop(baseRecyclerView, positionHolder, baseRecyclerView.getOffsetX(), baseRecyclerView.getOffsetY());
                                return;
                            }
                        } else {
                            if (!recyclerView.canScrollHorizontally(1)) {
                                onScrollArrivedRight(baseRecyclerView, positionHolder, baseRecyclerView.getOffsetX(), baseRecyclerView.getOffsetY());
                                return;
                            }
                            if (!recyclerView.canScrollHorizontally(-1)) {
                                onScrollArrivedLeft(baseRecyclerView, positionHolder, baseRecyclerView.getOffsetX(), baseRecyclerView.getOffsetY());
                                return;
                            }
                        }

                        break;
                    case RecyclerView.SCROLL_STATE_DRAGGING:
                        onDragging(baseRecyclerView, positionHolder, baseRecyclerView.getVelocity_x(), baseRecyclerView.getVelocity_y(),
                                baseRecyclerView.getOffsetX(), baseRecyclerView.getOffsetY());
                        break;
                    case RecyclerView.SCROLL_STATE_SETTLING:
                        onSettling(baseRecyclerView, positionHolder, baseRecyclerView.getVelocity_x(), baseRecyclerView.getVelocity_y(),
                                baseRecyclerView.getOffsetX(), baseRecyclerView.getOffsetY());
                        break;
                }
            }
        };
    }

    private BaseRecyclerView checkRecyclerView(RecyclerView recyclerView) {
        BaseRecyclerView baseRecyclerView = null;
        try {
            baseRecyclerView = (BaseRecyclerView) recyclerView;
        } catch (Exception e) {
            throw new IllegalArgumentException("You must use the class " + BaseRecyclerView.class.getName() + " or it's child class in " + getClass().getName());
        }
        return baseRecyclerView;
    }

    public PositionHolder computPosition(RecyclerView recyclerView) {
        PositionHolder positionHolder = new PositionHolder(new int[1], new int[1], new int[1], new int[1]);
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        if (layoutManagerType == null) {
            if (layoutManager instanceof LinearLayoutManager) {
                layoutManagerType = LAYOUT_MANAGER_TYPE.LINEAR;
            } else if (layoutManager instanceof GridLayoutManager) {
                layoutManagerType = LAYOUT_MANAGER_TYPE.GRID;
            } else if (layoutManager instanceof StaggeredGridLayoutManager) {
                layoutManagerType = LAYOUT_MANAGER_TYPE.STAGGERED_GRID;
            } else {
                throw new RuntimeException("Unsupported LayoutManager used. Valid ones are LinearLayoutManager, GridLayoutManager and StaggeredGridLayoutManager");
            }
        }

        switch (layoutManagerType) {
            case LINEAR:
                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) layoutManager;
                orientation = linearLayoutManager.getOrientation();
                positionHolder.getFirstVisibleItemPositions()[0] = (linearLayoutManager.findFirstVisibleItemPosition());
                positionHolder.getFirstCompletelyVisibleItemPositions()[0] = linearLayoutManager.findFirstCompletelyVisibleItemPosition();
                positionHolder.getLastVisibleItemPositions()[0] = linearLayoutManager.findLastVisibleItemPosition();
                positionHolder.getLastCompletelyVisibleItemPositions()[0] = linearLayoutManager.findLastCompletelyVisibleItemPosition();
                break;
            case GRID:
                GridLayoutManager gridLayoutManager = (GridLayoutManager) layoutManager;
                orientation = gridLayoutManager.getOrientation();
                positionHolder.getFirstVisibleItemPositions()[0] = (gridLayoutManager.findFirstVisibleItemPosition());
                positionHolder.getFirstCompletelyVisibleItemPositions()[0] = gridLayoutManager.findFirstCompletelyVisibleItemPosition();
                positionHolder.getLastVisibleItemPositions()[0] = gridLayoutManager.findLastVisibleItemPosition();
                positionHolder.getLastCompletelyVisibleItemPositions()[0] = gridLayoutManager.findLastCompletelyVisibleItemPosition();
                break;
            case STAGGERED_GRID:
                StaggeredGridLayoutManager staggeredGridLayoutManager = (StaggeredGridLayoutManager) layoutManager;
                orientation = staggeredGridLayoutManager.getOrientation();
                /**
                 * Note that, this value is not affected by layout orientation or item order traversal.
                 *      * ({@link #setReverseLayout(boolean)}). Views are sorted by their positions in the adapter,
                 *      * not in the layout.
                 */
                positionHolder.setFirstVisibleItemPositions(staggeredGridLayoutManager.findFirstVisibleItemPositions(null));
                positionHolder.setFirstCompletelyVisibleItemPositions(staggeredGridLayoutManager.findFirstCompletelyVisibleItemPositions(null));
                positionHolder.setLastVisibleItemPositions(staggeredGridLayoutManager.findLastVisibleItemPositions(null));
                positionHolder.setLastCompletelyVisibleItemPositions(staggeredGridLayoutManager.findLastCompletelyVisibleItemPositions(null));
                break;
        }
        return positionHolder;
    }

    public RecyclerView.OnScrollListener getOnScrollListener() {
        return onScrollListener;
    }


    public void onFirstScrolled(BaseRecyclerView baseRecyclerView, PositionHolder positionHolder, int offsetX, int offsetY) {
    }

    public void onScrollArrivedTop(BaseRecyclerView baseRecyclerView, PositionHolder positionHolder, int offsetX, int offsetY) {

    }

    public void onScrollArrivedBottom(BaseRecyclerView baseRecyclerView, PositionHolder positionHolder, int offsetX, int offsetY) {

    }

    public void onScrollArrivedLeft(BaseRecyclerView baseRecyclerView, PositionHolder positionHolder, int offsetX, int offsetY) {
    }

    public void onScrollArrivedRight(BaseRecyclerView baseRecyclerView, PositionHolder positionHolder, int offsetX, int offsetY) {
    }

    public void onScrollingFingerToBottom(BaseRecyclerView baseRecyclerView, PositionHolder positionHolder, int dy, int offsetX, int offsetY) {
    }

    public void onScrollingFingerToTop(BaseRecyclerView baseRecyclerView, PositionHolder positionHolder, int dy, int offsetX, int offsetY) {
    }

    public void onScrollingFingerToLeft(BaseRecyclerView baseRecyclerView, PositionHolder positionHolder, int dy, int offsetX, int offsetY) {
    }

    public void onScrollingFingerToRight(BaseRecyclerView baseRecyclerView, PositionHolder positionHolder, int dy, int offsetX, int offsetY) {
    }

    public void onItemShow(BaseRecyclerView baseRecyclerView, int position, PositionHolder positionHolder) {

    }

    public void onDragging(BaseRecyclerView baseRecyclerView, PositionHolder positionHolder,int velocity_x, int velocity_y, int offsetX, int offsetY) {
        onShouldResumePicLoad(baseRecyclerView, positionHolder, baseRecyclerView.getVelocity_x(), baseRecyclerView.getVelocity_y(),
                baseRecyclerView.getOffsetX(), baseRecyclerView.getOffsetY());
    }

    public void onSettling(BaseRecyclerView baseRecyclerView, PositionHolder positionHolder,
                           int velocity_x, int velocity_y, int offsetX, int offsetY) {
        if (orientation == RecyclerView.VERTICAL) {
            if (Math.abs(velocity_y) >= ViewConfiguration.get(baseRecyclerView.getContext()).getScaledMaximumFlingVelocity() * 3f / 4)
                onSettlingShouldPausePicLoad(baseRecyclerView, positionHolder, velocity_x, velocity_y, offsetX, offsetY);
        } else {
            if (Math.abs(velocity_x) >= ViewConfiguration.get(baseRecyclerView.getContext()).getScaledMaximumFlingVelocity() * 3f / 4)
                onSettlingShouldPausePicLoad(baseRecyclerView, positionHolder, velocity_x, velocity_y, offsetX, offsetY);
        }
    }
    public void onIdle(BaseRecyclerView baseRecyclerView, PositionHolder positionHolder,
                                    int velocity_x, int velocity_y, int offsetX, int offsetY) {
        onShouldResumePicLoad(baseRecyclerView, positionHolder, baseRecyclerView.getVelocity_x(), baseRecyclerView.getVelocity_y(),
                baseRecyclerView.getOffsetX(), baseRecyclerView.getOffsetY());
    }

    public void onSettlingShouldPausePicLoad(BaseRecyclerView baseRecyclerView, PositionHolder positionHolder,
                                             int velocity_x, int velocity_y, int offsetX, int offsetY) {
    }

    public void onShouldResumePicLoad(BaseRecyclerView baseRecyclerView, PositionHolder positionHolder,
                                      int velocity_x, int velocity_y, int offsetX, int offsetY) {
    }


}