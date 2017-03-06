package com.cmsc436.ms_diagnostic;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class BubbleActivity extends Activity {
    private static int num_sec = 0;

    int counter = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bubble);

        final TextView timeView = (TextView) findViewById(R.id.timeView);
        final Button startButton = (Button) findViewById(R.id.start_bubble_button);
        final BubbleView bubble1 = (BubbleView) findViewById(R.id.bubble1);

        final int total_sec = 0;

        /*final TimeCounter timer = new TimeCounter(30000) {
            public void onTick(int second) {
                timeView.setText(String.valueOf(second));
                num_sec = second;
            }
        };*/

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bubble1.startGame();
                startButton.setEnabled(false);
                //bubble.
                /*bubble1.setVisibility(View.INVISIBLE);
                bubble1.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        bubble1.setVisibility(View.VISIBLE);

                    }
                }, 3000);*/

                /*//Timer start
                timer.start();

                // the Bubble appears
                bubble1.setVisibility(View.VISIBLE);

                //when click, the bubble disappear, pause timer
                bubble1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        timer.cancel();
                        bubble1.setVisibility(View.INVISIBLE);
                        timeView.setText(String.valueOf(num_sec));
                    }
                });*/
            }
        });
    }
}
