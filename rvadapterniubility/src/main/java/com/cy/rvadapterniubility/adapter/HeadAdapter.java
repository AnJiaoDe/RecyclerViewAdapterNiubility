package com.cy.rvadapterniubility.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public abstract class HeadAdapter<H, T> extends RecyclerView.Adapter<BaseViewHolder> {
    @NonNull
    private Map<H, List<T>> mapBean = new LinkedHashMap<>();

    @NonNull
    @Override
    public final BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        BaseViewHolder holder = new BaseViewHolder(LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false));
        handleClick(holder);
        return holder;
    }

    @Override
    public final void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
    }

    @Override
    public final void onBindViewHolder(@NonNull BaseViewHolder holder, int position, @NonNull List<Object> payloads) {
        H header = getHeaderBean(position);
        if (header != null) {
            bindDataToHeadView(holder, position, header,payloads);
            return;
        }
        T bean = getItemBean(position);
        if (bean != null) {
            bindDataToView(holder, position, bean,payloads);
        }
    }

    @Override
    public int getItemCount() {
        int count = 0;
        for (List<T> list : mapBean.values()) {
            if (list != null) {
                count += list.size() + 1;
            }
        }
        return count;
    }

    @Override
    public final int getItemViewType(int position) {
        H header = getHeaderBean(position);
        if (header != null) {
            return getHeadLayoutID(position, header);
        }
        T bean = getItemBean(position);
        if (bean == null) {
            return 0;
        }
        return getItemLayoutID(position, bean);
    }

    public final boolean isFullSpan(int position) {
        return getHeaderBean(position) != null;
    }

    @Nullable
    protected H getHeaderBean(int position) {
        int index = 0;
        for (Map.Entry<H, List<T>> entry : mapBean.entrySet()) {
            List<T> list = entry.getValue();
            if (list == null || list.isEmpty()) {
                continue;
            }
            if (position == index) {
                return entry.getKey();
            }
            index += list.size() + 1;
            if (position < index) {
                return null;
            }
        }
        return null;
    }

    /**
     * RecyclerView position → T
     */
    @Nullable
    protected T getItemBean(int position) {
        int index = 0;
        for (List<T> list : mapBean.values()) {
            if (list == null || list.isEmpty()) {
                continue;
            }
            // 跳过 Header
            index++;
            if (position < index + list.size()) {
                return list.get(position - index);
            }
            index += list.size();
        }
        return null;
    }

    private void handleClick(@NonNull final BaseViewHolder holder) {
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getBindingAdapterPosition();
                if (position < 0) return;

                H header = getHeaderBean(position);
                if (header != null) {
                    onHeadClick(holder, position, header);
                    return;
                }

                T bean = getItemBean(position);
                if (bean == null) return;
                onItemClick(holder, position, bean);
            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                int position = holder.getBindingAdapterPosition();
                if (position < 0) return false;

                H header = getHeaderBean(position);
                if (header != null) {
                    onHeadLongClick(holder, position, header);
                    return true;
                }

                T bean = getItemBean(position);
                if (bean == null) return false;
                onItemLongClick(holder, position, bean);
                return true;
            }
        });
    }

    @LayoutRes
    public abstract int getHeadLayoutID(int position, H bean);

    @LayoutRes
    public abstract int getItemLayoutID(int position, T bean);

    public abstract void bindDataToHeadView(@NonNull BaseViewHolder holder, int position, H bean, @NonNull List<Object> payloads);

    public abstract void bindDataToView(@NonNull BaseViewHolder holder, int position, T bean, @NonNull List<Object> payloads);

    public abstract void onHeadClick(@NonNull BaseViewHolder holder, int position, H bean);

    public void onHeadLongClick(@NonNull BaseViewHolder holder, int position, H bean) {
    }

    public abstract void onItemClick(@NonNull BaseViewHolder holder, int position, T bean);

    public void onItemLongClick(@NonNull BaseViewHolder holder, int position, T bean) {
    }

    @NonNull
    public Map<H, List<T>> getMapBean() {
        return mapBean;
    }

    public void setMapBean(@NonNull Map<H, List<T>> mapBean) {
        this.mapBean = mapBean;
    }

    @NonNull
    public List<T> getListBean() {
        List<T> result = new ArrayList<>();
        for (List<T> list : mapBean.values()) {
            if (list != null) {
                result.addAll(list);
            }
        }
        return result;
    }
    public HeadAdapter<H, T> addHeadNoNotify(@NonNull H bean) {
        mapBean.put(bean, new ArrayList<T>());
        return this;
    }

    public HeadAdapter<H, T> addHead(@NonNull H bean) {
        int position = getItemCount();
        addHeadNoNotify(bean);
        notifyItemInserted(position);
        return this;
    }

    public HeadAdapter<H, T> addItemNoNotify(@NonNull H head, @NonNull T bean) {
        List<T> list = mapBean.get(head);
        if (list == null) {
            list = new ArrayList<>();
            mapBean.put(head, list);
            // 新增 Header + Item
            list.add(bean);
            return this;
        }
        list.add(bean);
        return this;
    }

    public HeadAdapter<H, T> addItem(@NonNull H head, @NonNull T bean) {
        List<T> list = mapBean.get(head);
        if (list == null) {
            // Header 不存在
            int position = getItemCount();

            list = new ArrayList<>();
            list.add(bean);
            mapBean.put(head, list);

            notifyItemRangeInserted(position, 2);
            return this;
        }
        int position = getHeaderPosition(head) + list.size() + 1;
        list.add(bean);
        notifyItemInserted(position);
        return this;
    }
    public HeadAdapter<H, T> addItemNoNotify(@NonNull H head, @NonNull List<T> list) {
        List<T> oldList = mapBean.get(head);

        if (oldList == null) {
            mapBean.put(head, new ArrayList<>(list));
        } else {
            oldList.addAll(list);
        }

        return this;
    }

    public HeadAdapter<H, T> addItem(@NonNull H head, @NonNull List<T> list) {
        if (list.isEmpty()) {
            return this;
        }

        List<T> oldList = mapBean.get(head);

        if (oldList == null) {
            int position = getItemCount();

            mapBean.put(head, new ArrayList<>(list));

            notifyItemRangeInserted(position, list.size() + 1);
        } else {
            int position = getHeaderPosition(head) + 1 + oldList.size();

            oldList.addAll(list);

            notifyItemRangeInserted(position, list.size());
        }

        return this;
    }

    private int getHeaderPosition(@NonNull H head) {
        int position = 0;
        for (Map.Entry<H, List<T>> entry : mapBean.entrySet()) {
            List<T> list = entry.getValue();
            if (list == null || list.isEmpty()) {
                continue;
            }
            //用equals
            if (Objects.equals(entry.getKey(), head)) {
                return position;
            }
            position += list.size() + 1;
        }
        return -1;
    }

}