package com.cy.recyclerviewadapter;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by lenovo on 2017/8/2.
 */

public class ToastUtils {
    public static  void showToast(Context context,String string){
        Toast.makeText(context,string ,Toast.LENGTH_SHORT).show();
    }
}
