package com.cy.rvadapterniubility.adapter;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;

import androidx.annotation.CallSuper;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;


import com.cy.rvadapterniubility.R;
import com.cy.rvadapterniubility.ThreadUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * ListAdapter好用但不如直接使用diffResult靠谱，ListAdapter下拉刷新后会导致列表顶上去
 *
 * @param <T>
 */
public abstract class SimpleAdapter<T> extends RecyclerView.Adapter<BaseViewHolder> {
    private List<T> list_bean;//数据源

    public SimpleAdapter() {
        list_bean = new ArrayList<>();//数据源
    }

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new BaseViewHolder(LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false));
    }

    @Override
    public final void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
//        recycleData(holder.getTag());
//        handleClick(holder);
//        //场景一旦复杂，各种remove 各种add 各种notify，各种multiadapter，很容易数组越界，故而必须判断
//        if (position < 0 || position >= list_bean.size()) return;
//        holder.setTag(setHolderTagPreBindData(holder, position, list_bean.get(position)));
//        bindDataToView(holder, position, list_bean.get(position));
    }

    @Override
    public final void onBindViewHolder(@NonNull BaseViewHolder holder, int position, @NonNull List<Object> payloads) {
        recycleData(holder.getTag());
        handleClick(holder);
        //场景一旦复杂，各种remove 各种add 各种notify，各种multiadapter，很容易数组越界，故而必须判断
        if (position < 0 || position >= list_bean.size()) return;
        holder.setTag(setHolderTagPreBindData(holder, position, list_bean.get(position)));
        bindDataToView(holder, position, list_bean.get(position), payloads);
    }

    /**
     * 可以在此处回收holder的tag对应的数据，比如bitmap,
     * 当然主动持有bitmap显然是不明智的，当view detachwindow之后，bitmap自然就没有可达对象引用它了，会自动被垃圾回收
     *
     * @param holder Holder of the view being detached
     */
    @CallSuper
    @Override
    public void onViewDetachedFromWindow(@NonNull BaseViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        recycleData(holder.getTag());
    }

    @Override
    public int getItemViewType(int position) {
        //场景一旦复杂，各种remove 各种add 各种notify，各种multiadapter，很容易数组越界，故而必须判断
        if (position < 0 || position >= list_bean.size()) return R.layout.cy_staggerd_item_0;
        return getItemLayoutID(position, list_bean.get(position));
    }

    //get出来的position一般都是-1，故而不用
