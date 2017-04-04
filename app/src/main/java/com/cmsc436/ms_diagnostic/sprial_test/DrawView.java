package com.cmsc436.ms_diagnostic.sprial_test;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.CountDownTimer;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Created By Lauren L. and Shubham P.
 * This Class implement a Drawable View to be used for the Tracing Activity
 */

public class DrawView extends View {
    private static final int TIMEOUT = 5000;

    private static final int NUM_SPIRAL = 3;

    CountDownTimer timer;

    //drawing path
    private Path drawPath;

    private Path spiralPath;

    private Paint spiralPaint;

    Point point;

    ArrayList<PointF> spiralList;
    ArrayList<PointF> drawList;



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

    //brush size
    private float currentBrushSize, lastBrushSize;

    // various constructors for loading XML
    /*public DrawView(Context context) {
        super(context);
        drawPath = new Path();
        drawPaint = new Paint();
        drawPaint.setColor(paintColor);
        drawPaint.setAntiAlias(true);
        drawPaint.setStrokeWidth(20);
        drawPaint.setStyle(Paint.Style.STROKE);
        drawPaint.setStrokeJoin(Paint.Join.ROUND);
        drawPaint.setStrokeCap(Paint.Cap.ROUND);
        canvasPaint = new Paint(Paint.DITHER_FLAG);
    }      */

    public DrawView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public DrawView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public DrawView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init(){
        drawPath = new Path();
        spiralPath = new Path();
        point = new Point();

        spiralList = new ArrayList<>();
        drawList = new ArrayList<>();

//        drawPath.

        drawPaint = new Paint();
        drawPaint.setColor(paintColor);
        drawPaint.setAntiAlias(true);
        drawPaint.setStrokeWidth(20);
        drawPaint.setStyle(Paint.Style.STROKE);
        drawPaint.setStrokeJoin(Paint.Join.ROUND);
        drawPaint.setStrokeCap(Paint.Cap.ROUND);

        spiralPaint = new Paint();
        spiralPaint.setColor(Color.RED);
        spiralPaint.setAntiAlias(true);
        spiralPaint.setStrokeWidth(10);
        spiralPaint.setStyle(Paint.Style.STROKE);
        spiralPaint.setStrokeJoin(Paint.Join.ROUND);
        spiralPaint.setStrokeCap(Paint.Cap.ROUND);

        canvasPaint = new Paint(Paint.DITHER_FLAG);

    }

    private void drawSpiral(){
        int hight = getHeight();
        int width = getWidth();
        PointF center = new PointF(this.getWidth()/2f, this.getHeight()/2f);
        int totalAngle = NUM_SPIRAL * 360;
        spiralPath.moveTo(center.x,center.y);
        for(int i = 0; i < totalAngle; i++){
            float angle = i*.1f;
//            PointF p = new PointF(  (float) ((100+angle)*Math.cos(degToRad(angle))), //x
//                                    (float) ((100+angle)*Math.sin(degToRad(angle))));//y

            PointF p = new PointF(  (float) ((angle)*Math.cos(angle)*30), //x
                                    (float) ((angle)*Math.sin(angle)*30));//y


            p.offset(center.x,center.y);
            spiralList.add(p);
            if(p.x > width || p.y > hight ) break;
            spiralPath.lineTo(p.x,p.y);

        }

        drawCanvas.drawPath(spiralPath,spiralPaint);

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        setMeasuredDimension(w,h);
        canvasBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        drawCanvas = new Canvas(canvasBitmap);
        drawSpiral();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawBitmap(canvasBitmap, 0, 0, canvasPaint);
        canvas.drawPath(drawPath, drawPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        float X = event.getX();
        float Y = event.getY();

        //Path drawPath = new Path();

        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                if(timer != null){
                    timer.cancel();
                }

                drawPath.moveTo(X,Y);
                drawList.add(new PointF(X,Y));
                break;
            case MotionEvent.ACTION_MOVE:
                drawPath.lineTo(X, Y);
                drawList.add(new PointF(X,Y));
                break;
            case MotionEvent.ACTION_UP:
                drawCanvas.drawPath(drawPath, drawPaint);

                //drawPath.lineTo(X, Y); //not sure if needed
                drawPath.reset(); //not sure if needed

                startTimeoutClock();
                break;

            default:
                return false;
        }

        invalidate();
        return true;

    }

    private void startTimeoutClock() {
        timer = new CountDownTimer(TIMEOUT, 1000) {
            public void onTick(long millisUntilFinished) {

            }

            public void onFinish() {

            }
        }.start();
    }

    public void clearDrawing(){
        drawList = new ArrayList<>();
        spiralList = new ArrayList<>();
        drawCanvas.drawColor(0, PorterDuff.Mode.CLEAR);
        drawSpiral();
    }

    public List<PointF> getSpiralCoordinats(){
        return spiralList;
    }

    public List<PointF> getDrawnCoordinates(){
        return drawList;
    }

    public PointF getCenter(){
        return new PointF(getWidth()/2f, getHeight()/2f);
    }


}
