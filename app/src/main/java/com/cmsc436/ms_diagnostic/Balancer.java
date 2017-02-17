package com.cmsc436.ms_diagnostic;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class Balancer extends AppCompatActivity implements SensorEventListener {

    Sensor sensor;
    SensorManager sensorManager;
    TextView x_text;
    TextView y_text;
    TextView z_text;

    public static int x;
    public static int y;

    CDrawView mCDrawView;
    ShapeDrawable mDraw = new ShapeDrawable();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mCDrawView = new CDrawView(this);
        setContentView(mCDrawView);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
       // sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL);

        x_text = (TextView) findViewById(R.id.x_text);
        y_text = (TextView) findViewById(R.id.y_text);
        z_text = (TextView) findViewById(R.id.z_text);




    }

    @Override
    public void onSensorChanged(SensorEvent event) {
//        x_text.setText("X: " + event.values[0]);
//        y_text.setText("Y: " + event.values[1]);
//        z_text.setText("Z: " + event.values[2]);

        {
            if(event.sensor.getType() == Sensor.TYPE_ACCELEROMETER){
                x = (int) Math.pow(event.values[0], 2);
                y = (int) Math.pow(event.values[1], 2);
            }
        }
    }

    @Override
    protected void onResume() {

        super.onResume();

        // Register this class as a listener for the accelerometer sensor
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_NORMAL);
        // ...and the orientation sensor
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION),
                SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    protected void onStop()
    {
        // Unregister the listener
        sensorManager.unregisterListener(this);
        super.onStop();
    }

    public class CDrawView extends View {

        static final int width = 50;
        static final int height = 50;

        public CDrawView(Context context){
            super(context);

            mDraw = new ShapeDrawable(new OvalShape());
            mDraw.getPaint().setColor(0xff74AC23);
            mDraw.setBounds(x,y,x+width,y+height);
        }

        protected void onDraw(Canvas canvas){
            RectF oval = new RectF(Balancer.x,Balancer.y, Balancer.x + width,Balancer.y + height);
            Paint p = new Paint();
            p.setColor(Color.BLUE);
            canvas.drawColor(Color.BLACK);
            canvas.drawOval(oval,p);

            invalidate();
        }

    }

}






