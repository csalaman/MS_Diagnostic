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
    private float xzAngle;
    private float xyAngle;
    private float yzAngle;

    double vo;


    //added by Lauren
    private float timestamp;
    private static final float NS2S = 1.0f / 1000000000.0f;

    private float[] velocity;
    private float[] position;
    private float[] last_values;


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

        vo = 0.0;

        velocity = new float[3];
        position = new float[3];
        last_values = new float[3];

        for (int i = 0; i < 3; i++) {
            velocity[i] = 0.0f;
            position[i] = 0.0f;
        }

        return START_STICKY;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if(event.sensor.getType() == Sensor.TYPE_ACCELEROMETER){
            accelerationVector = event.values;
            float dt = (event.timestamp - timestamp) * NS2S; //added by lauren

            for (int i = 0; i < 3; i++) {
//                velocity[i] += (accelerationVector[i] + last_values[i])/2 * dt;
                velocity[i] += accelerationVector[i] * dt;
                position[i] += velocity[i] * dt;
            }

            System.arraycopy(accelerationVector, 0, last_values, 0, 3);
            timestamp = event.timestamp;
        }else {
            magneticVector = event.values;
        }

        if(accelerationVector == null || magneticVector == null) return;

        R = new float[9];
//        I = new float[9];

        if(!SensorManager.getRotationMatrix(R,null,accelerationVector, magneticVector)) return;

        orientation = new float[3];
        SensorManager.getOrientation(R,orientation);
        xzAngle = orientation[0];
        xyAngle = orientation[1];
        yzAngle = orientation[2];

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    public class LocalBinder extends Binder{
        public SensorService getServerInstance(){
            return SensorService.this;
        }
    }


    public float getYAngle(){
        return xyAngle;
    }

    public float getxAngle(){
        return yzAngle;
    }

    public double getDeltaX(){
        return Math.tan(yzAngle);
    }

    public double getDeltaY(){
        return Math.tan(xyAngle);
    }


    public float[] getOrientation(){
        return orientation;
    }


}
