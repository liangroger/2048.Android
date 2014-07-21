package com.daylab.g2048.base;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;


public class SharedPreferencesManager {
    /** 常用数据 */
    private static final String USED_DATA = "data";
	
	public static void remove(Context context,String key) {
		Editor sharedata = context.getSharedPreferences(
				USED_DATA, 0).edit();
		sharedata.remove(key);
		sharedata.commit();
	}
	
	public static boolean getBooleanInfo(Context context,String key){
		SharedPreferences sharedPreferences = context
			.getSharedPreferences(USED_DATA, 0);
		return sharedPreferences.getBoolean(key, false);
	}
	
	public static void saveBooleanInfo(Context context,String key,boolean value){
		Editor sharedata = context.getSharedPreferences(
				USED_DATA, 0).edit();
		sharedata.putBoolean(key, value);
		sharedata.commit();
	}
	
	//string
	public static void saveStringInfo(Context context,String key, String value) {
		Editor sharedata = context.getSharedPreferences(
				USED_DATA, 0).edit();
		sharedata.putString(key, value);
		sharedata.commit();
	}

	public static String getStringInfo(Context context,String key) {
		SharedPreferences sharedPreferences = context
				.getSharedPreferences(USED_DATA, 0);
		return sharedPreferences.getString(key, null);
	}
	
	//int
	public static void saveIntInfo(Context context,String key,int value){
		Editor sharedata = context.getSharedPreferences(
				USED_DATA, 0).edit();
		sharedata.putInt(key, value);
		sharedata.commit();
	}
	
	public static int getIntInfo(Context context,String key){
		SharedPreferences sharedPreferences = context
				.getSharedPreferences(USED_DATA, 0);
		return sharedPreferences.getInt(key, 0);
	}
	
	//float
	public static void saveFloatInfo(Context context,String key,float value){
		Editor sharedata = context.getSharedPreferences(
				USED_DATA, 0).edit();
		sharedata.putFloat(key, value);
		sharedata.commit();
	}
	
	public static float getFloatInfo(Context context,String key){
		SharedPreferences sharedPreferences = context
				.getSharedPreferences(USED_DATA, 0);
		return sharedPreferences.getFloat(key, 0);
	}
	
	//long
	public static void savaLongInfo(Context context,String key,long value){
		Editor sharedata = context.getSharedPreferences(
				USED_DATA, 0).edit();
		sharedata.putLong(key, value);
		sharedata.commit();
	}
	
	public static long getLongInfo(Context context,String key){
		SharedPreferences sharedPreferences = context
				.getSharedPreferences(USED_DATA, 0);
		return sharedPreferences.getLong(key, 0);
	}
	
}
