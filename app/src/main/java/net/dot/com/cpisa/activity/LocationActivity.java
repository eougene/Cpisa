package net.dot.com.cpisa.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import net.dot.com.cpisa.R;
import net.dot.com.cpisa.utils.CoordinateTransformUtil;
import net.dot.com.cpisa.utils.MyUtils;
import net.dot.com.cpisa.utils.PreferencesUtil;

import java.io.IOException;

/**
 * 单点定位示例，用来展示基本的定位结果，配置在LocationService.java中
 * 默认配置也可以在LocationService中修改
 * 默认配置的内容自于开发者论坛中对开发者长期提出的疑问内容
 *
 * @author baidu
 */


public class LocationActivity extends Activity {

    private Location lastLocation;

    private LocationManager locationManager;

    private Button mButton;
    private TextView nlatitudeTextView, sureTextView, timeTextView, titleTextView;
    private TimeCount time;
    private ImageView backimageView;
    //
    private MediaPlayer mediaPlayer;
    private boolean playBeep;
    private static final float BEEP_VOLUME = 0.10f;
    private boolean vibrate;
    private static final long VIBRATE_DURATION = 200L;
    private int count = 0;
    private LinearLayout sureLinearLayout;
    private boolean isTrue = true;
    //
    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            switch (view.getId()) {
                case R.id.mButton:
                    Intent intent = new Intent();
                    intent.setClass(LocationActivity.this, MipcaActivityCapture.class);
                    startActivity(intent);
                    break;

                case R.id.backImageView:
                    finish();
                    break;
            }

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // -----------demo view config ------------
        setContentView(R.layout.location);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        //音频提示
        this.setVolumeControlStream(AudioManager.STREAM_MUSIC);
        /*** 构造CountDownTimer对象 ****/
        time = new TimeCount(300000, 1000);


        mButton = (Button) findViewById(R.id.mButton);
        mButton.setOnClickListener(onClickListener);
        //经度纬度
        nlatitudeTextView = (TextView) findViewById(R.id.nlatitudeTextView);
        //有效的GPS数据
        sureTextView = (TextView) findViewById(R.id.sureTextView);
        //显示倒计时
        timeTextView = (TextView) findViewById(R.id.timeTextView);

        //移动手机提示
        titleTextView = (TextView) findViewById(R.id.titleTextView);

        //界面显示
        sureLinearLayout = (LinearLayout) findViewById(R.id.sureLinearLayout);

        //返回按钮

