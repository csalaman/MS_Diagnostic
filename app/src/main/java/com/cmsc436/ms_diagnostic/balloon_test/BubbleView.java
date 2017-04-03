package com.cmsc436.ms_diagnostic.balloon_test;

import android.content.Context;
import android.graphics.Canvas;
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

public class BubbleView extends View {
    private static final int NUM_OF_TRIALS = 10;

    Paint paint;
    long startTime, prevTime, totalTime;

    static int r = 0, g = 0, b = 0;
    static float x, y;
    float radius;
    int width, height, counter;

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

    protected void onSizeChanged(int width, int height, int oldw, int oldh) {
        this.width = width;
        this.height = height;
        x = this.width / 2;
        y = this.height / 2;
        super.onSizeChanged(width, height, oldw, oldh);
    }

    private void init(){
        radius = 140;
        paint = new Paint();
        //paint.setAntiAlias(true);
        random = new Random();
        setFocusable(true);

        // null so that we can leave it to the activity to update the view when implemented
        this.listner = null;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        randColor();
        moveBubble();
        paint.setARGB(255, r, g, b);
        canvas.drawCircle(x, y, radius, paint);
    }

    public boolean onTouchEvent(MotionEvent event) {
        // checks that event is when user is touching down
        if ((event.getAction() == MotionEvent.ACTION_DOWN)  ) {

            float delta_x = event.getX() - x;
            float delta_y = event.getY() - y;

            //checks that bubble has been popped less than max number trials
            if ((counter < NUM_OF_TRIALS) && (Math.hypot(delta_x, delta_y) <= radius)) {

                //current event time
                long currTime = event.getEventTime();
                System.out.println("currTime: "+ currTime + " prevTime: " + prevTime);
                // time elapsed since bubble appeared (user reaction time)
                long deltaTime = currTime - prevTime;

                //adding deltaTime to total time for total reaction time
                totalTime += deltaTime;

                // "popping bubble"
                setVisibility(View.INVISIBLE);

                //incrementing counter for total num of trial completed
                counter++;

                //when the bubble is pressed, do the listener's actions as well
                if (listner != null) {
                    listner.onBubbleUpdate();
                }

                if (counter == NUM_OF_TRIALS) {
                    //after 10 trials completed, removing visibility from view
                    setVisibility(View.INVISIBLE);

                    //for testing purposes, feel free to remove
                    Toast.makeText(getContext(), "Average time:" + getAverageTime(), Toast.LENGTH_LONG).show();
                    System.out.println("Average time: " + getAverageTime());

                    listner.onDone();
                } else {
                    final int randomTime = getRandomTime(1000, 2000);

                    // ~~ randomizing time from 1000 ms to 2000 ms
                    postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            //making bubble appear after being popped
                            setVisibility(View.VISIBLE);

                            //setting prev time after bubble appears
                            prevTime = uptimeMillis();
                        }
                    }, randomTime);
                }
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

    // Generating random x and y coordinates to move bubble to
    public void moveBubble() {
        x = (float) (Math.random() * (width - (2 * radius))) + radius;
        y = (float) (Math.random() * (height - (2 * radius))) + radius;
    }

    //f Fag for starting game
    public void startGame() {
        startTime = uptimeMillis();
        prevTime = startTime;
    }

    // Getter for counter
    public int getCurrentCounter() {
        return counter;
    }

    // average time - to be called after bubble popped 10 (NUM_OF_TRIALS) times
    public long getAverageTime() {
        return totalTime / NUM_OF_TRIALS;
    }

    //generates random number between the given range
    private int getRandomTime(int min, int max){
        return random.nextInt((max - min) + 1) + min;
    }

    // Interface for communicating with the parent activity, lets you update whatever you want
    // When the bubble has changed, Uses Observer Design Pattern
    public interface OnBubbleUpdateListener {
        void onBubbleUpdate();
        void onDone();
    }

    // Listener Setter
    public void setOnBubbleUpdateListner(OnBubbleUpdateListener l){
        listner = l;
    }

    private boolean inBounds(float evt_x, float evt_y) {
        return (Math.hypot(evt_x - x, evt_y - y) <= radius);
    }

    private boolean inBounds(MotionEvent event) {
        float delta_x = event.getX() - x;
        float delta_y = event.getY() - y;
        return (Math.hypot(delta_x, delta_y) <= radius);
    }


}

