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

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bubble1.startGame();
                startButton.setEnabled(false);
            }
        });
    }
}
