package com.cmsc436.ms_diagnostic.balloon_test;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.cmsc436.ms_diagnostic.R;

import java.util.ArrayList;

public class BubbleActivity extends Activity{
    // Debug Log Codes
    final String ACTIVITY = "ACTIVITY_LOCATION";
    final static String SCORE = "SCORE";
    final static String DATA = "DATA";

    // ~~ Listener for Bubble View
    BubbleView.OnBubbleUpdateListener bubbleUpdateListener;

    int counter = 0;
    TextView timeView;

    ArrayList<Object> data;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bubble);

        timeView = (TextView) findViewById(R.id.timeView);
        final Button startButton = (Button) findViewById(R.id.start_bubble_button);
        final FrameLayout frameLayout = (FrameLayout) findViewById(R.id.bubble_frame);
        final BubbleView bubble = new BubbleView(frameLayout.getContext());

        // Setting the listener
        bubble.setOnBubbleUpdateListner(new BubbleView.OnBubbleUpdateListener() {

            // When the bubble is pressed, timeView is updated to the new counter
            @Override
            public void onBubbleUpdate() {
                String counterText = getString(R.string.balloon_counter, bubble.getCurrentCounter());
                timeView.setText(counterText);
            }

            // For handling where the intent is coming from
            @Override
            public void onDone() {
                startButton.setEnabled(true);
                startButton.setText("Done");
                startButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // I know this is bad coding but iam too lazy atm...
//                        data.add("N/A");
                        data.add(bubble.getAverageTime());
                        Intent intent = new Intent();
                        intent.putExtra(SCORE,bubble.getAverageTime());
                        intent.putExtra(DATA,data);
                        setResult(RESULT_OK,intent);
                        finish();
                    }
                });
            }
        });

        data = new ArrayList<>();

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //final FrameLayout frameLayout = (FrameLayout) findViewById(R.id.bubble_frame);
                //final BubbleView bubble1 = new BubbleView(frameLayout.getContext());
                frameLayout.addView(bubble);
                bubble.startGame();
                startButton.setEnabled(false);
            }
        });

        Log.d(ACTIVITY, "onCreate");
    }




}
