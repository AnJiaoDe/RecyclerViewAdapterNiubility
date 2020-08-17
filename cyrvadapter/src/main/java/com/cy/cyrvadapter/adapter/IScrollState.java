package com.cy.cyrvadapter.adapter;


import com.cy.cyrvadapter.recyclerview.OnVerticalScrollListener;

public interface IScrollState<T extends SimpleAdapter> extends IExpandAdapter<T> {

    public void onFirstOnScrolled(OnVerticalScrollListener.PositionHolder positionHolder);

    public void onScrollArrivedTop(OnVerticalScrollListener.PositionHolder positionHolder);

    public void onScrollArrivedBottom(OnVerticalScrollListener.PositionHolder positionHolder);

    public void onScrollingUp(int dy);

    public void onScrollingDown(int dy);

    public void onIdle(OnVerticalScrollListener.PositionHolder positionHolder);

    public void onDragging(OnVerticalScrollListener.PositionHolder positionHolder);

    public void onSettling(OnVerticalScrollListener.PositionHolder positionHolder);

}
