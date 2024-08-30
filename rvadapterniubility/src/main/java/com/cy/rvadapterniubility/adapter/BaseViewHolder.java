package com.cy.rvadapterniubility.adapter;

import android.graphics.Bitmap;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.IdRes;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.cy.BaseAdapter.R;

public class BaseViewHolder extends RecyclerView.ViewHolder {
    private SparseArray<View> array_view;
    private boolean isFullSpan = false;

    public BaseViewHolder(View itemView) {
        super(itemView);
        array_view = new SparseArray<View>();

    }

    //获取View
    public <T extends View> T getView(@IdRes int viewId) {
        View view = array_view.get(viewId);
        if (view == null) {
            view = itemView.findViewById(viewId);
            array_view.put(viewId, view);
        }
        return (T) view;
    }

    public BaseViewHolder putView(@IdRes int viewId, View view) {
        array_view.put(viewId, view);
        return this;
    }

    public boolean isFullSpan() {
        return isFullSpan;
    }

    public void setFullSpan(boolean fullSpan) {
        isFullSpan = fullSpan;
    }
//???????????????????????????????????????????????????????????????


    //设置View显示
    public BaseViewHolder setVisible(@IdRes int res_id) {
        getView(res_id).setVisibility(View.VISIBLE);
        return this;
    }
    //设置View隐藏

    public BaseViewHolder setInVisible(@IdRes int res_id) {
        getView(res_id).setVisibility(View.INVISIBLE);
        return this;
    }
    //设置View Gone

    public void setGone(@IdRes int res_id) {
        getView(res_id).setVisibility(View.GONE);
    }

    public void setVisibility(@IdRes int res_id,  int visibility) {
        getView(res_id).setVisibility(visibility);
    }

    //???????????????????????????????????????????????????????????????

    /**
     * 防止图片先显示复用ITEM的图片再显示自己的
     *
     * @param res_id
     * @param tag
     * @param callbackIVTag
     */
    public void loadPicWithTag(@IdRes int res_id, Object tag, CallbackIVTag callbackIVTag) {
        //防止图片先显示复用ITEM的图片再显示自己的
        if (getTag(res_id) != null && getTag(res_id).equals(tag))
            callbackIVTag.onTagEquls(tag);
    }
    public void setHeight(@IdRes int res_id,int height){
        // 在加载图片之前设定好图片的宽高，防止出现item错乱及闪烁
        View view=getView(res_id);
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        layoutParams.height = height;
        view.setLayoutParams(layoutParams);
    }
    public void setWidth(@IdRes int res_id,int width){
        // 在加载图片之前设定好图片的宽高，防止出现item错乱及闪烁
        View view=getView(res_id);
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        layoutParams.width = width;
        view.setLayoutParams(layoutParams);
    }
    public void setLayoutParams(@IdRes int res_id,int width,int height){
        // 在加载图片之前设定好图片的宽高，防止出现item错乱及闪烁
        View view=getView(res_id);
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        layoutParams.width = width;
        layoutParams.height = height;
        view.setLayoutParams(layoutParams);
    }

    /**
     * 防止图片先显示复用ITEM的图片再显示自己的
     *
     * @param res_id
     * @param tag
     */
    public void setImageViewTag(@IdRes int res_id, Object tag) {
        setImageBitmap(res_id, null);
        setTag(res_id, tag);
    }

    public void setTag(@IdRes int res_id, Object tag) {
        getView(res_id).setTag(tag);
    }

    @Nullable
    public Object getTag(@IdRes int res_id) {
        return getView(res_id).getTag();
    }

    //null转空String
    public String nullToString(Object object) {
        return object == null ? "" : object.toString();
    }

    //设置TextView 的Text

    public BaseViewHolder setText(@IdRes int tv_id, Object text) {
        TextView tv = getView(tv_id);
        tv.setText(nullToString(text));
        return this;
    }


    //设置TextView 前面+¥
    public BaseViewHolder setPriceText(@IdRes int tv_id, Object text) {
        TextView tv = getView(tv_id);
        tv.setText("¥" + String.valueOf(text));
        return this;
    }


    //设置TextView或者EditText的TextColor
    public BaseViewHolder setTextColor(@IdRes int tv_id, int color) {
        TextView tv = getView(tv_id);
        tv.setTextColor(color);
        return this;
    }

    //获取TextView的文本值(去空格)

    public String getTVText(@IdRes int tv_id) {
        TextView tv = getView(tv_id);
        return tv.getText().toString().trim();
    }

    //获取EditText的文本值(去空格)
    public String getETText(@IdRes int tv_id) {
        EditText tv = getView(tv_id);
        return tv.getText().toString().trim();
    }

    //???????????????????????????????????????????????????????????????

    //设置View的BackgroundResource

    public BaseViewHolder setBackgroundResource(@IdRes int v_id, int resid) {
        View view = getView(v_id);
        view.setBackgroundResource(resid);
        return this;
    }

    //设置ImageView的ImageBitmap
    public BaseViewHolder setImageBitmap(@IdRes int iv_id, Bitmap bitmap) {
        ImageView view = getView(iv_id);
        view.setImageBitmap(bitmap);
        return this;
    }

    //设置ImageView的ImageResource

    public BaseViewHolder setImageResource(@IdRes int iv_id, int resID) {
        ImageView view = getView(iv_id);
        view.setImageResource(resID);
        return this;
    }


    //???????????????????????????????????????????????????????????????
    //设置进度条进度
    public void setProgress(@IdRes int progress_id, int progress) {
        ProgressBar progressBar = getView(progress_id);
        progressBar.setProgress(progress);
    }

    //???????????????????????????????????????????????????????????????
    //设置点击监听
    public void setOnClickListener(@IdRes int res_id, View.OnClickListener onClickListener) {
        getView(res_id).setOnClickListener(onClickListener);
    }

    //设置长按监听
    public void setOnLongClickListener(@IdRes int res_id, View.OnLongClickListener onLongClickListener) {
        getView(res_id).setOnLongClickListener(onLongClickListener);
    }

}
