package com.foodorder.activity;

import android.os.Bundle;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.foodorder.R;
import com.foodorder.contant.AppKey;
import com.foodorder.contant.EventTag;
import com.foodorder.log.DLOG;
import com.foodorder.runtime.event.EventManager;
import com.foodorder.server.ServerManager;
import com.foodorder.util.PreferenceHelper;

import cn.bingoogolapple.qrcode.core.QRCodeView;
import cn.bingoogolapple.qrcode.zxing.ZXingView;

public class ScanActivity extends AppCompatActivity implements QRCodeView.Delegate, View.OnClickListener {

    private ImageButton ib_back;
    private QRCodeView mQRCodeView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);
        ib_back = (ImageButton) findViewById(R.id.ib_back);
        mQRCodeView = (ZXingView) findViewById(R.id.zxingview);
        mQRCodeView.setDelegate(this);

        mQRCodeView.startSpotDelay(3000);
        ib_back.setOnClickListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mQRCodeView.startCamera();
//        mQRCodeView.startCamera(Camera.CameraInfo.CAMERA_FACING_FRONT);
    }

    @Override
    protected void onStop() {
        mQRCodeView.stopCamera();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        mQRCodeView.onDestroy();
        super.onDestroy();
    }

    private void vibrate() {
        Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        vibrator.vibrate(200);
    }

    @Override
    public void onScanQRCodeSuccess(String result) {
        Toast.makeText(this, result, Toast.LENGTH_SHORT).show();
        ServerManager.SERVER_DOMAIN = result;
        PreferenceHelper.ins().storeShareStringData(AppKey.SERVER_DOMAIN, result);
        PreferenceHelper.ins().commit();
        vibrate();
        mQRCodeView.startSpot();
        EventManager.ins().sendEvent(EventTag.GET_SERVER_DOMAIN_FROM_ZXING, 0, 0, result);
        finish();
    }

    @Override
    public void onScanQRCodeOpenCameraError() {
        DLOG.e("打开相机出错");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ib_back:
                onBackPressed();
                break;
        }
    }
}
