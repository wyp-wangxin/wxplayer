package com.wyp.wxplayer.utils;

import android.util.Log;

/**
 * Created by wyp on 2021/1/31.
 */

public class myLog {
    private static final boolean ENABLE = true;

    public static void e(String tag,String msg){
        if (ENABLE)
            Log.e("wwxx "+tag,msg);
    }

    public static void e(Class cls,String msg){
        if (ENABLE)
            Log.e("wwxx "+cls.getSimpleName(),msg);
    }
}
