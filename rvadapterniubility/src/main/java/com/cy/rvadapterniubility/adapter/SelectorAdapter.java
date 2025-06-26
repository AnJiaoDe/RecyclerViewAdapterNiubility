package com.cy.rvadapterniubility.adapter;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.AsyncDifferConfig;
import androidx.recyclerview.widget.DiffUtil;

import com.cy.rvadapterniubility.swipelayout.OnSwipeListener;
import com.cy.rvadapterniubility.swipelayout.SwipeLayout;

import java.util.List;

public abstract class SelectorAdapter<T> extends SimpleAdapter<T> {
    private int positionSelectedLast = -1;
    private int positionSelected = 0;

    @Override
    public final void bindDataToView(BaseViewHolder holder, int position, T bean, @NonNull List<Object> payloads) {
        bindDataToView(holder, position, bean, position == getPositionSelected(),payloads);
    }

    @Override
    public final void onItemClick(BaseViewHolder holder, int position, T bean) {
        //设置选中的item
        if (positionSelectedLast != position) {
            positionSelectedLast = positionSelected;
            positionSelected = position; //选择的position赋值给参数，
            notifyItemChanged(positionSelectedLast);
            notifyItemChanged(positionSelected);

            positionSelectedLast = positionSelected;
        }
        onItemClick__(holder, position, getList_bean().get(position));
    }

    public abstract void onItemClick__(BaseViewHolder holder, int position, T bean);

    public abstract void bindDataToView(BaseViewHolder holder, int position, T bean, boolean isSelected,@NonNull List<Object> payloads);


    public int getPositionSelectedLast() {
        return positionSelectedLast;
    }

    public void setPositionSelectedLast(int positionSelectedLast) {
        this.positionSelectedLast = positionSelectedLast;
    }

    public int getPositionSelected() {
        return positionSelected;
    }

    public void setPositionSelected(int positionSelected) {
        if (positionSelectedLast != positionSelected) {
            this.positionSelected = positionSelected; //选择的position赋值给参数，
            notifyItemChanged(positionSelectedLast);
            notifyItemChanged(positionSelected);

            positionSelectedLast = positionSelected;
        }
    }

    public void setPositionSelectedNoNotify(int positionSelected) {
        this.positionSelected = positionSelected;
    }

    public void setPositionSelectedNotifyAll(int positionSelected) {
        if (positionSelectedLast != positionSelected) {
            this.positionSelected = positionSelected;
            notifyDataSetChanged();
            positionSelectedLast = positionSelected;
        }
    }
}
