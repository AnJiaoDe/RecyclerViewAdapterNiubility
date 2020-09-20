package com.cy.rvadapterniubility.recyclerview;


import android.view.ViewConfiguration;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;


/**
 * Created by lenovo on 2017/12/31.
 */

public abstract class OnSimpleScrollListener {
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
    private int orientation = RecyclerView.VERTICAL;

    public OnSimpleScrollListener() {
        positionHolder = new PositionHolder(new int[1], new int[1], new int[1], new int[1]);
        onScrollListener = new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                BaseRecyclerView baseRecyclerView = checkRecyclerView(recyclerView);
                if (firstCallOnScrolled) {
                    computPosition(recyclerView);
                    onFirstScrolled(recyclerView, positionHolder, baseRecyclerView.getOffsetX(), baseRecyclerView.getOffsetY());
                    firstCallOnScrolled = false;
                }
                if (dy < 0) { // 当前处于上滑状态
                    onScrollingFingerToBottom(recyclerView, dy, baseRecyclerView.getOffsetX(), baseRecyclerView.getOffsetY());
                    return;
                }
                if (dy > 0) { // 当前处于下滑状态
                    onScrollingFingerToTop(recyclerView, dy, baseRecyclerView.getOffsetX(), baseRecyclerView.getOffsetY());
                    return;
                }
                if (dx < 0) { // 当前处于上滑状态
                    onScrollingFingerToRight(recyclerView, dy, baseRecyclerView.getOffsetX(), baseRecyclerView.getOffsetY());
                    return;
                }
                if (dx > 0) { // 当前处于下滑状态
                    onScrollingFingerToLeft(recyclerView, dy, baseRecyclerView.getOffsetX(), baseRecyclerView.getOffsetY());
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
                computPosition(recyclerView);
                BaseRecyclerView baseRecyclerView = checkRecyclerView(recyclerView);
                switch (newState) {
                    case RecyclerView.SCROLL_STATE_IDLE:
                        onIdleShouldResumePicLoad(recyclerView, positionHolder, baseRecyclerView.getVelocity_x(), baseRecyclerView.getVelocity_y(),
                                baseRecyclerView.getOffsetX(), baseRecyclerView.getOffsetY());
                        if (orientation == RecyclerView.VERTICAL) {
                            if (!recyclerView.canScrollVertically(1)) {
                                onScrollArrivedBottom(recyclerView, positionHolder, baseRecyclerView.getOffsetX(), baseRecyclerView.getOffsetY());
                                return;
                            }
                            if (!recyclerView.canScrollVertically(-1)) {
                                onScrollArrivedTop(recyclerView, positionHolder, baseRecyclerView.getOffsetX(), baseRecyclerView.getOffsetY());
                                return;
                            }
                        } else {
                            if (!recyclerView.canScrollHorizontally(1)) {
                                onScrollArrivedRight(recyclerView, positionHolder, baseRecyclerView.getOffsetX(), baseRecyclerView.getOffsetY());
                                return;
                            }
                            if (!recyclerView.canScrollHorizontally(-1)) {
                                onScrollArrivedLeft(recyclerView, positionHolder, baseRecyclerView.getOffsetX(), baseRecyclerView.getOffsetY());
                                return;
                            }
                        }

                        break;
                    case RecyclerView.SCROLL_STATE_DRAGGING:
                        onDragging(recyclerView, positionHolder, baseRecyclerView.getOffsetX(), baseRecyclerView.getOffsetY());
                        break;
                    case RecyclerView.SCROLL_STATE_SETTLING:
                        onSettling(recyclerView, positionHolder, baseRecyclerView.getVelocity_x(), baseRecyclerView.getVelocity_y(),
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


    public void onFirstScrolled(RecyclerView recyclerView, PositionHolder positionHolder, int offsetX, int offsetY) {
    }

    public void onScrollArrivedTop(RecyclerView recyclerView, PositionHolder positionHolder, int offsetX, int offsetY) {

    }

    public void onScrollArrivedBottom(RecyclerView recyclerView, PositionHolder positionHolder, int offsetX, int offsetY) {

    }

    public void onScrollArrivedLeft(RecyclerView recyclerView, PositionHolder positionHolder, int offsetX, int offsetY) {
    }

    public void onScrollArrivedRight(RecyclerView recyclerView, PositionHolder positionHolder, int offsetX, int offsetY) {
    }

    public void onScrollingFingerToBottom(RecyclerView recyclerView, int dy, int offsetX, int offsetY) {
    }

    public void onScrollingFingerToTop(RecyclerView recyclerView, int dy, int offsetX, int offsetY) {
    }

    public void onScrollingFingerToLeft(RecyclerView recyclerView, int dy, int offsetX, int offsetY) {
    }

    public void onScrollingFingerToRight(RecyclerView recyclerView, int dy, int offsetX, int offsetY) {
    }


    public void onDragging(RecyclerView recyclerView, PositionHolder positionHolder, int offsetX, int offsetY) {
    }

    public void onSettling(RecyclerView recyclerView, PositionHolder positionHolder,
                           int velocity_x, int velocity_y, int offsetX, int offsetY) {
        if (orientation == RecyclerView.VERTICAL) {
            if (Math.abs(velocity_y) >= ViewConfiguration.get(recyclerView.getContext()).getScaledMaximumFlingVelocity() / 2)
                onSettlingShouldPausePicLoad(recyclerView, positionHolder, velocity_x, velocity_y, offsetX, offsetY);
        } else {
            if (Math.abs(velocity_x) >= ViewConfiguration.get(recyclerView.getContext()).getScaledMaximumFlingVelocity() / 2)
                onSettlingShouldPausePicLoad(recyclerView, positionHolder, velocity_x, velocity_y, offsetX, offsetY);
        }
    }

    public abstract void onSettlingShouldPausePicLoad(RecyclerView recyclerView, PositionHolder positionHolder,
                                                      int velocity_x, int velocity_y, int offsetX, int offsetY);
    public abstract void onIdleShouldResumePicLoad(RecyclerView recyclerView, PositionHolder positionHolder,
                                                      int velocity_x, int velocity_y, int offsetX, int offsetY);

    public static class PositionHolder {
        private int[] firstVisibleItemPositions;
        private int[] firstCompletelyVisibleItemPositions;
        private int[] lastVisibleItemPositions;
        private int[] lastCompletelyVisibleItemPositions;

        private PositionHolder(int[] firstVisibleItemPositions, int[] firstCompletelyVisibleItemPositions, int[] lastVisibleItemPositions, int[] lastCompletelyVisibleItemPositions) {
            this.firstVisibleItemPositions = firstVisibleItemPositions;
            this.firstCompletelyVisibleItemPositions = firstCompletelyVisibleItemPositions;
            this.lastVisibleItemPositions = lastVisibleItemPositions;
            this.lastCompletelyVisibleItemPositions = lastCompletelyVisibleItemPositions;
        }

        private int[] getFirstVisibleItemPositions() {
            return firstVisibleItemPositions;
        }

        public PositionHolder setFirstVisibleItemPositions(int[] firstVisibleItemPositions) {
            this.firstVisibleItemPositions = firstVisibleItemPositions;
            return this;
        }

        public int[] getFirstCompletelyVisibleItemPositions() {
            return firstCompletelyVisibleItemPositions;
        }

        public PositionHolder setFirstCompletelyVisibleItemPositions(int[] firstCompletelyVisibleItemPositions) {
            this.firstCompletelyVisibleItemPositions = firstCompletelyVisibleItemPositions;
            return this;
        }

        public int[] getLastVisibleItemPositions() {
            return lastVisibleItemPositions;
        }

        public PositionHolder setLastVisibleItemPositions(int[] lastVisibleItemPositions) {
            this.lastVisibleItemPositions = lastVisibleItemPositions;
            return this;
        }

        public int[] getLastCompletelyVisibleItemPositions() {
            return lastCompletelyVisibleItemPositions;
        }

        public PositionHolder setLastCompletelyVisibleItemPositions(int[] lastCompletelyVisibleItemPositions) {
            this.lastCompletelyVisibleItemPositions = lastCompletelyVisibleItemPositions;
            return this;
        }
    }

}