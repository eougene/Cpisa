package net.dot.com.cpisa.newzxing.decoding;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;

import net.dot.com.cpisa.R;
import net.dot.com.cpisa.view.MipcaActivityCapture;
import net.dot.com.cpisa.newzxing.camera.NewCameraManager;
import net.dot.com.cpisa.newzxing.view.NewViewfinderResultPointCallback;

import java.util.Vector;

/**
 * Created by e-dot on 2017/4/14.
 */

public final class NewLoginActivityHandler extends Handler {

    private static final String TAG = NewCaptureActivityHandler.class.getSimpleName();

    private final MipcaActivityCapture activity;
    private final NewLoginDecodeThread decodeThread;
    private State state;

    private enum State {
        PREVIEW,
        SUCCESS,
        DONE
    }

    public NewLoginActivityHandler(MipcaActivityCapture activity, Vector<BarcodeFormat> decodeFormats,
                                   String characterSet) {
        this.activity = activity;
        decodeThread = new NewLoginDecodeThread(activity, decodeFormats, characterSet,
                new NewViewfinderResultPointCallback(activity.getViewfinderView()));
        decodeThread.start();
        state = State.SUCCESS;
        // Start ourselves capturing previews and decoding.
        NewCameraManager.get().startPreview();
        restartPreviewAndDecode();
    }


    @Override
    public void handleMessage(Message message) {
        switch (message.what) {
            case R.id.auto_focus:
                //Log.d(TAG, "Got auto-focus message");
                // When one auto focus pass finishes, start another. This is the closest thing to
                // continuous AF. It does seem to hunt a bit, but I'm not sure what else to do.
                if (state == State.PREVIEW) {
                    NewCameraManager.get().requestAutoFocus(this, R.id.auto_focus);
                }
                break;
            case R.id.restart_preview:
                Log.d(TAG, "Got restart preview message");
                restartPreviewAndDecode();
                break;
            case R.id.decode_succeeded:
                Log.d(TAG, "Got decode succeeded message");
                state = State.SUCCESS;
                Bundle bundle = message.getData();

                /***********************************************************************/
                Bitmap barcode = bundle == null ? null :
                        (Bitmap) bundle.getParcelable(NewDecodeThread.BARCODE_BITMAP);
                activity.handleDecode((Result) message.obj, barcode);
                break;
            case R.id.decode_failed:
                // We're decoding as fast as possible, so when one decode fails, start another.
                state = State.PREVIEW;
                NewCameraManager.get().requestPreviewFrame(decodeThread.getHandler(), R.id.decode);
                break;
            case R.id.return_scan_result:
                Log.d(TAG, "Got return scan result message");
                activity.setResult(Activity.RESULT_OK, (Intent) message.obj);
                activity.finish();
                break;
            case R.id.launch_product_query:
                Log.d(TAG, "Got product query message");
                String url = (String) message.obj;
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
                activity.startActivity(intent);
                break;
        }
    }

    public void quitSynchronously() {
        state = State.DONE;
        NewCameraManager.get().stopPreview();
        Message quit = Message.obtain(decodeThread.getHandler(), R.id.quit);
        quit.sendToTarget();
        try {
            decodeThread.join();
        } catch (InterruptedException e) {
            // continue
        }

        // Be absolutely sure we don't send any queued up messages
        removeMessages(R.id.decode_succeeded);
        removeMessages(R.id.decode_failed);
    }

    private void restartPreviewAndDecode() {
        if (state == State.SUCCESS) {
            state = State.PREVIEW;
            NewCameraManager.get().requestPreviewFrame(decodeThread.getHandler(), R.id.decode);
            NewCameraManager.get().requestAutoFocus(this, R.id.auto_focus);
            activity.drawViewfinder();
        }
    }

}
