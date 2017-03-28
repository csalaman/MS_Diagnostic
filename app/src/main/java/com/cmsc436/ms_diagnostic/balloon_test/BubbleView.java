package com.cmsc436.ms_diagnostic.balloon_test;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import java.util.Random;

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
    int counter = 1; //had to change it to 1, we were doing 11 trials
    int radius, width, height;

    Random random; // used to generate random times
    OnBubbleUpdateListener listner; // Similar to fragment communication, need to access the listener

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

    public void reset(){
        paint = new Paint();
        paint.setColor(Color.BLUE);
        random = new Random();
        counter =1;
        invalidate();
    }

    private void init(){
        paint = new Paint();
        paint.setColor(Color.BLUE);
        random = new Random();

        // ~~ null so that we can leave it to the
        // activity to update the view when implemented
        this.listner = null;
    }

    private void setDimensions(int height, int width) {
        this.height = height;
        this.width = width;
        x = this.width / 2;
        y = this.height / 2;
        radius = width / 7;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (counter == 1) {
            setDimensions(getHeight(), getWidth());
        }

        //paint.setColor(Color.RED);
        paint.setARGB(255, r, g, b);
        canvas.drawCircle(x, y, radius, paint);
        randColor();
        moveBubble();
    }

    public boolean onTouchEvent(MotionEvent event) {
        // checks if game has been started by startGame() being called
        if (started) {

            //checks that bubble has been popped less than max number trials
            if (counter < NUM_OF_TRIALS) {

                // checks that event is when user is touching down
               if (event.getAction() == MotionEvent.ACTION_DOWN) {

                   //current event time
                   long currTime = event.getEventTime();

                   // time elapsed since bubble appeared (user reaction time)
                   long deltaTime = currTime - prevTime;

                   //adding deltaTime to total time for total reaction time
                   totalTime += deltaTime;

                   // "popping bubble"
                   setVisibility(View.INVISIBLE);

                   //incrementing counter for total num of trial completed
                   counter++;

                   //when the bubble is pressed, do the listener's actions as well
                   if (listner != null) listner.onBubbleUpdate();

                   // ~~ randomizing time from 1000 ms to 2000 ms
                   //  since Atif had said the delay between two bubbles should be random

                   postDelayed(new Runnable() {
                       @Override
                       public void run() {

                           //making bubble appear after being popped
                           setVisibility(View.VISIBLE);

                           //setting prev time after bubble appears
                           prevTime = uptimeMillis();
                       }
                   }, getRandomTime(1000,2000));

               }
            } else {
                //after 10 trials completed, removing visibility from view
                setVisibility(View.INVISIBLE);

                //for testing purposes, feel free to remove
                Toast.makeText(getContext(), "Average time:" + getAverageTime(), Toast.LENGTH_LONG).show();
                System.out.println("Average time: " + getAverageTime());
                listner.onDone();
            }
        }
        return super.onTouchEvent(event);
    }

    // just making things colorful, but I think Dr. Memon might have specified
    // for bubble to be red. In that case this method can be removed
    public void randColor() {
        r = (int) (Math.random() * 255);
        g = (int) (Math.random() * 255);
        b = (int) (Math.random() * 255);
    }

    // generating random x and y coordinates to move bubble to
    public void moveBubble() {
        x = (int) (Math.random() * (width - (2 * radius))) + radius;
        y = (int) (Math.random() * (height - (2 * radius))) + radius;
    }

    //flag for starting game
    public void startGame() {
        started = true;
        startTime = uptimeMillis();
        prevTime = startTime;

        // ~~ Had to make counter = 0 every time the activity would start this
        //  because for some reason the counter's number was stored
        //  regardless of the activity being destroyed
        //counter = 1;
    }

    // ~~ getter for counter
    public int getCurrentCounter(){ return  counter; }

    // average time - to be called after bubble popped 10 (NUM_OF_TRIALS) times
    public long getAverageTime() {
        return totalTime / NUM_OF_TRIALS;
    }

    //generates random number between the given range
    private int getRandomTime(int min, int max){
        return random.nextInt((max - min) + 1) + min;
    }

    // ~~ interface for communicating with the parent activity, lets you update what every you want
    //  when the bubble has changed, Uses Observer Design Pattern
    public interface OnBubbleUpdateListener {
        void onBubbleUpdate();
        void onDone();
    }

    // ~~ Listener Setter
    public void setOnBubbleUpdateListner(OnBubbleUpdateListener l){
        listner = l;
    }

}

