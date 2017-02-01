package com.cmsc436.ms_diagnostic;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

/*
* This is the Activity for the Tapping Test
* */
public class TapActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tap);





        final Button button = (Button) findViewById(R.id.button_tap);
        button.setOnClickListener(new View.OnClickListener() {
            int count_taps = 0;
            public void onClick(View v) {
                // Perform action on click
                count_taps++;
                Toast.makeText(getApplicationContext(),count_taps,Toast.LENGTH_SHORT ).show();
            }
        });


    }
}
