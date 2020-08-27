package com.cy.rvadapterniubility.recyclerview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


/**
 * Created by cy on 2017/7/2.
 */

public class StaggeredVerticalRecyclerView extends BaseRecyclerView<StaggeredVerticalRecyclerView> implements ILinearRecyclerView{
    private LinearItemDecoration linearItemDecoration;
    private int downX; // 按下时 X轴坐标值
    private int downY; // 按下时 Y 轴坐标值
    public StaggeredVerticalRecyclerView(Context context) {
        this(context, null);
    }

    public StaggeredVerticalRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
//        setOverScrollMode(OVER_SCROLL_NEVER);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downX = (int) ev.getX();
                downY = (int) ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                int moveX = (int) ev.getX();
                int moveY = (int) ev.getY();
//                int distanceY = moveY - downY;
                if (Math.abs(moveX - downX) < Math.abs(moveY - downY)) {
                    return false;
                }
                downX = moveX;
                downY = moveY;
        }
        return true;
    }

    @Override
    public void setAdapter(RecyclerView.Adapter adapter) {
        setLayoutManager(new LinearLayoutManager(getContext()));
        super.setAdapter(adapter);
    }

    @Override
    public void addItemDecoration(@NonNull RecyclerView.ItemDecoration decor, int index) {
        try {
            this.linearItemDecoration = (LinearItemDecoration) decor;
        }catch (Exception e){
            throw new IllegalAccessError("You can only use LinearItemDecoration in VerticalRecyclerView or HorizontalRecyclerView");
        }
        super.addItemDecoration(decor, index);
    }

    @Override
    public void addItemDecoration(@NonNull RecyclerView.ItemDecoration decor) {
        try {
            this.linearItemDecoration = (LinearItemDecoration) decor;
        }catch (Exception e){
            throw new IllegalAccessError("You can only use LinearItemDecoration in VerticalRecyclerView or HorizontalRecyclerView");
        }
        super.addItemDecoration(decor);
    }

    @NonNull
    @Override
    public RecyclerView.ItemDecoration getItemDecorationAt(int index) {
        return linearItemDecoration;
    }

    public LinearItemDecoration getItemDecoration() {
        return linearItemDecoration;
    }


    @Override
    public <T extends RecyclerView> T getRecyclerView() {
        return (T) this;
    }
}
