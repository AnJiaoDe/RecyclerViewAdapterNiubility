package com.cy.rvadapterniubility.adapter;

public abstract class AnimAdapter<T> extends SimpleAdapter<T> {
    /**
     * 必须返回false，否则无法实现拖拽动画
     * @return
     */
    @Override
    public final boolean hasStableIds_() {
        return false;
    }

}
