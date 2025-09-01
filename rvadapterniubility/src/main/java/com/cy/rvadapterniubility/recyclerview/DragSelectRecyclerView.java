package com.cy.rvadapterniubility.recyclerview;

import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewParent;
import android.widget.OverScroller;

import androidx.annotation.Nullable;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.cy.rvadapterniubility.adapter.BaseViewHolder;
import com.cy.rvadapterniubility.adapter.DragSelectorAdapter;

public class DragSelectRecyclerView<T extends DragSelectRecyclerView> extends BaseRecyclerView<T> {
    private GestureDetector gestureDetector;
    private boolean isLongPress = false;
    private boolean isSelectMoving = false;
    private float downX;
    private float downY;
    private float touchSlop;
    private DragSelectorAdapter dragSelectorAdapter;
    private int position_will_select = NO_POSITION;
    private int position_start = NO_POSITION;
    private int position_end = NO_POSITION;
    private int position_start_last = NO_POSITION;
    private int position_end_last = NO_POSITION;
    private boolean inTopScrollRange = false;
    private boolean inBottomScrollRange = false;
    private int topBoundFrom;
    private int topBoundTo;
    private int bottomBoundFrom;
    private int bottomBoundTo;
    private int scrollDistance;
    private float scrollSpeedFactor;
    private float x_last = -1;
    private float y_last = -1;
    private OverScroller overScroller;
    private Runnable runnableScroll;
    private int maxScrollDistance = 48;
    private int autoScrollDistance = (int) (Resources.getSystem().getDisplayMetrics().density * 56);
    private boolean downSelected = false;
    private boolean cancelSelect = false;
    private boolean dx_dy = false;

    public DragSelectRecyclerView(Context context) {
        //注意是this,否则GG
        this(context, null);
    }

