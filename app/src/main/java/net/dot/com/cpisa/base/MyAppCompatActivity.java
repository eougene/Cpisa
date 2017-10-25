package net.dot.com.cpisa.base;

import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

/**
 * Created by e-dot on 2017/10/20.
 * 自定义注解获取控件
 */

public class MyAppCompatActivity extends AppCompatActivity {

    protected <T extends View> T obtainViewById(@IdRes int resId) {
        return (T) findViewById(resId);
    }

    protected <T extends View> T obtainViewById(View parent, @IdRes int resId) {
        return (T) parent.findViewById(resId);
    }
}
