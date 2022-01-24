package com.example.project;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.appcompat.app.AppCompatActivity;

public class RbPreference {

    private final String PREF_NAME = "com.example.project";

    static Context mContext;

    public RbPreference(Context context){
        mContext = context;
    }

    public void put(String key, String value){
        SharedPreferences pref = mContext.getSharedPreferences(PREF_NAME, AppCompatActivity.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public void putUrl(String key, String value){
        SharedPreferences pref = mContext.getSharedPreferences(PREF_NAME, AppCompatActivity.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public String getValue(String key, String dftValue){
        SharedPreferences pref = mContext.getSharedPreferences(PREF_NAME, AppCompatActivity.MODE_PRIVATE);
        try{
            return pref.getString(key, dftValue);
        }catch (Exception e) {
            return dftValue;
        }
    }

    public String getValueUrl(String key, String dftValue){
        SharedPreferences pref = mContext.getSharedPreferences(PREF_NAME, AppCompatActivity.MODE_PRIVATE);
        try{
            return pref.getString(key, dftValue);
        }catch (Exception e) {
            return dftValue;
        }
    }
}
