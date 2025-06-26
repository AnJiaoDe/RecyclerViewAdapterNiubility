package com.cy.rvadapterniubility.swipelayout;

import android.content.Context;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cy.rvadapterniubility.adapter.SwipeAdapter;
import com.cy.rvadapterniubility.recyclerview.BaseRecyclerView;


/**
 * Created by cy on 2017/7/2.此处不继承VerticalRecyclerView,是为避免多重继承带来的维护成本和强耦合
 */

public class SwipeRecyclerView extends BaseRecyclerView<SwipeRecyclerView> {
    private float downX;
    private float downY;
    private SwipeAdapter swipeAdapter;

    public SwipeRecyclerView(Context context) {
        this(context, null);
    }

    public SwipeRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        setLayoutManager(linearLayoutManager);
    }

    public SwipeRecyclerView swipe(SwipeAdapter swipeAdapter) {
        this.swipeAdapter = swipeAdapter;
        return this;
    }

    @Override
    public final void setAdapter(RecyclerView.Adapter adapter) {
        if (swipeAdapter == null)
            throw new RuntimeException("You must call swipe(SwipeAdapter swipeAdapter) first");
        super.setAdapter(adapter);
    }

    /**
     * 计算指定的 View 在屏幕中的坐标。
     */
    private RectF calcViewScreenLocation(View view) {
        int[] location = new int[2];
        // 获取控件在屏幕中的位置，返回的数组分别为控件左顶点的 x、y 的值
        view.getLocationOnScreen(location);
        return new RectF(location[0], location[1],
                location[0] + view.getWidth(), location[1] + view.getHeight());
    }

    /**
     * 判断触摸点是否在控件内
     */
    private boolean isInViewRange(View view, MotionEvent event) {

        // MotionEvent event;
        // event.getX(); 获取相对于控件自身左上角的 x 坐标值
        // event.getY(); 获取相对于控件自身左上角的 y 坐标值
        float x = event.getRawX(); // 获取相对于屏幕左上角的 x 坐标值
        float y = event.getRawY(); // 获取相对于屏幕左上角的 y 坐标值
        // View view;
        RectF rect = calcViewScreenLocation(view);
        return rect.contains(x, y);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (swipeAdapter.getOpened() != null &&
                !isInViewRange(swipeAdapter.getOpened().getSideView(), ev)) {
            swipeAdapter.closeOpened();
            return true;
        }
        int action = ev.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                downX = ev.getX();
                downY = ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                float moveX = ev.getX();
                float moveY = ev.getY();
                if (Math.abs(moveX - downX) > Math.abs(moveY - downY)) {
                    return false;
                } else if (swipeAdapter.getScrolled() != null) {
                    swipeAdapter.closeScrolled();
                    return false;
                }
                break;
            case MotionEvent.ACTION_UP:
                break;
            default:
                break;
        }
        return super.onInterceptTouchEvent(ev);
    }
}
