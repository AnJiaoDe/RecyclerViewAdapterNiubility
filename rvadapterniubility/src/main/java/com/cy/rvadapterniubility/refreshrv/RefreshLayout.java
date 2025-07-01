package com.cy.rvadapterniubility.refreshrv;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import com.cy.refreshlayoutniubility.HeadViewSimple;
import com.cy.refreshlayoutniubility.IHeadView;
import com.cy.refreshlayoutniubility.OnRefreshListener;
import com.cy.refreshlayoutniubility.RefreshCallback;


public class RefreshLayout extends LinearLayout {
    private GestureDetector gestureDetector;
    private int velocity_y;
    private OnRefreshListener onRefreshListener;
    private IHeadView headView;
    private View contentView;
    private boolean enableRefresh = true;
    private RefreshCallback refreshCallback = new RefreshCallback() {

        @Override
        public void onRefreshStart() {
            if (onRefreshListener != null) onRefreshListener.onRefreshStart(headView);
        }

        @Override
        public void onRefreshFinish() {
            if (onRefreshListener != null) onRefreshListener.onRefreshFinish(headView);
        }


        @Override
        public void onRefreshCancel() {
            if (onRefreshListener != null) onRefreshListener.onRefreshCancel(headView);

        }

        @Override
        public <T> void bindDataToRefreshFinishedLayout(View view, T msg) {
            if (onRefreshListener != null)
                onRefreshListener.bindDataToRefreshFinishedLayout(view, msg);
        }
    };

    public RefreshLayout(Context context) {
        this(context, null);
    }

    public RefreshLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setOrientation(VERTICAL);
        headView = new HeadViewSimple(context);
        contentView = new View(context);
        addHead();
        setContentView(contentView);
        gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                if (enableRefresh && distanceY < 0 && !contentView.canScrollVertically(-1)) {
                    //下滑
                    headView.onDragingDown((int) distanceY);
                } else if (distanceY > 0 && headView.getView().getHeight() != 0) {
                    //上滑
                    headView.onDragingUp((int) distanceY);
                }
                return true;
            }
        });
    }


    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        if (getChildCount() > 3)
            throw new RuntimeException("Exception:You can add only one contentView in " + getClass().getName());
        View view = getChildAt(2);
        if (view != null) {
            contentView = view;
            setContentView(contentView);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        headView.getView().measure(widthMeasureSpec,
                MeasureSpec.makeMeasureSpec(headView.getView().getLayoutParams().height, MeasureSpec.EXACTLY));
        contentView.measure(widthMeasureSpec,
                MeasureSpec.makeMeasureSpec(getMeasuredHeight(), MeasureSpec.EXACTLY));
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        int width_parent = getMeasuredWidth();
        int height_parent = getMeasuredHeight();
        int height_head = headView.getView().getLayoutParams().height;
        headView.getView().layout(0, 0, width_parent, height_head);
        if (contentView != null)
            contentView.layout(0, height_head, width_parent, height_parent + height_head);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
//        gestureDetector.onTouchEvent(event);

        VelocityTracker velocityTracker = VelocityTracker.obtain();
        velocityTracker.addMovement(event);
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                velocityTracker.computeCurrentVelocity(1000);
                velocity_y = (int) velocityTracker.getYVelocity();
                if (headView.getView().getHeight() != 0) headView.onDragRelease(velocity_y);
                break;
        }
        velocityTracker.recycle();
        return super.dispatchTouchEvent(event);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        gestureDetector.onTouchEvent(event);
        //必须，否则GG
        if (headView.getView().getHeight() != 0) return true;
        return super.onInterceptTouchEvent(event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        gestureDetector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

    private <T extends RefreshLayout> T addHead() {
        addView(headView.getView(), 0, new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0));
        return (T) this;
    }


    public <T extends RefreshLayout> T setContentView(View view) {
        removeView(contentView);
        LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0);
        layoutParams.weight = 1;
        this.contentView = view;
        addView(contentView, 1, layoutParams);
        return (T) this;
    }

    public void setOnRefreshListener(OnRefreshListener onRefreshListener) {
        this.onRefreshListener = onRefreshListener;
        headView.setRefreshFinishedLayoutID(onRefreshListener.getRefreshFinishedLayoutID());
        headView.setCallback(refreshCallback);
    }

    public <T extends RefreshLayout> T setHeadView(IHeadView headView) {
        removeView(this.headView.getView());
        this.headView = headView;
        addHead();
        return (T) this;
    }

    public IHeadView getHeadView() {
        return headView;
    }

    public void startRefresh() {
        if (enableRefresh) headView.refreshStart();
    }

    public void finishRefresh() {
        headView.refreshFinish();
    }

    public <T> void finishRefresh(T msg) {
        headView.refreshFinish(msg);
    }

    public <T> void finishRefreshDelay(final T msg, int ms) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                headView.refreshFinish(msg);
            }
        }, ms);
    }

    public void closeRefresh() {
        headView.closeRefresh();
    }

    public <T> void closeRefreshDelay(T msg, int ms) {
        headView.refreshFinish(msg);
        //因为refreshFinish是耗时的，所以，时间+1S
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                headView.closeRefresh();
            }
        }, ms + 1000);
    }

    public <T> void closeRefreshDelay(T msg) {
        closeRefreshDelay(msg, 1000);
    }

    /**
     * --------------------------------------------------------------------------------------
     */
    public <T extends RefreshLayout> T setEnableRefresh(boolean refresh) {
        this.enableRefresh = refresh;
        return (T) this;

    }

    public boolean enableRefresh() {
        return enableRefresh;
    }

    public <T extends RefreshLayout> T setRefreshColor(int color) {
        headView.getAnimationView().setColor(color);
        return (T) this;
    }

}
