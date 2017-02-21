package com.cmsc436.ms_diagnostic;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.icu.text.DecimalFormat;
import android.icu.text.NumberFormat;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;


public class Balancer extends Activity {

    int INNER_CIRCLE = 100;
    int MID_CIRCLE = 66;
    int OUT_CIRCLE = 33;
    int NEG_CIR = -15;

    BallView mBallView = null;
    Circles circles = null;
    Handler RedrawHandler = new Handler(); //so redraw occurs in main thread
    Timer mTmr = null;
    TimerTask mTsk = null;
    int mScrWidth, mScrHeight;
    android.graphics.PointF mBallPos, mBallSpd,mCentPos;

    double score;

    float X_SCALAR;
    float Y_SCALAR;

    Button start_button;
    TextView timer_view;
    TextView score_text;
    TextView hand_text;

    boolean isDone = false;
    NumberFormat format;

    double left_score;
    double right_score;

    DrawPath drawPath;
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE); //hide title bar
        //set app to full screen and keep screen on
        getWindow().setFlags(0xFFFFFFFF,
                WindowManager.LayoutParams.FLAG_FULLSCREEN| WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_balancer);

        start_button = (Button) findViewById(R.id.balance_bottom_button);
        timer_view = (TextView) findViewById(R.id.balance_time_text);
        score_text = (TextView) findViewById(R.id.balance_text_score);
        hand_text = (TextView) findViewById(R.id.balance_text_hand);

        hand_text.append(" " + getString(R.string.LEFT));
        //create pointer to main screen
        final FrameLayout mainView =
                (android.widget.FrameLayout)findViewById(R.id.balance_view);


        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        mScrWidth = displaymetrics.widthPixels;
        mScrHeight = (int) (displaymetrics.heightPixels * .70);

        X_SCALAR = mScrHeight/700f;
        Y_SCALAR = mScrWidth/300f;

        System.out.println(">>>>>>>>>>> "+mScrHeight);
        System.out.println("------------------- "+ mScrWidth);
        mBallPos = new android.graphics.PointF();
        mBallSpd = new android.graphics.PointF();
        mCentPos = new android.graphics.PointF();

        //create variables for ball position and speed
        mBallPos.x = mScrWidth/2;
        mBallPos.y = mScrHeight/2;
        mCentPos.x = mScrWidth/2;
        mCentPos.y = mScrHeight/2;
        mBallSpd.x = 0;
        mBallSpd.y = 0;

        circles = (Circles) findViewById(R.id.balance_circles);
        //create initial ball
        mBallView = new BallView(this, mBallPos.x, mBallPos.y, mScrWidth/30);
        mBallView.setLayoutParams(new FrameLayout.LayoutParams(mScrWidth, mScrHeight));
        drawPath = new DrawPath(this);
        drawPath.setLayoutParams(new FrameLayout.LayoutParams(mScrWidth, mScrHeight));
//        mainView.addView(mBallView); //add ball to main screen
        mainView.addView(mBallView,1);
        mainView.addView(drawPath,2);
