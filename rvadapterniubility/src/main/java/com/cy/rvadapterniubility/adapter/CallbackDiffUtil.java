package com.cy.rvadapterniubility.adapter;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DiffUtil;

import java.util.List;

public abstract class CallbackDiffUtil<T> extends DiffUtil.Callback {
    protected List<T> listOld;
    protected List<T> listNew;

    public CallbackDiffUtil(List<T> listOld, List<T> listNew) {
        this.listOld = listOld;
        this.listNew = listNew;
    }

    /**
     * 返回旧数据集的大小。
     * 用于构建差分计算的矩阵边界。
     *
     * @return
     */
    @Override
    public final int getOldListSize() {
        return listOld.size();
    }

    /**
     * 返回新数据集的大小。
     *
     * @return
     */
    @Override
    public final int getNewListSize() {
        return listNew.size();
    }

    /**
     * 判断 两项是否为“同一个 item”（通常比较唯一 ID）
     * 如果返回 true，才会继续调用 areContentsTheSame() 来比较内容
     * 如果返回 false，就认为旧项被删除、新项是新增项
     * 注意：
     * 不要用位置比较（位置是会变的）
     * 通常比较数据库 ID 或全局唯一字段
     *
     * @param oldItemPosition The position of the item in the old list
     * @param newItemPosition The position of the item in the new list
     * @return
     */
    @Override
    public final boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return areItemsTheSame(oldItemPosition, newItemPosition, listOld.get(oldItemPosition), listNew.get(newItemPosition));
    }

    /**
     * 判断两个“同一个 item”是否内容相同
     * 如果返回 false，RecyclerView 会调用 onBindViewHolder() 刷新这个 item
     * 如果返回 true，RecyclerView 会跳过更新，保持原样
     * 注意：
     * 你需要覆盖 equals()，或者手动比较字段
     * 如果 layout 依赖的内容变了也要返回 false！
     *
     * @param oldItemPosition The position of the item in the old list
     * @param newItemPosition The position of the item in the new list which replaces the
     *                        oldItem
     * @return
     */
    @Override
    public final boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        return areContentsTheSame(oldItemPosition, newItemPosition, listOld.get(oldItemPosition), listNew.get(newItemPosition));
    }

    /**
     * 用于实现 局部刷新（payload） 的高级特性。
     * 如果你想只刷新部分字段（如只更新图片，不更新文本），可以返回一个 payload 对象，
     * 在 onBindViewHolder(holder, position, payloads) 中使用。
     *
     * @param oldItemPosition The position of the item in the old list
     * @param newItemPosition The position of the item in the new list
     * @return
     */
    @Nullable
    @Override
    public final Object getChangePayload(int oldItemPosition, int newItemPosition) {
        return super.getChangePayload(oldItemPosition, newItemPosition);
    }

    public abstract boolean areItemsTheSame(int oldItemPosition, int newItemPosition, T beanOld, T beanNew);

    public abstract boolean areContentsTheSame(int oldItemPosition, int newItemPosition, T beanOld, T beanNew);

    public Object getChangePayload(int oldItemPosition, int newItemPosition, T beanOld, T beanNew) {
        return null;
    }
}
