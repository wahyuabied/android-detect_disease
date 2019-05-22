package com.mrabid.detectdiseases.Helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Environment;
import android.preference.PreferenceManager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.FileOutputStream;
import java.lang.reflect.Type;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Mr Abid on 10/9/2018.
 */

public class PreProcessing {
    public static double Degree(float x){
        return (x*(Math.PI/180));
    }

    public static File bitmapToFile(Bitmap finalBitmap) {
        File folder = new File(Environment.getExternalStorageDirectory() + File.separator + "DetectDisease");
        boolean success = true;
        if (!folder.exists()) {success = folder.mkdirs(); }
        if (success) {} else {}

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String fname = "DD_"+ timeStamp +".jpg";

        File file = new File(folder, fname);
        if (file.exists()) file.delete ();
        try {
            FileOutputStream out = new FileOutputStream(file);
            finalBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return file;
    }

    public static boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    public static void saveArrayList(String key, ArrayList<ArrayList<String>> value, Context context){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(value);
        editor.putString(key,json);
        editor.apply();
    }

    public static ArrayList<ArrayList<String>> getArrayList(String key,Context context){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        Gson gson = new Gson();
        String json = preferences.getString(key,null);
        Type type = new TypeToken<ArrayList<ArrayList<String>>>(){}.getType();
        return gson.fromJson(json,type);
    }

    public static String kelvinCelcius(String kelvin){
        Double suhu =  Double.parseDouble(kelvin);
        suhu = Double.parseDouble(new DecimalFormat("##.###").format(suhu-273.15));
        return suhu+"";
    }


    public static boolean fasePenyebaranLate (String kelvin){
        Double suhu = Double.parseDouble(PreProcessing.kelvinCelcius(kelvin));
        if(suhu>15 && suhu<22){
            return true;
        }
        return false;
    }

    public static boolean fasePenyebaranEarly (String kelvin){
        Double suhu = Double.parseDouble(PreProcessing.kelvinCelcius(kelvin));
        if(suhu>26 && suhu<30){
            return true;
        }
        return false;
    }

    public static int[] isPlant (Bitmap bitmap){
        int [] color = new int[3];
        for(int i=0;i<bitmap.getHeight();i++){
            for(int j=0;j<bitmap.getWidth();j++){
                int pixel = bitmap.getPixel(i,j);
                color[0]=color[0]+Color.red(pixel);
                color[1]=color[1]+Color.green(pixel);
                color[2]=color[2]+Color.blue(pixel);
            }
        }
        color[0] = color[0]/(256*256);
        color[1] = color[1]/(256*256);
        color[2] = color[2]/(256*256);
        return color;
    }

}
