package com.cy.cyrvadapter.adapter;

import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.cy.cyrvadapter.bitmap.GlideUtils;

import java.util.Collection;
import java.util.List;

/**
 * Created by lenovo on 2017/6/24.
 */

public abstract class RVAdapter<T> extends RecyclerView.Adapter<RVAdapter.MyViewHolder> {
    private List<T> list_bean;
    private boolean haveHeadView = false;
    private boolean haveFootView = false;
    private boolean isStaggeredGrid = false;
    private int selectedPosition = 0; //默认选中位置
    private int lastSelectedPosition = 0; //上次选中位置


    public RVAdapter(List<T> list_bean) {
        this.list_bean = list_bean;
    }

    public RVAdapter(List<T> list, boolean isStaggeredGrid) {
        this.list_bean = list;
        this.isStaggeredGrid = isStaggeredGrid;
    }

    public RVAdapter(List<T> list_bean, boolean haveHeadView, boolean haveFootView) {

        this.haveFootView = haveFootView;
        this.haveHeadView = haveHeadView;
        this.list_bean = list_bean;
        if (haveHeadView) {
            selectedPosition = 1;
            lastSelectedPosition = 1;
        }
    }

    public RVAdapter(List<T> list_bean, boolean isStaggeredGrid, boolean haveHeadView, boolean haveFootView) {
        this.isStaggeredGrid = isStaggeredGrid;

        this.haveFootView = haveFootView;
        this.haveHeadView = haveHeadView;
        this.list_bean = list_bean;
        if (haveHeadView) {
            selectedPosition = 1;
            lastSelectedPosition = 1;
        }
    }


