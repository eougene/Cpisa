package net.dot.com.cpisa.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import net.dot.com.cpisa.R;
import net.dot.com.cpisa.javabean.User;
import net.dot.com.cpisa.utils.MyUtils;
import net.dot.com.cpisa.utils.PreferencesUtil;
import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

public class CommintInfoActivity extends Activity {
    private ImageView backImageView;
    private Button cancelButton, saveButton;
    private TextView title, locationTextView, timeTextView, userName, userLon, userLan;
    private TimeCount time;
    private String sResult = null, androidId;

    private String URL = "http://wh.eyoungroup.com/wui/visitDetail/locate?";
    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.saveButton:
                    commitInfo(1);
                    break;
                case R.id.cancelButton:
                    finish();
                    break;
                case R.id.backImageView:
                    finish();
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_commint_info);

        title = (TextView) findViewById(R.id.title);
        title.setText("信息");

        //
        backImageView = (ImageView) findViewById(R.id.backImageView);
        backImageView.setBackgroundResource(R.mipmap.back);
        backImageView.setOnClickListener(onClickListener);


        androidId = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);
        AndroidApplication androidApplication = (AndroidApplication) getApplicationContext();
        androidApplication.setAndroidId(androidId);


        //获取二维码扫描的结果

        Bundle bundle = getIntent().getBundleExtra("bundle");
        sResult = bundle.getString("data");
        Log.e("二维码扫描结果", "data:" + sResult + "androidId:" + androidId);


        backImageView = (ImageView) findViewById(R.id.backImageView);
        cancelButton = (Button) findViewById(R.id.cancelButton);
        saveButton = (Button) findViewById(R.id.saveButton);
        backImageView.setOnClickListener(onClickListener);
        cancelButton.setOnClickListener(onClickListener);
        saveButton.setOnClickListener(onClickListener);
        locationTextView = (TextView) findViewById(R.id.locationTextView);
        timeTextView = (TextView) findViewById(R.id.timeTextView);


        locationTextView.setText("纬度: " + PreferencesUtil.getlatitude_01() + "       \n经度: " + PreferencesUtil.getLontitude_01());


        //用户信息:
        userName = (TextView) findViewById(R.id.userName);
        userLon = (TextView) findViewById(R.id.userLon);
        userLan = (TextView) findViewById(R.id.userLan);

        /*** 构造CountDownTimer对象 ****/
        time = new TimeCount(PreferencesUtil.getTime(), 1000);
        time.start();


        //获取用户的信息
        if (!TextUtils.isEmpty(sResult)) {
            commitInfo(0);
        }

    }

    private void commitInfo(final int visitType) {
        //先检查网络是否连接
        if (MyUtils.checkNetworkState(CommintInfoActivity.this)) {
            FinalHttp finalHttp = new FinalHttp();
            AjaxParams ajaxParams = new AjaxParams();
            ajaxParams.put("deviceCode", androidId);

            ajaxParams.put("tagCode", sResult);

            //0：获取当前老人的经纬度
            // 1：更新当前老人的经纬度
            ajaxParams.put("visitType", visitType + "");

            ajaxParams.put("longitude", PreferencesUtil.getLontitude_01());

            ajaxParams.put("latitude", PreferencesUtil.getlatitude_01());

            ajaxParams.put("appKey", "KSQDS5FLJ9WEI68L7KJ7LASDF0TDLAS");

            finalHttp.post(URL, ajaxParams, new AjaxCallBack<String>() {


                @Override
                public void onSuccess(String s) {
                    super.onSuccess(s);
                    Log.e("获取数据", "s:" + s);
                    if (null != s) {
                        Gson gson = new Gson();
                        User user = gson.fromJson(s, User.class);


                        if (user.getStatus() == 0) {

                            if (visitType == 0) {
                                userName.setText("姓名:" + user.getObj().getMemberName());
                                userLan.setText("纬度:" + user.getObj().getLatitude());
                                userLon.setText("经度:" + user.getObj().getLongitude());

                            } else if (visitType == 1) {

                                Toast.makeText(CommintInfoActivity.this, "更新信息成功", Toast.LENGTH_LONG).show();
                                finish();

                            }

                        }


                    }
                }

                @Override
                public void onFailure(Throwable t, int errorNo, String strMsg) {
                    super.onFailure(t, errorNo, strMsg);
                    Log.e("获取数据", "s:" + strMsg);
                    Toast.makeText(CommintInfoActivity.this, "网络异常", Toast.LENGTH_LONG).show();
                }
            });


        } else {

            Toast.makeText(CommintInfoActivity.this, "网络异常", Toast.LENGTH_LONG).show();
        }

    }

    /**
     * 计时器
     */
    class TimeCount extends CountDownTimer {
        /**
         * 参数依次为总时长,和计时的时间间隔
         **/
        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        // 计时完毕时触发
        @Override
        public void onFinish() {
            saveButton.setFocusable(false);


        }

        // 计时过程显示
        @Override
        public void onTick(long millisUntilFinished) {

            timeTextView.setText("有效时间 : " + MyUtils.timeParse(millisUntilFinished));


        }
    }

}
