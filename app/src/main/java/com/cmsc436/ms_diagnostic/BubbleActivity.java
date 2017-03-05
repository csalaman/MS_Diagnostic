package com.cmsc436.ms_diagnostic;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.graphics.Color;

public class BubbleActivity extends Activity {
    private static int num_sec = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bubble);

        final TextView timeView = (TextView) findViewById(R.id.timeView);
        final Button bubbleButton = (Button) findViewById(R.id.bubble_button);
        final Button startButton = (Button) findViewById(R.id.start_bubble_button);
        final Bubble bubble1 = (Bubble) findViewById(R.id.bubble1);
        final Bubble bubble2 = (Bubble) findViewById(R.id.bubble2);

        final int total_sec = 0;


        final TimeCounter timer = new TimeCounter(30000) {
            public void onTick(int second) {
                timeView.setText(String.valueOf(second));
                num_sec = second;
            }
        };
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Timer start
                timer.start();
                // the Bubble appears
                bubble1.setVisibility(View.VISIBLE);
                bubble2.setVisibility(View.INVISIBLE);
                bubbleButton.setVisibility(View.INVISIBLE);
                //when click, the bubble disappear, pause timer

                bubble1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        timer.cancel();
                        //timeView.setText(String.valueOf(getDuration()));
                        bubble1.setVisibility(View.INVISIBLE);
                        timeView.setText(String.valueOf(num_sec));

                        bubble2.setVisibility(View.VISIBLE);

                    }
                });
            }
        });
    }
}
