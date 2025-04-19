package com.cy.rvadapterniubility.adapter;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;

import androidx.annotation.CallSuper;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.cy.BaseAdapter.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @param <T>
 */

public abstract class SimpleAdapter<T> extends RecyclerView.Adapter<BaseViewHolder> implements IAdapter<T, BaseViewHolder, SimpleAdapter> {

    private List<T> list_bean;//数据源

    private SparseArray<BaseViewHolder> sparseArrayHolder;

    public SimpleAdapter() {
        list_bean = new ArrayList<>();//数据源
        sparseArrayHolder = new SparseArray<>();
    }

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new BaseViewHolder(LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        handleClick(holder);
        if (position < 0 || position >= list_bean.size()) return;
        bindDataToView(holder, position, list_bean.get(position));
    }

    @Override
    public int getItemViewType(int position) {
//        if(position<0||position>=list_bean.size())return R.layout.cy_staggerd_item_0;
        return getItemLayoutID(position, list_bean.get(position));
    }

    @Override
    public void onViewRecycled(@NonNull BaseViewHolder holder) {
        super.onViewRecycled(holder);
        int position = holder.getAbsoluteAdapterPosition();
        if (position < 0 || position >= list_bean.size()) return;
        onViewRecycled(holder, position, list_bean.get(position));
    }

    @Override
    @CallSuper
    public void onViewRecycled(BaseViewHolder holder, int position, T bean) {
        sparseArrayHolder.remove(position);
    }

    @Override
    @CallSuper
    public void bindDataToView(BaseViewHolder holder, int position, T bean) {
        sparseArrayHolder.put(position,holder);
    }

    /**
     * 用于更新数据到VIEW，避免直接调用notifydatasetchanged导致：
     //只有一个item的时候，长按item会通知全选，崩溃,
     Cannot call this method while RecyclerView is computing a layout or scrolling
     */
    public void notifyDataToView() {
        int position;
        for (int i = 0; i < sparseArrayHolder.size(); i++) {
            position=sparseArrayHolder.keyAt(i);
            bindDataToView(sparseArrayHolder.get(position),position, getList_bean().get(position));
        }
    }

    @Override
    public int getItemCount() {
        return list_bean.size();
    }

    public List<T> getList_bean() {
        return list_bean;
    }