//        mainView.addView(circles);
        mBallView.invalidate(); //call onDraw in BallView

        score = 0.0;
        format = new DecimalFormat("#0.000");
        // Starting the sensor handler
        startSensorHandler();
        start_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                start_button.setText(R.string.next_hand);
                startCoordUpdate(true);
                start_button.setEnabled(false);
                recordTimer.start();
            }
        });
    }

    @Override
    public void onPause(){ //app moved to background, stop background threads

        startCoordUpdate(false);
        super.onPause();
    }

    @Override
    public void onResume(){ //app moved to foreground (also occurs at app startup)

        super.onResume();
    } // onResume


    @Override
    public void onDestroy(){ //main thread stopped
        super.onDestroy();
        System.runFinalizersOnExit(true); //wait for threads to exit before clearing app
    }

    private void startSensorHandler(){
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

    private void startCoordUpdate(boolean start){
        mTmr = new Timer();
        mTsk = new TimerTask() {
            @RequiresApi(api = Build.VERSION_CODES.N)
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
                score += getScore(mBallPos.x,mBallPos.y);

//                TODO: ONLY THE ORIGNAL THREAD THAT CREATED A VIEW HIERARCHY CAN TOUCH ITS VIEWS
//                score_text.append(""+format.format(score));

                //redraw ball. Must run in background thread to prevent thread lock.
                RedrawHandler.post(new Runnable() {
                    public void run() {
                        mBallView.invalidate();
                        drawPath.onCoordUpdate(mBallView.mX,mBallView.mY);
                    }});
            }}; // TimerTask

        mBallView.mX = mCentPos.x;
        mBallView.mY = mCentPos.y;
        if(start){
            //create timer to move ball to new position
//            mTmr = new Timer();
            mTmr.schedule(mTsk,10,10); //start timer
        }

        else{
            mTmr.cancel(); //kill\release timer (our only background thread)
            mTmr = null;
            mTsk = null;
        }
    }
    private double getScore(float x, float y){
        double dist = Math.sqrt(Math.pow(mCentPos.x - x,2) + Math.pow(mCentPos.y - y, 2));

        if(dist < circles.getSmallRadius()){
            return (.01 * INNER_CIRCLE);
        }else if(dist < circles.getMidiumRadius()){
            return .01 * MID_CIRCLE;
        }else if(dist < circles.getLargeRadius()){
            return .01 * OUT_CIRCLE;
        }else{
            return .01 * NEG_CIR;
        }
    }
    final CountDownTimer recordTimer = new CountDownTimer(10000, 1000) {

        @Override
        public void onTick(long millisUntilFinished) {
            timer_view.setText("Timer: " + (int) millisUntilFinished / 1000 + " sec");
        }

        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        public void onFinish() {
            timer_view.setText("");
            start_button.setEnabled(true);

            if(isDone){
                score_text.setText("Score: "+ format.format(score));
                right_score = score;
                final Intent intent = new Intent(Balancer.this,Results.class);
                intent.putExtra(getString(R.string.RIGHT),""+format.format(right_score));
                intent.putExtra(getString(R.string.LEFT),""+format.format(left_score));
                start_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(intent);
                    }
                });
            }else {
                score_text.setText("Score: "+ format.format(score));
                hand_text.setText(getString(R.string.hand)+getString(R.string.RIGHT));
                left_score = score;
                score = 0.0;
                startCoordUpdate(false);
                mBallView.mX = mScrWidth/2;
                mBallView.mY = mScrHeight/2;
                isDone = true;

                start_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        score_text.setText("");
                        recordTimer.start();
                        startCoordUpdate(true);
                    }
                });
            }

        }
    };



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

    public class DrawPath extends View{
        //drawing path
        private Path drawPath;

        //defines what to draw
        private Paint canvasPaint ;

        //defines how to draw
        private Paint drawPaint;

        //initial color
        private int paintColor = 0xFF660000;

        //canvas - holding pen, holds your drawings and transfers them to the view
        private Canvas drawCanvas;

        //canvas bitmap
        private Bitmap canvasBitmap;




        public DrawPath(Context context) {
            super(context);
            init();
        }

        public DrawPath(Context context, AttributeSet attrs) {
            super(context, attrs);
            init();
        }

        public DrawPath(Context context, AttributeSet attrs, int defStyleAttr) {
            super(context, attrs, defStyleAttr);
            init();
        }

        public DrawPath(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
            super(context, attrs, defStyleAttr, defStyleRes);
            init();
        }

        @Override
        protected void onSizeChanged(int w, int h, int oldw, int oldh) {
            super.onSizeChanged(w, h, oldw, oldh);
            canvasBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
            drawCanvas = new Canvas(canvasBitmap);
        }

        private void init(){
            drawPath = new Path();
            drawPaint = new Paint();
            drawPaint.setColor(paintColor);
            drawPaint.setAntiAlias(true);
            drawPaint.setStrokeWidth(20);
            drawPaint.setStyle(Paint.Style.STROKE);
            drawPaint.setStrokeJoin(Paint.Join.ROUND);
            drawPaint.setStrokeCap(Paint.Cap.ROUND);
            drawPaint.setAlpha(125);
            canvasPaint = new Paint(Paint.DITHER_FLAG);

            drawPath.moveTo(mCentPos.x,mCentPos.y);
        }

        @Override
        protected void onDraw(Canvas canvas) {
            canvas.drawBitmap(canvasBitmap, 0, 0, canvasPaint);
            canvas.drawPath(drawPath, drawPaint);
        }

        public void onCoordUpdate(float x, float y){
            drawPath.lineTo(x,y);
            invalidate();
        }
//        @Override
//        public boolean onGenericMotionEvent(MotionEvent event) {
//            drawPath.lineTo(event.getX(),event.getY());
//            invalidate();
//            return true;
//        }
    }
}
