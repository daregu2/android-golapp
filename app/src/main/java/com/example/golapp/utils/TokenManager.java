package com.example.golapp.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.golapp.AppConstants;
import com.example.golapp.BaseApplication;

public class TokenManager {

    Context context = BaseApplication.getContext();
    SharedPreferences preferences;

    public TokenManager() {
        this.preferences = context.getSharedPreferences(AppConstants.PREFS_TOKEN, Context.MODE_PRIVATE);
    }

    public void saveToken(String token){
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(AppConstants.USER_TOKEN,token);
        editor.apply();
    }

    public String getToken(){
        return preferences.getString(AppConstants.USER_TOKEN,"");
    }

    public void deleteToken(){
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(AppConstants.USER_TOKEN,"");
        editor.apply();
    }


}
