package com.cy.rvadapterniubility.recyclerview;

import android.graphics.Rect;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Grid 间隔均分
 */
public class GridItemDecoration extends RecyclerView.ItemDecoration {

    private final int space;

    private int[] leftOffsets;
    private int[] rightOffsets;

    private int cacheSpanCount = -1;
    private int cacheSpace = -1;

    public GridItemDecoration(float space) {
        this.space = Math.round(space);
    }

    public float getSpace() {
        return space;
    }

    private void ensureOffsets(int spanCount) {
        if (spanCount <= 0) {
            return;
        }

        if (cacheSpanCount == spanCount
                && cacheSpace == space
                && leftOffsets != null
                && rightOffsets != null) {
            return;
        }

        cacheSpanCount = spanCount;
        cacheSpace = space;

        leftOffsets = new int[spanCount];
        rightOffsets = new int[spanCount];

        float perSpace = space / (float) spanCount;

        for (int i = 0; i < spanCount; i++) {
            leftOffsets[i] = Math.round((spanCount - i) * perSpace);
            rightOffsets[i] = Math.round((i + 1) * perSpace);
        }
    }

    @Override
    public void getItemOffsets(
            @NonNull Rect outRect,
            @NonNull View view,
            @NonNull RecyclerView parent,
            @NonNull RecyclerView.State state) {

        if (!(parent instanceof GridRecyclerView)) {
            outRect.set(0, 0, 0, 0);
            return;
        }

        RecyclerView.LayoutManager layoutManager =
                parent.getLayoutManager();

        if (!(layoutManager instanceof GridLayoutManager)) {
            outRect.set(0, 0, 0, 0);
            return;
        }

        GridLayoutManager gridLayoutManager =
                (GridLayoutManager) layoutManager;

        int spanCount = gridLayoutManager.getSpanCount();
        int orientation = gridLayoutManager.getOrientation();

        ensureOffsets(spanCount);

        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();

        if (!(layoutParams instanceof GridLayoutManager.LayoutParams)) {
            outRect.set(0, 0, 0, 0);
            return;
        }

        GridLayoutManager.LayoutParams params =
                (GridLayoutManager.LayoutParams) layoutParams;

        int spanIndex = params.getSpanIndex();
        int spanSize = params.getSpanSize();

        if (spanIndex < 0 || spanIndex >= spanCount) {
            outRect.set(0, 0, 0, 0);
            return;
        }

        int position = parent.getChildAdapterPosition(view);

        if (position == RecyclerView.NO_POSITION) {
            outRect.set(0, 0, 0, 0);
            return;
        }

        // FullSpan 不添加左右间距
        if (spanSize == spanCount) {
            if (orientation == RecyclerView.VERTICAL) {
                outRect.set(0, position > 0 ? space : 0, 0, space);
            } else {
                outRect.set(position > 0 ? space : 0, 0, space, 0);
            }
            return;
        }

        int left = leftOffsets[spanIndex];
        int right = rightOffsets[spanIndex];

        boolean previousIsFullSpan = false;

        int previousPosition = position - spanIndex - 1;

        if (previousPosition >= 0) {
            previousIsFullSpan =
                    ((GridRecyclerView<?>) parent)
                            .getSparseArrayFullSpan()
                            .get(previousPosition) != null;
        }

        int top = previousIsFullSpan
                ? 0
                : (position < spanCount ? space : 0);

        if (orientation == RecyclerView.VERTICAL) {
            outRect.set(left, top, right, space);
        } else {
            outRect.set(top, left, space, right);
        }
    }
}