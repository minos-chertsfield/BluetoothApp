package com.minos.bluetoothapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import static cn.com.heaton.blelibrary.ble.Ble.REQUEST_ENABLE_BT;

/**
 * 低功耗蓝牙
 */
public class BleActivity extends AppCompatActivity {

    public static final String TAG = "BleActivity";
    private BluetoothAdapter bluetoothAdapter;
    private static final int REQUEST_ENABLE_BT = 10000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ble);

        checkBlueToothPermission();
        checkBleDevice();

        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            Toast.makeText(this, "该设备不支持BLE", Toast.LENGTH_SHORT).show();
            finish();
        }
        // 初始化蓝牙
        initBlueTooth();

    }

    // 检测蓝牙相关权限
    private void checkBlueToothPermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            //校验是否已具有模糊定位权限
            if (ContextCompat.checkSelfPermission(BleActivity.this,
                    Manifest.permission.ACCESS_COARSE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(BleActivity.this,
                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                        REQUEST_ENABLE_BT );
            }else{
                // 权限已打开
                Log.d(TAG, "权限已开启");
//                startScan();
            }
        } else { //小于23版本直接使用
//            startScan();
            Log.d(TAG, "低版本无需申请权限");
        }
    }

    private void initBlueTooth() {
        final BluetoothManager bluetoothManager =
                (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        // 获取蓝牙适配器
        bluetoothAdapter = bluetoothManager.getAdapter();

        if (bluetoothAdapter == null || !bluetoothAdapter.isEnabled()) {
            Log.d("BlueTooth", "蓝牙已启用");
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        } else {
            Toast.makeText(this, "蓝牙未启用", Toast.LENGTH_SHORT).show();
        }
    }

    private void checkBleDevice() {
        //首先获取BluetoothManager
        BluetoothManager bluetoothManager =
                (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        //获取BluetoothAdapter
        if (bluetoothManager != null) {
            BluetoothAdapter mBluetoothAdapter = bluetoothManager.getAdapter();
            if (mBluetoothAdapter != null) {
                if (!mBluetoothAdapter.isEnabled()) {
                    //调用enable()方法直接打开蓝牙
                    if (!mBluetoothAdapter.enable()){
                        Log.i("tag","蓝牙打开失败");
                    } else {
                        Log.i("tag","蓝牙已打开");
                    }
                    //该方法也可以打开蓝牙，但是会有一个很丑的弹窗，可以自行尝试一下
//                    Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
//                    enableBtIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                    startActivity(enableBtIntent);
                }
            } else {
                Log.i("tag","同意申请");
            }
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}