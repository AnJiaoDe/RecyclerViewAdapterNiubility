package com.cy.recyclerviewadapter.swipeview;//package com.cy.router.swipeview;
//
//import android.app.Activity;
//import android.content.Context;
//import android.content.res.TypedArray;
//import android.graphics.Canvas;
//import android.graphics.Matrix;
//import android.graphics.Rect;
//import android.graphics.drawable.Drawable;
//import android.util.AttributeSet;
//import android.view.MotionEvent;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.FrameLayout;
//
//import androidx.annotation.NonNull;
//import androidx.core.view.ViewCompat;
//
//import com.cy.androidview.LogUtils;
//import com.cy.androidview.R;
//
//public class SwipeLayout extends FrameLayout {
//    private View contentView;
////    private int left_content;
////    private int top_content;
//    private Drawable drawable_shadow_left;
//    private Drawable drawable_shadow_top;
//    private Drawable drawable_shadow_right;
//    private Drawable drawable_shadow_bottom;
//    private final Rect childRect;
//    private float downX;
//    private static final int SWIPE_THRESHOLD = 200; // 触发返回的滑动距离
//    private ViewGroup viewDecor;
//    private boolean draging = false;
//    private float dx;
//    private boolean convertToTransed = false;
//    private Callback callback;
//    private SwipeUtils swipeUtils;
//    private int edgeFlag = SwipeUtils.EDGE_ALL;
//    private float scrollPercent;
//    private static final float DEFAULT_SCROLL_THRESHOLD = 0.4f;
//    private static final int OVERSCROLL_DISTANCE = 10;
//    private float scrollFinishThreshold = DEFAULT_SCROLL_THRESHOLD;
//    private int edgeTracking;
//    private static final int DEFAULT_SCRIM_COLOR = 0x99000000;
//    private float scrimOpacity;
//    private int scrimColor = DEFAULT_SCRIM_COLOR;
//    private float scale;
//    private Matrix matrix;
//    public SwipeLayout(Context context) {
//        this(context, null);
//    }
//
//    public SwipeLayout(Context context, AttributeSet attrs) {
//        super(context, attrs);
//
//        matrix=new Matrix();
//        childRect = new Rect();
//        drawable_shadow_left = getResources().getDrawable(R.drawable.shadow_left);
//        drawable_shadow_top = getResources().getDrawable(R.drawable.shadow_top);
//        drawable_shadow_right = getResources().getDrawable(R.drawable.shadow_right);
//        drawable_shadow_bottom = getResources().getDrawable(R.drawable.shadow_bottom);
////        setShadow(R.drawable.shadow_left, 0);
////        setShadow(R.drawable.shadow_right, 0);
////        setShadow(R.drawable.shadow_bottom, 0);
//        swipeUtils = SwipeUtils.create(this, new SwipeUtils.Callback() {
//            @Override
//            public boolean tryCaptureView(@NonNull View child, int pointerId) {
//                if (swipeUtils.isEdgeTouched(SwipeUtils.EDGE_LEFT, pointerId)) {
//                    edgeTracking = SwipeUtils.EDGE_LEFT;
//                    LogUtils.log("tryCaptureView", "EDGE_LEFT");
//                } else if (swipeUtils.isEdgeTouched(SwipeUtils.EDGE_TOP, pointerId)) {
//                    edgeTracking = SwipeUtils.EDGE_TOP;
//                    LogUtils.log("tryCaptureView", "EDGE_TOP");
//                } else if (swipeUtils.isEdgeTouched(SwipeUtils.EDGE_RIGHT, pointerId)) {
//                    edgeTracking = SwipeUtils.EDGE_RIGHT;
//                    LogUtils.log("tryCaptureView", "EDGE_RIGHT");
//                } else if (swipeUtils.isEdgeTouched(SwipeUtils.EDGE_BOTTOM, pointerId)) {
//                    edgeTracking = SwipeUtils.EDGE_BOTTOM;
//                    LogUtils.log("tryCaptureView", "EDGE_BOTTOM");
//                }
//                boolean dragEnable = swipeUtils.isEdgeTouched(edgeFlag, pointerId);
//                if (dragEnable && callback != null)
//                    dragEnable = callback.convertActivityToTranslucent();
//                LogUtils.log("dragEnable",dragEnable);
//                return dragEnable;
//            }
//
//            @Override
//            public int getViewHorizontalDragRange(View child) {
//                return edgeFlag & (SwipeUtils.EDGE_LEFT | SwipeUtils.EDGE_RIGHT);
//            }
//
//            @Override
//            public int getViewVerticalDragRange(View child) {
//                return edgeFlag & (SwipeUtils.EDGE_TOP | SwipeUtils.EDGE_BOTTOM);
//            }
//
//            @Override
//            public int clampViewPositionHorizontal(View child, int left, int dx) {
//                int ret = 0;
//                if ((edgeTracking & SwipeUtils.EDGE_LEFT) != 0) {
//                    LogUtils.log("clampViewPositionHorizontal","EDGE_LEFT");
//                    ret = Math.min(child.getWidth(), Math.max(left, 0));
//                } else if ((edgeTracking & SwipeUtils.EDGE_RIGHT) != 0) {
//                    LogUtils.log("clampViewPositionHorizontal","EDGE_RIGHT");
//                    ret = Math.min(0, Math.max(left, -child.getWidth()));
//                }
//                return ret;
//            }
//
//            @Override
//            public int clampViewPositionVertical(View child, int top, int dy) {
//                int ret = 0;
//                if ((edgeTracking & SwipeUtils.EDGE_TOP) != 0) {
//                    LogUtils.log("clampViewPositionVertical","EDGE_TOP");
//                    ret = Math.min(child.getHeight(), Math.max(top, 0));
//                } else if ((edgeTracking & SwipeUtils.EDGE_BOTTOM) != 0) {
//                    LogUtils.log("clampViewPositionVertical","EDGE_BOTTOM");
//                    ret = Math.min(0, Math.max(top, -child.getHeight()));
//                }
//                return ret;
//            }
//
//            @Override
//            public void onViewPositionChanged(@NonNull View changedView, int left, int top, int dx, int dy) {
//                super.onViewPositionChanged(changedView, left, top, dx, dy);
//
//                if ((edgeTracking & SwipeUtils.EDGE_LEFT) != 0) {
//                    scrollPercent = Math.abs((float) left
//                            / (contentView.getWidth() + drawable_shadow_left.getIntrinsicWidth()));
//                } else if ((edgeTracking & SwipeUtils.EDGE_TOP) != 0) {
//                    scrollPercent = Math.abs((float) top
//                            / (contentView.getHeight() + drawable_shadow_top.getIntrinsicHeight()));
//                } else if ((edgeTracking & SwipeUtils.EDGE_RIGHT) != 0) {
//                    scrollPercent = Math.abs((float) left
//                            / (contentView.getWidth() + drawable_shadow_right.getIntrinsicWidth()));
//                } else if ((edgeTracking & SwipeUtils.EDGE_BOTTOM) != 0) {
//                    scrollPercent = Math.abs((float) top
//                            / (contentView.getHeight() + drawable_shadow_bottom.getIntrinsicHeight()));
//                }
////
////                left_content = left;
////                top_content = top;
////                LogUtils.log("scrollPercent", scrollPercent);
//                invalidate();
//                if (callback != null) callback.onDragScrolled(scrollPercent);
//            }
//
//
//            @Override
//            public void onViewReleased(@NonNull View releasedChild, float xvel, float yvel) {
//                final int childWidth = releasedChild.getWidth();
//                final int childHeight = releasedChild.getHeight();
//                int left = 0, top = 0;
//                if ((edgeTracking & SwipeUtils.EDGE_LEFT) != 0) {
//                    left = xvel > 0 || xvel == 0 && scrollPercent > scrollFinishThreshold ? childWidth
//                            + drawable_shadow_left.getIntrinsicWidth() + OVERSCROLL_DISTANCE : 0;
//                } else if ((edgeTracking & SwipeUtils.EDGE_TOP) != 0) {
//                    top = yvel > 0 || yvel == 0 && scrollPercent > scrollFinishThreshold ? childHeight
//                            + drawable_shadow_bottom.getIntrinsicHeight() + OVERSCROLL_DISTANCE : 0;
//                } else if ((edgeTracking & SwipeUtils.EDGE_RIGHT) != 0) {
//                    left = xvel < 0 || xvel == 0 && scrollPercent > scrollFinishThreshold ? -(childWidth
//                            + drawable_shadow_right.getIntrinsicWidth() + OVERSCROLL_DISTANCE) : 0;
//                } else if ((edgeTracking & SwipeUtils.EDGE_BOTTOM) != 0) {
//                    top = yvel < 0 || yvel == 0 && scrollPercent > scrollFinishThreshold ? -(childHeight
//                            + drawable_shadow_bottom.getIntrinsicHeight() + OVERSCROLL_DISTANCE) : 0;
//                }
//                swipeUtils.settleCapturedViewAt(left, top);
//                invalidate();
//            }
//
//            @Override
//            public void onViewDragStateChanged(int state) {
//                super.onViewDragStateChanged(state);
//                if (callback != null) callback.onDragStateChange(state);
//            }
//
//            @Override
//            public void onEdgeTouched(int edgeFlags, int pointerId) {
//                super.onEdgeTouched(edgeFlags, pointerId);
//            }
//        });
//        swipeUtils.setEdgeTrackingEnabled(edgeFlag);
//        swipeUtils.setmEdgeSize(200);
//    }
//
//    /**
//     * onViewReleased  自动滑动
//     */
//    @Override
//    public void computeScroll() {
//        scrimOpacity = 1 - scrollPercent;
//        if (swipeUtils.continueSettling(true)) {
//            ViewCompat.postInvalidateOnAnimation(this);
//        }
//    }
//
//    @Override
//    protected void onLayout(boolean changed, int l, int t, int r, int b) {
//        if (contentView != null){
//            contentView.layout(0, 0, contentView.getMeasuredWidth(),contentView.getMeasuredHeight());
//        }
//    }
//
//
//    @Override
//    protected boolean drawChild(Canvas canvas, View child, long drawingTime) {
//        final boolean drawContent = child == contentView;
//        scale = 1 - scrollPercent;
//
//        matrix.reset();
//        matrix.postScale(scale, scale, 0.5f * contentView.getMeasuredWidth(), 0.5f * contentView.getMeasuredHeight());
//        canvas.setMatrix(matrix);
//
//        boolean ret = super.drawChild(canvas, child, drawingTime);
//        if (scrimOpacity > 0 && drawContent && swipeUtils.getViewDragState() != SwipeUtils.STATE_IDLE) {
////            drawShadow(canvas, child);
////            drawScrim(canvas, child);
//        }
//        return ret;
//    }
//
//    private void drawScrim(Canvas canvas, View child) {
//        final int baseAlpha = (scrimColor & 0xff000000) >>> 24;
//        final int alpha = (int) (baseAlpha * scrimOpacity);
//        final int color = alpha << 24 | (scrimColor & 0xffffff);
//
//        if ((edgeTracking & SwipeUtils.EDGE_LEFT) != 0) {
//            canvas.clipRect(0, 0, child.getLeft(), getHeight());
//        } else if ((edgeTracking & SwipeUtils.EDGE_RIGHT) != 0) {
//            canvas.clipRect(child.getRight(), 0, getRight(), getHeight());
//        } else if ((edgeTracking & SwipeUtils.EDGE_TOP) != 0) {
//            canvas.clipRect(child.getLeft(), 0, getRight(), child.getTop());
//        } else if ((edgeTracking & SwipeUtils.EDGE_BOTTOM) != 0) {
//            canvas.clipRect(child.getLeft(), child.getBottom(), getRight(), getHeight());
//        }
//        canvas.drawColor(color);
//    }
//
//    private void drawShadow(Canvas canvas, View child) {
//        child.getHitRect(childRect);
//        if ((edgeFlag & SwipeUtils.EDGE_LEFT) != 0) {
//            drawable_shadow_left.setBounds(childRect.left - drawable_shadow_left.getIntrinsicWidth(),
//                    childRect.top,
//                    childRect.left,
//                    childRect.bottom);
//            drawable_shadow_left.setAlpha((int) (scrimOpacity * 255));
//            drawable_shadow_left.draw(canvas);
//        }
//
//        if ((edgeFlag & SwipeUtils.EDGE_RIGHT) != 0) {
//            drawable_shadow_right.setBounds(childRect.right,
//                    childRect.top,
//                    childRect.right + drawable_shadow_right.getIntrinsicWidth(),
//                    childRect.bottom);
//            drawable_shadow_right.setAlpha((int) (scrimOpacity * 255));
//            drawable_shadow_right.draw(canvas);
//        }
//
//        if ((edgeFlag & SwipeUtils.EDGE_TOP) != 0) {
//            drawable_shadow_top.setBounds(childRect.left,
//                    childRect.top - drawable_shadow_bottom.getIntrinsicHeight(),
//                    childRect.right,
//                    childRect.top);
//            drawable_shadow_top.setAlpha((int) (scrimOpacity * 255));
//            drawable_shadow_top.draw(canvas);
//        }
//        if ((edgeFlag & SwipeUtils.EDGE_BOTTOM) != 0) {
//            drawable_shadow_bottom.setBounds(childRect.left,
//                    childRect.bottom,
//                    childRect.right,
//                    childRect.bottom + drawable_shadow_bottom.getIntrinsicHeight());
//            drawable_shadow_bottom.setAlpha((int) (scrimOpacity * 255));
//            drawable_shadow_bottom.draw(canvas);
//        }
//    }
//
//    @Override
//    public boolean onInterceptTouchEvent(MotionEvent ev) {
//        return swipeUtils.shouldInterceptTouchEvent(ev);
//    }
//
//    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//        swipeUtils.processTouchEvent(event);
//        return true;
//    }
//    public void attachActivity(Activity activity) {
//        TypedArray a = activity.getTheme().obtainStyledAttributes(new int[]{
//                android.R.attr.windowBackground
//        });
//        int background = a.getResourceId(0, 0);
//        a.recycle();
//        ViewGroup decor = (ViewGroup) activity.getWindow().getDecorView();
//        View decorChild = decor.getChildAt(0);
//        decorChild.setBackgroundResource(background);
//        decor.removeView(decorChild);
//        addView(decorChild, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
//        contentView = decorChild;
//        decor.addView(this, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
//    }
//    public void setEdgeFlag(int edgeFlag) {
//        this.edgeFlag = edgeFlag;
//    }
//
//    public void setCallback(Callback callback) {
//        this.callback = callback;
//    }
//
////    public void setShadow(int resId, int edgeFlag) {
////        setShadow(getResources().getDrawable(resId), edgeFlag);
////    }
////
////    public void setShadow(Drawable shadow, int edgeFlag) {
//////        if ((edgeFlag & EDGE_LEFT) != 0) {
////        drawable_shadow_left = shadow;
//
//    /// /        } else if ((edgeFlag & EDGE_RIGHT) != 0) {
//    /// /            mShadowRight = shadow;
//    /// /        } else if ((edgeFlag & EDGE_BOTTOM) != 0) {
//    /// /            mShadowBottom = shadow;
//    /// /        }
////        invalidate();
////    }
//    public void setContentView(View contentView) {
//        this.contentView = contentView;
//    }
//
////    public void setLeft_content(int left_content) {
////        this.left_content = left_content;
////    }
////
////    public void setTop_content(int top_content) {
////        this.top_content = top_content;
////    }
//
//    public static interface Callback {
//        public boolean convertActivityToTranslucent();
//
//        public void onDragScrolled(float scrollPercent);
//
//        public void onDragStateChange(int state);
//    }
//}
