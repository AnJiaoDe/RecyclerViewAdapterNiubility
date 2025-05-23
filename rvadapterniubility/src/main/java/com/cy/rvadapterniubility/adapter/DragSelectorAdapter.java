package com.cy.rvadapterniubility.adapter;

import android.os.Handler;
import android.util.SparseArray;
import android.view.View;

import androidx.annotation.Nullable;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by cy on 2018/3/29.类似策略模式,引入IAdapter接口，面向多态编程
 */

public abstract class DragSelectorAdapter<T> implements IAdapter<T, BaseViewHolder, SimpleAdapter> {
    private SimpleAdapter<T> simpleAdapter;
    private Set<Integer> setSelector;
    private boolean useDragSelect=false;
    private int position_start=0,position_end=0;

    public DragSelectorAdapter() {
        setSelector=new HashSet<>();
        simpleAdapter = new SimpleAdapter<T>() {
            @Override
            public void recycleData(@Nullable Object tag) {
                DragSelectorAdapter.this.recycleData(tag);
            }

            @Nullable
            @Override
            public Object setHolderTagPreBindData(BaseViewHolder holder, int position, T bean) {
                return DragSelectorAdapter.this.setHolderTagPreBindData(holder, position, bean);
            }

            @Override
            public void bindDataToView(final BaseViewHolder holder, int position, T bean) {
                DragSelectorAdapter.this.bindDataToView(holder, position, bean,setSelector.contains(position));
            }

            @Override
            public int getItemLayoutID(int position, T bean) {
                return DragSelectorAdapter.this.getItemLayoutID(position, bean);
            }

            @Override
            public void onItemClick(BaseViewHolder holder, int position, T bean) {
                DragSelectorAdapter.this.onItemClick(holder, position, bean);
            }

            @Override
            public void onItemLongClick(BaseViewHolder holder, int position, T bean) {
                DragSelectorAdapter.this.onItemLongClick(holder, position, bean);
            }

            @Override
            public void onItemMove(int fromPosition, int toPosition, BaseViewHolder srcHolder, BaseViewHolder targetHolder) {
                super.onItemMove(fromPosition, toPosition, srcHolder, targetHolder);
                DragSelectorAdapter.this.onItemMove(fromPosition, toPosition, srcHolder, targetHolder);
            }
        };
    }

    public boolean isUseDragSelect() {
        return useDragSelect;
    }

    public void startDragSelect(int position_start) {
        this.useDragSelect = true;
        this.position_start=position_start;
    }
    public void stopDragSelect(){
        useDragSelect=false;
    }
    public void toggle(final int position,boolean select){
        if(select){
            setSelector.add(position);
        }else{
            setSelector.remove(position);
        }
        //必须用handler，否则GG  Cannot call this method while RecyclerView is computing a layout or scrolling
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                simpleAdapter.notifyItemChanged(position);
            }
        });
    }
    public void toggleRange(final int position_start, final int position_end, boolean select){
        for (int i = position_start; i <= position_end; i++) {
            if (select)
                setSelector.add(i);
            else
                setSelector.remove(i);
        }
        //必须用handler，否则GG  Cannot call this method while RecyclerView is computing a layout or scrolling
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                simpleAdapter.notifyItemRangeChanged(position_start,position_end-position_start+1);
            }
        });
    }

    @Override
    public void recycleData(@Nullable Object tag) {

    }

    @Nullable
    @Override
    public Object setHolderTagPreBindData(BaseViewHolder holder, int position, T bean) {
        return null;
    }

    @Override
    public final void bindDataToView(BaseViewHolder holder, int position, T bean) {

    }

    public abstract void bindDataToView(BaseViewHolder holder, int position, T bean, boolean isSelected);

    @Override
    public void onItemLongClick(BaseViewHolder holder, int position, T bean) {

    }

    @Override
    public void onItemMove(int fromPosition, int toPosition, BaseViewHolder srcHolder, BaseViewHolder targetHolder) {

    }

    @Override
    public SimpleAdapter<T> getAdapter() {
        return simpleAdapter;
    }
}
