package com.cmsc436.ms_diagnostic;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created By Lauren L. and Shubham P.
 * This Class implement a Drawable View to be used for the Tracing Activity
 */

public class DrawView extends View {
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

    //brush size
    private float currentBrushSize, lastBrushSize;

    public DrawView(Context context) {
        super(context);
        drawPaint = new Paint();
        drawPaint.setColor(paintColor);
        drawPaint.setAntiAlias(true);
        drawPaint.setStrokeWidth(20);
        drawPaint.setStyle(Paint.Style.STROKE);
        drawPaint.setStrokeJoin(Paint.Join.ROUND);
        drawPaint.setStrokeCap(Paint.Cap.ROUND);
        canvasPaint = new Paint(Paint.DITHER_FLAG);
    }

    @Override
    public boolean onTouchEvent(MotionEvent motionEvent){
        float X = getX();
        float Y = getY();

        Path drawPath = new Path();

        switch (motionEvent.getAction()){
            case MotionEvent.ACTION_DOWN:
                drawPath.moveTo(X,Y);
                break;
            case MotionEvent.ACTION_MOVE:
                drawPath.moveTo(X,Y);
                break;
            case MotionEvent.ACTION_UP:
                drawPath.moveTo(X,Y);
                break;
            default: return false;
        }
        invalidate();
        return true;

    }
}
