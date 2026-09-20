package com.cy.rvadapterniubility.adapter;

import android.util.SparseArray;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public abstract class DragSelectorAdapter<T> extends SimpleAdapter<T> {
    private boolean usingSelector = false;
    private Selector selector;
    protected final String NOTIFY_STATE_DRAG_SELECT = "NOTIFY_STATE_DRAG_SELECT";
    private boolean canItemClick = true;
    private int maxCountSelect = -1;
    private Set<Integer> setNoUseSelector;

    public DragSelectorAdapter() {
        super();
        selector = new Selector();
        setNoUseSelector = new HashSet<>();
    }

    public int getSelectedSize() {
        return selector.size();
    }

    public boolean isUsingSelector() {
        return usingSelector;
    }

    public boolean useSelector(int itemLayoutID) {
        return !isFullSpan(itemLayoutID);
    }

    @Override
    public int getItemViewType(int position) {
        int itemType = super.getItemViewType(position);
        if (!useSelector(itemType)) setNoUseSelector.add(position);
        return itemType;
    }

    /**
     * @return 有多少个item 不用selector
     */
    public final int getNoUseSelectorCount() {
        return setNoUseSelector.size();
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
        dispatchUpdatesToMsg(NOTIFY_STATE_DRAG_SELECT);
        return this;
    }

    /**
     * 其他地方触发，用这个
     *
     * @return
     */
    public DragSelectorAdapter<T> startDragSelectNotify() {
        usingSelector = true;
        dispatchUpdatesToMsg(NOTIFY_STATE_DRAG_SELECT);
        return this;
    }

    public DragSelectorAdapter<T> stopDragSelect() {
        if (!usingSelector) return this;
        usingSelector = false;
        selector.clear();
        dispatchUpdatesToMsg(NOTIFY_STATE_DRAG_SELECT);
        return this;
    }

    public DragSelectorAdapter<T> clearSelected() {
        if (!usingSelector) return this;
        selector.clear();
        dispatchUpdatesToMsg(NOTIFY_STATE_DRAG_SELECT);
        return this;
    }

    public DragSelectorAdapter<T> setMaxCountSelect(int maxCountSelect) {
        this.maxCountSelect = maxCountSelect;
        return this;
    }

    public int getMaxCountSelect() {
        return maxCountSelect;
    }

    public Selector getSelector() {
        return selector;
    }

    public boolean isAllSelected() {
        return selector.size() == getList_bean().size() - getNoUseSelectorCount();
    }

    public DragSelectorAdapter<T> selectAll(boolean isAllSelected) {
        boolean noChange = isAllSelected() == isAllSelected;
        if (noChange) return this;
        if (isAllSelected) {
            for (int i = 0; i < getList_bean().size(); i++) {
                if (!selector.put(i) && isOverMaxCountSelect()) break;
            }
        } else {
            selector.clear();
        }
        dispatchUpdatesToMsg(NOTIFY_STATE_DRAG_SELECT);
        return this;
    }

    /**
     * @param position
     * @return 旧的选中状态是否和新的选中状态一直，用于判断是否回调bindDataToView
     */
    public boolean toggleNoNotify(final int position) {
        if (selector.contains(position)) {
            return !selector.remove(position);
        } else {
            return !selector.put(position);
        }
    }

    public DragSelectorAdapter<T> toggle(final int position, @NonNull RecyclerView recyclerView) {
        toggleNoNotify(position);
        BaseViewHolder baseViewHolder = (BaseViewHolder) recyclerView.findViewHolderForAdapterPosition(position);
        if (baseViewHolder == null || position < 0 || position >= getList_bean().size())
            return this;
        bindDataToView(baseViewHolder, position,
                getList_bean().get(position), selector.contains(position),
                new ArrayList<Object>(Collections.singletonList(NOTIFY_STATE_DRAG_SELECT)),getIndexFullSpan(position));
        return this;
    }

    /**
     * @param position
     * @param select
     * @return 旧的选中状态是否和新的选中状态一直，用于判断是否回调bindDataToView
     */
    public boolean selectNoNotify(final int position, boolean select) {
        if (select == selector.contains(position)) return true;
        if (select) {
            return !selector.put(position);
        } else {
            selector.remove(position);
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
                new ArrayList<Object>(Collections.singletonList(NOTIFY_STATE_DRAG_SELECT)), getIndexFullSpan(position));
        return this;
    }

    public boolean isSelected(int position) {
        return selector.contains(position);
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
            bindDataToView(baseViewHolder, i, getList_bean().get(i), isSelected, new ArrayList<Object>(Collections.singletonList(NOTIFY_STATE_DRAG_SELECT)),
                    getIndexFullSpan(i));
        }
        return this;
    }

    @Override
    public final void bindDataToView(@NonNull BaseViewHolder holder, int position, T bean, @NonNull List<Object> payloads, int indexFullSpan) {
        bindDataToView(holder, position, bean, selector.contains(position), payloads, indexFullSpan);
    }

    public abstract void bindDataToView(@NonNull BaseViewHolder holder, int position, T bean, boolean isSelected, @NonNull List<Object> payloads, int indexFullSpan);

    /**
     * ----------------这个不能在使用的时候实现了，否则会导致回调2次长按事件，因为在DragRecyclerView中也做了长按回调------------------------------------------------------------------
     */

    @Override
    public final void onItemLongClick(@NonNull BaseViewHolder holder, int position, T bean, int indexFullSpan) {

    }

    public abstract void onItemLongClick__(@NonNull BaseViewHolder holder, int position, T bean, int indexFullSpan);

    /**
     * @param holder
     * @param position
     * @param bean
     */
    @Override
    public final void onItemClick(@NonNull BaseViewHolder holder, int position, T bean, int indexFullSpan) {
        if (!canItemClick) return;
        onItemClick__(holder, position, bean,indexFullSpan);
    }

    public abstract void onItemClick__(@NonNull BaseViewHolder holder, int position, T bean,int indexFullSpan);

    public abstract void onSelectCountChanged(boolean isAllSelected, int count_selected);

    public void onSelectCountOverMax(int max_count) {

    }

    public void canItemClick(boolean canItemClick) {
        this.canItemClick = canItemClick;
    }

    public boolean isOverMaxCountSelect() {
        return selector.size() == maxCountSelect;
    }

    public class Selector {
        private final TreeMap<Integer, T> treeMap;

        public Selector() {
            treeMap = new TreeMap<>();
        }

        public int size() {
            return treeMap.size();
        }

        /**
         * @param position
         * @return true表示添加成功
         */
        public boolean put(int position) {
            if (position < 0 || position >= getList_bean().size() || !useSelector(getItemLayoutID(position, getList_bean().get(position))))
                return false;
            if (treeMap.size() == maxCountSelect) {
                onSelectCountOverMax(maxCountSelect);
                return false;
            }
            treeMap.put(position, getList_bean().get(position));
            notifyCountSelected();
            return true;
        }

        public boolean remove(int position) {
            treeMap.remove(position);
            notifyCountSelected();
            return true;
        }

        public boolean contains(int position) {
            return treeMap.get(position) != null;
        }

        public boolean clear() {
            int count_selected = treeMap.size();
            treeMap.clear();
            if (count_selected != 0)
                notifyCountSelected();
            return true;
        }

        private void notifyCountSelected() {
            onSelectCountChanged(isAllSelected(), treeMap.size());
        }

        public TreeMap<Integer, T> getMap() {
            return treeMap;
        }
    }
}
