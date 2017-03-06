package com.cmsc436.ms_diagnostic;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import static android.os.SystemClock.uptimeMillis;

/**
 * Created by test on 3/4/2017.
 */

public class BubbleView extends View{
    private static final int NUM_OF_TRIALS = 10;

    Paint paint;
    boolean started = false;
    long startTime, prevTime, totalTime;

    static int r = 0, g = 0, b = 0;
    static int x, y;
    /*int x = this.getWidth()/2;
    int y = this.getHeight()/2;*/
    static int counter = 0;

    public BubbleView(Context context) {
        super(context);
        init();
    }

    public BubbleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BubbleView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init(){
        paint = new Paint();
        paint.setColor(Color.BLUE);
        x = this.getWidth()/2;
        y = this.getHeight()/2;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (counter == 0) {
            x = this.getWidth()/2;
            y = this.getHeight()/2;
        }
        //paint.setColor(Color.RED);
        paint.setARGB(255, r, g, b);
        //canvas.drawCircle(x, y, radius, paint);
        canvas.drawCircle(x, y, canvas.getWidth()/7, paint);
        randColor();
        moveBubble();
        //invalidate();
        //canvas.drawCircle(this.getWidth()/2, this.getHeight()/2, this.getWidth()/7, paint);
    }

    public boolean onTouchEvent(MotionEvent event) {
        if (started && (counter < NUM_OF_TRIALS) && (event.getAction() == MotionEvent.ACTION_DOWN)) {

            long currTime = event.getEventTime();
            long deltaTime = currTime - prevTime;
            totalTime += deltaTime;

            System.out.println(deltaTime);
            setVisibility(View.INVISIBLE);
            counter++;
            postDelayed(new Runnable() {
                @Override
                public void run() {
                    setVisibility(View.VISIBLE);
                    prevTime = uptimeMillis();
                }
            }, 1000);
        }

        return super.onTouchEvent(event);
    }

    public void randColor() {
        r = (int) (Math.random() * 255);
        g = (int) (Math.random() * 255);
        b = (int) (Math.random() * 255);
    }

    public void moveBubble() {
        x = (int) (Math.random() * 6 * getWidth() / 7);
        y = (int) (Math.random() * (getHeight() - getWidth() / 7));
    }

    public void startGame() {
        started = true;
        startTime = uptimeMillis();
        prevTime = startTime;
    }

    public long getAverageTime() {
        return totalTime / NUM_OF_TRIALS;
    }

}

