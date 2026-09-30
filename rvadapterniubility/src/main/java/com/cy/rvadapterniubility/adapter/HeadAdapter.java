package com.cy.rvadapterniubility.adapter;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;

import androidx.annotation.CallSuper;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.cy.rvadapterniubility.R;
import com.cy.rvadapterniubility.ThreadUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * 带Header分组的Adapter。
 * 数据结构：
 * Header
 * Item
 * Item
 * Item
 * Header
 * Item
 * Item
 * RecyclerView内部仍然使用扁平position。
 * 业务层使用 Header + Item 操作。
 *
 * @param <H> Header类型
 * @param <T> Item类型
 */
public abstract class HeadAdapter<H, T> extends RecyclerView.Adapter<BaseViewHolder> {
    /**
     * Section数据。
     */
    private final List<Section<H, T>> sections = new ArrayList<>();
    /**
     * RecyclerView实际使用的扁平数据。
     */
    private final List<Node<H, T>> nodes = new ArrayList<>();
    private Selector<T> selector;
    private boolean canItemClick = true;

    public HeadAdapter() {
        selector = new Selector<>(new Selector.Callback<T>() {
            @Override
            public void dispatchUpdatesToMsg(String notify_state_drag_select) {
                HeadAdapter.this.dispatchUpdatesToMsg(notify_state_drag_select);
            }

            @Override
            public int getItemCount() {
                return HeadAdapter.this.getItemCount();
            }

            @Override
            public void canItemClick(boolean canItemClick) {
                HeadAdapter.this.canItemClick = canItemClick;
            }

            @Override
            public boolean useSelector(int position) {
                return !HeadAdapter.this.isFullSpan(position);
            }

            @Nullable
            @Override
            public T getItem(int position) {
                return HeadAdapter.this.getItemByPosition(position);
            }

            @Override
            public void bindDataToView(@NonNull BaseViewHolder holder, int position, @NonNull List<Object> payloads) {
                HeadAdapter.this.bindItemToView(holder, position, HeadAdapter.this.getItemByPosition(position), payloads);
            }

            @Override
            public void onItemLongClick(@NonNull BaseViewHolder holder, int position) {
                Node<H, T> node = nodes.get(position);
                if (node.type == Node.TYPE_HEAD) {
                    HeadAdapter.this.onHeadLongClick(holder, position, node.head);
                } else {
                    HeadAdapter.this.onItemLongClick(holder, position, node.item);
                }
            }

            @NonNull
            @Override
            public List<T> getAllCanSelectItems() {
                return HeadAdapter.this.getAllCanSelectItems();
            }

            @Override
            public void onSelectCountChanged(boolean isAllSelected, int countSelected, int countAllCanSelect) {
                HeadAdapter.this.onSelectCountChanged(isAllSelected, countSelected, countAllCanSelect);
            }

            @Override
            public void onSelectCountOverMax(int maxCount) {
                HeadAdapter.this.onSelectCountOverMax(maxCount);
            }
        });
    }

    public Selector<T> getSelector() {
        return selector;
    }

    public static class Section<H, T> {
        private H head;
        private final List<T> items = new ArrayList<>();

        public Section(@NonNull H head) {
            this.head = head;
        }

        public Section(@NonNull H head, @NonNull List<T> items) {
            this.head = head;
            this.items.addAll(items);
        }

        @NonNull
        public H getHead() {
            return head;
        }

        public void setHead(@NonNull H head) {
            this.head = head;
        }

        @NonNull
        public List<T> getItems() {
            return items;
        }
    }

    /**
     * RecyclerView的一行。
     */
    private static class Node<H, T> {
        static final int TYPE_HEAD = 0;
        static final int TYPE_ITEM = 1;
        final int type;
        final H head;
        final T item;
        final int sectionIndex;
        final int itemIndex;

        private Node(int type, @Nullable H head, @Nullable T item, int sectionIndex, int itemIndex) {
            this.type = type;
            this.head = head;
            this.item = item;
            this.sectionIndex = sectionIndex;
            this.itemIndex = itemIndex;
        }

