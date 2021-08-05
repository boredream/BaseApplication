package com.boredream.baseapplication.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

public class SpUtils {

    private static final String SP_NAME = "config";

    public static SharedPreferences getSharedPreferences() {
        return AppKeeper.getApp().getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
    }

    public static void clear(String key) {
        getSharedPreferences().edit().remove(key).apply();
    }

    public static void putBoolean(String key, boolean value) {
        getSharedPreferences().edit().putBoolean(key, value).apply();
    }

    public static boolean getBoolean(String key, boolean defaultValue) {
        return getSharedPreferences().getBoolean(key, defaultValue);
    }

    public static void putString(String key, String value) {
        getSharedPreferences().edit().putString(key, value).apply();
    }

    public static String getString(String key) {
        return getSharedPreferences().getString(key, null);
    }

    public static void putInt(String key, int value) {
        getSharedPreferences().edit().putInt(key, value).apply();
    }

    public static int getInt(String key) {
        return getSharedPreferences().getInt(key, 0);
    }

    public static int getInt(String key, int defaultValue) {
        return getSharedPreferences().getInt(key, defaultValue);
    }

    public static <T> void save(String key, T model) {
        getSharedPreferences().edit().putString(key, new Gson().toJson(model)).apply();
    }

    public static <T> T get(String key, Class<T> clazz) {
        String json = getSharedPreferences().getString(key, null);
        try {
            return new Gson().fromJson(json, clazz);
        } catch (Throwable t) {
            t.printStackTrace();
            return null;
        }
    }

}
