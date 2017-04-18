package net.dot.com.cpisa.activity;

import android.app.Application;

/**
 * 主Application，所有百度定位SDK的接口说明请参考线上文档：http://developer.baidu.com/map/loc_refer/index.html
 * <p>
 * 百度定位SDK官方网站：http://developer.baidu.com/map/index.php?title=android-locsdk
 * <p>
 * 直接拷贝com.baidu.location.service包到自己的工程下，简单配置即可获取定位结果，也可以根据demo内容自行封装
 */

public class AndroidApplication extends Application {
    private static AndroidApplication mApp;

    public String getAndroidId() {
        return androidId;
    }

    public void setAndroidId(String androidId) {
        this.androidId = androidId;
    }

    private String androidId = "";
    public AndroidApplication() {

        mApp = this;
    }

    public static AndroidApplication getInstance() {

        return mApp;
    }

    @Override
    public void onCreate() {
        super.onCreate();


    }
}
