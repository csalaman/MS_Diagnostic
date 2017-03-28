package com.cmsc436.ms_diagnostic.level_test;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.cmsc436.ms_diagnostic.R;
import com.cmsc436.ms_diagnostic.sprial_test.Trace;

import java.util.Timer;
import java.util.TimerTask;


public class Balancer_Test extends AppCompatActivity {

    final String FROM_LAST_HAND_CODE = "NEXT_HAND";
    final String FROM_LAST_HAND_SCORE = "LAST_SCORE";



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

    double score;

    float X_SCALAR;
    float Y_SCALAR;

    //Point F is a good way to store x and y coordinats
    android.graphics.PointF mBallPos, mBallSpd,mCentPos;

    Button start_button;
    TextView timer_view;
    TextView score_text;
    TextView hand_text;

    DrawPath drawPath;

    int trailNum;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        requestWindowFeature(Window.FEATURE_NO_TITLE); //hide title bar
        //set app to full screen and keep screen on
        getWindow().setFlags(0xFFFFFFFF,
                WindowManager.LayoutParams.FLAG_FULLSCREEN |
                        WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_balancer);

        // Obtain Permissions
        getPermission();

        // Initializes all of the components of the app
        start_button = (Button) findViewById(R.id.balance_bottom_button);
        timer_view = (TextView) findViewById(R.id.balance_time_text);
        score_text = (TextView) findViewById(R.id.balance_text_score);
        hand_text = (TextView) findViewById(R.id.balance_text_hand);

        start_button.setText("Start Trail: "+
                getIntent().getIntExtra(BalancerInstr.TRIAL,-1)+
                " for Hand: "+
                getIntent().getStringExtra(BalancerInstr.HAND));

        // to obtains all metrics like size and width
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);

        // gets width as height of the ball
        mScrWidth = displaymetrics.widthPixels;
        mScrHeight = (int) (displaymetrics.heightPixels * .70);

        // this scalar determines the seed at which the balls moves
        X_SCALAR = mScrHeight/700f;
        Y_SCALAR = mScrWidth/300f;

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


        //create pointer to main screen
        FrameLayout mainView = (android.widget.FrameLayout)findViewById(R.id.balance_view);

        circles = (Circles) findViewById(R.id.balance_circles);

        //create initial ball
        mBallView = new BallView(this, mBallPos.x, mBallPos.y, mScrWidth/30);
        mBallView.setLayoutParams(new FrameLayout.LayoutParams(mScrWidth, mScrHeight));
        // draw view created
        drawPath = new DrawPath(this);
        drawPath.setLayoutParams(new FrameLayout.LayoutParams(mScrWidth, mScrHeight));
        //sets the views on the layout
        mainView.addView(mBallView,1);
        mainView.addView(drawPath,2);
        mBallView.invalidate(); //call onDraw in BallView

        //initializes score
        score = 0.0;
        trailNum = 1;

        startSensorHandler();

        start_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                start_button.setEnabled(false);
                startCoordinateUpdate();
                recordTimer.start();
                //TODO RECORD TIME
            }
        });
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

    // goes and gets permission for for saving pictures
    private void getPermission(){
        if (ContextCompat.checkSelfPermission(Balancer_Test.this,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(Balancer_Test.this,
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(Balancer_Test.this,
                        new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        1);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }

        }
    }


    private void startCoordinateUpdate(){
        mTmr = new Timer();

        mBallView.mX = mCentPos.x;
        mBallView.mY = mCentPos.y;
        mBallSpd.x = 0;
        mBallSpd.y = 0;

        final Runnable runnable = new Runnable() {
            public void run() {
                mBallView.invalidate();
                drawPath.onCoordUpdate(mBallView.mX,mBallView.mY);
            }};

        mTsk = new TimerTask() {
//            @RequiresApi(api = Build.VERSION_CODES.N)

            public void run() {
                //move ball based on current speed
                mBallPos.x += mBallSpd.x * X_SCALAR;
                mBallPos.y += mBallSpd.y * Y_SCALAR;

                //if ball goes off screen, reposition to opposite side of screen
                if (mBallPos.x > mScrWidth) {
                    mBallPos.x = mScrWidth;
                }
                if (mBallPos.y > mScrHeight -  (mScrHeight * .1f)) {
                    mBallPos.y = mScrHeight - (mScrHeight * .1f);
                }
                if (mBallPos.x < 0) {
                    mBallPos.x = 0;
                }
                if (mBallPos.y < 0) {
                    mBallPos.y = 0;
                }
                //update ball class instance
                mBallView.mX = mBallPos.x;
                mBallView.mY = mBallPos.y;
                score += getScore(mBallPos.x,mBallPos.y);

                //redraw ball. Must run in background thread to prevent thread lock.
                RedrawHandler.post(runnable);
            }}; // TimerTask

        mTmr.schedule(mTsk, 10, 10);

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



    // Takes care of timerss and what to at the end of the test
    final CountDownTimer recordTimer = new CountDownTimer(10000,1000) {
        @Override
        public void onTick(long millisUntilFinished) {
            timer_view.setText("Time: " + (int) millisUntilFinished / 1000 + " sec");
        }

        @Override
        public void onFinish() {
            timer_view.setText("Score: "+ score);

            // Saving the image
            FrameLayout frameLayout = (FrameLayout) findViewById(R.id.balance_view);
            frameLayout.setDrawingCacheEnabled(true);
            frameLayout.buildDrawingCache();
            Bitmap bm = finalBitmap(frameLayout.getDrawingCache());
            MediaStore.Images.Media.insertImage(getContentResolver(), bm, "" , "");

            mBallView.setVisibility(View.GONE);
            drawPath.setVisibility(View.GONE);

            start_button.setText("Back to Main Screen");
            start_button.setEnabled(true);
            start_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.putExtra(BalancerInstr.GET_SCORE,score);
//                    intent.putExtra(BalancerInstr.TRIAL,getIntent().getIntExtra(BalancerInstr.TRIAL,0));
                    setResult(RESULT_OK,intent);
                    finish();
                }
            });

        }
    };

    private Bitmap finalBitmap(Bitmap b1){
        Bitmap overlay = Bitmap.createBitmap(b1.getWidth(),b1.getHeight(), b1.getConfig());
        Canvas canvas = new Canvas(overlay);
        canvas.drawColor(Color.WHITE);
        canvas.drawBitmap(b1,0,0,null);

        return overlay;
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


    public class DrawPath extends View {
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

        public void clearDrawing(){
            init();
            drawCanvas.drawColor(0, PorterDuff.Mode.CLEAR);
        }

    }
}
