package com.cy.http.utils;

import android.util.Log;

/**
 * Created by lenovo on 2017/8/20.
 */

public class LogUtils {
    private static boolean printLog=true;

    public static void debug(boolean printLog) {
        LogUtils.printLog = printLog;
    }

    public static void log(String tag, Object content) {
        if (!printLog)return;
        if (tag==null)tag="LOG_E";

        Log.e(tag, "----------------------------------->>>>" + content);
    }
    public static void log(Object tag, Object content) {
        if (!printLog)return;

        if (tag==null)tag="LOG_E";

        Log.e(String.valueOf(tag), "----------------------------------->>>>" + content);
    }

    public static void log(String tag) {
        if (!printLog)return;

        if (tag==null)tag="LOG_E";

        Log.e(tag, "----------------------------------->>>>" );

    }

    public static void log(Object tag) {
        if (!printLog)return;

        if (tag==null)tag="LOG_E";
        Log.e(String.valueOf(tag), "---------------------------------->>>>" );

    }
}

