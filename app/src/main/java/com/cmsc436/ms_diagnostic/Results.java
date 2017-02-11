package com.cmsc436.ms_diagnostic;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.widget.TextView;

public class Results extends Activity {
    TextView leftHand;
    TextView rightHand;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_results);

        //Gets data from the Intent that started the activity
        // and sets it to the left hand and right hand
        leftHand = (TextView) findViewById(R.id.tap_count_left);
        leftHand.setText("LEFT: "+ getIntent().getLongExtra(Trace.STATE_LH,-99999999));

        rightHand = (TextView) findViewById(R.id.tap_count_right);
        rightHand.setText("RIGHT: "+getIntent().getLongExtra(Trace.STATE_RH,-9999999));


    }
}
