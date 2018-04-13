package com.cy.recyclerviewadapter.bean;

/**
 * Created by cy on 2018/4/10.
 */

public class SGRVBean {

    private int width_item;
    private int height_item;


    private int margin_left;
    private int margin_top;
    private int margin_right;
    private int margin_bottom;

    private String text;
    private String url;

    public SGRVBean(String url, String text) {
        this.url = url;
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getWidth_item() {
        return width_item;
    }

    public void setWidth_item(int width_item) {
        this.width_item = width_item;
    }

    public int getHeight_item() {
        return height_item;
    }

    public void setHeight_item(int height_item) {
        this.height_item = height_item;
    }

    public int getMargin_left() {
        return margin_left;
    }

    public void setMargin_left(int margin_left) {
        this.margin_left = margin_left;
    }

    public int getMargin_top() {
        return margin_top;
    }

    public void setMargin_top(int margin_top) {
        this.margin_top = margin_top;
    }

    public int getMargin_right() {
        return margin_right;
    }

    public void setMargin_right(int margin_right) {
        this.margin_right = margin_right;
    }

    public int getMargin_bottom() {
        return margin_bottom;
    }

    public void setMargin_bottom(int margin_bottom) {
        this.margin_bottom = margin_bottom;
    }
}
