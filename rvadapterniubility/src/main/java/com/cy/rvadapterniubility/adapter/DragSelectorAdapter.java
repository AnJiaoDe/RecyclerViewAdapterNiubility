package com.cy.rvadapterniubility.adapter;

import android.os.Handler;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.AsyncDifferConfig;
import androidx.recyclerview.widget.DiffUtil;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public abstract class DragSelectorAdapter<T> extends SimpleAdapter<T> {
    private boolean usingSelector = false;
    private SetSelector setSelector;

    public DragSelectorAdapter() {
        setSelector = new SetSelector();
    }

    public int getSelectedSize() {
        return setSelector.size();
    }

    public boolean isUsingSelector() {
        return usingSelector;
    }

    public void startDragSelect(int position) {
        usingSelector = true;
        toggleNoNotify(position);
        postNotifyDataSetChanged();
    }

    public SetSelector getSetSelector() {
        return setSelector;
    }

    public void stopDragSelect() {
        usingSelector = false;
        setSelector.clear();
        postNotifyDataSetChanged();
    }

    public void selectAll(boolean isAllSelected) {
        boolean noChange = (setSelector.size() == getList_bean().size()) == isAllSelected;
        if (noChange) return;
        if (isAllSelected) {
            for (int i = 0; i < getList_bean().size(); i++) {
                setSelector.add(i);
            }
        } else {
            setSelector.clear();
        }
        postNotifyDataSetChanged();
    }

    public void toggleNoNotify(final int position) {
        if (setSelector.contains(position)) {
            setSelector.remove(position);
        } else {
            setSelector.add(position);
        }
    }

    public void toggle(final int position) {
        toggleNoNotify(position);
        //必须用handler，否则GG  Cannot call this method while RecyclerView is computing a layout or scrolling
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                notifyItemChanged(position);
            }
        });
    }

    public void select(final int position, boolean select) {
        if (select == setSelector.contains(position)) return;
        if (select) {
            setSelector.add(position);
        } else {
            setSelector.remove(position);
        }
        //必须用handler，否则GG  Cannot call this method while RecyclerView is computing a layout or scrolling
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                notifyItemChanged(position);
            }
        });
    }

    public boolean isSelected(int position) {
        return setSelector.contains(position);
    }

    public void selectRange(final int start, final int end, boolean isSelected) {
        for (int i = start; i <= end; i++) {
            if (isSelected)
                setSelector.add(i);
            else
                setSelector.remove(i);
        }
        //必须用handler，否则GG  Cannot call this method while RecyclerView is computing a layout or scrolling
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                notifyItemRangeChanged(start, end - start + 1);
            }
        });
    }

    @Override
    public final void bindDataToView(BaseViewHolder holder, int position, T bean, @NonNull List<Object> payloads) {
        bindDataToView(holder, position, bean, setSelector.contains(position), payloads);
    }

    public abstract void bindDataToView(BaseViewHolder holder, int position, T bean, boolean isSelected, @NonNull List<Object> payloads);

    /**
     * ----------------这个不能在使用的时候实现了，否则会导致回调2次长按事件，因为在DragRecyclerView中也做了长按回调------------------------------------------------------------------
     */
    @Override
    public final void onItemLongClick(BaseViewHolder holder, int position, T bean) {

    }

    public abstract void onItemLongClick__(BaseViewHolder holder, int position, T bean);

    public abstract void onSelectCountChanged(boolean isAllSelected, int count_selected);

    public class SetSelector {
        //方便从后往前删除
        private TreeSet<Integer> treeSet;

        public SetSelector() {
            treeSet = new TreeSet<>();
        }

        public int size() {
            return treeSet.size();
        }

        public void add(int position) {
            treeSet.add(position);
            notifyCountSelected();
        }

        public void remove(int position) {
            treeSet.remove(position);
            notifyCountSelected();
        }

        public boolean contains(int position) {
            return treeSet.contains(position);
        }

        public void clear() {
            int count_selected = treeSet.size();
            treeSet.clear();
            if (count_selected != 0)
                notifyCountSelected();
        }

        private void notifyCountSelected() {
            onSelectCountChanged(getList_bean().size() == treeSet.size(), treeSet.size());
        }

        public TreeSet<Integer> getTreeSet() {
            return treeSet;
        }
    }
}
