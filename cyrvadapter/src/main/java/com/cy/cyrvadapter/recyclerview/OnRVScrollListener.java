package com.cy.cyrvadapter.recyclerview;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.bumptech.glide.Glide;

/**
 * Created by leifeng on 2018/8/30.
 */

public class OnRVScrollListener extends RecyclerView.OnScrollListener {

    private Context context;

    /**
     *        最好使用getApplicationContext否则Glide容易内存泄漏

     * @param context
     */
    public OnRVScrollListener(Context context) {
        this.context = context;
    }

    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        super.onScrollStateChanged(recyclerView, newState);
        if (newState == RecyclerView.SCROLL_STATE_DRAGGING || newState == RecyclerView.SCROLL_STATE_SETTLING) {

            Glide.with(context).pauseRequests();
        } else if (newState == RecyclerView.SCROLL_STATE_IDLE) {
            Glide.with(context).resumeRequests();


        }
    }
}
