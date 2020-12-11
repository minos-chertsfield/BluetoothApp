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
import android.text.TextUtils;
import android.util.Log;

import com.minos.bluetoothapp.device.BleRssiDevice;

import java.util.ArrayList;
import java.util.List;

import cn.com.heaton.blelibrary.ble.Ble;
import cn.com.heaton.blelibrary.ble.callback.BleScanCallback;
import cn.com.heaton.blelibrary.ble.model.BleDevice;
import cn.com.heaton.blelibrary.ble.model.ScanRecord;

import static cn.com.heaton.blelibrary.ble.Ble.REQUEST_ENABLE_BT;

public class ThirdActivity extends AppCompatActivity {

    // 设备列表
    private List<BleRssiDevice> devices;
    private Ble<BleRssiDevice> ble;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third);

        checkBlueToothPermission();

        checkBleDevice();
        // 实例化设备列表
        devices = new ArrayList<>();
        ble = Ble.getInstance();
        ble.startScan(scanCallback);
    }

    // 检测蓝牙相关权限
    private void checkBlueToothPermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            //校验是否已具有模糊定位权限
            if (ContextCompat.checkSelfPermission(ThirdActivity.this,
                    Manifest.permission.ACCESS_COARSE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(ThirdActivity.this,
                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                        REQUEST_ENABLE_BT );
            }else{
                // 权限已打开
//                startScan();
            }
        } else { //小于23版本直接使用
//            startScan();
        }
    }

    // 蓝牙扫描的回调
    BleScanCallback<BleRssiDevice> scanCallback = new BleScanCallback<BleRssiDevice>() {
        @Override
        public void onLeScan(final BleRssiDevice device, int rssi, byte[] scanRecord) {
            //Scanned devices
//            Log.d("BlueTooth", "蓝牙扫描设备");
            synchronized (ble.getLocker()) {
                // 遍历设备列表
                for (int i = 0; i < devices.size(); i++) {
                    BleRssiDevice rssiDevice = devices.get(i);
                    if (TextUtils.equals(rssiDevice.getBleAddress(), device.getBleAddress())){
                        if (rssiDevice.getRssi() != rssi && System.currentTimeMillis()-rssiDevice.getRssiUpdateTime() > 1000L){
                            rssiDevice.setRssiUpdateTime(System.currentTimeMillis());
                            rssiDevice.setRssi(rssi);
//                            adapter.notifyItemChanged(i);
                            Log.d("BlueTooth", "更新设备" + devices.get(i).getBleName());
                        }
                        return;
                    }
                }
                device.setScanRecord(ScanRecord.parseFromBytes(scanRecord));
                device.setRssi(rssi);
                devices.add(device);
//                adapter.notifyDataSetChanged()
                Log.d("BlueTooth", "蓝牙设备列表=" + devices.toString());
            }
        }

        @Override
        public void onStart() {
            super.onStart();
        }

        @Override
        public void onStop() {
            super.onStop();
        }

        @Override
        public void onScanFailed(int errorCode) {
            super.onScanFailed(errorCode);
            Log.e("BlueTooth", "onScanFailed: "+errorCode);
        }


    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == REQUEST_ENABLE_BT && resultCode == Activity.RESULT_CANCELED) {
            finish();
        } else if (requestCode == REQUEST_ENABLE_BT && resultCode == Activity.RESULT_OK) {
            ble.startScan(scanCallback);
        }
//        else if (requestCode == REQUEST_GPS){
//
//        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void checkBleDevice() {
        //首先获取BluetoothManager
        BluetoothManager bluetoothManager =
                (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        //获取BluetoothAdapter
        if (bluetoothManager != null) {
            BluetoothAdapter mBluetoothAdapter = bluetoothManager.getAdapter();
            if (mBluetoothAdapter != null) {
                Log.d("tag", "蓝牙启用状态=" + mBluetoothAdapter.isEnabled());
                if (!mBluetoothAdapter.isEnabled()) {
                    //调用enable()方法直接打开蓝牙
                    if (!mBluetoothAdapter.enable()) {
                        Log.i("tag","蓝牙打开失败");
                    } else{
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

}