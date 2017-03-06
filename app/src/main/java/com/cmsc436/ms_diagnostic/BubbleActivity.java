package com.cmsc436.ms_diagnostic;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class BubbleActivity extends Activity{
    // Debug Log Codes
    final String ACTIVITY = "ACTIVITY_LOCATION";

    // ~~ code for starting new activity Intent
    final String FROM_LAST_HAND_CODE = "NEXT_HAND";
    final String FROM_LAST_HAND_SCORE = "LAST_SCORE";

    // ~~ Listener for Bubble View
    BubbleView.OnBubbleUpdateListener bubbleUpdateListener;
    private static int num_sec = 0;
    int counter = 0;
    TextView timeView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bubble);

        timeView = (TextView) findViewById(R.id.timeView);
        final Button startButton = (Button) findViewById(R.id.start_bubble_button);
        final BubbleView bubble1 = (BubbleView) findViewById(R.id.bubble1);


        // ~~ Setting the listener
        bubble1.setOnBubbleUpdateListner(new BubbleView.OnBubbleUpdateListener() {
            // ~~ when the bubble is pressed, timeView is updated to the new counter
            @Override
            public void onBubbleUpdate() {
                timeView.setText("Counter: "+ bubble1.getCurrentCounter());
            }

            // ~~ for handling where the intent is coming from
            @Override
            public void onDone() {
                // ~~ if the intent is comming from left hand, we go to the result
                if(getIntent().getBooleanExtra(FROM_LAST_HAND_CODE,false)){

                    final Intent toResult = new Intent(BubbleActivity.this,Results.class);
                    toResult.putExtra(getString(R.string.LEFT), getIntent().getStringExtra(FROM_LAST_HAND_SCORE));
                    toResult.putExtra(getString(R.string.RIGHT), ""+bubble1.getAverageTime());

                    startButton.setEnabled(true);
                    startButton.setText("Results");
                    startButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            startActivity(toResult);
                        }
                    });
                }
                // ~~ handling if the activity is going to right hand
                //  P.S DO I EVEN NEED THIS IF STATEMENT?? OR SHOULD I USE JUST ELSE
                else if (!getIntent().getBooleanExtra(FROM_LAST_HAND_CODE,false)){
                    final Intent toRight = new Intent(BubbleActivity.this, BubbleActivity.class);
                    toRight.putExtra(FROM_LAST_HAND_CODE,true);
                    toRight.putExtra(FROM_LAST_HAND_SCORE,""+bubble1.getAverageTime());

                    startButton.setEnabled(true);
                    startButton.setText("Right Hand");
                    startButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            startActivity(toRight);
                        }
                    });
                }
            }
        });

        final int total_sec = 0;

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bubble1.startGame();
                startButton.setEnabled(false);
            }
        });

        Log.d(ACTIVITY, "onCreate");
    }




}
