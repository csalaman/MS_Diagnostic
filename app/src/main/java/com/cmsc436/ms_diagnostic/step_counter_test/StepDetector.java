package com.cmsc436.ms_diagnostic.step_counter_test;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;

/**
 * Created by laurenleach on 4/16/17.
 */

public class StepDetector implements SensorEventListener {



    private StepListener listener;
    private int stepCount;

    public void registerListener(StepListener listener) {
        this.listener = listener;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        //stepCount = even;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

}
