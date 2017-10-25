package net.dot.com.cpisa.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Window;
import android.view.WindowManager;

import net.dot.com.cpisa.R;
import net.dot.com.cpisa.utils.PreferencesUtil;

public class StartActivity extends Activity {
    private final static int SWITCH_WELCOME_ACTIVITY = 1000;

    public Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SWITCH_WELCOME_ACTIVITY:


                    //判断是否登录,
                    if (PreferencesUtil.getUserName().equals("") || PreferencesUtil.getUserName() == "" || PreferencesUtil.getUserName() == null) {
                        startActivity(new Intent(StartActivity.this, LoginActivity.class));

                    } else {
                        startActivity(new Intent(StartActivity.this, HomeActivity.class));
                    }
                    // 加上这句话不会出现闪一下的情况
                    overridePendingTransition(0, 0);
                    finish();
                    break;

            }

        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //无title
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //全屏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_start);
        mHandler.sendEmptyMessageDelayed(SWITCH_WELCOME_ACTIVITY, 3000);
    }
}
