package com.cmsc436.ms_diagnostic;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;
import java.util.Random;

/**
 * Created by test on 3/4/2017.
 */

public class Bubble extends View{
    int width;
    int height = this.getHeight();
    Paint paint;
    Path path;

    public Bubble(Context context) {
        super(context);
        init();
    }

    public Bubble(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public Bubble(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init(){
        paint = new Paint();
        paint.setColor(Color.BLUE);
//        paint.setStrokeWidth(10);
//        paint.setStyle(Paint.Style.STROKE);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        // TODO Auto-generated method stub
        super.onDraw(canvas);
        paint.setColor(Color.RED);
        //paint.setStyle(Paint.Style.STROKE);

        Random r1 = new Random(100);
        Random r2 = new Random(200);
        canvas.drawCircle(this.getWidth()/2, this.getHeight()/2, this.getWidth()/7, paint);



    }

//    protected void changeColor(Canvas canvas,Color color) {
//        paint = new Paint();
//        paint.setColor(Color.RED);
//
//        canvas.drawCircle(this.getWidth()/2, this.getHeight()/2, this.getWidth()/7, paint);
//    }

    public float getSmallRadius(){
        return getWidth()/7;
    }

    public float getMidiumRadius(){
        return (width * 2) / 7;
    }

    public float getLargeRadius(){
        return (width * 3) / 7;
    }
}

