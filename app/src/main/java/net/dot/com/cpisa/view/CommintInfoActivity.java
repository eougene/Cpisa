package net.dot.com.cpisa.view;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import net.dot.com.cpisa.R;
import net.dot.com.cpisa.base.BaseActivity;
import net.dot.com.cpisa.base.PresenterInject;
import net.dot.com.cpisa.bean.User;
import net.dot.com.cpisa.contract.UserUpdateConract;
import net.dot.com.cpisa.presenter.UserUpdatePresenter;
import net.dot.com.cpisa.utils.MyUtils;
import net.dot.com.cpisa.utils.PreferencesUtil;


@PresenterInject(UserUpdatePresenter.class)
public class CommintInfoActivity extends BaseActivity<UserUpdateConract.Presenter> implements UserUpdateConract.View {

    private Button cancelButton, saveButton;
    private TextView locationTextView, timeTextView, userName, userLon, userLan;
    private TimeCount time;
    private String sResult = null, androidId;

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                //更新
                case R.id.saveButton:
                    presenter.useUpdate(androidId, sResult, "1", PreferencesUtil.getLontitude_01(), PreferencesUtil.getlatitude_01());
                    break;
                //取消
                case R.id.cancelButton:
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

        presenter.setView(this);
        setContentView(R.layout.activity_commint_info);
        setTitle("信息");


        androidId = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);
        //获取二维码扫描的结果
        Bundle bundle = getIntent().getBundleExtra("bundle");
        sResult = bundle.getString("data");
        cancelButton = obtainViewById(R.id.cancelButton);
        saveButton = obtainViewById(R.id.saveButton);
        cancelButton.setOnClickListener(onClickListener);
        saveButton.setOnClickListener(onClickListener);
        locationTextView = obtainViewById(R.id.locationTextView);
        timeTextView = obtainViewById(R.id.timeTextView);


        locationTextView.setText("纬度:" + PreferencesUtil.getlatitude_01() + "\n" +"经度:" + PreferencesUtil.getLontitude_01());


        //用户信息:
        userName = obtainViewById(R.id.userName);
        userLon = obtainViewById(R.id.userLon);
        userLan = obtainViewById(R.id.userLan);

        /*** 构造CountDownTimer对象 ****/
        time = new TimeCount(PreferencesUtil.getTime(), 1000);
        time.start();


        //获取用户的信息
        if (!TextUtils.isEmpty(sResult)) {
            presenter.useUpdate(androidId, sResult, "0", PreferencesUtil.getLontitude_01(), PreferencesUtil.getlatitude_01());
        }

    }


    @Override
    public void useUpdate(User user) {
        userName.setText("姓名:" + user.getObj().getMemberName());
        userLan.setText("纬度:" + user.getObj().getLatitude());
        userLon.setText("经度:" + user.getObj().getLongitude());


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
