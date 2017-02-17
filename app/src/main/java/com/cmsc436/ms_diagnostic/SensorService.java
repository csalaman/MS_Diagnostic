package com.cmsc436.ms_diagnostic;

import android.app.Service;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * Created by Shubham Patel
 * This service collects sensor orientation data to be used by the other Activity
 */

public class SensorService extends Service implements SensorEventListener{
//    public static final int SERVICE_STARTED = 1;
    Sensor accelerometer;
    Sensor magneticFeild;

    SensorManager sensorManager;

    float [] R;
    float [] I;

    float [] orientation;
    float [] magneticVector;
    float [] accelerationVector;
    private float pichAngle;
    private float rollAngle;

//    float inclineAngle;

    private IBinder binder = new LocalBinder();
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        magneticFeild = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

        sensorManager.registerListener(this,accelerometer,5);
        sensorManager.registerListener(this,magneticFeild,5);

        return START_STICKY;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if(event.sensor.getType() == Sensor.TYPE_ACCELEROMETER){
            accelerationVector = event.values;
        }else {
            magneticVector = event.values;
        }

        if(accelerationVector == null || magneticVector == null) return;

        R = new float[9];
//        I = new float[9];

        if(!SensorManager.getRotationMatrix(R,null,accelerationVector, magneticVector)) return;

        orientation = new float[3];
        SensorManager.getOrientation(R,orientation);
        pichAngle = orientation[1];
        rollAngle = orientation[2];

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    public class LocalBinder extends Binder{
        public SensorService getServerInstance(){
            return SensorService.this;
        }
    }

    public float getPichAngle(){
        return pichAngle;
    }

    public float getRollAngle(){
        return rollAngle;
    }

    public double getYScalar(){
        return Math.tan(pichAngle);
    }

    public double getXScalar(){
        return Math.tan(rollAngle);
    }


}
