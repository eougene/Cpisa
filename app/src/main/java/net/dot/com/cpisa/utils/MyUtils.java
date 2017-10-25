package net.dot.com.cpisa.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.provider.Settings;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by ${bai} on 17/2/14.
 */

public class MyUtils {
    public final static String DATE_FORMAT_DATETIME = "yyyy-MM-dd HH:mm:ss";
    public final static SimpleDateFormat sdfDateTime = new SimpleDateFormat(DATE_FORMAT_DATETIME, Locale.getDefault());

    /**
     * 检测网络是否连接
     *
     * @return
     */
    public static boolean checkNetworkState(Context context) {
        boolean flag = false;
        //得到网络连接信息
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        //去进行判断网络是否连接
        if (manager.getActiveNetworkInfo() != null) {
            flag = manager.getActiveNetworkInfo().isAvailable();
        }
        if (!flag) {
            setNetwork(context);
        }
        return flag;
    }

    /**
     * 网络未连接时，调用设置方法
     */
    public static void setNetwork(final Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("网络提示信息");
        builder.setMessage("网络不可用，如果继续，请先设置网络！");
        builder.setPositiveButton("去设置", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = null;
                /**
                 * 判断手机系统的版本！如果API大于10 就是3.0+
                 * 因为3.0以上的版本的设置和3.0以下的设置不一样，调用的方法不同
                 */
                if (android.os.Build.VERSION.SDK_INT > 10) {
                    intent = new Intent(android.provider.Settings.ACTION_WIFI_SETTINGS);
                } else {
                    intent = new Intent();
                    ComponentName component = new ComponentName(
                            "com.android.settings",
                            "com.android.settings.WirelessSettings");
                    intent.setComponent(component);
                    intent.setAction("android.intent.action.VIEW");
                }
                context.startActivity(intent);
            }
        });
        builder.create();
        builder.show();
    }


    public static void isOpenGPS(final Activity activity) {
        LocationManager locationManager = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);
        // 判断GPS模块是否开启，如果没有则开启
        if (!locationManager.isProviderEnabled(android.location.LocationManager.GPS_PROVIDER)) {
            final AlertDialog.Builder dialog = new AlertDialog.Builder(activity);
            dialog.setTitle("GPS提示信息");
            dialog.setMessage("GPS未打开，如果继续，请先打开GPS!");
            dialog.setPositiveButton("去设置", new android.content.DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface arg0, int arg1) {
                    // 转到手机设置界面，用户设置GPS
                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    activity.startActivity(intent); // 设置完成后返回到原来的界面
                }
            });
            dialog.create();
            dialog.show();
        }
    }

    public static String timeParse(long duration) {
        String time = "";

        long minute = duration / 60000;
        long seconds = duration % 60000;

        long second = Math.round((float) seconds / 1000);

        if (minute < 10) {
            time += "0";
        }
        time += minute + " 分 ";

        if (second < 10) {
            time += "0";
        }
        time += second + " 秒 ";

        return time;
    }

    /**
     * get system date
     *
     * @return The sysDateString value
     */
    public static String getSysDateTimeString() {
        return toString(new Date(System.currentTimeMillis()), sdfDateTime);
    }

    private static String toString(Date dt, SimpleDateFormat formatter) {
        return formatter.format(dt).toString();
    }

    /**
     * 版本
     */
    public static String getVersionCode(Context context) {
        PackageManager packageManager = context.getPackageManager();
        PackageInfo packageInfo;
        String versionCode = "";
        try {
            packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            versionCode = packageInfo.versionCode + "";
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionCode;
    }
}