//    @Override
//    public void onViewRecycled(@NonNull BaseViewHolder holder) {
//        super.onViewRecycled(holder);
//        int position = holder.getAbsoluteAdapterPosition();
//        //场景一旦复杂，各种remove 各种add 各种notify，各种multiadapter，很容易数组越界，故而必须判断
//        if (position < 0 || position >= list_bean.size()) return;
//        onViewRecycled(holder, position, list_bean.get(position));
//    }


    @Override
    public int getItemCount() {
        return list_bean.size();
    }

    public List<T> getList_bean() {
        return list_bean;
    }

    protected void handleClick(final BaseViewHolder holder) {
        //添加Item的点击事件
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getBindingAdapterPosition();
                //场景一旦复杂，各种remove 各种add 各种notify，各种multiadapter，很容易数组越界，故而必须判断
                if (position < 0 || position >= list_bean.size()) return;
                onItemClick(holder, position, list_bean.get(position));
            }
        });
        //添加Item的长按事件
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                int position = holder.getBindingAdapterPosition();
                //场景一旦复杂，各种remove 各种add 各种notify，各种multiadapter，很容易数组越界，故而必须判断
                if (position < 0 || position >= list_bean.size()) return false;
                onItemLongClick(holder, position, list_bean.get(position));
                return true;
                //返回true，那么长按监听只执行长按监听中执行的代码，返回false，还会继续响应其他监听中的事件。
            }
        });
    }

    public abstract void bindDataToView(@NonNull BaseViewHolder holder, int position, T bean, @NonNull List<Object> payloads);

    public abstract int getItemLayoutID(int position, T bean);

    public abstract void onItemClick(@NonNull BaseViewHolder holder, int position, T bean);

    /**
     * 先于setHolderTagPreBindData被调用，可以在此处回收tag对应的数据，比如bitmap，
     * 当然主动持有bitmap显然是不明智的，当view detachwindow之后，bitmap自然就没有可达对象引用它了，会自动被垃圾回收
     *
     * @param tag
     */
    public void recycleData(@Nullable Object tag) {

    }

    /**
     * 给holder设置TAG，用于处理图片错乱
     *
     * @param holder
     * @param position
     * @param bean
     * @return
     */
    @Nullable
    public Object setHolderTagPreBindData(@NonNull BaseViewHolder holder, int position, T bean) {
        return null;
    }

    public void onItemLongClick(@NonNull BaseViewHolder holder, int position, T bean) {

    }

    public final void onItemMove__(int fromPosition, int toPosition,@NonNull RecyclerView.ViewHolder srcHolder,@NonNull RecyclerView.ViewHolder targetHolder) {
        onItemMove(fromPosition, toPosition, (BaseViewHolder) srcHolder, (BaseViewHolder) targetHolder);
    }

    public void onItemMove(int fromPosition, int toPosition,@NonNull BaseViewHolder srcHolder,@NonNull BaseViewHolder targetHolder) {

    }

    public void startDefaultAttachedAnim(@NonNull BaseViewHolder holder) {

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


    /**
     * 尽量用postNotifyDataSetChanged代替notifyDataSetChanged
     * 返回 RecyclerView 当前是否正在计算布局。
     * 如果此方法返回 true，则表示 RecyclerView 处于锁定状态，任何更新适配器内容的尝试都将导致异常，因为在 RecyclerView 尝试计算布局时无法更改适配器内容。
     * 您的代码不太可能在此状态下运行，因为当发生布局遍历或 RecyclerView 响应系统事件（触摸、无障碍等）开始滚动时，框架会调用此代码。
     * 如果您有一些自定义逻辑来响应可能在布局计算期间触发的 View 回调（例如焦点改变回调）来更改适配器内容，则可能会发生这种情况。在这些情况下，您应该使用 Handler 或类似机制推迟更改。
     * 返回：
     * 如果 RecyclerView 当前正在计算布局，则返回 true；否则返回 false
     * Cannot call this method while RecyclerView is computing a layout or scrolling
     */
    public void postNotifyDataSetChanged() {
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                notifyDataSetChanged();
            }
        });
    }

    /**
     * 注意引用问题，listNew引用坚决不能和List_bean一致，否则GG
     * 比较的时候，比较的是新旧LIST的所有数据，所有数据都会回调
     * //ListAdapter好用但不如直接使用diffResult靠谱，ListAdapter下拉刷新后会导致列表顶上去
     *
     * @param listNew
     */
    public void dispatchUpdatesTo(@NonNull final List<T> listNew) {
        ThreadUtils.getInstance().runThread(new ThreadUtils.RunnableCallback<DiffUtil.DiffResult>() {
            @Override
            public DiffUtil.DiffResult runThread() {
                DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new DiffUtil.Callback() {
                    @Override
                    public int getOldListSize() {
                        return list_bean.size();
                    }

                    @Override
                    public int getNewListSize() {
                        return listNew.size();
                    }

                    @Override
                    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                        return SimpleAdapter.this.areItemsTheSame(list_bean.get(oldItemPosition), listNew.get(newItemPosition));
                    }

                    @Override
                    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                        return SimpleAdapter.this.areContentsTheSame(list_bean.get(oldItemPosition), listNew.get(newItemPosition));
                    }

                    @Nullable
                    @Override
                    public Object getChangePayload(int oldItemPosition, int newItemPosition) {
                        return SimpleAdapter.this.getChangePayload(list_bean.get(oldItemPosition), listNew.get(newItemPosition));
                    }
                });
                return diffResult;
            }

            @Override
            public void runUIThread(DiffUtil.DiffResult diffResult) {
                list_bean = listNew;
                diffResult.dispatchUpdatesTo(SimpleAdapter.this);
            }
        });
    }

    /**
     * 注意引用问题，listNew引用坚决不能和List_bean一致，否则GG
     * //ListAdapter好用但不如直接使用diffResult靠谱，ListAdapter下拉刷新后会导致列表顶上去
     * 这个是专供间隔均分的Grid布局和Staggered布局使用的，只要数据有增删和位移，就必须notifydatasetchanged，否则间隔错乱
     * @param listNew
     */
    public void dispatchUpdatesToItemDecoration(@NonNull final List<T> listNew) {
        ThreadUtils.getInstance().runThread(new ThreadUtils.RunnableCallback<Boolean>() {
            @Override
            public Boolean runThread() {
                if (list_bean.size() == listNew.size()) {
                    for (int i = 0; i < list_bean.size(); i++) {
                        if (!areItemsTheSame(list_bean.get(i), listNew.get(i)))
                            return true;
                    }
                    return false;
                }
                return true;
            }

            @Override
            public void runUIThread(Boolean refresh) {
                if (refresh) {
                    clearAdd(listNew);
                }else {
                    dispatchUpdatesTo(listNew);
                }
            }
        });
    }

    public void dispatchUpdatesToWithMsg(final Object msg) {
        ThreadUtils.getInstance().runThread(new ThreadUtils.RunnableCallback<DiffUtil.DiffResult>() {
            @Override
            public DiffUtil.DiffResult runThread() {
                DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new DiffUtil.Callback() {
                    @Override
                    public int getOldListSize() {
                        return getList_bean().size();
                    }

                    @Override
                    public int getNewListSize() {
                        return getList_bean().size();
                    }

                    @Override
                    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                        return true;
                    }

                    @Override
                    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                        return false;
                    }

                    /**
                     * 在bindDataToView 中，判断payloads是否有msg 决定是否只更新item的部分内容
                     * @param oldItemPosition The position of the item in the old list
                     * @param newItemPosition The position of the item in the new list
                     * @return
                     */
                    @Nullable
                    @Override
                    public Object getChangePayload(int oldItemPosition, int newItemPosition) {
                        return msg;
                    }
                });
                return diffResult;
            }

            @Override
            public void runUIThread(DiffUtil.DiffResult diffResult) {
                diffResult.dispatchUpdatesTo(SimpleAdapter.this);
            }
        });
    }

    /**
     * @param beanOld
     * @param beanNew
     * @return
     */
