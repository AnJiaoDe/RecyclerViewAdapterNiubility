package com.cy.rvadapterniubility.adapter;

import android.os.Handler;
import android.util.SparseArray;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.cy.rvadapterniubility.LogUtils;
import com.cy.rvadapterniubility.ThreadUtils;
import com.cy.rvadapterniubility.recyclerview.DragSelectRecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class DragSelectorAdapter<T> extends SimpleAdapter<T> {
    private boolean usingSelector = false;
    private SparseArraySelector sparseArraySelector;
    protected final String NOTIFY_STATE_DRAG_SELECT = "NOTIFY_STATE_DRAG_SELECT";

    public DragSelectorAdapter() {
        sparseArraySelector = new SparseArraySelector();
    }

    public int getSelectedSize() {
        return sparseArraySelector.size();
    }

    public boolean isUsingSelector() {
        return usingSelector;
    }

    public void startDragSelect(int position) {
        boolean b = usingSelector;
        usingSelector = true;
        toggleNoNotify(position);
        if (b) return;
        dispatchUpdatesToWithMsg(NOTIFY_STATE_DRAG_SELECT);
    }

    public void stopDragSelect() {
        if (!usingSelector) return;
        usingSelector = false;
        sparseArraySelector.clear();
        dispatchUpdatesToWithMsg(NOTIFY_STATE_DRAG_SELECT);
    }

    public SparseArraySelector getSparseArraySelector() {
        return sparseArraySelector;
    }

    public void selectAll(boolean isAllSelected) {
        boolean noChange = (sparseArraySelector.size() == getList_bean().size()) == isAllSelected;
        if (noChange) return;
        if (isAllSelected) {
            for (int i = 0; i < getList_bean().size(); i++) {
                sparseArraySelector.put(i);
            }
        } else {
            sparseArraySelector.clear();
        }
        dispatchUpdatesToWithMsg(NOTIFY_STATE_DRAG_SELECT);
    }

    public void toggleNoNotify(final int position) {
        if (sparseArraySelector.contains(position)) {
            sparseArraySelector.remove(position);
        } else {
            sparseArraySelector.put(position);
        }
    }

    public void toggle(final int position, @NonNull RecyclerView recyclerView) {
        toggleNoNotify(position);
        bindDataToView((BaseViewHolder) recyclerView.findViewHolderForAdapterPosition(position), position,
                getList_bean().get(position), sparseArraySelector.contains(position),
                new ArrayList<Object>(Collections.singletonList(NOTIFY_STATE_DRAG_SELECT)));
    }

    /**
     * @param position
     * @param select
     * @return 旧的选中状态是否和新的选中状态一直，用于判断是否回调bindDataToView
     */
    public boolean selectNoNotify(final int position, boolean select) {
        if (select == sparseArraySelector.contains(position)) return true;
        if (select) {
            sparseArraySelector.put(position);
        } else {
            sparseArraySelector.remove(position);
        }
        return false;
    }

    public void select(final int position, boolean select, @NonNull RecyclerView recyclerView) {
        if (selectNoNotify(position, select)) return;
        //不刷新，防止闪烁（选择的时候，一般会加蒙版，刷新会导致蒙版闪烁厉害）， 直接回调bindDataToView
        bindDataToView((BaseViewHolder) recyclerView.findViewHolderForAdapterPosition(position),
                position, getList_bean().get(position), select,
                new ArrayList<Object>(Collections.singletonList(NOTIFY_STATE_DRAG_SELECT)));
    }

    public boolean isSelected(int position) {
        return sparseArraySelector.contains(position);
    }

    public void selectRange(final int start, final int end, boolean isSelected, @NonNull RecyclerView recyclerView) {
        LogUtils.log("selectRange",start);
        for (int i = start; i <= end; i++) {
            if (selectNoNotify(i, isSelected)) continue;
            bindDataToView((BaseViewHolder) recyclerView.findViewHolderForAdapterPosition(i), i, getList_bean().get(i),
                    isSelected, new ArrayList<Object>(Collections.singletonList(NOTIFY_STATE_DRAG_SELECT)));
        }
    }

    @Override
    public final void bindDataToView(BaseViewHolder holder, int position, T bean, @NonNull List<Object> payloads) {
        bindDataToView(holder, position, bean, sparseArraySelector.contains(position), payloads);
    }

    public abstract void bindDataToView(@NonNull BaseViewHolder holder, int position, T bean, boolean isSelected, @NonNull List<Object> payloads);

    /**
     * ----------------这个不能在使用的时候实现了，否则会导致回调2次长按事件，因为在DragRecyclerView中也做了长按回调------------------------------------------------------------------
     */
    @Override
    public final void onItemLongClick(BaseViewHolder holder, int position, T bean) {

    }

    public abstract void onItemLongClick__(BaseViewHolder holder, int position, T bean);

    public abstract void onSelectCountChanged(boolean isAllSelected, int count_selected);

    public class SparseArraySelector {
        private final SparseArray<T> sparseArray;

        public SparseArraySelector() {
            sparseArray = new SparseArray<>();
        }

        public int size() {
            return sparseArray.size();
        }

        public void put(int position) {
            sparseArray.put(position, getList_bean().get(position));
            notifyCountSelected();
        }

        public void remove(int position) {
            sparseArray.remove(position);
            notifyCountSelected();
        }

        public boolean contains(int position) {
            return sparseArray.get(position) != null;
        }

        public void clear() {
            int count_selected = sparseArray.size();
            sparseArray.clear();
            if (count_selected != 0)
                notifyCountSelected();
        }

        private void notifyCountSelected() {
            onSelectCountChanged(getList_bean().size() == sparseArray.size(), sparseArray.size());
        }

        public SparseArray<T> getSparseArray() {
            return sparseArray;
        }
    }
}
