package com.boredream.baseapplication.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SpUtils {

    private static final String SP_NAME = "config";

    public static SharedPreferences getSharedPreferences() {
        return AppKeeper.getApp().getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
    }

    public static void putBoolean(String key, boolean value) {
        AppKeeper.getApp().getSharedPreferences(SP_NAME, Context.MODE_PRIVATE).edit().putBoolean(key, value).apply();
    }

    public static boolean getBoolean(String key, boolean defaultValue) {
        return AppKeeper.getApp().getSharedPreferences(SP_NAME, Context.MODE_PRIVATE).getBoolean(key, defaultValue);
    }

    public static void putString(String key, String value) {
        AppKeeper.getApp().getSharedPreferences(SP_NAME, Context.MODE_PRIVATE).edit().putString(key, value).apply();
    }

    public static String getString(String key) {
        return AppKeeper.getApp().getSharedPreferences(SP_NAME, Context.MODE_PRIVATE).getString(key, null);
    }

    public static void putInt(String key, int value) {
        AppKeeper.getApp().getSharedPreferences(SP_NAME, Context.MODE_PRIVATE).edit().putInt(key, value).apply();
    }

    public static int getInt(String key) {
        return AppKeeper.getApp().getSharedPreferences(SP_NAME, Context.MODE_PRIVATE).getInt(key, 0);
    }

    public static int getInt(String key, int defaultValue) {
        return AppKeeper.getApp().getSharedPreferences(SP_NAME, Context.MODE_PRIVATE).getInt(key, defaultValue);
    }


}
