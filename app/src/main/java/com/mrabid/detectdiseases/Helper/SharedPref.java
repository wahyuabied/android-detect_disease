package com.mrabid.detectdiseases.Helper;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Mr Abid on 9/5/2018.
 */

public class SharedPref {
    static Activity activity;
    static Context context;

    public SharedPref(Context context) {
        this.context = context;
    }


    public static void saveData(String name, String value){
        SharedPreferences prefs;
        if(context==null){
            prefs = activity.getSharedPreferences("UserData", 0);
        }else{
            prefs = context.getSharedPreferences("UserData", 0);
        }

        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(name, value);
        editor.commit();
    }

    public static String loadData(String name){
        SharedPreferences prefs;
        if(context==null){
            prefs = activity.getSharedPreferences("UserData", 0);
        }else{
            prefs = context.getSharedPreferences("UserData", 0);
        }
        String data = prefs.getString(name,"");
        return data;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public Activity getActivity() {
        return activity;
    }
}

