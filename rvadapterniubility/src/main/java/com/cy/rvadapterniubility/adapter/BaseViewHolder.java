package com.cy.rvadapterniubility.adapter;

import android.graphics.Bitmap;
import android.util.SparseArray;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class BaseViewHolder extends RecyclerView.ViewHolder {
    private SparseArray<View> array_view;
    private boolean isFullSpan=false;
    public BaseViewHolder(View itemView) {
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
    public BaseViewHolder putView(int viewId, View view){
        array_view.put(viewId,view);
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
    public BaseViewHolder setVisible(int res_id) {
        getView(res_id).setVisibility(View.VISIBLE);
        return this;
    }
    //设置View隐藏

    public BaseViewHolder setInVisible(int res_id) {
        getView(res_id).setVisibility(View.INVISIBLE);
        return this;
    }
    //设置View Gone

    public void setGone(int res_id) {
        getView(res_id).setVisibility(View.GONE);
    }


    //???????????????????????????????????????????????????????????????

    //null转空String
    public String nullToString(Object object) {
        return object == null ? "" : object.toString();
    }

    //设置TextView 的Text

    public BaseViewHolder setText(int tv_id, Object text) {
        TextView tv = getView(tv_id);
        tv.setText(nullToString(text));
        return this;
    }


    //设置TextView 前面+¥
    public BaseViewHolder setPriceText(int tv_id, Object text) {
        TextView tv = getView(tv_id);
        tv.setText("¥" + String.valueOf(text));
        return this;
    }


    //设置TextView或者EditText的TextColor
    public BaseViewHolder setTextColor(int tv_id, int color) {
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

    public BaseViewHolder setBackgroundResource(int v_id, int resid) {
        View view = getView(v_id);
        view.setBackgroundResource(resid);
        return this;
    }

    //设置ImageView的ImageBitmap
    public BaseViewHolder setImageBitmap(int iv_id, Bitmap bitmap) {
        ImageView view = getView(iv_id);
        view.setImageBitmap(bitmap);
        return this;
    }

    //设置ImageView的ImageResource

    public BaseViewHolder setImageResource(int iv_id, int resID) {
        ImageView view = getView(iv_id);
        view.setImageResource(resID);
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
