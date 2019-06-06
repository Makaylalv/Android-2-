package com.john.software.helpeachother.code.Util.SpUtils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Administrator on 2017/11/8.
 */

public class SpUtils {
    private static SharedPreferences sp;
    public static void putBoolean(Context context, String key, Boolean values){
        if (sp==null){
            sp=context.getSharedPreferences("config",Context.MODE_PRIVATE);
        }
        sp.edit().putBoolean(key,values).commit();
    }
    public static Boolean getBoolean(Context context,String key,Boolean defValue){
        if (sp==null){
            sp=context.getSharedPreferences("config",Context.MODE_PRIVATE);
        }
       return sp.getBoolean(key,defValue);
    }
    public static void putString(Context context, String key, String values){
        if (sp==null){
            sp=context.getSharedPreferences("config",Context.MODE_PRIVATE);
        }
        sp.edit().putString(key,values).commit();
    }
    public static String getString(Context context,String key,String defValue){
        if (sp==null){
            sp=context.getSharedPreferences("config",Context.MODE_PRIVATE);
        }
        return sp.getString(key,defValue);
    }
    public static void putInt(Context context, String key, int values){
        if (sp==null){
            sp=context.getSharedPreferences("config",Context.MODE_PRIVATE);
        }
        sp.edit().putInt(key,values).commit();
    }
    public static int getInt(Context context,String key,int defValue){
        if (sp==null){
            sp=context.getSharedPreferences("config",Context.MODE_PRIVATE);
        }
        return sp.getInt(key,defValue);
    }
}
