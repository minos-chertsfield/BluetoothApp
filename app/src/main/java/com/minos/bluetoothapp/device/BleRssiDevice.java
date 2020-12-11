package com.minos.bluetoothapp.device;

import cn.com.heaton.blelibrary.ble.model.BleDevice;
import cn.com.heaton.blelibrary.ble.model.ScanRecord;

/**
 * @Author : 沈智生
 * @Description: 描述
 * @CreateDate: 2020/12/10 11:16
 * @UpdateUser: 更新者
 * @UpdateDate: 2020/12/10 11:16
 **/
public class BleRssiDevice extends BleDevice {
    private ScanRecord scanRecord;
    private int rssi;
    private long rssiUpdateTime;

    public BleRssiDevice(String address, String name) {
        super(address, name);
    }

    /*public BleRssiDevice(BleDevice device, ScanRecord scanRecord, int rssi) {
        this.device = device;
        this.scanRecord = scanRecord;
        this.rssi = rssi;
    }*/

    public ScanRecord getScanRecord() {
        return scanRecord;
    }

    public void setScanRecord(ScanRecord scanRecord) {
        this.scanRecord = scanRecord;
    }

    public int getRssi() {
        return rssi;
    }

    public void setRssi(int rssi) {
        this.rssi = rssi;
    }

    public long getRssiUpdateTime() {
        return rssiUpdateTime;
    }

    public void setRssiUpdateTime(long rssiUpdateTime) {
        this.rssiUpdateTime = rssiUpdateTime;
    }
}
