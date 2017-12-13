package com.willkernel.app.hencodercanvas;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    /**
     * Camera setLocation(0,0,newZ)
     * x 轴正向 顺时针为+
     * y 轴正向 逆时针为-
     * z 轴正向 顺时针为+
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}
