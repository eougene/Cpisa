package net.dot.com.cpisa.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Vibrator;
import android.text.TextUtils;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;

import net.dot.com.cpisa.R;
import net.dot.com.cpisa.utils.IsChineseOrNot;
import net.dot.com.cpisa.utils.MyUtils;
import net.dot.com.cpisa.utils.PreferencesUtil;
import net.dot.com.cpisa.zxing.camera.CameraManager;
import net.dot.com.cpisa.zxing.decoding.CaptureActivityHandler;
import net.dot.com.cpisa.zxing.decoding.InactivityTimer;
import net.dot.com.cpisa.zxing.view.ViewfinderView;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Vector;

public class MipcaActivityCapture extends Activity implements SurfaceHolder.Callback {
    private CaptureActivityHandler handler;
    private ViewfinderView viewfinderView;
    private boolean hasSurface;
    private Vector<BarcodeFormat> decodeFormats;
    private String characterSet = "ISO-8859-1";
    private InactivityTimer inactivityTimer;
    private MediaPlayer mediaPlayer;
    private boolean playBeep;
    private static final float BEEP_VOLUME = 0.10f;
    private boolean vibrate;
    private TimeCount time;
    private ImageView backImageView;

    private TextView nlatitudeTextView, resultTextView, title;

    private String scaleString = "";
    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.backImageView:
                    finish();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mipca_capture);
        initView();

    }

    private void initView() {

        CameraManager.init(getApplication());

        title = (TextView) findViewById(R.id.title);
        title.setText("扫一扫");

        backImageView = (ImageView) findViewById(R.id.backImageView);
        backImageView.setBackgroundResource(R.mipmap.back);
        backImageView.setOnClickListener(onClickListener);

        viewfinderView = (ViewfinderView) findViewById(R.id.viewfinder_view);
        nlatitudeTextView = (TextView) findViewById(R.id.nlatitudeTextView);
        resultTextView = (TextView) findViewById(R.id.resultTextView);

        hasSurface = false;
        inactivityTimer = new InactivityTimer(this);


        nlatitudeTextView.setText("有效的GPS数据\n" +"\n" + "纬度: " + PreferencesUtil.getlatitude_01() + "    " + "\n经度: " + PreferencesUtil.getLontitude_01());

        /*** 构造CountDownTimer对象 ****/
        time = new TimeCount(PreferencesUtil.getTime(), 1000);
        time.start();

    }

    @Override
    protected void onResume() {
        super.onResume();


        SurfaceView surfaceView = (SurfaceView) findViewById(R.id.preview_view);
        SurfaceHolder surfaceHolder = surfaceView.getHolder();
        if (hasSurface) {
            initCamera(surfaceHolder);
        } else {
            surfaceHolder.addCallback(this);
            surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        }
        decodeFormats = null;
        characterSet = null;

        playBeep = true;
        AudioManager audioService = (AudioManager) getSystemService(AUDIO_SERVICE);
        if (audioService.getRingerMode() != AudioManager.RINGER_MODE_NORMAL) {
            playBeep = false;
        }
        initBeepSound();
        vibrate = true;

    }

    @Override
    protected void onPause() {
        super.onPause();
        if (handler != null) {
            handler.quitSynchronously();
            handler = null;
        }
        CameraManager.get().closeDriver();
    }

    @Override
    protected void onDestroy() {
        inactivityTimer.shutdown();
        super.onDestroy();
    }

    /**
     * 处理扫描结果
     *
     * @param result
     * @param barcode
     */
    public void handleDecode(Result result, Bitmap barcode) {
        inactivityTimer.onActivity();
        playBeepSoundAndVibrate();
        String resultString = result.getText();
        Log.e("扫面返回结果**", "resultString:" + resultString);

        if (resultString.equals("")) {
            Toast.makeText(MipcaActivityCapture.this, "Scan failed!", Toast.LENGTH_SHORT).show();
        } else {
            Intent resultIntent = new Intent();
            Bundle bundle = new Bundle();
            try {
                String UTF_Str = "";
                String GB_Str = "";
                boolean is_cN = false;

                UTF_Str = new String(resultString.getBytes("ISO-8859-1"), "UTF-8");
                is_cN = IsChineseOrNot.isChineseCharacter(UTF_Str);
                // 防止有人特意使用乱码来生成二维码来判断的情况
                boolean b = IsChineseOrNot.isSpecialCharacter(resultString);
                if (b) {
                    is_cN = true;
                }
                if (!is_cN) {
                    GB_Str = new String(resultString.getBytes("ISO-8859-1"), "GB2312");
                    resultString = GB_Str;
                }

                scaleString = resultString.replaceAll("#", "-");
                if (!TextUtils.isEmpty(scaleString)) {
                    nextActivity(scaleString);
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

        }

    }

    private void initCamera(SurfaceHolder surfaceHolder) {
        try {
            CameraManager.get().openDriver(surfaceHolder);
        } catch (IOException ioe) {
            return;
        } catch (RuntimeException e) {
            return;
        }
        if (handler == null) {
            handler = new CaptureActivityHandler(this, decodeFormats, characterSet);
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (!hasSurface) {
            hasSurface = true;
            initCamera(holder);
        }

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        hasSurface = false;

    }

    public ViewfinderView getViewfinderView() {
        return viewfinderView;
    }

    public Handler getHandler() {
        return handler;
    }

    public void drawViewfinder() {
        viewfinderView.drawViewfinder();

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

    private static final long VIBRATE_DURATION = 200L;

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


        }

        // 计时过程显示
        @Override
        public void onTick(long millisUntilFinished) {

            resultTextView.setText("有效时间: " + MyUtils.timeParse(millisUntilFinished));
            //保存时间
            PreferencesUtil.setTime(millisUntilFinished);
        }
    }


    private void nextActivity(String data) {

        if (!TextUtils.isEmpty(data)) {

            Bundle bundle=new Bundle();
            bundle.putString("data",data);
            Intent intent = new Intent(MipcaActivityCapture.this, CommintInfoActivity.class);
            intent.putExtra("bundle",bundle);
            startActivity(intent);

        } else {
            Toast.makeText(MipcaActivityCapture.this, "未扫描的任何信息,请重新扫描", Toast.LENGTH_LONG).show();
        }

    }


}
