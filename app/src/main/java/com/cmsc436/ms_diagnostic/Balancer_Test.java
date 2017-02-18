package com.cmsc436.ms_diagnostic;

import android.content.ComponentName;
import android.content.Context;
import android.content.ServiceConnection;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;


public class Balancer_Test extends AppCompatActivity {

    BallView mBallView = null;
    Handler RedrawHandler = new Handler(); //so redraw occurs in main thread
    Timer mTmr = null;
    TimerTask mTsk = null;
    int mScrWidth, mScrHeight;
    android.graphics.PointF mBallPos, mBallSpd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE); //hide title bar
    //set app to full screen and keep screen on
        getWindow().setFlags(0xFFFFFFFF,
                WindowManager.LayoutParams.FLAG_FULLSCREEN| WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_balancer_test);
//create pointer to main screen
        final FrameLayout mainView =
                (android.widget.FrameLayout)findViewById(R.id.balance_view);

//        Display display = getWindowManager().getDefaultDisplay();
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        mScrWidth = displaymetrics.widthPixels;
        mScrHeight = displaymetrics.heightPixels;
        mBallPos = new android.graphics.PointF();
        mBallSpd = new android.graphics.PointF();

//create variables for ball position and speed
        mBallPos.x = mScrWidth/2;
        mBallPos.y = mScrHeight/2;
        mBallSpd.x = 0;
        mBallSpd.y = 0;

        //create initial ball
        mBallView = new BallView(this, mBallPos.x, mBallPos.y, mScrHeight/30, 1.5f,1.5f);

        mainView.addView(mBallView); //add ball to main screen
        mBallView.invalidate(); //call onDraw in BallView

        ((SensorManager)getSystemService(Context.SENSOR_SERVICE)).registerListener(
                new SensorEventListener() {
                    @Override
                    public void onSensorChanged(SensorEvent event) {
                        //set ball speed based on phone tilt (ignore Z axis)
                        mBallSpd.x = -event.values[0];
                        mBallSpd.y = event.values[1];
                        //timer event will redraw ball
                    }
                    @Override
                    public void onAccuracyChanged(Sensor sensor, int accuracy) {} //ignore
                },
                ((SensorManager)getSystemService(Context.SENSOR_SERVICE))
                        .getSensorList(Sensor.TYPE_ACCELEROMETER).get(0),
                SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void onPause() //app moved to background, stop background threads
    {
        mTmr.cancel(); //kill\release timer (our only background thread)
        mTmr = null;
        mTsk = null;
        super.onPause();
    }

    @Override
    public void onResume() //app moved to foreground (also occurs at app startup)
    {
        //create timer to move ball to new position
        mTmr = new Timer();
        mTsk = new TimerTask() {
            public void run() {
                //if debugging with external device,

                //move ball based on current speed
                mBallPos.x += mBallSpd.x;
                mBallPos.y += mBallSpd.y;
                //if ball goes off screen, reposition to opposite side of screen
                if (mBallPos.x > mScrWidth) mBallPos.x=0;
                if (mBallPos.y > mScrHeight) mBallPos.y=0;
                if (mBallPos.x < 0) mBallPos.x=mScrWidth;
                if (mBallPos.y < 0) mBallPos.y=mScrHeight;
                //update ball class instance
                mBallView.mX = mBallPos.x;
                mBallView.mY = mBallPos.y;
                //redraw ball. Must run in background thread to prevent thread lock.
                RedrawHandler.post(new Runnable() {
                    public void run() {
                        mBallView.invalidate();
                    }});
            }}; // TimerTask

        mTmr.schedule(mTsk,10,10); //start timer
        super.onResume();
    } // onResume


    @Override
    public void onDestroy() //main thread stopped
    {
        super.onDestroy();
        System.runFinalizersOnExit(true); //wait for threads to exit before clearing app
    }


    public class BallView extends View {


        public float mX;
        public float mY;
        private final int mR;
        private final Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        float xScale;
        float yScale;

        //construct new ball object
        public BallView(Context context, float x, float y, int r , float xS, float yS) {
            super(context);
            //color hex is [transparency][red][green][blue]
            mPaint.setColor(0xFF00FF00); //not transparent. color is green
            this.mX = x;
            this.mY = y;
            this.mR = r; //radius

            xScale = xS;
            yScale = yS;
        }

        //called by invalidate()
        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            canvas.drawCircle(mX * xScale, mY * yScale, mR, mPaint);
        }
    }
}
