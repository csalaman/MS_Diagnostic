package com.cmsc436.ms_diagnostic.flex_test;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.cmsc436.ms_diagnostic.R;

import java.util.ArrayList;

public class FlexTest extends AppCompatActivity implements SensorEventListener{
    private final int NUM_TRIAL = 3;
    public static final String TO_FLEX = "TOFLEX";
    public static final String TO_REST = "TOREST";
    public static final String LIST_FLEX = "LISTFLEX";
    public static final String LIST_REST = "LISTREST";

    Button readyButton;
    TextView textView;
    TextView timeText;

    Vibrator vibrator;
    SensorManager sensorManager;
    Sensor accel;

    float x,y,z;
    float[] baseRest;
    float[] baseFlex;

    float[] margin;

    float[] currRest;

    boolean backToRest = false;
    boolean started = false;

    ArrayList<Float> acclOnPhone;
    ArrayList<Long> time;
    ArrayList<Long>toFlexList;
    ArrayList<Long>toRestList;


//    boolean toFlex = false;
//    boolean toRest = false;

//    long startTime;
//    long endTime;

    long timeToFlex;
    long timeToRest;

    int trialNum = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flex_test);

        textView = (TextView) findViewById(R.id.flex_test_text);
        timeText = (TextView) findViewById(R.id.flex_test_time_text);
        readyButton = (Button) findViewById(R.id.flex_test_button);

        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        accel = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(this,accel,SensorManager.SENSOR_DELAY_NORMAL);

        acclOnPhone = new ArrayList<>();
        time = new ArrayList<>();
        toFlexList = new ArrayList<>();
        toRestList = new ArrayList<>();

        baseRest = getIntent().getFloatArrayExtra(FlexCalibrate.REST);
//        baseFlex = getIntent().getFloatArrayExtra(FlexCalibrate.FLEX);

        readyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currRest = new float[3];
                margin = new float[3];

                currRest[0] = x;
                currRest[1] = y;
                currRest[2] = z;

                margin[0] = Math.abs(baseRest[0] - x)+1;
                margin[1] = Math.abs(baseRest[1] - y)+1;
                margin[2] = Math.abs(baseRest[2] - z)+1;
                readyButton.setEnabled(false);
                startTest.start();
            }
        });



    }

    CountDownTimer startTest = new CountDownTimer(3000,1000) {
        @Override
        public void onTick(long millisUntilFinished) {
            timeText.setText("Get Ready to Flex in: "+millisUntilFinished/1000);
        }

        @Override
        public void onFinish() {
            timeText.setText("FLEX!");
            vibrator.vibrate(100);
//            toFlex = true;
//            startTime = System.currentTimeMillis();
//            endTime = startTime;
            backToRest = false;
            started = true;
        }
    };

    @Override
    public void onSensorChanged(SensorEvent event) {
        x = event.values[0];
        y = event.values[1];
        z = event.values[2];

        if(started){

            acclOnPhone.add(z);
            time.add(System.currentTimeMillis());

            if(backToRest && inMargin(z)){
                started = false;
                calculateMetric();
                timeText.setText("TIME: "+timeToFlex+timeToRest+
                        "\nMargin: "+ margin[2]);
                trialNum++;
                vibrator.vibrate(200);

                if(trialNum > NUM_TRIAL){
                    readyButton.setText("Done");
                    readyButton.setEnabled(true);
                    readyButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent();
                            intent.putExtra(TO_FLEX,timeToFlex);
                            intent.putExtra(TO_REST,timeToRest);
                            intent.putExtra(LIST_FLEX,toFlexList);
//                            intent.putExtra(LIST_REST,toRestList);
                            setResult(RESULT_OK,intent);
                            finish();
                        }
                    });
                }

                else {
                    readyButton.setEnabled(true);
                    time = new ArrayList<>();
                    acclOnPhone = new ArrayList<>();
                }

            }

            if(!backToRest && !inMargin(z)) {
               backToRest = true;
            }
        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    private void calculateMetric(){
        int idx = -1;
        float min = Float.MAX_VALUE;
        for(int i = 0; i < acclOnPhone.size(); i++){
            if(acclOnPhone.get(i) < min){
                min = acclOnPhone.get(i);
            }
            idx = i;
        }
        long toF = time.get(idx) - time.get(0);
        long toR = time.get(time.size()-1) - time.get(idx);
        toFlexList.add(toF);
        toRestList.add(toR);
        timeToFlex += toF;
        timeToRest += toR;
    }



    private boolean inMargin(float z){
        return (z > (baseRest[2] - margin[2]));
//        if(flexing){
//            return (z < (baseRest[2]+margin[2]));
//        }else{
//            return (z > (baseRest[2] - margin[2]));
//        }
    }
}
