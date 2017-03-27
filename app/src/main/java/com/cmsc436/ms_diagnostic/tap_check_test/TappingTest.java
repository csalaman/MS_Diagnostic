package com.cmsc436.ms_diagnostic.tap_check_test;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.cmsc436.ms_diagnostic.R;

import java.util.ArrayList;
import java.util.List;

public class TappingTest extends Activity {

    public final static String DATA_LIST = "TAPP_DATA_LIST";

    int testCount = 0;
    double tapCountAverage = 0;
    int count_taps = 0; // ~~ making this var now only count one single test
    int totalCount = 0; // ~~ this count will count the total taps and will be added to intent extra
    boolean onCount;

    // ~~ this is the list of data will be sent to the Google Docs
    // ~~ additionally I am using ArrayList because its Serializable and makes my life easy
    ArrayList<Object> data;

    TextView display_text;
    CountDownTimer downTimer;
    Button tap_imgButton;
    Button start_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tapping_test_activity);
        display_text = (TextView) findViewById(R.id.display_text);
        tap_imgButton = (Button) findViewById(R.id.tap_imgbutton);
        start_button = (Button) findViewById(R.id.start_test_button);

        data = new ArrayList<>();

        display_text.setText("Press \"Start\" to start Test "+ (testCount+1));

        downTimer = new CountDownTimer(10000,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                display_text.setText("Time: " + millisUntilFinished/1000);
            }

            @Override
            public void onFinish() {

                display_text.setText("Press \"Start\" to start Test "+ (testCount+1));
                start_button.setVisibility(View.VISIBLE);

                // ~~ updating the data list
                data.add(count_taps);

                // ~~ update totalCount and reset count_taps
                totalCount += count_taps;
                count_taps = 0;

                if(testCount == 3){
                    // To-Do: Return results to previous activity, and get average of 3 test
                    // the data we will like to send is the tapCountAverage = count_tap / 3;
                    Intent result = new Intent();
                    result.putExtra("data",totalCount); //instead of count_taps being sent, now total is
                    result.putExtra(TappingTest.DATA_LIST,data);

                    setResult(Activity.RESULT_OK,result);
                    finish();
                }
            }
        };


    }


    public void startTest(View v){
        downTimer.start();

        start_button.setVisibility(View.INVISIBLE);
        tap_imgButton.setOnTouchListener(new View.OnTouchListener() {


            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        // PRESSED

                            count_taps++;

                        tap_imgButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.button_to_tap_imt_white));
                        return true; // if you want to handle the touch event
                    case MotionEvent.ACTION_UP:
                        // RELEASED
                        tap_imgButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.button_to_tap_img_black));
                        return true; // if you want to handle the touch event
                }
                return false;
            }



        });
        testCount++;
    }


}
