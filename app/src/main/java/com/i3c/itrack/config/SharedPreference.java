package com.i3c.itrack.config;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreference {
    public static final String PREFS_NAME = "iTRACK";
    public static final String USER_ID = "USER_ID";
    public static final String USER_NAME = "USER_NAME";
    public Context context;

    public SharedPreference(Context context) {
        this.context = context;
    }


    public void setValue(String key,String val){
        SharedPreferences settings;
        SharedPreferences.Editor editor;

        settings=context.getSharedPreferences(PREFS_NAME,context.MODE_PRIVATE);
        editor=settings.edit();
        editor.putString(key,val);
        editor.commit();
    }
    public String getValue(String key){
        SharedPreferences settings;
        settings=context.getSharedPreferences(PREFS_NAME,context.MODE_PRIVATE);

        if(settings.contains(key)){
            return (settings.getString(key,null));
        }
        else{
            return null;
        }
    }
    public void setUserID(String code){
        SharedPreferences settings;
        SharedPreferences.Editor editor;

        settings=context.getSharedPreferences(PREFS_NAME,context.MODE_PRIVATE);
        editor=settings.edit();
        editor.putString(USER_ID,code);
        editor.commit();
    }
    public String getUserID(){
        SharedPreferences settings;
        settings=context.getSharedPreferences(PREFS_NAME,context.MODE_PRIVATE);

        if(settings.contains(USER_ID)){
            return (settings.getString(USER_ID,null));
        }
        else{
            return null;
        }
    }
    public String getUserName(){
        SharedPreferences settings;
        settings=context.getSharedPreferences(PREFS_NAME,context.MODE_PRIVATE);

        if(settings.contains(USER_NAME)){
            return (settings.getString(USER_NAME,null));
        }
        else{
            return null;
        }
    }
}
