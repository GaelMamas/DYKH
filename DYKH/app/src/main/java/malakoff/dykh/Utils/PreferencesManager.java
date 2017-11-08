package malakoff.dykh.Utils;

import android.content.Context;
import android.content.SharedPreferences;

import malakoff.dykh.AppApplication.AppApplication;
import malakoff.dykh.AppApplication.Constants;

/**
 * Created by user on 27/06/2016.
 */

public class PreferencesManager {


    public static void clearAll() {
        SharedPreferences prefs = AppApplication.getAppContext().getSharedPreferences(Constants.DYKH_PREFERENCES, Context.MODE_PRIVATE);
        prefs.edit().clear().apply();
    }

    public static void clear(String name) {
        SharedPreferences prefs = AppApplication.getAppContext().getSharedPreferences(Constants.DYKH_PREFERENCES, Context.MODE_PRIVATE);
        prefs.edit().remove(name).apply();
    }

    public static boolean getBool(String name, boolean defaultValue) {
        SharedPreferences prefs = AppApplication.getAppContext().getSharedPreferences(Constants.DYKH_PREFERENCES, Context.MODE_PRIVATE);
        return prefs.getBoolean(name, defaultValue);
    }

    public static void setBool(String name, boolean value) {
        SharedPreferences prefs = AppApplication.getAppContext().getSharedPreferences(Constants.DYKH_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(name, value);
        editor.apply();
    }

    public static int getInt(String name, int defaultValue) {
        SharedPreferences prefs = AppApplication.getAppContext().getSharedPreferences(Constants.DYKH_PREFERENCES, Context.MODE_PRIVATE);
        return prefs.getInt(name, defaultValue);
    }

    public static void setInt(String name, int value) {
        SharedPreferences prefs = AppApplication.getAppContext().getSharedPreferences(Constants.DYKH_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(name, value);
        editor.apply();
    }

    public static long getLong(String name, long defaultValue) {
        SharedPreferences prefs = AppApplication.getAppContext().getSharedPreferences(Constants.DYKH_PREFERENCES, Context.MODE_PRIVATE);
        return prefs.getLong(name, defaultValue);
    }

    public static void setLong(String name, long value) {
        SharedPreferences prefs = AppApplication.getAppContext().getSharedPreferences(Constants.DYKH_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putLong(name, value);
        editor.apply();
    }

    public static String getString(String name, String defaultValue) {
        SharedPreferences prefs = AppApplication.getAppContext().getSharedPreferences(Constants.DYKH_PREFERENCES, Context.MODE_PRIVATE);
        return prefs.getString(name, defaultValue);
    }

    public static void setString(String name, String value) {
        SharedPreferences prefs = AppApplication.getAppContext().getSharedPreferences(Constants.DYKH_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(name, value);
        editor.apply();
    }

}