    public DragSelectRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {

            @Override
            public void onLongPress(MotionEvent e) {
                super.onLongPress(e);
                isLongPress = true;

                if (dragSelectorAdapter == null) return;
                View child = findChildViewUnder(e.getX(), e.getY());
                int position = getChildAdapterPosition(child);
                if (position < 0 || position >= dragSelectorAdapter.getList_bean().size())
                    return;

                position_start = position;
                position_end = position;
                position_start_last = position;
                position_end_last = position;

                BaseViewHolder baseViewHolder = (BaseViewHolder) findViewHolderForAdapterPosition(position);
                if (baseViewHolder == null) return;
                dragSelectorAdapter.onItemLongClick__(baseViewHolder,
                        position, dragSelectorAdapter.getList_bean().get(position));
            }

        });

        final ViewConfiguration viewConfiguration = ViewConfiguration.get(context);
        touchSlop = viewConfiguration.getScaledTouchSlop();
        overScroller = new OverScroller(context);
        runnableScroll = new Runnable() {
            @Override
            public void run() {
                if (overScroller != null && overScroller.computeScrollOffset()) {
                    int s;
                    if (scrollDistance > 0)
                        s = Math.min(scrollDistance, maxScrollDistance);
                    else
                        s = Math.max(scrollDistance, -maxScrollDistance);
                    scrollBy(0, s);
                    //不能少
                    updateSelectedRange(x_last, y_last);
                    ViewCompat.postOnAnimation(DragSelectRecyclerView.this, this);
                    //是否能下滑
//                    LogUtils.log("computeScrollOffset",canScrollVertically(-1));
                    //是否能上滑
//                    LogUtils.log("computeScrollOffset 11111",canScrollVertically(1));
                    if (scrollDistance > 0 && !canScrollVertically(1) || scrollDistance < 0 && !canScrollVertically(-1)) {
                        stopAutoScroll();
                    }
                }
            }
        };
    }

    public T dragSelector(DragSelectorAdapter dragSelectorAdapter) {
        this.dragSelectorAdapter = dragSelectorAdapter;
        return (T) this;
    }

    /**
     * 何故不能直接判断是否是dragSelectorAdapter，然后直接强转呢？因为有些Adapter是ConcatAdapter,
     * dragSelectorAdapter只是ConcatAdapter的其中一个
     *
     * @param adapter The new adapter to set, or null to set no adapter.
     */
    @Override
    public void setAdapter(@Nullable Adapter adapter) {
        if (adapter instanceof DragSelectorAdapter && this.dragSelectorAdapter == null)
            throw new IllegalStateException("must call dragSelector(DragSelectorAdapter dragSelectorAdapter) first!");
        super.setAdapter(adapter);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        int height = getHeight();
        topBoundFrom = 0;
        topBoundTo = autoScrollDistance;
        bottomBoundFrom = height - autoScrollDistance;
        bottomBoundTo = height;
    }

    /**
     * 1.长按，switch当前item选中状态
     * 2.如果当前在选择模式下，非竖直滑动，移动能switch选中状态（手指按下时的当前item选中状态的！），往回移动都是非选中
     * 3.如果当前在选择模式下，非竖直滑动，手指移动后往下拖或者往上拖，如果超出边界，
     * 需要滚动RV，顶部或者底部无法触摸到的item，都要switch选中状态
     * 4.如果当前在选择模式下，非竖直滑动，手指移动后往下拖或者往上拖，即使没有超出边界，没有触发滚动，也要能switch有空格的行
     * （目前是没有用findlastvisibleitems做判断，而是不能再上滑做的判断，虽然不完美，暂时这样吧，这玩意烦躁得很）
     *
     * @param event The motion event to be dispatched.
     * @return
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (dragSelectorAdapter == null || dragSelectorAdapter.getList_bean().isEmpty())
            return super.dispatchTouchEvent(event);
        LayoutManager layoutManager = getLayoutManager();
        if (layoutManager == null) return super.dispatchTouchEvent(event);

        int orientation = 0;
        //注意：GridLayoutManager继承于LinearLayoutManager
        if (layoutManager instanceof LinearLayoutManager) {
            orientation = ((LinearLayoutManager) layoutManager).getOrientation();
        } else if (layoutManager instanceof StaggeredGridLayoutManager) {
            orientation = ((StaggeredGridLayoutManager) layoutManager).getOrientation();
        }
        if (orientation != RecyclerView.VERTICAL) return super.dispatchTouchEvent(event);

        gestureDetector.onTouchEvent(event);
        if (!dragSelectorAdapter.isUsingSelector()) return super.dispatchTouchEvent(event);

        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_POINTER_DOWN:
            case MotionEvent.ACTION_DOWN:
                dragSelectorAdapter.canItemClick(true);
                isLongPress = false;
                isSelectMoving = false;
                downX = event.getX();
                downY = event.getY();
                dx_dy = false;
                downSelected = false;
                cancelSelect = false;
                scrollDistance = 0;
                View c = findChildViewUnder(downX, downY);
                if (c != null) {
                    int position = getChildAdapterPosition(c);
                    if (position != NO_POSITION) {
                        position_start = position;
                        position_end = position;
                        position_start_last = position;
                        position_end_last = position;
                        //这个必须写在这里
                        downSelected = dragSelectorAdapter.isSelected(position_start);
                    }
                }
                break;
            case MotionEvent.ACTION_MOVE:
                final float moveX = event.getX();
                final float moveY = event.getY();
                float dx = Math.abs(moveX - downX);
                float dy = Math.abs(moveY - downY);
                dx_dy = dx > dy;
                boolean moveH = dx > touchSlop && dx > dy;
                downX = moveX;
                downY = moveY;
                //滑动过程中，不能触发选择，否则很容易错选不想选的，
                // 横向滑动程度大于竖向滑动程度，横向滑动超过一定距离，选中当前ITEM，并且拦截竖直滑动，直到UP之后
                if (getScrollState() == RecyclerView.SCROLL_STATE_IDLE && (isLongPress || isSelectMoving || moveH)) {
                    View child = findChildViewUnder(moveX, moveY);
                    if (child != null) {
                        int position = getChildAdapterPosition(child);
                        if (position != NO_POSITION) {
                            isSelectMoving = true;
                            dragSelectorAdapter.canItemClick(false);
//                            if (position == position_start)
//                                dragSelectorAdapter.select(position, !cancelSelect && !downSelected, this);

//                            if (position == position_start){
//                                position_will_select=position;
//                            }else {
//                                dragSelectorAdapter.select(position_will_select, !cancelSelect && !downSelected, this);
//                            }
                        }
                    }
                    //注意：MOVE事件，手指超出当前VIEW的边界后，如果在VIEW边界上面，Y是负数，如果在VIEW边界下面，Y比VIEW的高度大
                    //手指在列表顶部，列表下滑
                    if (!moveH && moveY >= topBoundFrom && moveY <= topBoundTo) {
                        x_last = moveX;
                        y_last = moveY;
                        scrollSpeedFactor = (((float) topBoundTo - (float) topBoundFrom) - (moveY - (float) topBoundFrom))
                                / ((float) topBoundTo - (float) topBoundFrom);
                        scrollDistance = (int) ((float) maxScrollDistance * scrollSpeedFactor * -1f);
                        if (!inTopScrollRange) {
                            inTopScrollRange = true;
                            startAutoScroll();
                        }
                        //手指在列表最顶部外面，列表最大速度下滑
                    } else if (!moveH && moveY < topBoundFrom) {
                        x_last = moveX;
                        y_last = moveY;
                        scrollDistance = maxScrollDistance * -1;
                        if (!inTopScrollRange) {
                            inTopScrollRange = true;
                            startAutoScroll();
                        }
                        //手指在列表底部，列表上滑
                    } else if (!moveH && moveY >= bottomBoundFrom && moveY <= bottomBoundTo) {
                        x_last = moveX;
                        y_last = moveY;
                        scrollSpeedFactor = ((moveY - (float) bottomBoundFrom)) / ((float) bottomBoundTo - (float) bottomBoundFrom);
                        scrollDistance = (int) ((float) maxScrollDistance * scrollSpeedFactor);
                        if (!inBottomScrollRange) {
                            inBottomScrollRange = true;
                            startAutoScroll();
                        }
                        //手指在列表最底部外面，列表最大速度上滑
                    } else if (!moveH && moveY > bottomBoundTo) {
                        x_last = moveX;
                        y_last = moveY;
                        scrollDistance = maxScrollDistance;
                        if (!inBottomScrollRange) {
                            inBottomScrollRange = true;
                            startAutoScroll();
                        }
                    } else {
                        inTopScrollRange = false;
                        inBottomScrollRange = false;
                        x_last = -1;
                        y_last = -1;
                        stopAutoScroll();
                    }
//                    if (!inTopScrollRange && !inBottomScrollRange)
                    updateSelectedRange(moveX, moveY);
                }
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:
                //不能少，否则G
                dragSelectorAdapter.canItemClick(true);

                position_start = NO_POSITION;
                position_end = NO_POSITION;
                position_start_last = NO_POSITION;
                position_end_last = NO_POSITION;
                inTopScrollRange = false;
                inBottomScrollRange = false;
                scrollDistance = 0;
                x_last = -1;
                y_last = -1;
                stopAutoScroll();
                break;
        }
        //ACTION_UP ACTION_CANCEL 等也要拦截，否则会导致itemview如果只有down和up就成了itemview单击了，应该把up拦截掉，
        //如果dx>dy，必须要求父控件（如Viewpager）不拦截，否则会导致无法接受下一次的MOVE，导致无法滑动选择
        if (isLongPress || isSelectMoving || dx_dy) {
            //需要防止被刷新控件拦截
            requestDisallowInterceptTouchEvent();
            return true;
        }
        return super.dispatchTouchEvent(event);
    }

    private void requestDisallowInterceptTouchEvent() {
        final ViewParent parent = getParent();
        if (parent != null) parent.requestDisallowInterceptTouchEvent(true);
    }

    private void startAutoScroll() {
        if (overScroller.isFinished()) {
            removeCallbacks(runnableScroll);
            overScroller.startScroll(0, overScroller.getCurrY(), 0, 5000, 100000);
            ViewCompat.postOnAnimation(this, runnableScroll);
        }
    }

    private void stopAutoScroll() {
        if (!overScroller.isFinished()) {
            removeCallbacks(runnableScroll);
            overScroller.abortAnimation();
        }
    }

    private void updateSelectedRange(float x, float y) {
        View child = findChildViewUnder(x, y);
        if (child != null) {
            int position = getChildAdapterPosition(child);
            if (position != NO_POSITION && position != position_end) {
                position_end = position;
            }
        } else if (y > getHeight() && !canScrollVertically(1)) {
            //上滑，且不能再上滑，手指超出边界，选中最后一个，这里暂且不做findLastVisibleItemPositions处理，因为我懒得做了，烦躁 damn
            position_end = dragSelectorAdapter.getList_bean().size() - 1;
        } else if (y < 0 && !canScrollVertically(-1)) {
            //下滑，且不能再下滑
            position_end = 0;
        }
        if (position_start == RecyclerView.NO_POSITION || position_end == RecyclerView.NO_POSITION)
            return;

        int newStart, newEnd;
        newStart = Math.min(position_start, position_end);
        newEnd = Math.max(position_start, position_end);

        if (newStart > position_start_last) {
            //往上往前拖动的前提下   再往后往下拖动，会触发
//            LogUtils.log("selectRange position_start", position_start);
//            LogUtils.log("selectRange    position_end", position_end);
//            LogUtils.log("selectRange newStart", newStart);
//            LogUtils.log("selectRange     newEnd", newEnd);
//            LogUtils.log("selectRange", "newStart > position_start_last");
            cancelSelect = true;
            dragSelectorAdapter.selectRange(position_start_last, newStart, false, this);
        } else if (newStart < position_start_last) {
            //往前往上拖动
//            LogUtils.log("selectRange position_start", position_start);
//            LogUtils.log("selectRange    position_end", position_end);
//            LogUtils.log("selectRange newStart", newStart);
//            LogUtils.log("selectRange     newEnd", newEnd);
//            LogUtils.log("selectRange", "newStart < position_start_last");
            dragSelectorAdapter.selectRange(newStart, position_start_last,
                    isLongPress ? dragSelectorAdapter.isSelected(position_start) : !downSelected, this);
        }
        //注意：这里不是else if 而是if,否则GG
        if (newEnd > position_end_last) {
            // 往下往后拖动
//            LogUtils.log("selectRange position_start", position_start);
//            LogUtils.log("selectRange    position_end", position_end);
//            LogUtils.log("selectRange newStart", newStart);
//            LogUtils.log("selectRange     newEnd", newEnd);
//            LogUtils.log("selectRange", "newEnd > position_end_last");
            dragSelectorAdapter.selectRange(position_end_last, newEnd,
                    isLongPress ? dragSelectorAdapter.isSelected(position_start) : !downSelected, this);
        } else if (newEnd < position_end_last) {
            //往下往后拖动的前提下   再往前往上拖动，会触发
//            LogUtils.log("selectRange position_start", position_start);
//            LogUtils.log("selectRange    position_end", position_end);
//            LogUtils.log("selectRange newStart", newStart);
//            LogUtils.log("selectRange     newEnd", newEnd);
//            LogUtils.log("selectRange", "newEnd < position_end_last");
            cancelSelect = true;
            dragSelectorAdapter.selectRange(newEnd, position_end_last, false, this);
        }
        position_start_last = newStart;
        position_end_last = newEnd;
    }
}
