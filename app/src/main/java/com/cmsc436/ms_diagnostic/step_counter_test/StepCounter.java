package com.cmsc436.ms_diagnostic.step_counter_test;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.cmsc436.ms_diagnostic.R;

public class StepCounter extends Activity  {
    private TextView textView;
    private SensorManager sensorManager;
    private Sensor countSensor;
    //private static final String TEXT_NUM_STEPS = "Number of Steps: ";
    private float numSteps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_counter);

        textView = (TextView) findViewById(R.id.count);
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        countSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        if (countSensor != null) {
            sensorManager.registerListener(mySensorEventListener, countSensor, SensorManager.SENSOR_DELAY_UI);
        } else {
            Toast.makeText(this, "Count sensor not available!", Toast.LENGTH_LONG).show();
        }
    }

    private SensorEventListener mySensorEventListener = new SensorEventListener() {
        public void onSensorChanged(SensorEvent event) {
            numSteps = event.values[0];
            System.out.println(numSteps);
            textView.setText(String.valueOf(event.values[0]));
        }

        public void onAccuracyChanged(Sensor sensor, int i) {

        }
    };
}
