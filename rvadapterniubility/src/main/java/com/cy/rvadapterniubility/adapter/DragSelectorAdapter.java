package com.cy.rvadapterniubility.adapter;

import android.os.Handler;

import androidx.annotation.Nullable;

import com.cy.rvadapterniubility.LogUtils;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by cy on 2018/3/29.类似策略模式,引入IAdapter接口，面向多态编程
 */

public abstract class DragSelectorAdapter<T> implements IAdapter<T, BaseViewHolder, SimpleAdapter> {
    private SimpleAdapter<T> simpleAdapter;
    private boolean usingSelector = false;
    private boolean isAllSelected = false;
    private SetSelector setSelector;

    public DragSelectorAdapter() {
        setSelector = new SetSelector();
        simpleAdapter = new SimpleAdapter<T>() {
            @Override
            public void recycleData(@Nullable Object tag) {
                DragSelectorAdapter.this.recycleData(tag);
            }

            @Nullable
            @Override
            public Object setHolderTagPreBindData(BaseViewHolder holder, int position, T bean) {
                return DragSelectorAdapter.this.setHolderTagPreBindData(holder, position, bean);
            }

            @Override
            public void bindDataToView(final BaseViewHolder holder, int position, T bean) {
                DragSelectorAdapter.this.bindDataToView(holder, position, bean, setSelector.contains(position));
            }

            @Override
            public int getItemLayoutID(int position, T bean) {
                return DragSelectorAdapter.this.getItemLayoutID(position, bean);
            }

            @Override
            public void onItemClick(BaseViewHolder holder, int position, T bean) {
                DragSelectorAdapter.this.onItemClick(holder, position, bean);
            }

            /**
             * 这里不能用了，否则GG
             * @param fromPosition
             * @param toPosition
             * @param srcHolder
             * @param targetHolder
             */
//            @Override
//            public void onItemLongClick(BaseViewHolder holder, int position, T bean) {
//                DragSelectorAdapter.this.onItemLongClick(holder, position, bean);
//            }
            @Override
            public void onItemMove(int fromPosition, int toPosition, BaseViewHolder srcHolder, BaseViewHolder targetHolder) {
                super.onItemMove(fromPosition, toPosition, srcHolder, targetHolder);
                DragSelectorAdapter.this.onItemMove(fromPosition, toPosition, srcHolder, targetHolder);
            }
        };
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
        simpleAdapter.notifyDataSetChanged();
    }

    public void stopDragSelect() {
        usingSelector = false;
        simpleAdapter.postNotifyDataSetChanged();
    }

    public void selectAll(boolean isAllSelected) {
        boolean noChange = this.isAllSelected == isAllSelected;
        this.isAllSelected = isAllSelected;
        if (noChange) return;
        if (isAllSelected) {
            for (int i = 0; i < simpleAdapter.getList_bean().size(); i++) {
                setSelector.add(i);
            }
        } else {
            setSelector.clear();
        }
        simpleAdapter.postNotifyDataSetChanged();
    }

    public boolean isAllSelected() {
        return isAllSelected;
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
                simpleAdapter.notifyItemChanged(position);
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
                simpleAdapter.notifyItemChanged(position);
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
                simpleAdapter.notifyItemRangeChanged(start, end - start + 1);
            }
        });
    }


    @Override
    public void recycleData(@Nullable Object tag) {

    }

    @Nullable
    @Override
    public Object setHolderTagPreBindData(BaseViewHolder holder, int position, T bean) {
        return null;
    }

    @Override
    public final void bindDataToView(BaseViewHolder holder, int position, T bean) {

    }

    public abstract void bindDataToView(BaseViewHolder holder, int position, T bean, boolean isSelected);

    @Override
    public void onItemLongClick(BaseViewHolder holder, int position, T bean) {

    }

    public abstract void onAllSelectChanged(boolean isAllSelected);

    @Override
    public void onItemMove(int fromPosition, int toPosition, BaseViewHolder srcHolder, BaseViewHolder targetHolder) {

    }

    @Override
    public SimpleAdapter<T> getAdapter() {
        return simpleAdapter;
    }

    private class SetSelector {
        private Set<Integer> set;

        public SetSelector() {
            set = new HashSet<>();
        }

        public int size() {
            return set.size();
        }

        public void add(int position) {
            set.add(position);
            LogUtils.log("add", position);
            LogUtils.log("add set size", set.size());
            for (int p : set) {
                LogUtils.log("position", p);
            }
            notifyIsAllSelected();
        }

        public void remove(int position) {
            set.remove(position);
            notifyIsAllSelected();
        }

        public boolean contains(int position) {
            return set.contains(position);
        }

        public void clear() {
            set.clear();
            notifyIsAllSelected();
        }

        private void notifyIsAllSelected() {
            boolean s = !set.isEmpty() && set.size() == simpleAdapter.getList_bean().size();
            if (isAllSelected == s) return;
            LogUtils.log("notifyIsAllSelected", simpleAdapter.getList_bean().size());
            LogUtils.log("notifyIsAllSelected set", set.size());
            onAllSelectChanged(isAllSelected = s);
        }
    }
}