    protected void handleClick(final BaseViewHolder holder) {
        /**
         *
         */
        //添加Item的点击事件
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getBindingAdapterPosition();
                if (position < 0 || position >= list_bean.size()) return;
                onItemClick(holder, position, list_bean.get(position));
            }
        });
        //添加Item的长按事件
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                int position = holder.getBindingAdapterPosition();
                if (position < 0 || position >= list_bean.size()) return false;
                onItemLongClick(holder, position, list_bean.get(position));
                return true;
                //返回true，那么长按监听只执行长按监听中执行的代码，返回false，还会继续响应其他监听中的事件。
            }
        });
    }

    /**
     * ----------------------------------------------------------------------------------
     */
    @Override
    public void onItemLongClick(BaseViewHolder holder, int position, T bean) {

    }

    public final void onItemMove__(int fromPosition, int toPosition, RecyclerView.ViewHolder srcHolder, RecyclerView.ViewHolder targetHolder) {
        onItemMove(fromPosition, toPosition, (BaseViewHolder) srcHolder, (BaseViewHolder) targetHolder);
    }

    @Override
    public void onItemMove(int fromPosition, int toPosition, BaseViewHolder srcHolder, BaseViewHolder targetHolder) {

    }

    public void startDefaultAttachedAnim(BaseViewHolder holder) {

        final ObjectAnimator objectAnimator_scaleX = ObjectAnimator.ofFloat(holder.itemView, "scaleX", 0.5f, 1);

        final ObjectAnimator objectAnimator_scaleY = ObjectAnimator.ofFloat(holder.itemView, "scaleY", 0.5f, 1);

        final ObjectAnimator objectAnimator_alpha = ObjectAnimator.ofFloat(holder.itemView, "alpha", 0.5f, 1);

//        final ObjectAnimator objectAnimator_transX = ObjectAnimator.ofFloat(holder.itemView, "translationX", -1000,0);

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.setDuration(1000);
        animatorSet.playTogether(objectAnimator_scaleX, objectAnimator_scaleY, objectAnimator_alpha);
        animatorSet.setInterpolator(new DecelerateInterpolator());
        animatorSet.start();
    }

    @Override
    public void onViewDetachedFromWindow(@NonNull BaseViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
    }

    @Override
    public void onViewAttachedToWindow(@NonNull BaseViewHolder holder) {
        super.onViewAttachedToWindow(holder);
    }


    @Override
    public SimpleAdapter getAdapter() {
        return this;
    }


    /**
     * ------------------------------------------------------------------------------
     */

    /**
     * ---------------------------------------------------------------------------------
     */

    public void swap(int i, int j) {
        Collections.swap(list_bean, i, j);
    }

    /**
     * @param list_bean
     */
    public SimpleAdapter<T> setList_bean(List<T> list_bean) {
        int size = list_bean.size();
        list_bean.clear();
        notifyItemRangeRemoved(0, size);
        this.list_bean = list_bean;
        notifyItemRangeInserted(0, list_bean.size());
        return this;
    }

    /**
     * 删除相应position的数据Item
     */
    public SimpleAdapter<T> removeNoNotify(int position) {
        list_bean.remove(position);
        return this;
    }

    /**
     * 删除相应position的数据Item ,并且notify,
     */
    public SimpleAdapter<T> remove(int position) {
        removeNoNotify(position);
        //必须notifyDataSetChanged，否则瀑布流边距GG
        notifyDataSetChanged();
        /**
         onBindViewHolder回调的position永远是最后一个可见的item的position,
         比如一次最多只能看到5个item,只要执行了notifyItemRemoved(position)，
         onBindViewHolder回调的position永远是4
         */
//        notifyItemRemoved(position);
        return this;
    }

    /**
     * 添加一条数据item
     */
    public SimpleAdapter<T> addNoNotify(int position, T bean) {
        list_bean.add(position, bean);
        return this;
    }

    /**
     * 添加一条数据item,并且notify
     */
    public SimpleAdapter<T> add(int position, T bean) {
        addNoNotify(position, bean);
        notifyItemInserted(position);
        return this;
    }


    /**
     * 添加一条数据item
     */
    public SimpleAdapter<T> addNoNotify(T bean) {
        list_bean.add(bean);
        return this;
    }

    /**
     * 添加一条数据item,并且notify
     */
    public SimpleAdapter<T> add(T bean) {
        addNoNotify(bean);
        notifyItemInserted(list_bean.size() - 1);
        return this;
    }

    /**
     * 添加一条数据item到position 0
     */

    public SimpleAdapter<T> addToTopNoNotify(T bean) {
        list_bean.add(0, bean);
        return this;
    }

    /**
     * 添加一条数据item到position 0,并且notify
     */
    public SimpleAdapter<T> addToTop(T bean) {
        addToTopNoNotify(bean);
        //必须notifyDataSetChanged
        notifyDataSetChanged();
        //瀑布流，间隔出问题
//        notifyItemInserted(0);
        return this;
    }

    /**
     * 添加List到position 0
     */

    public SimpleAdapter<T> addToTopNoNotify(List<T> beans) {
        list_bean.addAll(0, beans);
        return this;
    }

    /**
     * 添加List到position 0,并且notify
     * 注意：千万不能notifydatasetchanged,否则整个列表都会被刷新，如果是比较耗时的加载图片，会闪烁
     * 刷新时顶部留白
     * 使用 notifyDataSetChanged()方法做刷新时，据说：会触发StaggeredGridLayoutManager 的onItemsChanged 方法，
     * 导致item的spanIndex重现进行计算，item所在列的位置出现了变化，导致了顶部空白。
     * 而用notifyItemRangeInserted，notifyItemRangeChanged做局部刷新时则不会引起变化。
     */
    public SimpleAdapter<T> addToTop(List<T> beans) {
        addToTopNoNotify(beans);
        //必须notifyDataSetChanged
        notifyDataSetChanged();
        //瀑布流，间隔出问题
//        notifyItemRangeChanged(0, beans.size());
//        notifyItemRangeInserted(0, beans.size());
        return this;
    }

    /**
     * 添加List
     */
    public SimpleAdapter<T> addNoNotify(List<T> beans) {
        list_bean.addAll(beans);
        return this;
    }

    /**
     * 添加List,并且notify
     */
    public SimpleAdapter<T> add(List<T> beans) {
        addNoNotify(beans);
        notifyItemRangeInserted(list_bean.size() - beans.size(), beans.size());
        return this;
    }

    /**
     * 先清空后添加List
     */

    public SimpleAdapter<T> clearAddNoNotify(List<T> beans) {
        list_bean.clear();
        list_bean.addAll(beans);
        return this;
    }

    /**
     * 先清空后添加List,并且notify
     */

    public SimpleAdapter<T> clearAdd(List<T> beans) {
        clearAddNoNotify(beans);
        notifyDataSetChanged();
        //这2个就不灵了
//        notifyItemRangeRemoved(0, size);
//        notifyItemRangeInserted(0, beans.size());
        return this;
    }

    /**
     * 先清空后添加
     */

    public SimpleAdapter<T> clearAddNoNotify(T bean) {
        list_bean.clear();
        list_bean.add(bean);
        return this;
    }

    /**
     * 先清空后添加,并且notify
     */

    public SimpleAdapter<T> clearAdd(T bean) {
        clearAddNoNotify(bean);
        notifyDataSetChanged();
        //这2个就不灵了
//        notifyItemRangeRemoved(0, size);
//        notifyItemInserted(0);
        return this;
    }


    /**
     * 清空list
     */
    public SimpleAdapter<T> clearNoNotify() {
        list_bean.clear();
        return this;
    }

    /**
     * 清空list
     */
    public SimpleAdapter<T> clear() {
        int size = list_bean.size();
        clearNoNotify();
        notifyItemRangeRemoved(0, size);
        return this;
    }

    public SimpleAdapter<T> setNoNotify(int index, T bean) {
        list_bean.set(index, bean);
        return this;
    }

    public SimpleAdapter<T> set(int index, T bean) {
        setNoNotify(index, bean);
        notifyItemChanged(index);
        return this;
    }

}
