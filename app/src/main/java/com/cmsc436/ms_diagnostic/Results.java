package com.cmsc436.ms_diagnostic;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

public class Results extends Activity {
    TextView leftHand;
    TextView rightHand;
    Button homeButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_results);

        String result = getIntent().getStringExtra(getString(R.string.LEFT));
        result = result.substring(0,Math.min(5,result.length()));
        //Gets data from the Intent that started the activity
        // and sets it to the left hand and right hand
        leftHand = (TextView) findViewById(R.id.tap_count_left);
        leftHand.setText(result);

        result = getIntent().getStringExtra(getString(R.string.RIGHT));
        result = result.substring(0,Math.min(5,result.length()));

        rightHand = (TextView) findViewById(R.id.tap_count_right);
        rightHand.setText(result);

        // this button will pop all of the back stack and return home
        homeButton = (Button) findViewById(R.id.result_home_button);
        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Results.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish(); // call this to finish the current activity
            }
        });


    }
}
