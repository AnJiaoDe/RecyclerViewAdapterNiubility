package com.cy.rvadapterniubility.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class Selector<T> {
    public static final String NOTIFY_STATE_DRAG_SELECT = "NOTIFY_STATE_DRAG_SELECT";
    private final Callback<T> callback;
    /**
     * 注意：
     * 这里保存的是T，不是position。
     */
    private final Set<T> selected = new LinkedHashSet<>();
    private boolean usingSelector = false;
    private int maxCountSelect = -1;

    private int positionC = -1;
    @NonNull
    private RecyclerView recyclerView;

    public Selector(@NonNull Callback<T> callback) {
        this.callback = callback;
    }

    public Selector<T> setRecyclerView(@NonNull RecyclerView recyclerView) {
        this.recyclerView = recyclerView;
        return this;
    }

    public boolean isUsingSelector() {
        return usingSelector;
    }

    public boolean useSelector(int position) {
        return callback.useSelector(position);
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
    public Selector<T> startDragSelect(int position) {
        usingSelector = true;
        toggleNoNotify(position);
        callback.dispatchUpdatesToMsg(NOTIFY_STATE_DRAG_SELECT);
        return this;
    }

    /**
     * 其他地方触发，用这个
     *
     * @return
     */
    public Selector<T> startDragSelectNotify() {
        usingSelector = true;
        callback.dispatchUpdatesToMsg(NOTIFY_STATE_DRAG_SELECT);
        return this;
    }

    public Selector<T> stopDragSelectNoNotify() {
        usingSelector = false;
        clear();
        return this;
    }
    public Selector<T> stopDragSelect() {
        if (!usingSelector) return this;
        usingSelector = false;
        clear();
        callback.dispatchUpdatesToMsg(NOTIFY_STATE_DRAG_SELECT);
        return this;
    }

    public Selector<T> clearSelected() {
        if (!usingSelector) return this;
        clear();
        callback.dispatchUpdatesToMsg(NOTIFY_STATE_DRAG_SELECT);
        return this;
    }

    public int size() {
        return selected.size();
    }

    public boolean isEmpty() {
        return selected.isEmpty();
    }

    public boolean isSelected(int position) {
        T item = callback.getItem(position);
        // Item
        if (item != null) {
            return selected.contains(item);
        }
        // Header
        List<T> items = callback.getItemsByHeadPosition(position);
        if (items == null || items.isEmpty()) {
            return false;
        }
        for (T t : items) {
            if (!selected.contains(t)) {
                return false;
            }
        }
        return true;
    }

    public Selector<T> setMaxCountSelect(int maxCountSelect) {
        this.maxCountSelect = maxCountSelect;
        return this;
    }

    public int getMaxCountSelect() {
        return maxCountSelect;
    }

    public boolean isOverMaxCountSelect() {
        return maxCountSelect >= 0 && selected.size() >= maxCountSelect;
    }

    public boolean isAllSelected() {
        return selected.size() == callback.getAllCanSelectItems().size();
    }

    /**
     * 设置某个position的选择状态。
     *
     * @return true表示原状态和目标状态相同，没有变化
     */
    public boolean selectNoNotify(int position, boolean select) {
        positionC = position;
        T item = callback.getItem(position);
        if (item == null || !callback.useSelector(position)) {
            return false;
        }
        boolean oldSelected = selected.contains(item);
        if (oldSelected == select) {
            return true;
        }
        if (select) {
            select(item);
        } else {
            unselect(item);
        }
        return false;
    }

    /**
     * position只是入口。
     * Selector真正保存的是item。
     *
     * @return true表示状态发生变化
     */
    public boolean toggleNoNotify(int position) {
        positionC = position;
        T item = callback.getItem(position);
        if (item == null || !callback.useSelector(position)) {
            return false;
        }
        if (selected.contains(item)) {
            selected.remove(item);
            notifyCountChanged();
            return true;
        }
        return select(item);
    }

    /**
     * RecyclerView点击选择。
     */
    public Selector<T> toggle(int position, @NonNull RecyclerView recyclerView) {
        if (!toggleNoNotify(position)) return this;
        bindDataToView(position, recyclerView);
        return this;
    }

    /**
     * RecyclerView设置选择状态。
     */
    public Selector<T> select(int position, boolean select, @NonNull RecyclerView recyclerView) {
        T item = callback.getItem(position);
        if (item == null || !callback.useSelector(position)) {
            return this;
        }
        if (selectNoNotify(position, select)) {
            return this;
        }
        bindDataToView(position, recyclerView);
        return this;
    }

    /**
     * 范围选择。
     */
    public Selector<T> selectRange(int start, int end, boolean isSelected, @NonNull RecyclerView recyclerView) {
        for (int position = start; position <= end; position++) {
            T item = callback.getItem(position);
            if (item == null || !callback.useSelector(position)) {
                continue;
            }
            if (selectNoNotify(position, isSelected)) {
                continue;
            }
            bindDataToView(position, recyclerView);
        }
        return this;
    }

    /**
     * 全选/取消全选。
     * <p>
     * 注意这里不遍历position，
     * 因为HeadAdapter存在Header。
     */
    public Selector<T> selectAll(boolean all) {
        return selectAll(callback.getAllCanSelectItems(), all);
    }

    public Selector<T> selectHead(int headPosition, boolean all) {
        List<T> items = callback.getItemsByHeadPosition(headPosition);
        if (items == null || items.isEmpty()) return this;
        return selectAll(items, all);
    }

    private Selector<T> selectAll(@NonNull List<T> items, boolean all) {
        boolean changed = false;
        if (all) {
            for (T item : items) {
                if (item == null || selected.contains(item)) {
                    continue;
                }
                if (maxCountSelect >= 0 && selected.size() >= maxCountSelect) {
                    callback.onSelectCountOverMax(maxCountSelect);
                    break;
                }
                selected.add(item);
                changed = true;
            }
        } else {
            for (T item : items) {
                if (selected.remove(item)) {
                    changed = true;
                }
            }
        }
        if (changed) {
            notifyCountChanged();
            callback.dispatchUpdatesToMsg(NOTIFY_STATE_DRAG_SELECT);
        }
        return this;
    }

    /**
     * 直接选择Item。
     */
    public boolean select(@Nullable T item) {
        if (item == null || selected.contains(item)) {
            return false;
        }
        if (maxCountSelect >= 0 && selected.size() >= maxCountSelect) {
            callback.onSelectCountOverMax(maxCountSelect);
            return false;
        }
        selected.add(item);
        notifyCountChanged();
        return true;
    }

    /**
     * 直接取消Item。
     */
    public boolean unselect(@Nullable T item) {
        if (item == null || !selected.remove(item)) {
            return false;
        }
        notifyCountChanged();
        return true;
    }

    public boolean select(@Nullable T item, boolean select) {
        return select ? select(item) : unselect(item);
    }

    /**
     * 删除数据后调用，防止Selector保留已经不存在的Item。
     */
    public void remove(@Nullable T item) {
        if (item == null) {
            return;
        }
        if (selected.remove(item)) {
            notifyCountChanged();
        }
    }

    public void removeAll(@NonNull List<T> items) {
        boolean changed = false;
        for (T item : items) {
            if (selected.remove(item)) {
                changed = true;
            }
        }
        if (changed) {
            notifyCountChanged();
        }
    }

    public void clear() {
        if (selected.isEmpty()) {
            return;
        }
        selected.clear();
        notifyCountChanged();
    }

    @NonNull
    public Set<T> getSelected() {
        return Collections.unmodifiableSet(selected);
    }

    @NonNull
    public List<T> getSelectedList() {
        return new ArrayList<>(selected);
    }

    public int getSelectedSize(){
        return selected.size();
    }

    //直接bindDataToView比dispatchto 性能好
    private void bindDataToView(int position, @NonNull RecyclerView recyclerView) {
        BaseViewHolder baseViewHolder = (BaseViewHolder) recyclerView.findViewHolderForAdapterPosition(position);
        if (baseViewHolder == null) return;
        callback.bindItemToView(baseViewHolder, position, new ArrayList<Object>(Collections.singletonList(NOTIFY_STATE_DRAG_SELECT)));
    }

    private void notifyCountChanged() {
        int countAllCanSelect = callback.getAllCanSelectItems().size();
        callback.onSelectCountChanged(selected.size() == countAllCanSelect, selected.size(), countAllCanSelect);

        int p=callback.getHeadPosition(positionC);
        if (p < 0) return;
        BaseViewHolder holder = (BaseViewHolder) recyclerView.findViewHolderForAdapterPosition(p);
        if (holder == null) return;
        callback.bindHeadToView(holder, p, Collections.<Object>singletonList(NOTIFY_STATE_DRAG_SELECT));
    }

    public void onItemLongClick(@NonNull BaseViewHolder holder, int position) {
        callback.onItemLongClick(holder, position);
    }

    public int getItemCount() {
        return callback.getItemCount();
    }

    public void canItemClick(boolean canItemClick) {
        callback.canItemClick(canItemClick);
    }

    public interface Callback<T> {
        public void dispatchUpdatesToMsg(String notify_state_drag_select);

        /**
         * 获取指定Header对应的所有可选择Item。
         *
         * @param position Header的RecyclerView position
         */
        @Nullable
        List<T> getItemsByHeadPosition(int position);

        public int getItemCount();

        public void canItemClick(boolean canItemClick);

        /**
         * position是否允许选择。
         */
        public boolean useSelector(int position);

        /**
         * 获取Item所属的Header position。
         *
         * @param position Item的RecyclerView position
         * @return Header position，非HeaderAdapter场景返回-1
         */
        public int getHeadPosition(int position);

        /**
         * 根据RecyclerView position获取Item。
         * Header等非Item位置返回null。
         */
        @Nullable
        public T getItem(int position);

        /**
         * 直接绑定选中状态。
         */
        public void bindHeadToView(@NonNull BaseViewHolder holder, int position, @NonNull List<Object> payloads);

        /**
         * 直接绑定选中状态。
         */
        public void bindItemToView(@NonNull BaseViewHolder holder, int position, @NonNull List<Object> payloads);

        public void onItemLongClick(@NonNull BaseViewHolder holder, int position);

        /**
         * 获取所有可以被选择的Item。
         * <p>
         * SimpleAdapter：返回所有普通Item。
         * HeadAdapter：返回所有Section中的Item。
         */
        @NonNull
        public List<T> getAllCanSelectItems();

        /**
         * 选中数量变化。
         */
        public void onSelectCountChanged(boolean isAllSelected, int countSelected, int countAllCanSelect);

        public void onSelectCountOverMax(int maxCount);
    }

}