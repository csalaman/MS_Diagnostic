package com.cmsc436.ms_diagnostic;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener2;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

public class moveBall extends AppCompatActivity implements SensorEventListener2 {

    private float xPos, xAccel, xVel = 0.0f;
    private float yPos, yAccel, yVel = 0.0f;
    //private float zPos, zAccel, zVel = 0.0f;
    private float xMax, yMax, zMax;
    private Bitmap ball;
    private SensorManager sensorManager;
    BallView ballView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
        ballView = new BallView(this);
        setContentView(ballView);

//        Display display = getWindowManager().getDefaultDisplay();
//        Point size = new Point();
//        display.getSize(size);
        xMax = ballView.getWidth();
        yMax = ballView.getHeight();
        xPos = xMax / 2;
        yPos = yMax / 2;




        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
    }

    @Override
    protected void onStart() {
        super.onStart();
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_GAME);
    }

    @Override
    protected void onStop() {
        sensorManager.unregisterListener(this);
        super.onStop();
    }

    @Override
    public void onFlushCompleted(Sensor sensor) {

    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {

        if (sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            System.out.println("in sensor change");
            xAccel = sensorEvent.values[0];
            yAccel = sensorEvent.values[1];
            updateBall();
        }
    }

    private void updateBall() {
        float frameTime = 0.7f;
        xVel -= (xAccel * frameTime);
        yVel += (yAccel * frameTime);
        float xS = (xVel * frameTime) + (1/2 * xAccel * frameTime * frameTime);
        float yS = (yVel * frameTime) + (1/2 * yAccel * frameTime * frameTime);

        if(xPos +xS > xMax) {
            xPos = xMax;
        } else if(xPos + xS < 0) {
            xPos = 0;
        } else {
            xPos += xS;
        }

        if(yPos +yS > yMax) {
            yPos = yMax;
        } else if(yPos + yS < 0) {
            yPos = 0;
        } else {
            yPos += yS;
        }


//        float frameTime = 0.7f;
//        xVel -= (xAccel * frameTime);
//        yVel += (yAccel * frameTime);
//
//        float xS = (xVel * frameTime) + (1/2 * xAccel * frameTime * frameTime);
//        float yS = (yVel * frameTime) + (1/2 * yAccel * frameTime * frameTime);

//        if(xPos+xS > 0 && xPos+xS < xMax){
//            xPos += xS;
//        }
//        if(yPos+yS > 0 && yPos+yS < yMax){
//            yPos += yS;
//        }



    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    private class BallView extends View {

        public BallView(Context context) {
            super(context);
            Bitmap ballSrc = BitmapFactory.decodeResource(getResources(), R.drawable.ball);
            final int dstWidth = 100;
            final int dstHeight = 100;
            ball = Bitmap.createScaledBitmap(ballSrc, dstWidth, dstHeight, true);
        }

        @Override
        protected void onDraw(Canvas canvas) {
            canvas.drawBitmap(ball, xPos, yPos, null);
            invalidate();
        }
    }
}

