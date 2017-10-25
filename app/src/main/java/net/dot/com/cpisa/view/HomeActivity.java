package net.dot.com.cpisa.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import net.dot.com.cpisa.R;
import net.dot.com.cpisa.base.BaseActivity;


/**
 * 首页
 */
public class HomeActivity extends BaseActivity {
    private RelativeLayout changeRela;
    private long mExitTime = 0;

    private View.OnClickListener mOnCliceListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                //修正定位点
                case R.id.changeRela:
                    Intent intent = new Intent(HomeActivity.this, LocationActivity.class);
                    startActivity(intent);
                    break;

            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        hideBack();
        setTitle(getString(R.string.app_name));

        changeRela = obtainViewById(R.id.changeRela);
        changeRela.setOnClickListener(mOnCliceListener);


    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if ((System.currentTimeMillis() - mExitTime) > 2000) {
                // 如果两次按键时间间隔大于2000毫秒，则不退出
                Toast.makeText(this, getResources().getString(R.string.app_exit), Toast.LENGTH_SHORT).show();
                mExitTime = System.currentTimeMillis();// 更新mExitTime
            } else {
                System.exit(0);// 否则退出程序
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);

    }
}
