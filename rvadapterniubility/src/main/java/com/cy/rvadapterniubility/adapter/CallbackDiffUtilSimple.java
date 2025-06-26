package com.cy.rvadapterniubility.adapter;

import java.util.List;

public abstract class CallbackDiffUtilSimple<T> extends CallbackDiffUtil<T> {
    public CallbackDiffUtilSimple(List<T> listOld, List<T> listNew) {
        super(listOld, listNew);
    }
    @Override
    public final boolean areItemsTheSame(int oldItemPosition, int newItemPosition, T beanOld, T beanNew) {
        return areDataTheSame(oldItemPosition, newItemPosition, listOld.get(oldItemPosition), listNew.get(newItemPosition));
    }

    @Override
    public final boolean areContentsTheSame(int oldItemPosition, int newItemPosition, T beanOld, T beanNew) {
        return areDataTheSame(oldItemPosition, newItemPosition, listOld.get(oldItemPosition), listNew.get(newItemPosition));
    }
    public abstract boolean areDataTheSame(int oldItemPosition, int newItemPosition, T beanOld, T beanNew);
}
