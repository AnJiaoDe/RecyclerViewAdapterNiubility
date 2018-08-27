package com.cy.cyrvadapter.adapter;

import android.content.Context;
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

import java.util.List;

/**
 * Created by lenovo on 2017/6/24.
 */

public abstract class RVAdapter<T> extends RecyclerView.Adapter<RVAdapter.RVViewHolder> {
    private List<T> list_bean;//数据源
    private boolean haveHeadView = false;//是否需要head
    private boolean haveFootView = false;//是否需要foot
    private boolean isStaggeredGrid = false;//是否是瀑布流
    private int selectedPosition = 0; //默认选中位置,整个RV做单选，点击到哪个，哪个就是选中状态
    private int lastSelectedPosition = 0; //上次选中位置

    //以下是构造方法

    public RVAdapter(List<T> list_bean) {
        this.list_bean = list_bean;
    }

    /*
    是否使用瀑布流
     */
    public RVAdapter(List<T> list, boolean isStaggeredGrid) {
        this.list_bean = list;
        this.isStaggeredGrid = isStaggeredGrid;
    }

    /*
    是否添加head,foot
     */
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


    //??????????????????????????????????????????????????????????????????????????

    //如果想添加方法而继承RVAdapter,记得复写此方法，并且return super
    @Override
    public  RVViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new RVViewHolder(LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false));
    }

    //如果想添加方法而继承RVAdapter,记得复写此方法，并且调用 super

    @Override
    public    void onBindViewHolder(final RVViewHolder holder, final int position) {

        if (isStaggeredGrid) {
            // 获取cardview的布局属性，记住这里要是布局的最外层的控件的布局属性，如果是里层的会报cast错误
            StaggeredGridLayoutManager.LayoutParams layoutParams = (StaggeredGridLayoutManager.LayoutParams) holder.itemView.getLayoutParams();
//         最最关键一步，设置当前view占满列数，这样就可以占据两列实现头部了
            if (position == 0) {
                if (haveHeadView) {
                    layoutParams.setFullSpan(true);

                }

            } else if (position == getItemCount() - 1 && haveFootView) {

                layoutParams.setFullSpan(true);


            }

        }
        //??????????????????????????????????????????????????????????????????????????


        //添加Item的点击事件
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (position == 0) {
                    if (haveHeadView) {
                        onHeadClick();
                    } else {

                        onItemClick(position, list_bean.get(position));
                    }

                } else {
                    if (position == getItemCount() - 1 && haveFootView) {

                        onFootClick();

                    } else {

                        if (haveHeadView) {
                            onItemClick(position, list_bean.get(position - 1));

                        } else {

                            onItemClick(position, list_bean.get(position));
                        }
                    }


                }

                //????????????????????????????????????????????????????????????????????????????????

                //设置选中的item
                if (lastSelectedPosition == position) {
                    return;
                }
                selectedPosition = position; //选择的position赋值给参数，
                notifyItemChanged(selectedPosition);
                notifyItemChanged(lastSelectedPosition);

                lastSelectedPosition = position;

            }
        });

        //??????????????????????????????????????????????????????????????????????????
        //添加Item的长按事件
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {
                if (position == 0) {
                    if (haveHeadView) {
                        onHeadLongClick();
                    } else {

                        onItemLongClick(position, list_bean.get(position));
                    }

                } else {
                    if (position == getItemCount() - 1 && haveFootView) {

                        onFootLongClick();

                    } else {

                        if (haveHeadView) {
                            onItemLongClick(position, list_bean.get(position - 1));

                        } else {

                            onItemLongClick(position, list_bean.get(position));
                        }
                    }


                }
                return true;
                //返回true，那么长按监听只执行长按监听中执行的代码，返回false，还会继续响应其他监听中的事件。
            }
        });
        //??????????????????????????????????????????????????????????????????????????

        //回调bindDataToView

        if (position == 0) {
            if (haveHeadView) {
                bindDataToHeadView(holder);
            } else {
                if (haveFootView && getItemCount() == 1) {

                    bindDataToFootView(holder);
                    return;
                }

                bindDataToView(holder, position, list_bean.get(position), position == selectedPosition ? true : false);

            }

        } else {
            if (position == getItemCount() - 1 && haveFootView) {

                bindDataToFootView(holder);

            } else {

                if (haveHeadView) {
                    bindDataToView(holder, position - 1, list_bean.get(position - 1), position == selectedPosition ? true : false);


                } else {

                    bindDataToView(holder, position, list_bean.get(position), position == selectedPosition ? true : false);

                }
            }


        }


    }


    //??????????????????????????????????????????????????????????????????????????


    @Override
    public    int getItemCount() {
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


    @Override
    public   int getItemViewType(int position) {

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

    //??????????????????????????????????????????????????????????????????????????


    //填充数据，isSelected:整个RV做单选，点击到哪个，哪个就是选中状态
    public abstract void bindDataToView(RVViewHolder holder, int position, T bean, boolean isSelected);

    /*
          取得ItemView的布局文件
          @return
         */
    public abstract int getItemLayoutID(int position, T bean);

      /*
      ItemView的单击事件

      @param position
     */

    public abstract void onItemClick(int position, T bean);

    //添加头部 填充数据
    public void bindDataToHeadView(RVViewHolder holder) {
    }

    //添加尾部 填充数据
    public void bindDataToFootView(RVViewHolder holder) {
    }

    //head点击回调
    public void onHeadClick() {
    }

    //foot 点击回调
    public void onFootClick() {
    }

    //长按回调
    public void onItemLongClick(int position, T bean) {
    }

    //head长按回调
    public void onHeadLongClick() {
    }

    //foot长按回调
    public void onFootLongClick() {
    }

    //??????????????????????????????????????????????????????????????????????????

    /**
     * @return真实count
     */
    public int getRealItemCount() {
        return list_bean.size();
    }

    /**
     * @return是否有head
     */
    public boolean isHaveHeadView() {
        return haveHeadView;
    }

    /**
     * @param haveHeadView 添加还是移除head
     */
    public void setHaveHeadView(boolean haveHeadView) {
        if (this.haveHeadView == haveHeadView) {
            return;
        }
        this.haveHeadView = haveHeadView;

        notifyDataSetChanged();
    }

    /**
     * @return是否有foot
     */
    public boolean isHaveFootView() {
        return haveFootView;
    }

    /**
     * @param haveFootView 添加还是移除foot
     */
    public void setHaveFootView(boolean haveFootView) {

        if (this.haveFootView == haveFootView) {
            return;
        }
        this.haveFootView = haveFootView;

        notifyDataSetChanged();
    }


    /**
     * @return是否是瀑布流
     */
    public boolean isStaggeredGrid() {
        return isStaggeredGrid;
    }

    /**
     * @param staggeredGrid 切换瀑布流
     */
    public void setStaggeredGrid(boolean staggeredGrid) {

        if (this.isStaggeredGrid == staggeredGrid) {
            return;
        }
        this.isStaggeredGrid = staggeredGrid;

        notifyDataSetChanged();
    }

    //获取list
    public List<T> getList_bean() {
        return list_bean;
    }
    //更换List,并且notifyDataSetChanged

    public void setList_bean(List<T> list_bean) {
        this.list_bean = list_bean;
        notifyDataSetChanged();
    }


    //??????????????????????????????????????????????????????????????????????????

    //以下方法是操作数据项的

    //删除相应position的数据Item ,并且notifyDataSetChanged
    public void remove(int position) {
        list_bean.remove(position);
        notifyDataSetChanged();
    }

    //删除相应position的数据Item
    public void removeNoNotify(int position) {
        list_bean.remove(position);
    }

    //添加一条数据item,并且notifyDataSetChanged
    public void add(T bean) {
        list_bean.add(bean);
        notifyDataSetChanged();
    }
    //添加一条数据item

    public void addNoNotify(T bean) {
        list_bean.add(bean);
    }
    //添加一条数据item到position 0,并且notifyDataSetChanged

    public void addToHead(T bean) {
        list_bean.add(0, bean);
        notifyDataSetChanged();
    }
    //添加一条数据item到position 0

    public void addToHeadNoNotify(T bean) {
        list_bean.add(0, bean);
    }
    //添加List,并且notifyDataSetChanged

    public void addAll(List<T> beans) {
        list_bean.addAll(beans);

        notifyDataSetChanged();
    }
    //添加List

    public void addAllNoNotify(List<T> beans) {
        list_bean.addAll(beans);

    }

    //先清空后添加List,并且notifyDataSetChanged

    public void clearAddAll(List<T> beans) {
        list_bean.clear();
        list_bean.addAll(beans);
        notifyDataSetChanged();

    }
    //先清空后添加List

    public void clearAddAllNoNotify(List<T> beans) {
        list_bean.clear();
        list_bean.addAll(beans);

    }
    //添加List到position 0,并且notifyDataSetChanged

    public void addAllToHead(List<T> beans) {
        list_bean.addAll(0, beans);
        notifyDataSetChanged();
    }
    //添加List到position 0

    public void addAllToHeadNoNotify(List<T> beans) {
        list_bean.addAll(0, beans);
    }
    //清空list,并且notifyDataSetChanged

    public void clear() {
        list_bean.clear();
        notifyDataSetChanged();
    }
    //清空list

    public void clearNoNotify() {
        list_bean.clear();
    }

    public int getSelectedPosition() {
        return selectedPosition;
    }

    public void setSelectedPosition(int selectedPosition) {
        //设置选中的item
        if (lastSelectedPosition == selectedPosition) {
            return;
        }
        this.selectedPosition = selectedPosition; //选择的position赋值给参数，
//        notifyItemChanged(selectedPosition);
//        notifyItemChanged(lastSelectedPosition);

        notifyDataSetChanged();
        lastSelectedPosition = selectedPosition;

    }

    public int getLastSelectedPosition() {
        return lastSelectedPosition;
    }



    /**
     * 如果想在ViewHolder添加方法,首先继承RVAdapter,然后继承此类，并且实现其构造方法
     */
    public static   class RVViewHolder extends RecyclerView.ViewHolder {
        private SparseArray<View> array_view;

        //构造方法
        public RVViewHolder(View itemView) {
            super(itemView);


            array_view = new SparseArray<View>();

        }


        //获取View
        public <T extends View> T getView(int viewId) {

            View view = array_view.get(viewId);
            if (view == null) {
                view = itemView.findViewById(viewId);
                array_view.put(viewId, view);
            }
            return (T) view;
        }

        //???????????????????????????????????????????????????????????????


        //设置View显示
        public RVViewHolder setVisible(int res_id) {
            getView(res_id).setVisibility(View.VISIBLE);
            return this;
        }
        //设置View隐藏

        public RVViewHolder setInVisible(int res_id) {
            getView(res_id).setVisibility(View.INVISIBLE);
            return this;
        }
        //设置View Gone

        public void setViewGone(int res_id) {
            getView(res_id).setVisibility(View.GONE);
        }


        //???????????????????????????????????????????????????????????????

        //null转空String
        public String nullToString(Object object) {
            return object == null ? "" : object.toString();
        }

        //设置TextView 的Text

        public RVViewHolder setText(int tv_id, Object text) {
            TextView tv = getView(tv_id);


            tv.setText(nullToString(text));

            return this;
        }


        //设置TextView 前面+¥
        public RVViewHolder setPriceText(int tv_id, Object text) {
            TextView tv = getView(tv_id);

            tv.setText("¥" + String.valueOf(text));

            return this;
        }


        //设置TextView或者EditText的TextColor
        public RVViewHolder setTextColor(int tv_id, int color) {
            TextView tv = getView(tv_id);
            tv.setTextColor(color);

            return this;
        }

        //获取TextView的文本值(去空格)

        public String getTVText(int tv_id) {
            TextView tv = getView(tv_id);
            return tv.getText().toString().trim();
        }

        //获取EditText的文本值(去空格)
        public String getETText(int tv_id) {
            EditText tv = getView(tv_id);
            return tv.getText().toString().trim();
        }

        //???????????????????????????????????????????????????????????????

        //设置View的BackgroundResource

        public RVViewHolder setBackgroundResource(int v_id, int resid) {
            View view = getView(v_id);
            view.setBackgroundResource(resid);

            return this;
        }

        //设置ImageView的ImageBitmap
        public RVViewHolder setImageBitmap(int iv_id, Bitmap bitmap) {
            ImageView view = getView(iv_id);
            view.setImageBitmap(bitmap);

            return this;
        }

        //设置ImageView的ImageResource

        public RVViewHolder setImageResource(int iv_id, int resID) {
            ImageView view = getView(iv_id);
            view.setImageResource(resID);

            return this;
        }

        //???????????????????????????????????????????????????????????????
        //Glide 记载网络和本地图片

        /**
         *
         * @param context 注意：context必须传入fragment级别以上，不然会导致fragment或者activity被回收后，glide依然在执行任务
         * @param iv_id
         * @param url
         * @return
         */
        public RVViewHolder setImage(Context context,int iv_id, String url) {
            ImageView iv = getView(iv_id);

            GlideUtils.loadImageByGlide(context, url, iv);

            return this;
        }
        //Glide 记载网络和本地图片
        /**
         *
         * @param context 注意：context必须传入fragment级别以上，不然会导致fragment或者activity被回收后，glide依然在执行任务
         * @param iv_id
         * @param url
         * @return
         */
        public RVViewHolder setImage(Context context,int iv_id, String url, int default_res) {
            ImageView iv = getView(iv_id);

            GlideUtils.loadImageByGlide(context, url, iv, default_res);

            return this;
        }
        //Glide 记载网络和本地图片
        /**
         *
         * @param context 注意：context必须传入fragment级别以上，不然会导致fragment或者activity被回收后，glide依然在执行任务
         * @param iv_id
         * @param url
         * @return
         */
        public RVViewHolder setImage(Context context,int iv_id, String url, int width, int height) {
            ImageView iv = getView(iv_id);

            GlideUtils.loadImageByGlide(context, url, iv, width, height);

            return this;
        }
        //Glide 记载网络和本地图片
        /**
         *
         * @param context 注意：context必须传入fragment级别以上，不然会导致fragment或者activity被回收后，glide依然在执行任务
         * @param iv_id
         * @param url
         * @return
         */

        public RVViewHolder setImage(Context context,int iv_id, String url, int width, int height, int default_res) {
            ImageView iv = getView(iv_id);

            GlideUtils.loadImageByGlide(context, url, iv, width, height, default_res);

            return this;
        }


        //???????????????????????????????????????????????????????????????
        //设置进度条进度
        public void setProgress(int progress_id, int progress) {
            ProgressBar progressBar = getView(progress_id);
            progressBar.setProgress(progress);

        }

        //???????????????????????????????????????????????????????????????
        //设置点击监听
        public void setOnClickListener(int res_id, View.OnClickListener onClickListener) {
            getView(res_id).setOnClickListener(onClickListener);
        }

        //设置长按监听
        public void setOnLongClickListener(int res_id, View.OnLongClickListener onLongClickListener) {
            getView(res_id).setOnLongClickListener(onLongClickListener);
        }


    }


}