        static <H, T> Node<H, T> head(H head, int sectionIndex) {
            return new Node<>(TYPE_HEAD, head, null, sectionIndex, -1);
        }

        static <H, T> Node<H, T> item(T item, int sectionIndex, int itemIndex) {
            return new Node<>(TYPE_ITEM, null, item, sectionIndex, itemIndex);
        }
    }

    /**
     * position信息。
     */
    public static class PositionInfo<H, T> {
        private final boolean head;
        private final H headData;
        private final T item;
        private final int sectionIndex;
        private final int itemIndex;
        private final int adapterPosition;

        private PositionInfo(boolean head, @Nullable H headData, @Nullable T item, int sectionIndex, int itemIndex, int adapterPosition) {
            this.head = head;
            this.headData = headData;
            this.item = item;
            this.sectionIndex = sectionIndex;
            this.itemIndex = itemIndex;
            this.adapterPosition = adapterPosition;
        }

        public boolean isHead() {
            return head;
        }

        public boolean isItem() {
            return !head;
        }

        @Nullable
        public H getHead() {
            return headData;
        }

        @Nullable
        public T getItem() {
            return item;
        }

        public int getSectionIndex() {
            return sectionIndex;
        }

        public int getItemIndex() {
            return itemIndex;
        }

        public int getAdapterPosition() {
            return adapterPosition;
        }
    }

    // =========================================================================
    // RecyclerView
    // =========================================================================

