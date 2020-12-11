package com.minos.bluetoothapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    // 传统蓝牙
    Button btnTradition;
    // 低功耗蓝牙
    Button btnBLE;
    // 三方应用
    Button btnThird;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnTradition = (Button) findViewById(R.id.btn_tradition);
        btnBLE = (Button) findViewById(R.id.btn_ble);
        btnThird = (Button) findViewById(R.id.btn_third);

        btnTradition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, TraditionActivity.class);
                startActivity(intent);
            }
        });

        btnBLE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, BleActivity.class);
                startActivity(intent);
            }
        });

        btnThird.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ThirdActivity.class);
                startActivity(intent);
            }
        });

    }


}