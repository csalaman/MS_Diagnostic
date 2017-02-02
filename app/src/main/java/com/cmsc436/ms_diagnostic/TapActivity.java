package com.cmsc436.ms_diagnostic;

import android.app.Activity;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

/*
* This is the Activity for the Tapping Test
* */
public class TapActivity extends Activity {

    int count = 0;
    TextView text;
    TextView instructions;

    CountDownTimer startTimer;
    CountDownTimer recordTimer;
    Boolean isDone = false;
    Button button ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_tap);
        button = (Button) findViewById(R.id.button_tap);
        button.setVisibility(View.INVISIBLE);

        instructions = (TextView) findViewById(R.id.text_instructions);
        startTimer = new CountDownTimer(5000,1000){


            @Override
            public void onTick(long millisUntilFinished) {
                instructions.setText("Start Tapping in:" + (int)(millisUntilFinished/1000));
            }

            @Override
            public void onFinish() {
                instructions.setText(R.string.tap_start_tapping);

            }}.start();

        text = (TextView) findViewById(R.id.tap_count);

        recordTimer = new CountDownTimer(10000,1000) {

            @Override
            public void onTick(long millisUntilFinished) {
                instructions.setText("Keep Tapping for: "+(int) millisUntilFinished/1000+" sec");
            }

            @Override
            public void onFinish() {
                instructions.setText("OK STOP!");
                if(isDone){
                    text.setText("Test Done");
                }else{
                    isDone = true;
                    text.setText("Time to Switch Hands!");
                    startTimer.start();
                    recordTimer.start();
                }
            }

        }.start();



        button.setVisibility(View.VISIBLE);
        button.setOnClickListener(new View.OnClickListener() {
            int count_taps = 0;

            public void onClick(View v) {
                // Perform action on click

                count_taps++;
                count = count_taps;
                text.setText("You have tapped " + count + " times!");
            }
        });






    }
}
