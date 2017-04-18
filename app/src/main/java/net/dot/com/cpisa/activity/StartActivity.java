package net.dot.com.cpisa.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import net.dot.com.cpisa.R;

public class StartActivity extends Activity {
    private final static int SWITCH_WELCOME_ACTIVITY = 1000;

    public Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SWITCH_WELCOME_ACTIVITY:
                    startActivity(new Intent(StartActivity.this, LoginActivity.class));
                    // 加上这句话不会出现闪一下的情况
                    overridePendingTransition(0, 0);
                    break;

            }
            finish();
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        mHandler.sendEmptyMessageDelayed(SWITCH_WELCOME_ACTIVITY, 3000);
    }
}
