package com.cy.cyrvadapter.refreshlayout;

import android.view.View;

/**
 * Created by lcodecore on 2016/10/1.面向对象的精髓多态，接口的强扩展性
 */

public interface IAnimationView {

    public View getView();

    public <T extends IAnimationView> T setColor(int color);

    public void openLoadAnimation(IAnimationViewCallback animationViewCallback);

    /**
     * 开始load动画
     */
    public void startLoadAnimation();

    /**
     * 停止load动画
     */
    public void stopLoadAnimation();


    public void closeLoadAnimation(IAnimationViewCallback animationViewCallback);

    /**
     * 停止所有动画
     */
    public void cancelAllAnimation();

    public boolean isRunning();

    public void onDraging(int height_current, int height_load, int height_max);


    public void onDragClosed();

    public static interface IAnimationViewCallback {

        public void onLoadOpened();

        public void onLoadClosed();
    }
}