    @NonNull
    @Override
    public final BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new BaseViewHolder(LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false));
    }

    @Override
    public final void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
    }

    @Override
    public final void onBindViewHolder(@NonNull BaseViewHolder holder, int position, @NonNull List<Object> payloads) {
        recycleData(holder.getTag());
        handleClick(holder);
        if (position < 0 || position >= nodes.size()) {
            return;
        }
        Node<H, T> node = nodes.get(position);
        holder.setTag(setHolderTagPreBindData(holder, position, node));
        if (node.type == Node.TYPE_HEAD) {
            bindHeadToView(holder, position, node.head, payloads);
        } else {
            bindItemToView(holder, position, node.item, payloads);
        }
    }

    @Override
    public final int getItemViewType(int position) {
        if (position < 0 || position >= nodes.size()) {
            return R.layout.cy_staggerd_item_0;
        }
        Node<H, T> node = nodes.get(position);
        if (node.type == Node.TYPE_HEAD) {
            return getHeadLayoutID(position, node.head);
        }
        return getItemLayoutID(position, node.item);
    }

    @Override
    public final int getItemCount() {
        return nodes.size();
    }

    // =========================================================================
    // Layout
    // =========================================================================

    @LayoutRes
    public abstract int getHeadLayoutID(int position, H head);

    @LayoutRes
    public abstract int getItemLayoutID(int position, T item);

    /**
     * Header是否FullSpan。
     * Header固定FullSpan，Item固定非FullSpan。
     */
    public boolean isFullSpan(int position) {
        return nodes.get(position).type == Node.TYPE_HEAD;
    }

    // =========================================================================
    // Bind
    // =========================================================================

    public abstract void bindHeadToView(@NonNull BaseViewHolder holder, int position, H head, @NonNull List<Object> payloads);

    public abstract void bindItemToView(@NonNull BaseViewHolder holder, int position, T item, @NonNull List<Object> payloads);

    // =========================================================================
    // Click
    // =========================================================================

    protected void handleClick(@NonNull final BaseViewHolder holder) {
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getBindingAdapterPosition();
                if (position < 0 || position >= nodes.size()) {
                    return;
                }
                Node<H, T> node = nodes.get(position);
                if (node.type == Node.TYPE_HEAD) {
                    onHeadClick(holder, position, node.head);
                } else {
                    if (!canItemClick) return;
                    onItemClick(holder, position, node.item);
                }
            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                //滑动选择极其容易触发onLongClick，滑动选择模式下，onItemLongClick已经被recycler接管
                if (selector.isUsingSelector()) return false;

                int position = holder.getBindingAdapterPosition();
                if (position < 0 || position >= nodes.size()) {
                    return false;
                }
                Node<H, T> node = nodes.get(position);
                if (node.type == Node.TYPE_HEAD) {
                    onHeadLongClick(holder, position, node.head);
                } else {
                    onItemLongClick(holder, position, node.item);
                }
                return true;
            }
        });
    }

    public void onHeadClick(@NonNull BaseViewHolder holder, int position, H head) {
    }

    public abstract void onItemClick(@NonNull BaseViewHolder holder, int position, T item);

    public void onHeadLongClick(@NonNull BaseViewHolder holder, int position, H head) {
    }

    public void onItemLongClick(@NonNull BaseViewHolder holder, int position, T item) {
    }

    public void onSelectCountChanged(boolean isAllSelected, int countSelected, int countAllCanSelect) {

    }

    public void onSelectCountOverMax(int maxCount) {

    }
    // =========================================================================
    // Recycle / Tag
    // =========================================================================

    @CallSuper
    @Override
    public void onViewDetachedFromWindow(@NonNull BaseViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        recycleData(holder.getTag());
    }

    /**
     * 回收holder对应的数据。
     */
    public void recycleData(@Nullable Object tag) {
    }

    /**
     * 给holder设置TAG。
     */
    @Nullable
    public Object setHolderTagPreBindData(@NonNull BaseViewHolder holder, int position, @NonNull Node<H, T> node) {
        return null;
    }

    // =========================================================================
    // Move
    // =========================================================================

    public final void onItemMove__(int fromPosition, int toPosition, @NonNull RecyclerView.ViewHolder srcHolder, @NonNull RecyclerView.ViewHolder targetHolder) {
        onItemMove(fromPosition, toPosition, (BaseViewHolder) srcHolder, (BaseViewHolder) targetHolder);
    }

    public void onItemMove(int fromPosition, int toPosition, @NonNull BaseViewHolder srcHolder, @NonNull BaseViewHolder targetHolder) {
    }

    // =========================================================================
    // Animation
    // =========================================================================

    public void startDefaultAttachedAnim(@NonNull BaseViewHolder holder) {

        ObjectAnimator scaleX = ObjectAnimator.ofFloat(holder.itemView, "scaleX", 0.5f, 1);

        ObjectAnimator scaleY = ObjectAnimator.ofFloat(holder.itemView, "scaleY", 0.5f, 1);

        ObjectAnimator alpha = ObjectAnimator.ofFloat(holder.itemView, "alpha", 0.5f, 1);

        AnimatorSet animatorSet = new AnimatorSet();

        animatorSet.setDuration(1000);

        animatorSet.playTogether(scaleX, scaleY, alpha);

        animatorSet.setInterpolator(new DecelerateInterpolator());

        animatorSet.start();
    }

    // =========================================================================
    // Notify
    // =========================================================================

    public void postNotifyDataSetChanged() {
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                notifyDataSetChanged();
            }
        });
    }

    // =========================================================================
    // DiffUtil
    // =========================================================================

    public void dispatchUpdatesTo(@NonNull final List<Section<H, T>> newSections, @Nullable final CallbackDiff callbackDiff) {
        final List<Section<H, T>> oldSections = copySections(sections);
        ThreadUtils.getInstance().runThread(new ThreadUtils.RunnableCallback<DiffUtil.DiffResult>() {
            @Override
            public DiffUtil.DiffResult runThread() {
                final List<Node<H, T>> oldNodes = buildNodes(oldSections);
                final List<Node<H, T>> newNodes = buildNodes(newSections);
                return DiffUtil.calculateDiff(new DiffUtil.Callback() {
                    @Override
                    public int getOldListSize() {
                        return oldNodes.size();
                    }

                    @Override
                    public int getNewListSize() {
                        return newNodes.size();
                    }

                    @Override
                    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                        Node<H, T> oldNode = oldNodes.get(oldItemPosition);
                        Node<H, T> newNode = newNodes.get(newItemPosition);
                        if (oldNode.type != newNode.type) {
                            return false;
                        }
                        if (oldNode.type == Node.TYPE_HEAD) {
                            return HeadAdapter.this.areHeadsTheSame(oldNode.head, newNode.head, oldItemPosition, newItemPosition);
                        }
                        return HeadAdapter.this.areItemsTheSame(oldNode.item, newNode.item, oldItemPosition, newItemPosition);
                    }

                    @Override
                    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                        Node<H, T> oldNode = oldNodes.get(oldItemPosition);
                        Node<H, T> newNode = newNodes.get(newItemPosition);
                        if (oldNode.type == Node.TYPE_HEAD) {
                            return areHeadsContentsTheSame(oldNode.head, newNode.head, oldItemPosition, newItemPosition);
                        }
                        return HeadAdapter.this.areContentsTheSame(oldNode.item, newNode.item, oldItemPosition, newItemPosition);
                    }

                    @Nullable
                    @Override
                    public Object getChangePayload(int oldItemPosition, int newItemPosition) {
                        Node<H, T> oldNode = oldNodes.get(oldItemPosition);
                        Node<H, T> newNode = newNodes.get(newItemPosition);
                        if (oldNode.type == Node.TYPE_HEAD) {
                            return getHeadChangePayload(oldNode.head, newNode.head, oldItemPosition, newItemPosition);
                        }
                        return HeadAdapter.this.getChangePayload(oldNode.item, newNode.item, oldItemPosition, newItemPosition);
                    }
                });
            }

            @Override
            public void runUIThread(DiffUtil.DiffResult diffResult) {
                sections.clear();
                sections.addAll(copySections(newSections));
                rebuildNodes();
                diffResult.dispatchUpdatesTo(HeadAdapter.this);
                if (callbackDiff != null) {
                    callbackDiff.onDispatchUpdated();
                }
            }
        });
    }

    /**
     * 兼容原SimpleAdapter思想：
     * 数据结构发生增删移动时直接刷新。
     */
    public void dispatchUpdatesToItemDecoration(@NonNull final List<Section<H, T>> newSections, @Nullable final CallbackDiff callbackDiff) {

        ThreadUtils.getInstance().runThread(new ThreadUtils.RunnableCallback<Boolean>() {

            @Override
            public Boolean runThread() {

                List<Node<H, T>> oldNodes = new ArrayList<>(nodes);

                List<Node<H, T>> newNodes = buildNodes(newSections);

                if (oldNodes.size() != newNodes.size()) {
                    return true;
                }

                for (int i = 0; i < oldNodes.size(); i++) {

                    Node<H, T> oldNode = oldNodes.get(i);

                    Node<H, T> newNode = newNodes.get(i);

                    if (oldNode.type != newNode.type) {
                        return true;
                    }

                    if (oldNode.type == Node.TYPE_HEAD) {

                        if (!areHeadsTheSame(oldNode.head, newNode.head, i, i)) {
                            return true;
                        }

                    } else {

                        if (!areItemsTheSame(oldNode.item, newNode.item, i, i)) {
                            return true;
                        }
                    }
                }

                return false;
            }

            @Override
            public void runUIThread(Boolean refresh) {

                if (refresh) {
                    setSectionsNoNotify(newSections);

                    notifyDataSetChanged();

                    if (callbackDiff != null) {
                        callbackDiff.onDispatchUpdated();
                    }

                } else {

                    dispatchUpdatesTo(newSections, callbackDiff);
                }
            }
        });
    }

    // =========================================================================
    // Payload
    // =========================================================================

    public void dispatchUpdatesToMsg(final Object msg) {

        ThreadUtils.getInstance().runThread(new ThreadUtils.RunnableCallback<DiffUtil.DiffResult>() {

            @Override
            public DiffUtil.DiffResult runThread() {

                return DiffUtil.calculateDiff(new DiffUtil.Callback() {

                    @Override
                    public int getOldListSize() {
                        return nodes.size();
                    }

                    @Override
                    public int getNewListSize() {
                        return nodes.size();
                    }

                    @Override
                    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                        return true;
                    }

                    @Override
                    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                        return false;
                    }

                    @Nullable
                    @Override
                    public Object getChangePayload(int oldItemPosition, int newItemPosition) {
                        return msg;
                    }
                });
            }

            @Override
            public void runUIThread(DiffUtil.DiffResult diffResult) {
                diffResult.dispatchUpdatesTo(HeadAdapter.this);
            }
        });
    }

    public <Msg> void dispatchUpdatesToMsg(final Map<Integer, Msg> mapMsg) {

        ThreadUtils.getInstance().runThread(new ThreadUtils.RunnableCallback<DiffUtil.DiffResult>() {

            @Override
            public DiffUtil.DiffResult runThread() {

                return DiffUtil.calculateDiff(new DiffUtil.Callback() {

                    @Override
                    public int getOldListSize() {
                        return nodes.size();
                    }

                    @Override
                    public int getNewListSize() {
                        return nodes.size();
                    }

                    @Override
                    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                        return true;
                    }

                    @Override
                    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                        return mapMsg.containsKey(newItemPosition) ? false : true;
                    }

                    @Nullable
                    @Override
                    public Object getChangePayload(int oldItemPosition, int newItemPosition) {
                        return mapMsg.get(newItemPosition);
                    }
                });
            }

            @Override
            public void runUIThread(DiffUtil.DiffResult diffResult) {
                diffResult.dispatchUpdatesTo(HeadAdapter.this);
            }
        });
    }

    // =========================================================================
    // Diff Callback
    // =========================================================================

    public boolean areHeadsTheSame(H oldHead, H newHead, int oldPosition, int newPosition) {
        return oldHead.equals(newHead);
    }

    public boolean areHeadsContentsTheSame(H oldHead, H newHead, int oldPosition, int newPosition) {
        return oldHead.equals(newHead);
    }

    @Nullable
    public Object getHeadChangePayload(H oldHead, H newHead, int oldPosition, int newPosition) {
        return null;
    }

    public boolean areItemsTheSame(T oldItem, T newItem, int oldPosition, int newPosition) {
        return oldItem.equals(newItem);
    }

    public boolean areContentsTheSame(T oldItem, T newItem, int oldPosition, int newPosition) {
        return oldItem.equals(newItem);
    }

    @Nullable
    public Object getChangePayload(T oldItem, T newItem, int oldPosition, int newPosition) {
        return null;
    }

    // =========================================================================
    // Section CRUD
    // =========================================================================
    public List<T> getAllCanSelectItems() {
        List<T> result = new ArrayList<>();
        for (Section<H, T> section : sections) {
            result.addAll(section.items);
        }
        return result;
    }

    public HeadAdapter<H, T> addHeadNoNotify(@NonNull H head) {
        sections.add(new Section<H, T>(head));
        rebuildNodes();
        return this;
    }

    public HeadAdapter<H, T> addHeadNoNotify(@NonNull H head, @NonNull List<T> items) {
        sections.add(new Section<>(head, items));
        rebuildNodes();
        return this;
    }

    public HeadAdapter<H, T> addHead(@NonNull H head, @NonNull List<T> items) {
        addHeadNoNotify(head, items);
        notifyDataSetChanged();
        return this;
    }

    public HeadAdapter<H, T> removeHeadNoNotify(@NonNull H head) {
        int index = findSectionIndex(head);
        if (index < 0) {
            return this;
        }
        sections.remove(index);
        rebuildNodes();
        return this;
    }

    public HeadAdapter<H, T> removeHead(@NonNull H head) {
        removeHeadNoNotify(head);
        notifyDataSetChanged();
        return this;
    }

    public HeadAdapter<H, T> removeHeadNoNotify(int sectionIndex) {
        if (sectionIndex < 0 || sectionIndex >= sections.size()) {
            return this;
        }
        sections.remove(sectionIndex);
        rebuildNodes();
        return this;
    }

    public HeadAdapter<H, T> removeHead(int sectionIndex) {
        removeHeadNoNotify(sectionIndex);
        notifyDataSetChanged();
        return this;
    }

    // =========================================================================
    // Item CRUD
    // =========================================================================

    public HeadAdapter<H, T> addItemNoNotify(@NonNull H head, @NonNull T item) {
        Section<H, T> section = findSection(head);
        if (section == null) {
            section = new Section<>(head);
            sections.add(section);
        }
        section.items.add(item);
        rebuildNodes();
        return this;
    }

    public HeadAdapter<H, T> addItem(@NonNull H head, @NonNull T item) {
        addItemNoNotify(head, item);
        notifyDataSetChanged();
        return this;
    }

    public HeadAdapter<H, T> addItemNoNotify(@NonNull H head, @NonNull List<T> items) {
        Section<H, T> section = findSection(head);
        if (section == null) {
            section = new Section<>(head);
            sections.add(section);
        }
        section.items.addAll(items);
        rebuildNodes();
        return this;
    }

    public HeadAdapter<H, T> addItem(@NonNull H head, @NonNull List<T> items) {
        addItemNoNotify(head, items);
        notifyDataSetChanged();
        return this;
    }

    public HeadAdapter<H, T> addItemNoNotify(@NonNull H head, int index, @NonNull T item) {
        Section<H, T> section = findSection(head);
        if (section == null) {
            section = new Section<>(head);
            sections.add(section);
        }
        index = Math.max(0, Math.min(index, section.items.size()));
        section.items.add(index, item);
        rebuildNodes();
        return this;
    }

    public HeadAdapter<H, T> addItem(@NonNull H head, int index, @NonNull T item) {
        addItemNoNotify(head, index, item);
        notifyDataSetChanged();
        return this;
    }

    public HeadAdapter<H, T> removeItemNoNotify(@NonNull H head, @NonNull T item) {
        Section<H, T> section = findSection(head);
        if (section == null) {
            return this;
        }
        section.items.remove(item);
        if (section.items.isEmpty()) {
            sections.remove(section);
        }
        rebuildNodes();
        return this;
    }

    public HeadAdapter<H, T> removeItem(@NonNull H head, @NonNull T item) {
        removeItemNoNotify(head, item);
        notifyDataSetChanged();
        return this;
    }

    public HeadAdapter<H, T> removeItemNoNotify(@NonNull H head, int itemIndex) {
        Section<H, T> section = findSection(head);
        if (section == null) {
            return this;
        }
        if (itemIndex < 0 || itemIndex >= section.items.size()) {
            return this;
        }
        section.items.remove(itemIndex);
        if (section.items.isEmpty()) {
            sections.remove(section);
        }
        rebuildNodes();
        return this;
    }

    public HeadAdapter<H, T> removeItem(@NonNull H head, int itemIndex) {
        removeItemNoNotify(head, itemIndex);
        notifyDataSetChanged();
        return this;
    }

    /**
     * 删除RecyclerView position对应的数据。
     * <p>
     * Header会删除整个Section。
     * Item只删除Item。
     */
    public HeadAdapter<H, T> removeNoNotify(int position) {
        PositionInfo<H, T> info = getPositionInfo(position);
        if (info == null) {
            return this;
        }
        if (info.isHead()) {
            sections.remove(info.sectionIndex);
        } else {
            Section<H, T> section = sections.get(info.sectionIndex);
            section.items.remove(info.itemIndex);
            if (section.items.isEmpty()) {
                sections.remove(info.sectionIndex);
            }
        }
        rebuildNodes();
        return this;
    }

    public HeadAdapter<H, T> remove(int position) {
        removeNoNotify(position);
        notifyDataSetChanged();
        return this;
    }

    // =========================================================================
    // Set
    // =========================================================================

    public HeadAdapter<H, T> setNoNotify(@NonNull H head, int index, @NonNull T item) {
        Section<H, T> section = findSection(head);
        if (section == null) {
            return this;
        }
        if (index < 0 || index >= section.items.size()) {
            return this;
        }
        section.items.set(index, item);
        rebuildNodes();
        return this;
    }

    public HeadAdapter<H, T> set(@NonNull H head, int index, @NonNull T item) {
        setNoNotify(head, index, item);
        notifyItemChanged(getItemPosition(head, item));
        return this;
    }

    // =========================================================================
    // Clear
    // =========================================================================

    public HeadAdapter<H, T> clearNoNotify() {
        sections.clear();
        rebuildNodes();
        return this;
    }

    public HeadAdapter<H, T> clear() {
        int size = nodes.size();
        clearNoNotify();
        notifyItemRangeRemoved(0, size);
        return this;
    }

    public HeadAdapter<H, T> clearItemsNoNotify(@NonNull H head) {
        Section<H, T> section = findSection(head);
        if (section == null) {
            return this;
        }
        section.items.clear();
        rebuildNodes();
        return this;
    }

    public HeadAdapter<H, T> clearItems(@NonNull H head) {
        clearItemsNoNotify(head);
        notifyDataSetChanged();
        return this;
    }

    /**
     * 删除Header以及所有Item。
     */
    public HeadAdapter<H, T> removeItemsNoNotify(@NonNull H head) {
        return removeHeadNoNotify(head);
    }

    public HeadAdapter<H, T> removeItems(@NonNull H head) {
        removeItemsNoNotify(head);
        notifyDataSetChanged();
        return this;
    }

    // =========================================================================
    // Swap
    // =========================================================================

    public HeadAdapter<H, T> swapNoNotify(@NonNull H head, int fromIndex, int toIndex) {
        Section<H, T> section = findSection(head);
        if (section == null) {
            return this;
        }
        if (fromIndex < 0 || fromIndex >= section.items.size() || toIndex < 0 || toIndex >= section.items.size()) {
            return this;
        }
        Collections.swap(section.items, fromIndex, toIndex);
        rebuildNodes();
        return this;
    }

    public HeadAdapter<H, T> swap(@NonNull H head, int fromIndex, int toIndex) {
        swapNoNotify(head, fromIndex, toIndex);
        notifyDataSetChanged();
        return this;
    }

    // =========================================================================
    // Query
    // =========================================================================

    @Nullable
    private Section<H, T> findSection(@NonNull H head) {
        for (Section<H, T> section : sections) {
            if (same(section.head, head)) {
                return section;
            }
        }
        return null;
    }

    private int findSectionIndex(@NonNull H head) {
        for (int i = 0; i < sections.size(); i++) {
            if (same(sections.get(i).head, head)) {
                return i;
            }
        }
        return -1;
    }

    private boolean same(@Nullable Object a, @Nullable Object b) {
        if (a == b) {
            return true;
        }
        if (a == null || b == null) {
            return false;
        }
        return a.equals(b);
    }

    @NonNull
    public List<Section<H, T>> getSections() {
        return sections;
    }

    @Nullable
    public Section<H, T> getSection(int index) {
        if (index < 0 || index >= sections.size()) {
            return null;
        }
        return sections.get(index);
    }

    public int getHeadCount() {
        return sections.size();
    }

    @Nullable
    public H getHead(int sectionIndex) {
        Section<H, T> section = getSection(sectionIndex);
        return section == null ? null : section.head;
    }

    @Nullable
    public List<T> getItems(@NonNull H head) {
        Section<H, T> section = findSection(head);
        return section == null ? null : section.items;
    }

    public int getItemCount(@NonNull H head) {
        Section<H, T> section = findSection(head);
        return section == null ? 0 : section.items.size();
    }

    public boolean containsHead(@NonNull H head) {
        return findSection(head) != null;
    }

    public boolean containsItem(@NonNull H head, @NonNull T item) {
        Section<H, T> section = findSection(head);
        return section != null && section.items.contains(item);
    }

    // =========================================================================
    // Position
    // =========================================================================

    public int getHeaderPosition(@NonNull H head) {
        int position = 0;
        for (Section<H, T> section : sections) {
            if (same(section.head, head)) {
                return position;
            }
            position += 1;
            position += section.items.size();
        }
        return -1;
    }

    public int getItemPosition(@NonNull H head, @NonNull T item) {
        int position = 0;
        for (Section<H, T> section : sections) {
            if (same(section.head, head)) {
                for (int i = 0; i < section.items.size(); i++) {
                    if (same(section.items.get(i), item)) {
                        return position + 1 + i;
                    }
                }
                return -1;
            }
            position += 1;
            position += section.items.size();
        }
        return -1;
    }

    @Nullable
    public PositionInfo<H, T> getPositionInfo(int position) {
        if (position < 0 || position >= nodes.size()) {
            return null;
        }
        Node<H, T> node = nodes.get(position);
        if (node.type == Node.TYPE_HEAD) {
            return new PositionInfo<>(true, node.head, null, node.sectionIndex, -1, position);
        }
        Section<H, T> section = sections.get(node.sectionIndex);
        return new PositionInfo<>(false, section.head, node.item, node.sectionIndex, node.itemIndex, position);
    }

    public boolean isHeadPosition(int position) {
        PositionInfo<H, T> info = getPositionInfo(position);
        return info != null && info.isHead();
    }

    public boolean isItemPosition(int position) {
        PositionInfo<H, T> info = getPositionInfo(position);
        return info != null && info.isItem();
    }

    @Nullable
    public H getHeadByPosition(int position) {
        PositionInfo<H, T> info = getPositionInfo(position);
        return info == null ? null : info.getHead();
    }

    @Nullable
    public T getItemByPosition(int position) {
        PositionInfo<H, T> info = getPositionInfo(position);
        return info == null ? null : info.getItem();
    }

    // =========================================================================
    // set / replace
    // =========================================================================

    public HeadAdapter<H, T> setSectionsNoNotify(@NonNull List<Section<H, T>> newSections) {
        sections.clear();
        for (Section<H, T> section : newSections) {
            sections.add(new Section<>(section.head, section.items));
        }
        rebuildNodes();
        return this;
    }

    public HeadAdapter<H, T> setSections(@NonNull List<Section<H, T>> newSections) {
        setSectionsNoNotify(newSections);
        notifyDataSetChanged();
        return this;
    }

    // =========================================================================
    // Internal
    // =========================================================================

    private void rebuildNodes() {
        nodes.clear();
        for (int sectionIndex = 0; sectionIndex < sections.size(); sectionIndex++) {
            Section<H, T> section = sections.get(sectionIndex);
            nodes.add(Node.<H, T>head(section.head, sectionIndex));
            for (int itemIndex = 0; itemIndex < section.items.size(); itemIndex++) {
                nodes.add(Node.<H, T>item(section.items.get(itemIndex), sectionIndex, itemIndex));
            }
        }
    }

    @NonNull
    private List<Node<H, T>> buildNodes(@NonNull List<Section<H, T>> source) {
        List<Node<H, T>> result = new ArrayList<>();
        for (int sectionIndex = 0; sectionIndex < source.size(); sectionIndex++) {
            Section<H, T> section = source.get(sectionIndex);
            result.add(Node.<H, T>head(section.head, sectionIndex));
            for (int itemIndex = 0; itemIndex < section.items.size(); itemIndex++) {
                result.add(Node.<H, T>item(section.items.get(itemIndex), sectionIndex, itemIndex));
            }
        }
        return result;
    }

    @NonNull
    private List<Section<H, T>> copySections(@NonNull List<Section<H, T>> source) {
        List<Section<H, T>> result = new ArrayList<>(source.size());
        for (Section<H, T> section : source) {
            result.add(new Section<>(section.head, section.items));
        }
        return result;
    }

    public static interface CallbackDiff {
        void onDispatchUpdated();
    }
}