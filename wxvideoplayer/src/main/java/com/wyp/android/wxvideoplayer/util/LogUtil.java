package com.wyp.android.wxvideoplayer.util;

import android.util.Log;

/**
 * Created by XiaoJianjun on 2017/5/4.
 * log工具.
 */
public class LogUtil {

    private static final String TAG = "wwxx wxVideoPlayer";

    public static void d(String message) {
        Log.d(TAG, message);
    }

    public static void e(String message, Throwable throwable) {
        Log.e(TAG, message, throwable);
    }
}
