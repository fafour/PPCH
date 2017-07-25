package th.go.nacc.nacc_law.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SPUtils {
	
	public static void clear(Context context){
		SharedPreferences.Editor editor = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE).edit();
		editor.clear();
		editor.commit();
	}
	
	public static void delete(Context context, String key){
		SharedPreferences.Editor editor = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE).edit();
		editor.remove(key);
		editor.commit();
	}
	
	public static void set(Context context, String key, String value){
		SharedPreferences.Editor editor = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE).edit();
		editor.putString(key, value);
		editor.commit();
	}
	
	public static String getString(Context context, String key){
		SharedPreferences prefs = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
		return prefs.getString(key, "");
	}
	
	public static void set(Context context, String key, int value){
		SharedPreferences.Editor editor = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE).edit();
		editor.putInt(key, value);
		editor.commit();
	}
	
	public static int getInt(Context context, String key){
		SharedPreferences prefs = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
		return prefs.getInt(key, 0);
	}
	
	public static void set(Context context, String key, boolean value){
		SharedPreferences.Editor editor = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE).edit();
		editor.putBoolean(key, value);
		editor.commit();
	}
	
	public static boolean getBoolean(Context context, String key){
		SharedPreferences prefs = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
		return prefs.getBoolean(key, false);
	}
	
	public static void set(Context context, String key, long value){
		SharedPreferences.Editor editor = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE).edit();
		editor.putLong(key, value);
		editor.commit();
	}
	

}