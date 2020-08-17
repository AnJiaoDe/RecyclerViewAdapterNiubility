//package com.cy.cyrvadapter.adapter;
//
//import android.view.View;
//
//import androidx.annotation.NonNull;
//
//import com.cy.cyrvadapter.refreshlayout.LogUtils;
//
///**
// * 桥接模式，用组合代替继承，解耦
// */
//
//public abstract class HeadFootAdapter<T> implements IAdapter<T, BaseViewHolder> {
//    private HeadFootCallback headCallback;
//    private HeadFootCallback footCallback;
//    private SimpleAdapter<T> adapter;
//    public HeadFootAdapter() {
//        adapter = new SimpleAdapter<T>() {
//            @Override
//            public void onBindViewHolder(@NonNull final BaseViewHolder holder, final int position) {
//                //final导致BUG，慎用final,回调持有要小心
//                //final int count_head = headCallback == null ? 0 : getCount_head();
//                if (position >= getCount_head() && position < getCount_head() + getList_bean().size()) {
//                    //用super.导致BUG，因为holder.getAdapterPosition
//                    //super.onBindViewHolder(holder, position - count_head);
//                    /**
//                     *
//                     */
//                    //添加Item的点击事件
//                    holder.itemView.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            int position = holder.getAdapterPosition() - getCount_head();
//                            onItemClick(holder, position, getList_bean().get(position));
//                        }
//                    });
//                    //添加Item的长按事件
//                    holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
//                        @Override
//                        public boolean onLongClick(View v) {
//                            int position = holder.getAdapterPosition() - getCount_head();
//                            onItemLongClick(holder, position, getList_bean().get(position));
//                            return true;
//                            //返回true，那么长按监听只执行长按监听中执行的代码，返回false，还会继续响应其他监听中的事件。
//                        }
//                    });
//                    int p = position - getCount_head();
//                    bindDataToView(holder, p, getList_bean().get(p));
//                    return;
//                }
//                if (position <= getCount_head() - 1) {
//                    //添加Item的点击事件
//                    holder.itemView.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            int p = holder.getAdapterPosition();
//                            headCallback.onItemClick(holder, p, headCallback.getList_bean().get(p));
//                        }
//                    });
//                    //添加Item的长按事件
//                    holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
//                        @Override
//                        public boolean onLongClick(View v) {
//                            int p = holder.getAdapterPosition();
//                            headCallback.onItemLongClick(holder, p, headCallback.getList_bean().get(p));
//                            return true;
//                            //返回true，那么长按监听只执行长按监听中执行的代码，返回false，还会继续响应其他监听中的事件。
//                        }
//                    });
//                    headCallback.bindDataToView(holder, position, headCallback.getList_bean().get(position));
//                    return;
//                }
//                //添加Item的点击事件
//                holder.itemView.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        int p = footCallback.getList_bean().size() - (getItemCount() - holder.getAdapterPosition());
//                        footCallback.onItemClick(holder, p, footCallback.getList_bean().get(p));
//                    }
//                });
//                //添加Item的长按事件
//                holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
//                    @Override
//                    public boolean onLongClick(View v) {
//                        int p = footCallback.getList_bean().size() - (getItemCount() - 1 - holder.getAdapterPosition());
//                        footCallback.onItemLongClick(holder, p, footCallback.getList_bean().get(p));
//                        return true;
//                        //返回true，那么长按监听只执行长按监听中执行的代码，返回false，还会继续响应其他监听中的事件。
//                    }
//                });
//                int p = getCount_foot() - (getItemCount() - position);
//                footCallback.bindDataToView(holder, p, footCallback.getList_bean().get(p));
//            }
//
//            @Override
//            public int getItemCount() {
//                return getList_bean().size()
//                        + (headCallback == null ? 0 : getCount_head())
//                        + (footCallback == null ? 0 : getCount_foot());
//            }
//
//            @Override
//            public int getItemViewType(int position) {
//                if (position > getCount_head() - 1 && position < getCount_head() + getList_bean().size())
//                    return super.getItemViewType(position - getCount_head());
//                if (position <= getCount_head() - 1)
//                    return headCallback.getItemLayoutID(position, headCallback.getList_bean().get(position));
//                return footCallback.getItemLayoutID(position, footCallback.getList_bean().get(getCount_foot() - (getItemCount() - position)));
//            }
//
//            @Override
//            public void bindDataToView(BaseViewHolder holder, int position, T bean) {
//                HeadFootAdapter.this.bindDataToView(holder, position, bean);
//            }
//
//            @Override
//            public int getItemLayoutID(int position, T bean) {
//                return HeadFootAdapter.this.getItemLayoutID(position, bean);
//            }
//
//            @Override
//            public void onItemClick(BaseViewHolder holder, int position, T bean) {
//                HeadFootAdapter.this.onItemClick(holder, position, bean);
//            }
//
//            @Override
//            public void onItemLongClick(BaseViewHolder holder, int position, T bean) {
//                HeadFootAdapter.this.onItemLongClick(holder, position, bean);
//            }
//
//            @Override
//            public void onViewAttachedToWindow(@NonNull BaseViewHolder holder) {
//                int position = holder.getAdapterPosition();
//                int count_head = headCallback == null ? 0 : getCount_head();
//                if (position > count_head - 1 && position < getCount_head() + getList_bean().size()) {
//                    super.onViewAttachedToWindow(holder);
//                    return;
//                }
//                if (position <= count_head - 1) {
//                    headCallback.onViewAttachedToWindow(holder);
//                    return;
//                }
//                footCallback.onViewAttachedToWindow(holder);
//            }
//        };
//    }
//
//
//    @Override
//    public void onItemLongClick(BaseViewHolder holder, int position, T bean) {
//
//    }
//
//    public SimpleAdapter<T> getAdapter() {
//        return adapter;
//    }
//
//    @Override
//    public void onViewAttachedToWindow(BaseViewHolder holder) {
//
//    }
//
//    /**
//     * -------------------------------------------------------------------------------------
//     */
//    public HeadFootAdapter<T> setHeadAdapter(HeadFootCallback headAdapter) {
//        this.headCallback = headAdapter;
//        return this;
//    }
//
//    public HeadFootAdapter<T> setFootAdapter(HeadFootCallback footAdapter) {
//        this.footCallback = footAdapter;
//        return this;
//    }
//
//    public HeadFootCallback getHeadAdapter() {
//        return headCallback;
//    }
//
//    public HeadFootCallback getFootAdapter() {
//        return footCallback;
//    }
//
//    public int getCount_head() {
//        return headCallback == null ? 0 : headCallback.getItemCount();
//    }
//
//    public int getCount_foot() {
//        return footCallback == null ? 0 : footCallback.getItemCount();
//    }
//
//    public HeadFootAdapter<T> adHeadNoNotify(int position, Object obj) {
//        headCallback.add(position, obj);
//        return this;
//    }
//
//    public HeadFootAdapter<T> addHead(int position, Object obj) {
//        adHeadNoNotify(position, obj);
//        adapter.notifyItemInserted(position);
//        return this;
//    }
//
//    public HeadFootAdapter<T> addHeadNoNotify(Object obj) {
//        headCallback.add(0, obj);
//        return this;
//    }
//
//    public HeadFootAdapter<T> addHead(Object obj) {
//        addHeadNoNotify(obj);
//        adapter.notifyItemInserted(0);
//        return this;
//    }
//
//    public HeadFootAdapter<T> removeHeadNoNotify(int position) {
//        headCallback.remove(position);
//        return this;
//    }
//
//    public HeadFootAdapter<T> removeHead(int position) {
//        removeHeadNoNotify(position);
//        adapter.notifyItemRemoved(position);
//        return this;
//    }
//
//    public HeadFootAdapter<T> removeHeadNoNotify() {
//        headCallback.remove(0);
//        return this;
//    }
//
//    public HeadFootAdapter<T> removeHead() {
//        removeHeadNoNotify();
//        adapter.notifyItemRemoved(0);
//        return this;
//    }
//
//    public HeadFootAdapter<T> addFootNoNotify(int position, Object obj) {
//        footCallback.add(position, obj);
//        return this;
//    }
//
//    public HeadFootAdapter<T> addFoot(int position, Object obj) {
//        addFootNoNotify(position, obj);
//        LogUtils.log("adapter.getItemCount() - footCallback.getList_bean().size() + position", adapter.getItemCount() - footCallback.getList_bean().size() + position);
//        adapter.notifyItemInserted(adapter.getItemCount() - footCallback.getList_bean().size() + position);
//        return this;
//    }
//
//    public HeadFootAdapter<T> addFootNoNotify(Object obj) {
//        footCallback.add(obj);
//        return this;
//    }
//
//    public HeadFootAdapter<T> addFoot(Object obj) {
//        addFootNoNotify(obj);
//        adapter.notifyItemInserted(adapter.getItemCount() - 1);
//        return this;
//    }
//
//    public HeadFootAdapter<T> removeFootNoNotify(int position) {
//        footCallback.remove(position);
//        return this;
//    }
//
//    public HeadFootAdapter<T> removeFoot(int position) {
//        removeFootNoNotify(position);
//        adapter.notifyItemRemoved(adapter.getItemCount() - footCallback.getList_bean().size() + position);
//        return this;
//    }
//
//    public HeadFootAdapter<T> removeFootNoNotify() {
//        footCallback.remove(footCallback.getList_bean().size() - 1);
//        return this;
//    }
//
//    public HeadFootAdapter<T> removeFoot() {
//        removeFootNoNotify();
//        adapter.notifyItemRemoved(adapter.getItemCount());
//        return this;
//    }
//
//    public HeadFootAdapter<T> setHeadNoNotify(int position, Object obj) {
//        headCallback.getList_bean().set(position, obj);
//        return this;
//    }
//
//    public HeadFootAdapter<T> setHead(int position, Object obj) {
//        setHeadNoNotify(position, obj);
//        adapter.notifyItemChanged(position);
//        return this;
//    }
//
//    public HeadFootAdapter<T> setFootNoNotify(int position, Object obj) {
//        footCallback.getList_bean().set(position, obj);
//        return this;
//    }
//
//    public HeadFootAdapter<T> setFoot(int position, Object obj) {
//        setFootNoNotify(position, obj);
//        adapter.notifyItemChanged(adapter.getItemCount() - footCallback.getList_bean().size() + position);
//        return this;
//    }
//}