package com.donal.superne.app.client;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by jianghaofeng on 14-7-5.
 */
public final class Preferences
{
    private static SharedPreferences sPreferences;

    private final static String PREF_USERNAME = "pref_username";
    private final static String PREF_PASSWORD = "pref_password";
    private final static String PREF_LOGINED  = "pref_logined";

    public static void init(Context context)
    {
        sPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    private static String getString(String key, String defaultValue)
    {
        return sPreferences.getString(key, defaultValue);
    }

    private static boolean setString(String key, String value)
    {
        return sPreferences.edit().putString(key, value).commit();
    }

    private static int getInt(Context context, String key, int defaultValue)
    {
        return sPreferences.getInt(key, defaultValue);
    }

    private static int getIntMinValue(String key, int minValue, int defaultValue)
    {
        String val = getString(key, null);
        int nval;
        try
        {
            nval = Integer.valueOf(val);
        }
        catch (Exception e)
        {
            nval = defaultValue;
        }
        return (nval < minValue) ? minValue : nval;
    }

    private static long getLong(Context context, String key, long defaultValue)
    {
        return sPreferences.getLong(key, defaultValue);
    }

    /**
     * Retrieves a long and if >= 0 it sets it to -1.
     */
    private static long getLongOnce(Context context, String key)
    {
        long value = sPreferences.getLong(key, -1);
        if (value >= 0)
            sPreferences.edit().putLong(key, -1).commit();
        return value;
    }

    private static boolean getBoolean(String key, boolean defaultValue)
    {
        return sPreferences.getBoolean(key, defaultValue);
    }

    /**
     * Retrieve a boolean and if false set it to true.
     */
    private static boolean getBooleanOnce(Context context, String key)
    {
        boolean value = sPreferences.getBoolean(key, false);
        if (!value)
            sPreferences.edit().putBoolean(key, true).commit();
        return value;
    }

    public static String getServerURI()
    {
        return getString("pref_network_uri", null);
    }

    public static boolean setServerURI(Context context, String serverURI)
    {
        return sPreferences.edit()
                           .putString("pref_network_uri", serverURI)
                           .commit();
    }

    /**
     * 获取用户名
     *
     * @return
     */
    public static String getPrefUserName()
    {
        return getString(PREF_USERNAME, null);
    }

    public static String getPrefPassword()
    {
        return getString(PREF_PASSWORD, null);
    }

    public static boolean getPrefLoginFalg()
    {
        return getBoolean(PREF_LOGINED, false);
    }

    public static boolean saveAccount(String userName, String password)
    {
        return setString(PREF_USERNAME, userName) && setString(PREF_PASSWORD, password);

    }
}