    @Override
    public final MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false));
    }

    @Override
    public final void onBindViewHolder(final MyViewHolder holder, final int position) {

        if (isStaggeredGrid) {
            // 获取cardview的布局属性，记住这里要是布局的最外层的控件的布局属性，如果是里层的会报cast错误
            StaggeredGridLayoutManager.LayoutParams layoutParams = (StaggeredGridLayoutManager.LayoutParams) holder.itemView.getLayoutParams();
//         最最关键一步，设置当前view占满列数，这样就可以占据两列实现头部了
            if (haveHeadView) {
                if (haveFootView && position == getItemCount() - 1) {
                    layoutParams.setFullSpan(true);

                }
                if (position == 0) {

                    layoutParams.setFullSpan(true);
                }
            } else if (haveFootView) {
                if (position == getItemCount() - 1) {
                    layoutParams.setFullSpan(true);

                }
            }
        }


        //添加Item的点击事件
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (haveHeadView) {
                    if (position == 0) {
                        onItemHeadClick();
                        return;
                    }
                    if (haveFootView) {
                        if (position == getItemCount() - 1) {
                            onItemFootClick();
                            return;
                        }
                    }

                } else {
                    if (haveFootView) {
                        if (position == getItemCount() - 1) {
                            onItemFootClick();
                            return;
                        }
                    }
                }
                if (haveHeadView) {

                    onItemClick(position, list_bean.get(position - 1));
                } else {
                    onItemClick(position, list_bean.get(position));

                }

                if (lastSelectedPosition == position) {
                    return;
                }
                selectedPosition = position; //选择的position赋值给参数，
                notifyItemChanged(selectedPosition);
                notifyItemChanged(lastSelectedPosition);

                lastSelectedPosition = position;

            }
        });
        //有头部
        if (haveHeadView) {
            if (position == 0) {
//                if (list_bean.size() > 0) {

                bindDataToHeadView(holder);
//                }
                return;
            }
            if (haveFootView) {
                if (position == getItemCount() - 1) {
//                    if (list_bean.size() > 0) {
                    bindDataToFootView(holder);
//                    }
                    return;
                }
            }
            if (position == selectedPosition) {
                bindDataToView(holder, position - 1, list_bean.get(position - 1), true);

            } else {
                bindDataToView(holder, position - 1, list_bean.get(position - 1), false);

            }

        } else {
            if (haveFootView) {
                if (position == getItemCount() - 1) {
//                    if (list_bean.size() > 0) {

                    bindDataToFootView(holder);
//                    }
                    return;
                }
            }
            if (position == selectedPosition) {
                bindDataToView(holder, position, list_bean.get(position), true);

            } else {
                bindDataToView(holder, position, list_bean.get(position), false);


            }
        }


    }

    //填充数据
    public abstract void bindDataToView(MyViewHolder holder, int position, T bean, boolean isSelected);

    //添加头部
    public void bindDataToHeadView(MyViewHolder holder) {
    }

    //添加尾部
    public void bindDataToFootView(MyViewHolder holder) {
    }


    @Override
    public final int getItemCount() {
        if (haveHeadView) {
            if (haveFootView) {
                return list_bean.size() + 2;

            }
            return list_bean.size() + 1;
        }
        if (haveFootView) {
            return list_bean.size() + 1;
        }

        return list_bean.size();
    }

    public void removeFoot() {
        haveFootView = false;
        notifyDataSetChanged();
    }

    public boolean isHaveFootView() {
        return haveFootView;
    }

    /*
      取得ItemView的布局文件
      @return
     */
    public abstract int getItemLayoutID(int position, T bean);

    @Override
    public final int getItemViewType(int position) {

        if (haveHeadView) {
            if (haveFootView && position == getItemCount() - 1) {

                return getItemLayoutID(position, null);

            }
            return getItemLayoutID(position, position == 0 ? null : list_bean.get(position - 1));

        }
        if (haveFootView && position == getItemCount() - 1) {

            return getItemLayoutID(position, null);

        }
        return getItemLayoutID(position, list_bean.get(position));
    }

    public int getRealItemCount() {
        return list_bean.size();
    }
   /*
      ItemView的单击事件

      @param position
     */

    public abstract void onItemClick(int position, T bean);

    public void onItemHeadClick() {
    }

    public void onItemFootClick() {
    }

    public void onItemLongClick(int position) {
    }

    public void onItemHeadLongClick() {
    }

    public void onItemFootLongClick() {
    }

    public void remove(int position) {
        list_bean.remove(position);
        notifyDataSetChanged();
    }

    public void add(T bean) {
        list_bean.add(bean);
        notifyDataSetChanged();
    }

    public void addNoNotify(T bean) {
        list_bean.add(bean);
    }

    public void addToHead(T bean) {
        list_bean.add(0, bean);
        notifyDataSetChanged();
    }

    public int addAll(List<T> beans) {
        list_bean.addAll(beans);

        notifyDataSetChanged();
        return beans.size();
    }

    public void addAll(Collection<T> c) {
        list_bean.addAll(c);
        notifyDataSetChanged();


    }

    public int clearAddAll(List<T> beans) {
        list_bean.clear();
        list_bean.addAll(beans);
        notifyDataSetChanged();

        return beans.size();
    }

    public void addAllToHead(List<T> beans) {
        list_bean.addAll(0, beans);
        notifyDataSetChanged();
    }

    public void clear() {
        list_bean.clear();
        notifyDataSetChanged();
    }

    public void clearNoNotify() {
        list_bean.clear();
    }


    public static class MyViewHolder extends RecyclerView.ViewHolder {
        private SparseArray<View> array_view;

        public MyViewHolder(View itemView) {
            super(itemView);


            array_view = new SparseArray<View>();

        }


        public <T extends View> T getView(int viewId) {

            View view = array_view.get(viewId);
            if (view == null) {
                view = itemView.findViewById(viewId);
                array_view.put(viewId, view);
            }
            return (T) view;
        }


        public MyViewHolder setVisible(int res_id) {
            getView(res_id).setVisibility(View.VISIBLE);
            return this;
        }

        public MyViewHolder setInVisible(int res_id) {
            getView(res_id).setVisibility(View.INVISIBLE);
            return this;
        }

        public void setViewGone(int res_id) {
            getView(res_id).setVisibility(View.GONE);
        }

        public void setViewVisible(int res_id) {
            getView(res_id).setVisibility(View.VISIBLE);
        }


        public void setText(int tv_id, String text) {
            TextView tv = getView(tv_id);


            tv.setText(nullToString(text));
        }

        public String nullToString(Object object) {
            return object == null ? "" : object.toString();
        }

        public void setPriceText(int tv_id, String text) {
            TextView tv = getView(tv_id);

            tv.setText("¥" + text);
        }

        public void setCountText(int tv_id, String text) {
            TextView tv = getView(tv_id);

            tv.setText("x" + text);
        }

        public void setCountText(int tv_id, int text) {
            TextView tv = getView(tv_id);

            tv.setText("x" + text);
        }

        public void setPriceText(int tv_id, int text) {
            TextView tv = getView(tv_id);

            tv.setText("¥" + text);
        }

        public void setPriceText(int tv_id, float text) {
            TextView tv = getView(tv_id);

            tv.setText("¥" + text);
        }

        public void setText(int tv_id, int text) {
            TextView tv = getView(tv_id);
            tv.setText(String.valueOf(nullToString(text)));
        }

        public void setTextColor(int tv_id, int color) {
            TextView tv = getView(tv_id);
            tv.setTextColor(color);
        }

        public String getTVText(int tv_id) {
            TextView tv = getView(tv_id);
            return tv.getText().toString().trim();
        }

        public String getETText(int tv_id) {
            EditText tv = getView(tv_id);
            return tv.getText().toString().trim();
        }

        public void setBackgroundResource(int v_id, int resid) {
            View view = getView(v_id);
            view.setBackgroundResource(resid);
        }

        public void setImageBitmap(int v_id, Bitmap bitmap) {
            ImageView view = getView(v_id);
            view.setImageBitmap(bitmap);
        }

        public void setImageResource(int v_id, int resID) {
            ImageView view = getView(v_id);
            view.setImageResource(resID);
        }

        public void setImage(int iv_id, String url, int width, int height, int default_res) {
            ImageView iv = getView(iv_id);

            GlideUtils.loadImageByGlide(itemView.getContext(), url, iv, width, height);
        }

        public void setImage(int iv_id, String url) {
            ImageView iv = getView(iv_id);

            GlideUtils.loadImageByGlide(itemView.getContext(), url, iv);
        }

        public void setImage(int iv_id, String url, int width, int height) {
            ImageView iv = getView(iv_id);

            GlideUtils.loadImageByGlide(itemView.getContext(), url, iv, width, height);
        }

        public void setProgress(int progress_id, int progress) {
            ProgressBar progressBar = getView(progress_id);
            progressBar.setProgress(progress);

        }

        public void setOnClickListener(int res_id, View.OnClickListener onClickListener) {
            getView(res_id).setOnClickListener(onClickListener);
        }

        public void setOnLongClickListener(int res_id, View.OnLongClickListener onLongClickListener) {
            getView(res_id).setOnLongClickListener(onLongClickListener);
        }


    }

}
