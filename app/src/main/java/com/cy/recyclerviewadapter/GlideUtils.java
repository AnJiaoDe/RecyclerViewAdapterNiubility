package com.cy.recyclerviewadapter;

import android.content.Context;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;

/**
 * @Description:
 * @Author: cy
 * @CreateDate: 2020/11/10 16:49
 * @UpdateUser:
 * @UpdateDate: 2020/11/10 16:49
 * @UpdateRemark:
 * @Version:
 */

public class GlideUtils {
    public static RequestManager getRequestManager(Context context, CallbackRequestManager callbackRequestManager) {
        RequestManager requestManager = null;
        try {
            requestManager = Glide.with(context);
        } catch (Exception e) {

        }
        if (requestManager != null)
            callbackRequestManager.onRequestManagerGeted(requestManager);
        return requestManager;
    }
    public static interface CallbackRequestManager {
        public void onRequestManagerGeted(RequestManager requestManager);
    }
}
