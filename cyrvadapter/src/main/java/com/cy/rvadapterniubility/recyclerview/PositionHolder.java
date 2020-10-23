package com.cy.rvadapterniubility.recyclerview;

/**
 * @Description:
 * @Author: cy
 * @CreateDate: 2020/10/23 18:25
 * @UpdateUser:
 * @UpdateDate: 2020/10/23 18:25
 * @UpdateRemark:
 * @Version:
 */
public class PositionHolder {
    private int[] firstVisibleItemPositions;
    private int[] firstCompletelyVisibleItemPositions;
    private int[] lastVisibleItemPositions;
    private int[] lastCompletelyVisibleItemPositions;

    public PositionHolder(int[] firstVisibleItemPositions, int[] firstCompletelyVisibleItemPositions, int[] lastVisibleItemPositions, int[] lastCompletelyVisibleItemPositions) {
        this.firstVisibleItemPositions = firstVisibleItemPositions;
        this.firstCompletelyVisibleItemPositions = firstCompletelyVisibleItemPositions;
        this.lastVisibleItemPositions = lastVisibleItemPositions;
        this.lastCompletelyVisibleItemPositions = lastCompletelyVisibleItemPositions;
    }

    public int[] getFirstVisibleItemPositions() {
        return firstVisibleItemPositions;
    }

    public PositionHolder setFirstVisibleItemPositions(int[] firstVisibleItemPositions) {
        this.firstVisibleItemPositions = firstVisibleItemPositions;
        return this;
    }

    public int[] getFirstCompletelyVisibleItemPositions() {
        return firstCompletelyVisibleItemPositions;
    }

    public PositionHolder setFirstCompletelyVisibleItemPositions(int[] firstCompletelyVisibleItemPositions) {
        this.firstCompletelyVisibleItemPositions = firstCompletelyVisibleItemPositions;
        return this;
    }

    public int[] getLastVisibleItemPositions() {
        return lastVisibleItemPositions;
    }

    public PositionHolder setLastVisibleItemPositions(int[] lastVisibleItemPositions) {
        this.lastVisibleItemPositions = lastVisibleItemPositions;
        return this;
    }

    public int[] getLastCompletelyVisibleItemPositions() {
        return lastCompletelyVisibleItemPositions;
    }

    public PositionHolder setLastCompletelyVisibleItemPositions(int[] lastCompletelyVisibleItemPositions) {
        this.lastCompletelyVisibleItemPositions = lastCompletelyVisibleItemPositions;
        return this;
    }
}
