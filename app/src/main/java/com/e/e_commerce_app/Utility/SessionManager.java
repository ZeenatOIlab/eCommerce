package com.e.e_commerce_app.Utility;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

import com.e.e_commerce_app.Model.ProfileAdressModel;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * Created by server3 on 4/15/2017.
 */
public class SessionManager {
    Context context;
    SharedPreferences pref;
    Editor editor;
    private static final String PREF_NAME = "codeplayon.com";
    private static final String IS_FIRST_TIME_LAUNCH = "IsFirstTimeLaunch";

    // Constructor
    public SessionManager(Context context) {
        this.context = context;
        pref = this.context.getSharedPreferences("My_Pref", Context.MODE_PRIVATE);
        editor = pref.edit();
    }




    public void addString(String key, String str) {
        editor.putString(key, str);
        editor.commit();
    }

    public String getString(String key) {

        return pref.getString(key, "");
    }

    public void addBoolean(String key, boolean str) {
        editor.putBoolean(key, str);
        editor.commit();
    }

    public boolean getBoolean(String key) {

        return pref.getBoolean(key, false);
    }

    public void addInt(String key, int value) {
        editor.putInt(key, value);
        editor.commit();
    }

    public int getInt(String key) {

        return pref.getInt(key, 0);
    }

    public void saveArrayList(ArrayList<ProfileAdressModel> list, String key){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(list);
        editor.putString(key, json);
        editor.apply();

    }

    public ArrayList<String> getArrayList(String key){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        Gson gson = new Gson();
        String json = prefs.getString(key, null);
        Type type = new TypeToken<ArrayList<String>>() {}.getType();
        return gson.fromJson(json, type);
    }
    public void setFirstTimeLaunch(boolean isFirstTime) {
        editor.putBoolean(IS_FIRST_TIME_LAUNCH, isFirstTime);
        editor.commit();
    }

    public boolean isFirstTimeLaunch() {
        return pref.getBoolean(IS_FIRST_TIME_LAUNCH, true);
    }
    public void clear ()
        {
            editor.clear();
            editor.commit();

        }

    }


