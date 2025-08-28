package com.cy.rvadapterniubility.adapter;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.util.SparseArray;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class DragSelectorAdapter<T> extends SimpleAdapter<T> {
    private boolean usingSelector = false;
    private SparseArraySelector sparseArraySelector;
    protected final String NOTIFY_STATE_DRAG_SELECT = "NOTIFY_STATE_DRAG_SELECT";
    private boolean canItemClick = true;
    private int max_count = -1;

    public DragSelectorAdapter() {
        super();
        sparseArraySelector = new SparseArraySelector();
    }

    public int getSelectedSize() {
        return sparseArraySelector.size();
    }

    public boolean isUsingSelector() {
        return usingSelector;
    }

    /**
     * 图片选择器用这个
     */
    public void startDragSelect() {
        usingSelector = true;
    }

    /**
     * 相册长按打开选择菜单  用这个
     */
    public DragSelectorAdapter<T> startDragSelect(int position) {
        usingSelector = true;
        toggleNoNotify(position);
        dispatchUpdatesToWithMsg(NOTIFY_STATE_DRAG_SELECT);
        return this;
    }

    public DragSelectorAdapter<T> stopDragSelect() {
        if (!usingSelector) return this;
        usingSelector = false;
        sparseArraySelector.clear();
        dispatchUpdatesToWithMsg(NOTIFY_STATE_DRAG_SELECT);
        return this;
    }

    public DragSelectorAdapter<T> setMax_count(int max_count) {
        this.max_count = max_count;
        return this;
    }

    public int getMax_count() {
        return max_count;
    }

    public SparseArraySelector getSparseArraySelector() {
        return sparseArraySelector;
    }

    public DragSelectorAdapter<T> selectAll(boolean isAllSelected) {
        boolean noChange = (sparseArraySelector.size() == getList_bean().size()) == isAllSelected;
        if (noChange) return this;
        if (isAllSelected) {
            for (int i = 0; i < getList_bean().size(); i++) {
                if (!sparseArraySelector.put(i)) break;
            }
        } else {
            sparseArraySelector.clear();
        }
        dispatchUpdatesToWithMsg(NOTIFY_STATE_DRAG_SELECT);
        return this;
    }

    /**
     * @param position
     * @return 旧的选中状态是否和新的选中状态一直，用于判断是否回调bindDataToView
     */
    public boolean toggleNoNotify(final int position) {
        if (sparseArraySelector.contains(position)) {
            return !sparseArraySelector.remove(position);
        } else {
            return !sparseArraySelector.put(position);
        }
    }

    public DragSelectorAdapter<T> toggle(final int position, @NonNull RecyclerView recyclerView) {
        toggleNoNotify(position);
        BaseViewHolder baseViewHolder = (BaseViewHolder) recyclerView.findViewHolderForAdapterPosition(position);
        if (baseViewHolder == null || position < 0 || position >= getList_bean().size())
            return this;
        bindDataToView(baseViewHolder, position,
                getList_bean().get(position), sparseArraySelector.contains(position),
                new ArrayList<Object>(Collections.singletonList(NOTIFY_STATE_DRAG_SELECT)));
        return this;
    }

    /**
     * @param position
     * @param select
     * @return 旧的选中状态是否和新的选中状态一直，用于判断是否回调bindDataToView
     */
    public boolean selectNoNotify(final int position, boolean select) {
        if (select == sparseArraySelector.contains(position)) return true;
        if (select) {
            return !sparseArraySelector.put(position);
        } else {
            sparseArraySelector.remove(position);
        }
        return false;
    }

    public DragSelectorAdapter<T> select(final int position, boolean select, @NonNull RecyclerView recyclerView) {
        if (position < 0 || position >= getList_bean().size() || selectNoNotify(position, select))
            return this;
        BaseViewHolder baseViewHolder = (BaseViewHolder) recyclerView.findViewHolderForAdapterPosition(position);
        if (baseViewHolder == null) return this;
        //不刷新，防止闪烁（选择的时候，一般会加蒙版，刷新会导致蒙版闪烁厉害）， 直接回调bindDataToView
        bindDataToView(baseViewHolder, position, getList_bean().get(position), select,
                new ArrayList<Object>(Collections.singletonList(NOTIFY_STATE_DRAG_SELECT)));
        return this;
    }

    public boolean isSelected(int position) {
        return sparseArraySelector.contains(position);
    }

    /**
     * 直接notifyitemchange是肯定不灵的，会导致间隔均分失败，如果有loadMore布局，会出现item占满一行的情况，GG
     * 然而必须注意：有loadMore时，会导致findViewHolderForAdapterPosition 出来的BaseViewHolder是复用的loadMore的，故而在使用时，如果有LOADMORE，
     * 必须手动判断BaseViewHolder里的布局是不是正常的（可以直接设置tag，然后判断tag）
     *
     * @param start
     * @param end
     * @param isSelected
     * @param recyclerView
     */
    public DragSelectorAdapter<T> selectRange(final int start, final int end, boolean isSelected, @NonNull RecyclerView recyclerView) {
//        LogUtils.log("selectRange", start + ":" + end + ":" + isSelected);
        for (int i = start; i <= end; i++) {
            if (i < 0 || i >= getList_bean().size() || selectNoNotify(i, isSelected)) continue;
//        LogUtils.log("selectRange", i + ":" + isSelected);
            BaseViewHolder baseViewHolder = (BaseViewHolder) recyclerView.findViewHolderForAdapterPosition(i);
            if (baseViewHolder == null) continue;
            bindDataToView(baseViewHolder, i, getList_bean().get(i), isSelected, new ArrayList<Object>(Collections.singletonList(NOTIFY_STATE_DRAG_SELECT)));
        }
        return this;
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
    public final void onItemLongClick(@NonNull BaseViewHolder holder, int position, T bean) {

    }

    public abstract void onItemLongClick__(BaseViewHolder holder, int position, T bean);

    /**
     * @param holder
     * @param position
     * @param bean
     */
    @Override
    public final void onItemClick(@NonNull BaseViewHolder holder, int position, T bean) {
        if (!canItemClick) return;
        onItemClick__(holder, position, bean);
    }

    public abstract void onItemClick__(BaseViewHolder holder, int position, T bean);

    public abstract void onSelectCountChanged(boolean isAllSelected, int count_selected);

    public  void onSelectCountOverMax(){}

    public void canItemClick(boolean canItemClick) {
        this.canItemClick = canItemClick;
    }

    public boolean overMaxCount(){
        return sparseArraySelector.size()==max_count;
    }
    public class SparseArraySelector {
        private final SparseArray<T> sparseArray;

        public SparseArraySelector() {
            sparseArray = new SparseArray<>();
        }

        public int size() {
            return sparseArray.size();
        }

        /**
         * @param position
         * @return true表示添加成功
         */
        public boolean put(int position) {
            if (position < 0 || position >= getList_bean().size()) return false;
            if (sparseArray.size() == max_count) {
                onSelectCountOverMax();
                return false;
            }
            sparseArray.put(position, getList_bean().get(position));
            notifyCountSelected();
            return true;
        }

        public boolean remove(int position) {
            sparseArray.remove(position);
            notifyCountSelected();
            return true;
        }

        public boolean contains(int position) {
            return sparseArray.get(position) != null;
        }

        public boolean clear() {
            int count_selected = sparseArray.size();
            sparseArray.clear();
            if (count_selected != 0)
                notifyCountSelected();
            return true;
        }

        private void notifyCountSelected() {
            onSelectCountChanged(getList_bean().size() == sparseArray.size(), sparseArray.size());
        }

        public SparseArray<T> getSparseArray() {
            return sparseArray;
        }
    }
}
