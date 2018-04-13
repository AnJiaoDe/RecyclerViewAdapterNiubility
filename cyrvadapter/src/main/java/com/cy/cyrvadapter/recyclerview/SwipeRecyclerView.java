package com.cy.cyrvadapter.recyclerview;

import android.content.Context;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.cy.cyrvadapter.adapter.SwipeRVAdapter;

/**
 * Created by cy on 2017/7/2.
 */

public class SwipeRecyclerView extends RecyclerView {
    private float downX;
    private float downY;
    private boolean canScrollVertically = true;
    private SwipeRVAdapter rvAdapter;

    public SwipeRecyclerView(Context context) {
        this(context, null);
    }

    public SwipeRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void setAdapter(Adapter adapter) {
        super.setAdapter(adapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext()) {
            @Override
            public boolean canScrollVertically() {
                return canScrollVertically;

            }
        };
        setLayoutManager(linearLayoutManager);
        this.rvAdapter = (SwipeRVAdapter) adapter;

    }

    /**
     * 计算指定的 View 在屏幕中的坐标。
     */
    private RectF calcViewScreenLocation(View view) {
        int[] location = new int[2];
        // 获取控件在屏幕中的位置，返回的数组分别为控件左顶点的 x、y 的值
        view.getLocationOnScreen(location);
        return new RectF(location[0], location[1], location[0] + view.getWidth(),
                location[1] + view.getHeight());
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
        if (rvAdapter.getSl_opened() != null) {
            canScrollVertically = false;


            if (!isInViewRange(rvAdapter.getSl_opened().getActionView(), ev)) {
                rvAdapter.closeOpenedSL();

                return true;
            } else {
                return false;
            }


        } else {
            canScrollVertically = true;


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
                    }


                    break;
                case MotionEvent.ACTION_UP:
                    break;
                default:
                    break;
            }


        }


        return super.onInterceptTouchEvent(ev);
    }


}
