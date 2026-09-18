package com.cy.rvadapterniubility.adapter;

import android.util.SparseArray;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public abstract class DragSelectorAdapter<T> extends SimpleAdapter<T> {
    private boolean usingSelector = false;
    private MapSelector mapSelector;
    protected final String NOTIFY_STATE_DRAG_SELECT = "NOTIFY_STATE_DRAG_SELECT";
    private boolean canItemClick = true;
    private int maxCountSelect = -1;

    public DragSelectorAdapter() {
        super();
        mapSelector = new MapSelector();
    }

    public int getSelectedSize() {
        return mapSelector.size();
    }

    public boolean isUsingSelector() {
        return usingSelector;
    }

    public boolean useSelector(int itemLayoutID) {
        return !isFullSpan(itemLayoutID);
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
        mapSelector.clear();
        dispatchUpdatesToMsg(NOTIFY_STATE_DRAG_SELECT);
        return this;
    }

    public DragSelectorAdapter<T> clearSelected() {
        if (!usingSelector) return this;
        mapSelector.clear();
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

    public MapSelector getMapSelector() {
        return mapSelector;
    }

    public DragSelectorAdapter<T> selectAll(boolean isAllSelected) {
        boolean noChange = (mapSelector.size() == getList_bean().size()) == isAllSelected;
        if (noChange) return this;
        if (isAllSelected) {
            for (int i = 0; i < getList_bean().size(); i++) {
                if (!mapSelector.put(i)) break;
            }
        } else {
            mapSelector.clear();
        }
        dispatchUpdatesToMsg(NOTIFY_STATE_DRAG_SELECT);
        return this;
    }

    /**
     * @param position
     * @return 旧的选中状态是否和新的选中状态一直，用于判断是否回调bindDataToView
     */
    public boolean toggleNoNotify(final int position) {
        if (mapSelector.contains(position)) {
            return !mapSelector.remove(position);
        } else {
            return !mapSelector.put(position);
        }
    }

    public DragSelectorAdapter<T> toggle(final int position, @NonNull RecyclerView recyclerView) {
        toggleNoNotify(position);
        BaseViewHolder baseViewHolder = (BaseViewHolder) recyclerView.findViewHolderForAdapterPosition(position);
        if (baseViewHolder == null || position < 0 || position >= getList_bean().size())
            return this;
        bindDataToView(baseViewHolder, position,
                getList_bean().get(position), mapSelector.contains(position),
                new ArrayList<Object>(Collections.singletonList(NOTIFY_STATE_DRAG_SELECT)));
        return this;
    }

    /**
     * @param position
     * @param select
     * @return 旧的选中状态是否和新的选中状态一直，用于判断是否回调bindDataToView
     */
    public boolean selectNoNotify(final int position, boolean select) {
        if (select == mapSelector.contains(position)) return true;
        if (select) {
            return !mapSelector.put(position);
        } else {
            mapSelector.remove(position);
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
        return mapSelector.contains(position);
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
        bindDataToView(holder, position, bean, mapSelector.contains(position), payloads);
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

    public void onSelectCountOverMax(int max_count) {

    }

    public void canItemClick(boolean canItemClick) {
        this.canItemClick = canItemClick;
    }

    public boolean isOverMaxCountSelect() {
        return mapSelector.size() == maxCountSelect;
    }

    public class MapSelector {
        private final Map<Integer, T> map;

        public MapSelector() {
            map = new TreeMap<>();
        }

        public int size() {
            return map.size();
        }

        /**
         * @param position
         * @return true表示添加成功
         */
        public boolean put(int position) {
            if (position < 0 || position >= getList_bean().size() || !useSelector(getItemLayoutID(position, getList_bean().get(position))))
                return false;
            if (map.size() == maxCountSelect) {
                onSelectCountOverMax(maxCountSelect);
                return false;
            }
            map.put(position, getList_bean().get(position));
            notifyCountSelected();
            return true;
        }

        public boolean remove(int position) {
            map.remove(position);
            notifyCountSelected();
            return true;
        }

        public boolean contains(int position) {
            return map.get(position) != null;
        }

        public boolean clear() {
            int count_selected = map.size();
            map.clear();
            if (count_selected != 0)
                notifyCountSelected();
            return true;
        }

        private void notifyCountSelected() {
            onSelectCountChanged(getList_bean().size() == map.size(), map.size());
        }

        public Map<Integer, T> getMap() {
            return map;
        }
    }
}
