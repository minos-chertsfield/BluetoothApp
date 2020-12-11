package com.minos.bluetoothapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.app.Notification;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.Date;
import java.util.Set;
import java.util.UUID;

import static android.bluetooth.BluetoothAdapter.EXTRA_PREVIOUS_STATE;
import static android.bluetooth.BluetoothAdapter.EXTRA_STATE;
import static cn.com.heaton.blelibrary.ble.Ble.REQUEST_ENABLE_BT;

/**
 * 传统蓝牙
 */
public class TraditionActivity extends AppCompatActivity {

    public static final String TAG = "TraditionActivity";
    public static final int REQUEST_ENABLE_BT = 99;

    Button btnPairedDevice;

    Button btnScanDevice;

    BlueToothReceiver receiver;
    // 蓝牙适配器
    BluetoothAdapter bluetoothAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tradition);

        checkBlueToothPermission();

        btnPairedDevice = (Button) findViewById(R.id.btn_paired);
        btnScanDevice = (Button) findViewById(R.id.btn_scan);

        setUp();

        btnPairedDevice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPairedDevices();
            }
        });

        btnScanDevice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scan();
            }
        });

        getDeviceId();
    }

    @Override
    protected void onResume() {
        super.onResume();

        receiver = new BlueToothReceiver();
        IntentFilter intentFilter = new IntentFilter();
        // 蓝牙状态变化的广播
        intentFilter.addAction("android.bluetooth.adapter.action.STATE_CHANGED");
        // 蓝牙设备发现广播
        intentFilter.addAction(BluetoothDevice.ACTION_FOUND);
        // 设备扫描开始广播
        intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        // 设备扫描完成广播
        intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        // 注册蓝牙接收器
        registerReceiver(receiver, intentFilter);
    }

    /**
     * 设置蓝牙
     */
    private void setUp() {
        // 判断设备是否支持蓝牙
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter == null) {
            // Device doesn't support Bluetooth
            Log.d(TAG, "当前设备不支持蓝牙");
            Toast.makeText(this, "当前设备不支持蓝牙", Toast.LENGTH_SHORT).show();
        }
        // 判断当前的蓝牙是否启用
        if (!bluetoothAdapter.isEnabled()) {
            Log.d(TAG, "当前蓝牙未启用");
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }
    }

    // 检测蓝牙相关权限
    private void checkBlueToothPermission() {
        if(ContextCompat.checkSelfPermission(TraditionActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED){//未开启定位权限
            //开启定位权限,200是标识码
            ActivityCompat.requestPermissions(TraditionActivity.this,new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
            },200);
        }else{
            Toast.makeText(TraditionActivity.this,"已开启定位权限",Toast.LENGTH_LONG).show();
        }
    }

    // 检测手机状态
    private void checkDeviceStatePermission() {
        if(ContextCompat.checkSelfPermission(TraditionActivity.this, Manifest.permission.READ_PHONE_STATE)
                != PackageManager.PERMISSION_GRANTED){//未开启定位权限
            //开启定位权限,200是标识码
            ActivityCompat.requestPermissions(TraditionActivity.this,new String[]{
                    Manifest.permission.READ_PHONE_STATE
            },201);
        }else{
            Toast.makeText(TraditionActivity.this,"已开启设备状态权限",Toast.LENGTH_LONG).show();
        }
    }

    // 扫描附近的设备
    private void scan() {
        if (bluetoothAdapter != null) {
            if (bluetoothAdapter.isDiscovering()) {
                bluetoothAdapter.cancelDiscovery();
            } else {
                bluetoothAdapter.startDiscovery();
            }
        }
    }

    // 显示已配对设备
    private void showPairedDevices() {
        if (bluetoothAdapter != null) {
            Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();
            if (pairedDevices.size() > 0) {
                // 遍历当前设备已配对的设备
                for (BluetoothDevice device : pairedDevices) {
                    // 设备名称
                    String deviceName = device.getName();
                    // 设备的硬件地址
                    String deviceHardwareAddress = device.getAddress(); // MAC address
                    Log.d(TAG, "已配对设备[设备名=" + deviceName + ", 设备MAC地址=" + deviceHardwareAddress + "]");
                }
            }
        }
    }

    private String getDeviceId() {
        String uuid = new Date().getTime() + UUID.randomUUID().toString();
        Log.d(TAG, "获取=" + uuid);
        return uuid;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_ENABLE_BT) {
            Log.d(TAG, "返回蓝牙请求");
            if (resultCode == Activity.RESULT_OK) {
                Log.d(TAG, "蓝牙启用成功");
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Log.d(TAG, "蓝牙启用失败");
            }
        } else if (requestCode == 200) {
            if(resultCode == PackageManager.PERMISSION_GRANTED){ //用户同意权限,执行我们的操作
                Log.d(TAG, "定位权限已开启");
            } else {//用户拒绝之后,当然我们也可以弹出一个窗口,直接跳转到系统设置页面
                Toast.makeText(TraditionActivity.this,"未开启定位权限,请手动到设置去开启权限",Toast.LENGTH_LONG).show();
            }
        }
    }

    // 连接设备
    private void connect(BluetoothDevice device, String uuid) {
        ConnectThread thread = new ConnectThread(device, uuid);
        thread.start();
    }

    // 接收蓝牙相关的广播
    static class BlueToothReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (TextUtils.equals(intent.getAction(), "android.bluetooth.adapter.action.STATE_CHANGED")) {
                Log.d(TAG, "接收到蓝牙状态广播");

                int current = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, 0);
                int old = intent.getIntExtra(EXTRA_PREVIOUS_STATE, 0);

                Log.d(TAG, "当前状态");
                switch (current) {
                    case BluetoothAdapter.STATE_ON:
                        Log.d(TAG, "蓝牙开启");
                        break;
                    case BluetoothAdapter.STATE_OFF:
                        Log.d(TAG, "蓝牙关闭");
                        break;
                    case BluetoothAdapter.STATE_TURNING_ON:
                        Log.d(TAG, "蓝牙正在开启");
                        break;
                    case BluetoothAdapter.STATE_TURNING_OFF:
                        Log.d(TAG, "蓝牙正在关闭");
                        break;
                }

                Log.d(TAG, "老状态");
                switch (old) {
                    case BluetoothAdapter.STATE_ON:
                        Log.d(TAG, "蓝牙开启");
                        break;
                    case BluetoothAdapter.STATE_OFF:
                        Log.d(TAG, "蓝牙关闭");
                        break;
                    case BluetoothAdapter.STATE_TURNING_ON:
                        Log.d(TAG, "蓝牙正在开启");
                        break;
                    case BluetoothAdapter.STATE_TURNING_OFF:
                        Log.d(TAG, "蓝牙正在关闭");
                        break;
                }
            } else if (TextUtils.equals(intent.getAction(), BluetoothDevice.ACTION_FOUND)) {
                Log.d(TAG, "接收到设备发现广播");
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                String deviceName = device.getName();
                String deviceHardwareAddress = device.getAddress(); // MAC address
                Log.d(TAG, "发现设备=[设备名=" + deviceName + ", 设备MAC地址=" + deviceHardwareAddress + "]");
            }
        }
    }
}