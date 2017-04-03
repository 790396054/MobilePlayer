package com.app.gmm.mobileplyer.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by gmm on 2017/4/3.
 */

public class CachesUtils {

    public static final String ATGUIGU = "atguigu";

    public static void putString(Context context, String key, String value) {
        SharedPreferences sp = context.getSharedPreferences(ATGUIGU, Context.MODE_PRIVATE);
        sp.edit().putString(key, value).commit();
    }

    public static String getString(Context context, String key, String defaultValue) {
        SharedPreferences sp = context.getSharedPreferences(ATGUIGU, Context.MODE_PRIVATE);
        return sp.getString(key, defaultValue);
    }

    public static void putInt(Context context, String key, int value) {
        SharedPreferences sp = context.getSharedPreferences(ATGUIGU, Context.MODE_PRIVATE);
        sp.edit().putInt(key, value).commit();
    }

    public static int getInt(Context context, String key, int defalutValue) {
        SharedPreferences sp = context.getSharedPreferences(ATGUIGU, Context.MODE_PRIVATE);
        return sp.getInt(key, defalutValue);
    }
}
