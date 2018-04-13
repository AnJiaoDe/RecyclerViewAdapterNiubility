package com.cy.recyclerviewadapter.bean;

/**
 * Created by cy on 2018/4/6.
 */

public class VRMultiBean {

    private String title;
    private int [] resID;
    private int view_type;

    public VRMultiBean(String title, int[] resID, int view_type) {
        this.title = title;
        this.resID = resID;
        this.view_type = view_type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int[] getResID() {
        return resID;
    }

    public void setResID(int[] resID) {
        this.resID = resID;
    }

    public int getView_type() {
        return view_type;
    }

    public void setView_type(int view_type) {
        this.view_type = view_type;
    }
}
