package com.cy.rvadapterniubility.recyclerview;

import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.OverScroller;

import androidx.annotation.Nullable;
import androidx.core.view.ViewCompat;

import com.cy.rvadapterniubility.LogUtils;
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
    private int maxScrollDistance = 32;
    private int autoScrollDistance = (int) (Resources.getSystem().getDisplayMetrics().density * 56);

    public DragSelectRecyclerView(Context context) {
        //注意是this,否则GG
        this(context, null);
    }

    public DragSelectRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                return true;
            }

            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                LogUtils.log("onSingleTapUp");
                if (dragSelectorAdapter != null) {
                    View child = findChildViewUnder(e.getX(), e.getY());
                    int position = getChildAdapterPosition(child);
                    if (position > 0 && position < dragSelectorAdapter.getAdapter().getList_bean().size())
                        dragSelectorAdapter.toggle(position);
                }
                return super.onSingleTapUp(e);
            }

            @Override
            public void onLongPress(MotionEvent e) {
                super.onLongPress(e);
                LogUtils.log("onLongPress");
                isLongPress = true;

                if (dragSelectorAdapter == null) return;
                dragSelectorAdapter.setHaveChildLongPress(true);
                View child = findChildViewUnder(e.getX(), e.getY());
                int position = getChildAdapterPosition(child);
                if (position < 0 || position >= dragSelectorAdapter.getAdapter().getList_bean().size())
                    return;
                dragSelectorAdapter.onItemLongClick((BaseViewHolder) findViewHolderForAdapterPosition(position),
                        position, dragSelectorAdapter.getAdapter().getList_bean().get(position));
            }

        });

        final ViewConfiguration viewConfiguration = ViewConfiguration.get(context);
        touchSlop = viewConfiguration.getScaledTouchSlop();
    }

    public T dragSelector(DragSelectorAdapter dragSelectorAdapter) {
        this.dragSelectorAdapter = dragSelectorAdapter;
        return (T) this;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (dragSelectorAdapter == null || dragSelectorAdapter.getAdapter().getList_bean().isEmpty())
            return super.dispatchTouchEvent(event);
        gestureDetector.onTouchEvent(event);
        if (!dragSelectorAdapter.isUsingSelector()) return super.dispatchTouchEvent(event);
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_POINTER_DOWN:
            case MotionEvent.ACTION_DOWN:
                LogUtils.log("dispatchTouchEvent  ACTION_DOWN");
                isLongPress = false;
                downX = event.getX();
                downY = event.getY();
                isSelectMoving = false;

                View c = findChildViewUnder(downX, downY);
                if (c != null) {
                    int position = getChildAdapterPosition(c);
                    if (position != NO_POSITION) {
                        position_start = position;
                        position_end = position;
                        position_start_last = position;
                        position_end_last = position;
                    }
                }
                break;
            case MotionEvent.ACTION_MOVE:
                LogUtils.log("dispatchTouchEvent  ACTION_MOVE");
                float moveX = event.getX();
                float moveY = event.getY();
                float dx = Math.abs(moveX - downX);
                float dy = Math.abs(moveY - downY);
                boolean moveV = dy > touchSlop && dy >= dx;
                downX = moveX;
                downY = moveY;
                //    横向滑动程度大于竖向滑动程度，横向滑动超过一定距离，选中当前ITEM，并且拦截竖直滑动，
                //                    直到UP之后
                if (isSelectMoving || (!moveV && dx > touchSlop && dx >= dy)) {
                    View child = findChildViewUnder(moveX, moveY);
                    if (child != null) {
                        int position = getChildAdapterPosition(child);
                        if (position != NO_POSITION) {
                            isSelectMoving = true;
                            dragSelectorAdapter.select(position, true);

                            if (!inTopScrollRange && !inBottomScrollRange)
                                updateSelectedRange(moveX, moveY);
//                            int y = (int) event.getY();
//                            if (y >= topBoundFrom && y <= topBoundTo) {
//                                x_last = event.getX();
//                                y_last = event.getY();
//                                scrollSpeedFactor = (((float) topBoundTo - (float) topBoundFrom) - ((float) y - (float) topBoundFrom))
//                                        / ((float) topBoundTo - (float) topBoundFrom);
//                                scrollDistance = (int) ((float) maxScrollDistance * scrollSpeedFactor * -1f);
//                                if (!inTopScrollRange) {
//                                    inTopScrollRange = true;
//                                    startScroll();
//                                }
//                            } else if (y < topBoundFrom) {
//                                x_last = event.getX();
//                                y_last = event.getY();
//                                scrollDistance = maxScrollDistance * -1;
//                                if (!inTopScrollRange) {
//                                    inTopScrollRange = true;
//                                    startScroll();
//                                }
//                            } else if (y >= bottomBoundFrom && y <= bottomBoundTo) {
//                                x_last = event.getX();
//                                y_last = event.getY();
//                                scrollSpeedFactor = (((float) y - (float) bottomBoundFrom)) / ((float) bottomBoundTo - (float) bottomBoundFrom);
//                                scrollDistance = (int) ((float) maxScrollDistance * scrollSpeedFactor);
//                                if (!inBottomScrollRange) {
//                                    inBottomScrollRange = true;
//                                    startScroll();
//                                }
//                            } else if (y > bottomBoundTo) {
//                                x_last = event.getX();
//                                y_last = event.getY();
//                                scrollDistance = maxScrollDistance;
//                                if (!inBottomScrollRange) {
//                                    inBottomScrollRange = true;
//                                    startScroll();
//                                }
//                            } else {
//                                inTopScrollRange = false;
//                                inBottomScrollRange = false;
//                                x_last = -1;
//                                y_last = -1;
//                                stopScroll();
//
//                            }
                        }
                    }
                }
                break;
            case MotionEvent.ACTION_CANCEL:
                LogUtils.log("dispatchTouchEvent  ACTION_CANCEL");
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:
                LogUtils.log("dispatchTouchEvent  ACTION_UP");
                isLongPress = false;
                reset();
                break;
        }
        //ACTION_UP ACTION_CANCEL 等也要拦截，否则会导致itemview如果只有down和up就成了itemview单击了，应该把up拦截掉，
        if (isLongPress || isSelectMoving) return true;
        return super.dispatchTouchEvent(event);
    }

    private void reset() {
        position_start = NO_POSITION;
        position_end = NO_POSITION;
        position_start_last = NO_POSITION;
        position_end_last = NO_POSITION;
        inTopScrollRange = false;
        inBottomScrollRange = false;
        x_last = -1;
        y_last = -1;
        stopAutoScroll();
    }

    private void startAutoScroll() {
        if (overScroller == null) overScroller = new OverScroller(getContext());
        if (overScroller.isFinished()) {
            removeCallbacks(runnableScroll);
            overScroller.startScroll(0, overScroller.getCurrY(), 0, 5000, 100000);
            ViewCompat.postOnAnimation(this, runnableScroll);
        }
    }


    private void stopAutoScroll() {
        if (overScroller != null && !overScroller.isFinished()) {
            removeCallbacks(runnableScroll);
            overScroller.abortAnimation();
        }
    }

    private void updateSelectedRange(float x, float y) {
        LogUtils.log("updateSelectedRange00000");
        View child = findChildViewUnder(x, y);
        if (child != null) {
            int position = getChildAdapterPosition(child);
            LogUtils.log("updateSelectedRange111", position + "    " + position_end);
            if (position != NO_POSITION && position_end != position) {
                LogUtils.log("updateSelectedRange");
                position_end = position;

                int newStart, newEnd;
                newStart = Math.min(position_start, position_end);
                newEnd = Math.max(position_start, position_end);
                if (position_start_last == NO_POSITION || position_end_last == NO_POSITION) {
                    if (newEnd - newStart == 1)
                        dragSelectorAdapter.selectRange(newStart, newStart, true);
                    else
                        dragSelectorAdapter.selectRange(newStart, newEnd, true);
                } else {
                    if (newStart > position_start_last)
                        dragSelectorAdapter.selectRange(position_start_last, newStart - 1, false);
                    else if (newStart < position_start_last)
                        dragSelectorAdapter.selectRange(newStart, position_start_last - 1, true);
                    if (newEnd > position_end_last)
                        dragSelectorAdapter.selectRange(position_end_last + 1, newEnd, true);
                    else if (newEnd < position_end_last)
                        dragSelectorAdapter.selectRange(newEnd + 1, position_end_last, false);
                }
                position_start_last = newStart;
                position_end_last = newEnd;
            }
        }
    }
}
