package com.foodorder.activity;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.foodorder.R;
import com.foodorder.base.BaseActivity;
import com.foodorder.contant.AppKey;
import com.foodorder.entry.Bluetooth;
import com.foodorder.logic.UserManager;
import com.foodorder.pop.BluetoothPop;
import com.foodorder.pop.LoginUserPop;
import com.foodorder.runtime.RT;
import com.foodorder.runtime.WeakHandler;
import com.foodorder.server.api.API_Food;
import com.foodorder.server.callback.JsonResponseCallback;
import com.foodorder.util.PhoneUtil;
import com.foodorder.util.PreferenceHelper;
import com.foodorder.util.SoftKeyboardUtil;
import com.foodorder.util.ToastUtil;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.lzy.okhttputils.OkHttpUtils;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Set;


public class LoginActivity extends BaseActivity implements LoginUserPop.OnUserSelectedListener {
    private static final String TAG = "LoginActivity";
    public static final int ENABLE_BLUETOOTH = 1;
    private TextView tv_username, tv_bluetootch;
    private EditText et_password;
    private Button btn_login, btn_zxing;
    private RadioGroup rg_language;
    private RadioButton rb_zh, rb_fr;
    private BluetoothAdapter blueadapter;
    private List<Bluetooth> bluetoothList;
    private DeviceReceiver myDevice = new DeviceReceiver();
    private ArrayList<String> deviceList_found = new ArrayList<String>();
    private String lastTitle = "";
    private BluetoothPop bluetoothPop;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    public void initView() {
        tv_username = (TextView) findViewById(R.id.tv_username);
        tv_bluetootch = (TextView) findViewById(R.id.tv_bluetootch);
        et_password = (EditText) findViewById(R.id.et_password);
        btn_login = (Button) findViewById(R.id.btn_login);
        btn_zxing = (Button) findViewById(R.id.btn_zxing);
        rg_language = (RadioGroup) findViewById(R.id.rg_language);
        rb_zh = (RadioButton) findViewById(R.id.rb_zh);
        rb_fr = (RadioButton) findViewById(R.id.rb_fr);

        btn_login.setOnClickListener(this);
        btn_zxing.setOnClickListener(this);
        tv_username.setOnClickListener(this);
        tv_bluetootch.setOnClickListener(this);

        rg_language.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                String language = PreferenceHelper.ins().getStringShareData(AppKey.LANGUAGE, "zh");
                switch (checkedId) {
                    case R.id.rb_zh:
                        if ("zh".equals(language)) {
                            return;
                        }
                        switchLanguage("zh");
                        break;
                    case R.id.rb_fr:
                        if ("fr".equals(language)) {
                            return;
                        }
                        switchLanguage("fr");
                        break;
                }
            }
        });

        //注册蓝牙广播接收者
        IntentFilter filterStart = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        IntentFilter filterEnd = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        registerReceiver(myDevice, filterStart);
        registerReceiver(myDevice, filterEnd);

    }

    @Override
    public void initData() {
        if (bluetoothList == null) {
            bluetoothList = new ArrayList<>();
        }
        String bluetooth = PreferenceHelper.ins().getStringShareData(AppKey.DEFAULT_BLUETOOTCH, "");
        if (!TextUtils.isEmpty(bluetooth)) {
            tv_bluetootch.setText(bluetooth);
        }
        String language = PreferenceHelper.ins().getStringShareData(AppKey.LANGUAGE, PhoneUtil.isZh() ? "zh" : "fr");
        if (language.equals("zh")) {
            rb_zh.setChecked(true);
        } else {
            rb_fr.setChecked(true);
        }

        if (UserManager.getInstance().getUserList().size() == 1) {
            tv_username.setText(UserManager.getInstance().getUserList().get(0));
        } else {
            tv_username.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (tv_username != null) {
                        tv_username.performClick();
                    }
                }
            }, 500);

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        OkHttpUtils.getInstance().cancelTag(TAG);
        if (myDevice != null) {
            unregisterReceiver(myDevice);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login:
                SoftKeyboardUtil.hideSoftKeyboard(et_password);
                String username = tv_username.getText().toString();
                String password = et_password.getText().toString();
                String bluetooth = tv_bluetootch.getText().toString();
                if (TextUtils.isEmpty(username)) {
                    ToastUtil.showToast(getString(R.string.login_username_empty));
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    ToastUtil.showToast(getString(R.string.login_password_empty));
                    return;
                }

                if (RT.DEBUG) {
                    startActivity(new Intent(LoginActivity.this, OrdersActivity.class));
                    finish();
                } else {
//                    if (TextUtils.isEmpty(bluetooth)) {
//                        ToastUtil.showToast(getString(R.string.bluetootch_empty));
//                        return;
//                    }
                    API_Food.ins().login(TAG, username, password, new JsonResponseCallback() {
                        @Override
                        public boolean onJsonResponse(JSONObject json, int errcode, String errmsg, int id, boolean fromcache) {
                            if (errcode == 200) {
                                startActivity(new Intent(LoginActivity.this, OrdersActivity.class));
                                finish();
                                ToastUtil.showToast(getString(R.string.login_success));
                            } else {
                                ToastUtil.showToast(getString(R.string.login_failed));
                            }
                            return false;
                        }
                    });
                }

                break;
            case R.id.btn_zxing:
                Dexter.checkPermission(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        startActivity(new Intent(LoginActivity.this, ScanActivity.class));
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {

                    }
                }, Manifest.permission.CAMERA);
                break;
            case R.id.tv_username:
                if (UserManager.getInstance().getUserList() != null && UserManager.getInstance().getUserList().size() > 1) {
                    LoginUserPop userPop = new LoginUserPop(this);
                    userPop.setOnUserSelectedListener(this);
                    userPop.showPopup(tv_username);
                }
                break;
            case R.id.tv_bluetootch:
                setBluetooth();
                break;
        }
    }


    @Override
    public void selectedUser(String username) {
        tv_username.setText(username);
        et_password.requestFocus();
        new WeakHandler().postDelayed(new Runnable() {
            @Override
            public void run() {
                et_password.requestFocus();
                SoftKeyboardUtil.showSoftKeyboard(et_password);
            }
        }, 500);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ENABLE_BLUETOOTH && resultCode == RESULT_OK) {
            showblueboothlist();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    protected void switchLanguage(String language) {

        Resources resources = getResources();
        Configuration config = resources.getConfiguration();
        DisplayMetrics dm = resources.getDisplayMetrics();
        switch (language) {
            case "fr":
                config.locale = Locale.FRANCE;
                resources.updateConfiguration(config, dm);
                PreferenceHelper.ins().storeShareStringData(AppKey.LANGUAGE, "fr");
                PreferenceHelper.ins().commit();
                break;
            case "zh":
                config.locale = Locale.SIMPLIFIED_CHINESE;
                resources.updateConfiguration(config, dm);
                PreferenceHelper.ins().storeShareStringData(AppKey.LANGUAGE, "zh");
                PreferenceHelper.ins().commit();
                break;
            default:
                config.locale = Locale.SIMPLIFIED_CHINESE;
                resources.updateConfiguration(config, dm);
                break;
        }

        Intent intent = new Intent(LoginActivity.this, SplashActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    private void setBluetooth() {
        blueadapter = BluetoothAdapter.getDefaultAdapter();
        //确认开启蓝牙
        if (!blueadapter.isEnabled()) {
            //请求用户开启
            Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(intent, ENABLE_BLUETOOTH);

        } else {
            //蓝牙已开启
            showblueboothlist();
        }
    }

    private void showblueboothlist() {
        bluetoothPop = new BluetoothPop(this);
        bluetoothPop.showPopup(tv_bluetootch);
        bluetoothPop.setOnBluetoothSelectedListener(new BluetoothPop.OnBluetoothSelectedListener() {
            @Override
            public void selectedBluetooth(Bluetooth bluetooth) {
                String address = bluetooth.getAddress();
                if (!TextUtils.isEmpty(address)) {
                    PreferenceHelper.ins().storeShareStringData(AppKey.DEFAULT_BLUETOOTCH, address);
                    PreferenceHelper.ins().commit();
                    tv_bluetootch.setText(bluetooth.getAddress());
                }

            }
        });
        findAvalibleDevice();
    }

    private void findAvalibleDevice() {
        // TODO Auto-generated method stub
        //获取可配对蓝牙设备
        Set<BluetoothDevice> device = blueadapter.getBondedDevices();
        if (bluetoothList != null) {
            bluetoothList.clear();
        }

//        if (blueadapter != null && blueadapter.isDiscovering()) {
//            adapter1.notifyDataSetChanged();
//        }
        if (device.size() > 0) {
            //存在已经配对过的蓝牙设备
            for (Iterator<BluetoothDevice> it = device.iterator(); it.hasNext(); ) {
                BluetoothDevice btd = it.next();
                Bluetooth bluetooth = new Bluetooth();
                if (TextUtils.equals(lastTitle, getString(R.string.bluetootch_bonded))) {
                    bluetooth.setHasTitle(false);
                } else {
                    bluetooth.setHasTitle(true);
                }
                bluetooth.setType(getString(R.string.bluetootch_bonded));
                bluetooth.setTitle(btd.getName());
                bluetooth.setAddress(btd.getAddress());
                bluetoothList.add(bluetooth);
                lastTitle = getString(R.string.bluetootch_bonded);
            }
            bluetoothPop.setData(bluetoothList);

        }

    }


    /**
     * 蓝牙搜索状态广播监听
     */
    private class DeviceReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {    //搜索到新设备
                BluetoothDevice btd = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                //搜索没有配过对的蓝牙设备
                if (btd.getBondState() != BluetoothDevice.BOND_BONDED) {
                    if (!deviceList_found.contains(btd.getName() + '\n' + btd.getAddress())) {

                        deviceList_found.add(btd.getName() + '\n' + btd.getAddress());
                        Bluetooth bluetooth = new Bluetooth();
                        if (TextUtils.equals(lastTitle, getString(R.string.bluetootch_new))) {
                            bluetooth.setHasTitle(false);
                        } else {
                            bluetooth.setHasTitle(true);
                        }
                        bluetooth.setType(getString(R.string.bluetootch_bonded));
                        bluetooth.setTitle(btd.getName());
                        bluetooth.setAddress(btd.getAddress());
                        bluetoothList.add(bluetooth);
                        lastTitle = getString(R.string.bluetootch_bonded);
                    }

                }
            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {   //搜索结束

                bluetoothPop.setData(bluetoothList);
            }
        }
    }
}
