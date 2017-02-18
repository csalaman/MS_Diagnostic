package com.cmsc436.ms_diagnostic;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import java.util.Timer;
import java.util.TimerTask;


public class Balancer extends AppCompatActivity {

    BallView mBallView = null;
    Handler RedrawHandler = new Handler(); //so redraw occurs in main thread
    Timer mTmr = null;
    TimerTask mTsk = null;
    int mScrWidth, mScrHeight;
    android.graphics.PointF mBallPos, mBallSpd;

    float X_SCALAR;
    float Y_SCALAR;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE); //hide title bar
        //set app to full screen and keep screen on
        getWindow().setFlags(0xFFFFFFFF,
                WindowManager.LayoutParams.FLAG_FULLSCREEN| WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_balancer);
        //create pointer to main screen
        final FrameLayout mainView =
                (android.widget.FrameLayout)findViewById(R.id.balance_view);

        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        mScrWidth = displaymetrics.widthPixels;
        mScrHeight = displaymetrics.heightPixels;

        X_SCALAR = mScrHeight/500f;
        Y_SCALAR = mScrWidth/200f;

        System.out.println(">>>>>>>>>>> "+mScrHeight);
        System.out.println("------------------- "+ mScrWidth);
        mBallPos = new android.graphics.PointF();
        mBallSpd = new android.graphics.PointF();

        //create variables for ball position and speed
        mBallPos.x = mScrWidth/2;
        mBallPos.y = mScrHeight/2;
        mBallSpd.x = 0;
        mBallSpd.y = 0;

        //create initial ball
        mBallView = new BallView(this, mBallPos.x, mBallPos.y, mScrWidth/30);

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
    public void onPause(){ //app moved to background, stop background threads

        mTmr.cancel(); //kill\release timer (our only background thread)
        mTmr = null;
        mTsk = null;
        super.onPause();
    }

    @Override
    public void onResume(){ //app moved to foreground (also occurs at app startup)

        //create timer to move ball to new position
        mTmr = new Timer();
        mTsk = new TimerTask() {
            public void run() {
                //move ball based on current speed
                mBallPos.x += mBallSpd.x * X_SCALAR;
                mBallPos.y += mBallSpd.y * Y_SCALAR;
                //if ball goes off screen, reposition to opposite side of screen
                if (mBallPos.x > mScrWidth) mBallPos.x=mScrWidth;
                if (mBallPos.y > mScrHeight -  (mScrHeight * .1f)) mBallPos.y=mScrHeight - (mScrHeight * .1f);
                if (mBallPos.x < 0) mBallPos.x=0;
                if (mBallPos.y < 0) mBallPos.y=0;
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
    public void onDestroy(){ //main thread stopped
        super.onDestroy();
        System.runFinalizersOnExit(true); //wait for threads to exit before clearing app
    }


    public class BallView extends View {

        public float mX;
        public float mY;
        private final int mR;
        private final Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);


        //construct new ball object
        public BallView(Context context, float x, float y, int r) {
            super(context);

            mPaint.setColor(Color.RED); // Color setting
            this.mX = x;
            this.mY = y;
            this.mR = r; //radius

        }

        //called by invalidate()
        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            canvas.drawCircle(mX , mY, mR, mPaint);
        }
    }
}
