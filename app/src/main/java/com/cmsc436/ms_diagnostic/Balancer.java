package com.cmsc436.ms_diagnostic;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class Balancer extends AppCompatActivity implements SensorEventListener {

    Sensor sensor;
    SensorManager sensorManager;
    TextView x_text;
    TextView y_text;
    TextView z_text;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_balancer);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL);

        x_text = (TextView) findViewById(R.id.x_text);
        y_text = (TextView) findViewById(R.id.y_text);
        z_text = (TextView) findViewById(R.id.z_text);

    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        x_text.setText("X: " + event.values[0]);
        y_text.setText("Y: " + event.values[1]);
        z_text.setText("Z: " + event.values[2]);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