//    public boolean areItemsTheSameWithItemDecoration(T beanOld, T beanNew) {
//        return false;
//    }

    /**
     * 如果要用diffutil,尽量返回true,可以避免当前item被刷新，返回false的话，areContentsTheSame和getChangePayload不再回调
     * @param beanOld
     * @param beanNew
     * @return
     */
    public boolean areItemsTheSame(T beanOld, T beanNew) {
        return false;
    }

    /**
     * 返回false的话，getChangePayload不再回调
     * @param beanOld
     * @param beanNew
     * @return
     */
    public boolean areContentsTheSame(T beanOld, T beanNew) {
        return false;
    }

    /**
     * @param beanOld
     * @param beanNew
     * @return
     */
    public Object getChangePayload(T beanOld, T beanNew) {
        return null;
    }
    /**
     * ------------------------------------------------------------------------------
     */

    /**
     * ---------------------------------------------------------------------------------
     */
    public SimpleAdapter<T> notifyBehindInserted(int count) {
        notifyItemRangeInserted(list_bean.size() - count, count);
        return this;
    }

    public SimpleAdapter<T> swapNoNotify(int i, int j) {
        Collections.swap(list_bean, i, j);
        return this;
    }

    public SimpleAdapter<T> setListBeanNoNotify(List<T> list_bean) {
        this.list_bean = list_bean;
        return this;
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
