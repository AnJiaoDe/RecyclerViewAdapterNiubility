package com.cy.rvadapterniubility.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

/**
 * 日期带grid布局的   用concatadapter贼卡
 * @param <T>
 */
public abstract class GridAdapter<T> extends SimpleAdapter<T>{

    /**
     * 妙极了，再也不用手动调用putFullSpanPosition了，还及其容易出BUG
     * @param itemLayoutID
     * @return
     */
    public boolean isFullSpan(@LayoutRes int itemLayoutID) {
        return false;
    }
}
