//package com.cy.rvadapterniubility;
//
//import android.util.Log;
//
//import com.cy.BaseAdapter.BuildConfig;
//
///**
// * Created by lenovo on 2017/8/20.
// */
//
//public class LogUtils {
//    private static boolean DEBUG = BuildConfig.DEBUG;
//
//    private LogUtils() {
//    }
//
//    public static synchronized void debug(boolean debug) {
//        DEBUG = debug;
//    }
//
//    public static void logI(Object tag, Object content) {
//        if(!DEBUG)return;
//        if (tag == null) tag = "LOG_I";
//        Log.i(String.valueOf(tag), "----------------------------------->>>>" + content);
//    }
//    public static void logE(Object tag, Object content) {
//        if(!DEBUG)return;
//        if (tag == null) tag = "LOG_E";
//        Log.e(String.valueOf(tag), "----------------------------------->>>>" + content);
//    }
//
//
//    public static void logI(Object content) {
//        logI(null,content);
//    }
//    public static void logE(Object content) {
//        logE(null,content);
//    }
//
//    public static void log(Object tag, Object content) {
//        logE(tag,content);
//    }
//    public static void log(Object content) {
//        logE(content);
//    }
//}
