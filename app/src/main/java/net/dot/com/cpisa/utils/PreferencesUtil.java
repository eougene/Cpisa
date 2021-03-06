package net.dot.com.cpisa.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import net.dot.com.cpisa.base.AndroidApplication;

/**
 * Created by ${bai} on 17/2/21.
 */

public class PreferencesUtil {
    public static Activity activityInstance = null;
    private static SharedPreferences preferences = null;

    public static final SharedPreferences getPreferences() {
        if (preferences == null) {
            preferences = PreferenceManager.getDefaultSharedPreferences(AndroidApplication.getInstance().getApplicationContext());
        }
        return preferences;
    }

    public synchronized static final void setLontitude_01(String lontitude) {
        getPreferences().edit().putString("lontitude", lontitude).commit();
    }

    public synchronized static final String getLontitude_01() {
        return getPreferences().getString("lontitude", "");
    }

    public synchronized static final void removeLontitude_01() {

        getPreferences().edit().remove("lontitude").commit();
    }


    //
    public synchronized static final void setlatitude_01(String lontitude) {
        getPreferences().edit().putString("latitude", lontitude).commit();
    }

    public synchronized static final String getlatitude_01() {
        return getPreferences().getString("latitude", "");
    }

    public synchronized static final void removelatitude_01() {

        getPreferences().edit().remove("latitude").commit();
    }

    //保存计时器时间

    public synchronized static final void setTime(long time) {
        getPreferences().edit().putLong("time", time).commit();
    }

    public synchronized static final long getTime() {
        return getPreferences().getLong("time", -100);
    }

    public synchronized static final void removeTime() {

        getPreferences().edit().remove("time").commit();
    }


    /**
     * 保存用户信息
     */


    public synchronized static final void userName(String name) {
        getPreferences().edit().putString("name", name).commit();
    }

    public synchronized static final String getUserName() {
        return getPreferences().getString("name", "");
    }

    public synchronized static final void removeUserName() {

        getPreferences().edit().remove("name").commit();
    }


    // 保存经度
    public static void pushLons(Context context, String lons) {
        getPreferences().edit().putString("lons", lons).commit();
    }

    // 保存经度
    public static String pullLons(Context context) {
        return getPreferences().getString("lons", "");
    }


    // 保存经度
    public static void pushLans(Context context, String lans) {
        getPreferences().edit().putString("lans", lans).commit();
    }

    // 保存经度
    public static String pullLans(Context context) {
        return getPreferences().getString("lans", "");
    }



    //是否循环
    public static void pushCirculation(Context context, String lans) {
        getPreferences().edit().putString("circulation", lans).commit();
    }

    // 是否循环
    public static String pullCirculation(Context context) {
        return getPreferences().getString("circulation", "0");
    }


}
