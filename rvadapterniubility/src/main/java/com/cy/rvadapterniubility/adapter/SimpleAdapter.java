package com.cy.rvadapterniubility.adapter;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.cy.BaseAdapter.R;

import java.util.ArrayList;
import java.util.List;

/**
 * @param <T>
 */

public abstract class SimpleAdapter<T> extends RecyclerView.Adapter<BaseViewHolder> implements IAdapter<T, BaseViewHolder, SimpleAdapter>{

    private List<T> list_bean;//数据源


    public SimpleAdapter() {
        list_bean = new ArrayList<>();//数据源
        //防止刷新闪烁
        setHasStableIds(hasStableIds_());
    }

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new BaseViewHolder(LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        handleClick(holder);
        if(position<0||position>=list_bean.size())return;
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
        int position=holder.getAbsoluteAdapterPosition();
        if(position<0||position>=list_bean.size())return;
        onViewRecycled(holder, position,list_bean.get(position));
    }

    @Override
    public void onViewRecycled(BaseViewHolder holder, int position, T bean) {
    }

    @Override
    public long getItemId(int position) {
        //返回position即可，
        return position;
    }

    /**
     *解决notify,item闪烁问题
     * @return
     */
    @Override
    public boolean hasStableIds_() {
        return true;
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
                if(position<0||position>=list_bean.size())return;
                onItemClick(holder, position, list_bean.get(position));
            }
        });
        //添加Item的长按事件
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                int position = holder.getBindingAdapterPosition();
                if(position<0||position>=list_bean.size())return false;
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



    /**
     * @param list_bean
     */
    public SimpleAdapter<T> setList_bean(List<T> list_bean) {
        this.list_bean = list_bean;
        notifyDataSetChanged();
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
        /**
         onBindViewHolder回调的position永远是最后一个可见的item的position,
         比如一次最多只能看到5个item,只要执行了notifyItemRemoved(position)，
         onBindViewHolder回调的position永远是4
         */
        notifyItemRemoved(position);
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
        notifyItemChanged(0);
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
     * 先清空后添加
     */

    public SimpleAdapter<T> clearAddNoNotify(T bean) {
        clearAdd(bean);
        return this;
    }

    /**
     * 先清空后添加,并且notify
     */

    public SimpleAdapter<T> clearAdd(T bean) {
        clearNoNotify();
        addNoNotify(bean);
        notifyDataSetChanged();
        return this;
    }

    /**
     * 先清空后添加List,并且notify
     */

    public SimpleAdapter<T> clearAdd(List<T> beans) {
        clearAddNoNotify(beans);
        notifyDataSetChanged();
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
     */

    public SimpleAdapter<T> addToTop(List<T> beans) {
        addToTopNoNotify(beans);
        //没有刷新的作用
//        notifyItemRangeInserted(0, beans.size());
        notifyDataSetChanged();
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
        list_bean.clear();
        notifyDataSetChanged();
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
