package com.cy.rvadapterniubility.adapter;

import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewParent;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.cy.rvadapterniubility.LogUtils;

public class DragSelectFrameLayout extends FrameLayout {
    private float downX;
    private float downY;
    private GestureDetector gestureDetector;
    private DragSelectorAdapter<Object> dragSelectorAdapter;
    private RecyclerView recyclerView;
//    private boolean isLongPress = false;

    public DragSelectFrameLayout(@NonNull Context context) {
        this(context, null);
    }

    public DragSelectFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                return true;
            }

            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                LogUtils.log("onSingleTapUp");
                return super.onSingleTapUp(e);
            }

            @Override
            public void onLongPress(MotionEvent e) {
                super.onLongPress(e);
                LogUtils.log("onLongPress");

//                isLongPress = true;

                if (dragSelectorAdapter == null) return;
                dragSelectorAdapter.setHaveChildLongPress(true);
                int position = recyclerView.getChildAdapterPosition(DragSelectFrameLayout.this);
                if (position < 0 || position >= dragSelectorAdapter.getAdapter().getList_bean().size())
                    return;
                dragSelectorAdapter.onItemLongClick((BaseViewHolder) recyclerView.findViewHolderForAdapterPosition(position),
                        position, dragSelectorAdapter.getAdapter().getList_bean().get(position));
            }

        });
    }

    public void with(RecyclerView recyclerView, DragSelectorAdapter<?> dragSelectorAdapter) {
        this.recyclerView = recyclerView;
        this.dragSelectorAdapter = (DragSelectorAdapter<Object>) dragSelectorAdapter;
    }

    private void requestDisallowInterceptTouchEvent() {
        final ViewParent parent = getParent();
        if (parent != null) parent.requestDisallowInterceptTouchEvent(true);
    }


//    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//        gestureDetector.onTouchEvent(event);
//        switch (event.getActionMasked()) {
//            case MotionEvent.ACTION_POINTER_DOWN:
//            case MotionEvent.ACTION_DOWN:
//                LogUtils.log("child ACTION_DOWN");
//                downX = event.getX();
//                downY = event.getY();
//                if(dragSelectorAdapter!=null)dragSelectorAdapter.setHaveChildLongPress(false);
//                break;
//            case MotionEvent.ACTION_MOVE:
//                LogUtils.log("child ACTION_MOVE");
////                float moveX = event.getX();
////                float moveY = event.getY();
////                float dy = Math.abs(moveY - downY);
////                boolean moveV = dy > touchSlop && dy >= Math.abs(moveX - downX);
////                downX = moveX;
////                downY = moveY;
////                if (!moveV) {
////                    return true;
////                }
//                break;
//            case MotionEvent.ACTION_UP:
//            case MotionEvent.ACTION_POINTER_UP:
//                LogUtils.log("child ACTION_UP");
////                isLongPress = false;
//                if(dragSelectorAdapter!=null)dragSelectorAdapter.setHaveChildLongPress(false);
//                break;
//            case MotionEvent.ACTION_CANCEL:
//                LogUtils.log("child ACTION_CANCEL");
//                break;
//        }
//        return super.onTouchEvent(event);
//    }
}
