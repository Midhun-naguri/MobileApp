package com.android.restaurentsapp.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class PrefHelper {

    Context context;
    private int PRIVATE_MODE = 0;
    private String PREF_NAME = "com.android.restaurentsapp";
    private SharedPreferences sharedPreferences = null;
    private SharedPreferences.Editor prefEditor = null;

    public PrefHelper(Context cContext)
    {
        this.context = cContext;
        sharedPreferences = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        prefEditor = sharedPreferences.edit();
    }

    public void putInt(String key, int value)
    {
        prefEditor.putInt(key, value);
        prefEditor.commit();
    }

    public void putString(String key, String value)
    {
        prefEditor.putString(key, value);
        prefEditor.commit();
    }

    public void putBoolean(String key, boolean value)
    {
        prefEditor.putBoolean(key, value);
        prefEditor.commit();
    }

    public void putLong(String key, Long value)
    {
        prefEditor.putLong(key, value);
    }

    public void putFloat(String key, Float value)
    {
        prefEditor.putFloat(key, value);
    }

    public String getValue(String key) {
        return sharedPreferences.getString(key, "");
    }
    int getIntValue(String key) {
        return sharedPreferences.getInt(key, 0);
    }

    public Boolean getBool(String key){
        return sharedPreferences.getBoolean(key, false);
    }

    Boolean contains(String key){
        return sharedPreferences.contains(key);
    }

    void clear(){
        prefEditor.clear().commit();
        prefEditor.apply();
    }

}
