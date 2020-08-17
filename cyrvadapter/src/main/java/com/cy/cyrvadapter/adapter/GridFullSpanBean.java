package com.cy.cyrvadapter.adapter;

/**
 * @Description:
 * @Author: cy
 * @CreateDate: 2020/7/15 15:41
 * @UpdateUser:
 * @UpdateDate: 2020/7/15 15:41
 * @UpdateRemark:
 * @Version:
 */
public class GridFullSpanBean {
    private int decorationLeft=0;
    private int decorationTop=0;
    private int decorationRight=0;
    private int decorationBottom=0;

    public GridFullSpanBean() {
    }

    public GridFullSpanBean(int decorationLeft, int decorationTop, int decorationRight, int decorationBottom) {
        this.decorationLeft = decorationLeft;
        this.decorationTop = decorationTop;
        this.decorationRight = decorationRight;
        this.decorationBottom = decorationBottom;
    }


    public int getDecorationLeft() {
        return decorationLeft;
    }

    public GridFullSpanBean setDecorationLeft(int decorationLeft) {
        this.decorationLeft = decorationLeft;
        return this;
    }

    public int getDecorationTop() {
        return decorationTop;
    }

    public GridFullSpanBean setDecorationTop(int decorationTop) {
        this.decorationTop = decorationTop;
        return this;
    }

    public int getDecorationRight() {
        return decorationRight;
    }

    public GridFullSpanBean setDecorationRight(int decorationRight) {
        this.decorationRight = decorationRight;
        return this;

    }

    public int getDecorationBottom() {
        return decorationBottom;
    }

    public GridFullSpanBean setDecorationBottom(int decorationBottom) {
        this.decorationBottom = decorationBottom;
        return this;
    }
}
