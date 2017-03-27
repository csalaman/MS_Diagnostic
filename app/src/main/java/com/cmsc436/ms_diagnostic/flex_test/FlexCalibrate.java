package com.cmsc436.ms_diagnostic.flex_test;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.cmsc436.ms_diagnostic.R;

public class FlexCalibrate extends AppCompatActivity implements SensorEventListener {

    SensorManager sensorManager;
    Sensor accel;

    final static String REST = "REST";
    final static String FLEX = "FLEX";

    Button button;
    TextView textView;

    float x,y,z;


    Intent toSend;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flex_calibrate);
        toSend = new Intent();

        button = (Button) findViewById(R.id.flex_calibrate_button);
        textView = (TextView) findViewById(R.id.flex_calibrate_text);
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        accel = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(this,accel,SensorManager.SENSOR_DELAY_NORMAL);


    }


    @Override
    protected void onResume() {
        super.onResume();
        button.setOnClickListener(restOnClick);
    }



    private View.OnClickListener restOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            float [] pos = {x,y,z};
            toSend.putExtra(REST,pos);

            textView.setText("X: "+pos[0]+"\nY: "+pos[1]+"\nZ: "+pos[2]);
            button.setOnClickListener(flexOnClick);
            button.setText("FLEX");

        }
    };

    private View.OnClickListener flexOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            float [] pos = {x,y,z};
            toSend.putExtra(FLEX,pos);
            setResult(RESULT_OK,toSend);
            finish();
        }
    };

    @Override
    public void onSensorChanged(SensorEvent event) {
        x = event.values[0];
        y = event.values[1];
        z = event.values[2];
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