        backimageView = (ImageView) findViewById(R.id.backImageView);
        backimageView.setBackgroundResource(R.mipmap.back);
        backimageView.setOnClickListener(onClickListener);


    }


    /**
     * **********************************************************************
     * 使用设备的GPS
     */

    private LocationListener listener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            updateLocation(location);
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {
            updateLocation();
        }

        @Override
        public void onProviderEnabled(String s) {
            updateLocation();
        }

        @Override
        public void onProviderDisabled(String s) {

        }
    };

    private void updateLocation() {
        Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        if (location == null) {
            location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        }

        updateLocation(location);
    }

    private void updateLocation(Location location) {
        if (location == null) {
            return;
        }
        // 判断新的位置是否可替换当前位置
        if (isBetterLocation(location, lastLocation)) {
            updateView(location);
            lastLocation = location;
        }
    }

    /**
     * 判断位置信息是否比当前的位置信息好
     *
     * @param location            新的位置
     * @param currentBestLocation 当前的位置
     */
    protected boolean isBetterLocation(Location location, Location currentBestLocation) {
        if (currentBestLocation == null) {
            return true;
        }

        // 检测GPS时间
        long timeDelta = location.getTime() - currentBestLocation.getTime();
        boolean isSignificantlyNewer = timeDelta > 2000;
        boolean isSignificantlyOlder = timeDelta < -2000;
        boolean isNewer = timeDelta > 0;// 较新

        // If it's been more than two minutes since the current location, use
        // the new location
        // because the user has likely moved
        if (isSignificantlyNewer) {
            return true;
        } else if (isSignificantlyOlder) {
            return false;
        }

        // 检测GPS精度
        int accuracyDelta = (int) (location.getAccuracy() - currentBestLocation.getAccuracy());
        boolean isLessAccurate = accuracyDelta > 0;
        boolean isMoreAccurate = accuracyDelta < 0;
        boolean isSignificantlyLessAccurate = accuracyDelta > 200;

        // 检测提供者
        boolean isFromSameProvider = isSameProvider(location.getProvider(), currentBestLocation.getProvider());

        // Determine location quality using a combination of timeliness and
        // accuracy
        if (isMoreAccurate) {
            return true;
        } else if (isNewer && !isLessAccurate) {
            return true;
        } else if (isNewer && !isSignificantlyLessAccurate && isFromSameProvider) {
            return true;
        }
        return false;
    }

    /**
     * 判断是否为同一个提供者
     *
     * @param provider1
     * @param provider2
     * @return
     */
    private boolean isSameProvider(String provider1, String provider2) {
        if (provider1 == null) {
            return provider2 == null;
        }
        return provider1.equals(provider2);
    }


    /**
     * 实时更新文本内容
     *
     * @param location
     */
    private void updateView(Location location) {
        if (location != null) {
            Log.e("更新*****", "经度：" + location.getLongitude() + "\n纬度：" + location.getLatitude() + "\n服务商：" + location.getProvider() + "\n准确性：" + location.getAccuracy() + "\n高度：" + location.getAltitude() + "\n方向角：" + location.getBearing()
                    + "\n速度：" + location.getSpeed());
            nlatitudeTextView.setText("定位成功\n" + "\n" + "经度: " + location.getLongitude() + "       " + "\n纬度: " + location.getLatitude());
            // 把GPS经纬度转换百度经纬度
            double lactionArray[] = CoordinateTransformUtil.wgs84tobd09(Double.parseDouble(String.valueOf(location.getLongitude())), Double.parseDouble(String.valueOf(location.getLatitude())));

            String sLongitude = String.valueOf(lactionArray[0]);
            String sLatitude = String.valueOf(lactionArray[1]);


            while (isTrue) {
                if (location.getAccuracy() >= 5 && location.getAccuracy() <= 50) {
                    //保存经纬度
                    PreferencesUtil.setlatitude_01(sLatitude);
                    PreferencesUtil.setLontitude_01(sLongitude);
                    //显示有效的GPS数据
                    setLocationInfo(sLongitude, sLatitude);

                    isTrue = false;

                }
            }


        }
    }


    private void setLocationInfo(String lontitude, String latitude) {
        //提示音
        playBeepSoundAndVibrate();
        //计时器
        time.start();
        //设置数据
        titleTextView.setText(R.string.data);
        sureTextView.setText("经度:" + lontitude + "\n" + "纬 度 : " + latitude);

    }


    private void initBeepSound() {
        if (playBeep && mediaPlayer == null) {
            // The volume on STREAM_SYSTEM is not adjustable, and users found it
            // too loud,
            // so we now play on the music stream.
            setVolumeControlStream(AudioManager.STREAM_MUSIC);
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setOnCompletionListener(beepListener);
            mediaPlayer.setLooping(false);

            AssetFileDescriptor file = getResources().openRawResourceFd(R.raw.beep);
            try {
                mediaPlayer.setDataSource(file.getFileDescriptor(), file.getStartOffset(), file.getLength());
                file.close();
                mediaPlayer.setVolume(BEEP_VOLUME, BEEP_VOLUME);
                mediaPlayer.prepare();
            } catch (IOException e) {
                mediaPlayer = null;
            }
        }
    }


    /**
     * 震动提示
     */
    private void playBeepSoundAndVibrate() {
        if (playBeep && mediaPlayer != null) {
            mediaPlayer.start();
        }
        if (vibrate) {
            Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
            vibrator.vibrate(VIBRATE_DURATION);
        }
    }

    /**
     * When the beep has finished playing, rewind to queue up another one.
     */
    private final MediaPlayer.OnCompletionListener beepListener = new MediaPlayer.OnCompletionListener() {
        public void onCompletion(MediaPlayer mediaPlayer) {
            mediaPlayer.seekTo(0);
        }
    };


    @Override
    protected void onResume() {
        super.onResume();
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, listener);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, listener);

        Log.e("onResume**", "onResume");
        playBeep = true;
        AudioManager audioService = (AudioManager) getSystemService(AUDIO_SERVICE);
        if (audioService.getRingerMode() != AudioManager.RINGER_MODE_NORMAL) {
            playBeep = false;
        }
        initBeepSound();
        vibrate = true;
    }

    /***
     * Stop location service
     */
    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onStart() {

        super.onStart();


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mediaPlayer.release();
        locationManager.removeUpdates(listener);
        PreferencesUtil.removelatitude_01();
        PreferencesUtil.removeLontitude_01();
        PreferencesUtil.removeTime();
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

            sureLinearLayout.setVisibility(View.INVISIBLE);
            mButton.setVisibility(View.GONE);
            titleTextView.setText(R.string.move);
            count = 0;
            isTrue = true;


        }

        // 计时过程显示
        @Override
        public void onTick(long millisUntilFinished) {
            sureLinearLayout.setVisibility(View.VISIBLE);
            timeTextView.setText("有效时间 : " + MyUtils.timeParse(millisUntilFinished));
            mButton.setVisibility(View.VISIBLE);

            //保存时间
            PreferencesUtil.setTime(millisUntilFinished);
        }
    }


}
