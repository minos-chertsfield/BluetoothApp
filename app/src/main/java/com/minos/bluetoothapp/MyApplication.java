package com.minos.bluetoothapp;

import android.app.Application;

import com.minos.bluetoothapp.callback.MyBleWrapperCallback;
import com.minos.bluetoothapp.device.BleRssiDevice;

import java.util.UUID;

import cn.com.heaton.blelibrary.ble.Ble;
import cn.com.heaton.blelibrary.ble.BleLog;
import cn.com.heaton.blelibrary.ble.model.BleDevice;
import cn.com.heaton.blelibrary.ble.model.BleFactory;
import cn.com.heaton.blelibrary.ble.utils.UuidUtils;

/**
 * @Author : 沈智生
 * @Description: 描述
 * @CreateDate: 2020/12/10 10:56
 * @UpdateUser: 更新者
 * @UpdateDate: 2020/12/10 10:56
 **/
public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        initBle();
    }

    // 初始化低功耗蓝牙
    private void initBle() {
        Ble.options()//开启配置
                .setLogBleEnable(true)//设置是否输出打印蓝牙日志（非正式打包请设置为true，以便于调试）
                .setThrowBleException(true)//设置是否抛出蓝牙异常 （默认true）
                .setAutoConnect(false)//设置是否自动连接 （默认false）
                .setIgnoreRepeat(false)//设置是否过滤扫描到的设备(已扫描到的不会再次扫描)
                .setConnectTimeout(10 * 1000)//设置连接超时时长（默认10*1000 ms）
                .setMaxConnectNum(7)//最大连接数量
                .setScanPeriod(12 * 1000)//设置扫描时长（默认10*1000 ms）
//                .setScanFilter(scanFilter)//设置扫描过滤
                .setUuidService(UUID.fromString(UuidUtils.uuid16To128("fd00")))//设置主服务的uuid（必填）
                .setUuidWriteCha(UUID.fromString(UuidUtils.uuid16To128("fd01")))//设置可写特征的uuid （必填,否则写入失败）
                .setUuidReadCha(UUID.fromString(UuidUtils.uuid16To128("fd02")))//设置可读特征的uuid （选填）
                .setUuidNotifyCha(UUID.fromString(UuidUtils.uuid16To128("fd03")))//设置可通知特征的uuid （选填，库中默认已匹配可通知特征的uuid）
//                .setUuidServicesExtra(new UUID[]{BATTERY_SERVICE_UUID})//设置额外的其他服务组，如电量服务等
                .setFactory(new BleFactory() {//实现自定义BleDevice时必须设置
                    @Override
                    public BleRssiDevice create(String address, String name) {
                        return new BleRssiDevice(address, name);//自定义BleDevice的子类
                    }
                })
                .setBleWrapperCallback(new MyBleWrapperCallback())//设置全部蓝牙相关操作回调（例： OTA升级可以再这里实现,与项目其他功能逻辑完全解耦）
                .create(this, new Ble.InitCallback() {
                    @Override
                    public void success() {
                        BleLog.e("MainApplication", "初始化成功");
                    }

                    @Override
                    public void failed(int failedCode) {
                        BleLog.e("MainApplication", "初始化失败：" + failedCode);
                    }
                });

    }
}
