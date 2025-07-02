package com.cy.rvadapterniubility.adapter;

import android.os.Handler;
import android.util.SparseArray;

import androidx.annotation.NonNull;

import java.util.List;

public abstract class DragSelectorAdapter<T> extends SimpleAdapter<T> {
    private boolean usingSelector = false;
    private SparseArraySelector sparseArraySelector;

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
        usingSelector = true;
        toggleNoNotify(position);
        postNotifyDataSetChanged();
    }

    public SparseArraySelector getSparseArraySelector() {
        return sparseArraySelector;
    }

    public void stopDragSelect() {
        usingSelector = false;
        sparseArraySelector.clear();
        postNotifyDataSetChanged();
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
        postNotifyDataSetChanged();
    }

    public void toggleNoNotify(final int position) {
        if (sparseArraySelector.contains(position)) {
            sparseArraySelector.remove(position);
        } else {
            sparseArraySelector.put(position);
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
        if (select == sparseArraySelector.contains(position)) return;
        if (select) {
            sparseArraySelector.put(position);
        } else {
            sparseArraySelector.remove(position);
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
        return sparseArraySelector.contains(position);
    }

    public void selectRange(final int start, final int end, boolean isSelected) {
        for (int i = start; i <= end; i++) {
            if (isSelected)
                sparseArraySelector.put(i);
            else
                sparseArraySelector.remove(i);
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
        bindDataToView(holder, position, bean, sparseArraySelector.contains(position), payloads);
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
