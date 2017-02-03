package com.cmsc436.ms_diagnostic;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class Results extends AppCompatActivity {
    TextView leftHand;
    TextView rightHand;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        leftHand = (TextView) findViewById(R.id.tap_count_left);
        leftHand.setText(getIntent().getIntExtra("LEFT_HAND",0));

        rightHand = (TextView) findViewById(R.id.tap_count_right);
        rightHand.setText(getIntent().getIntExtra("RIGHT_HAND",0));


    }
}
