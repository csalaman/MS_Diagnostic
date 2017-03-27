package com.cmsc436.ms_diagnostic.flex_test;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.cmsc436.ms_diagnostic.R;
import com.cmsc436.ms_diagnostic.Results;

public class FlexInstr extends AppCompatActivity {

    final int LEFT_CODE = 1;
    final int RIGHT_CODE = 2;
    final int CALIBRATE_CODE = 3;

    Button leftTest, rightTest, calibrate, doneButton;
    boolean calibrated,leftDone,rightDone = false;
    float[] rest,flex;
    long leftTime,rightTime;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flex_instr);
        leftTest = (Button) findViewById(R.id.flex_instr_left);
        rightTest = (Button) findViewById(R.id.flex_instr_right);
        calibrate = (Button) findViewById(R.id.flex_instr_calibrate);
        doneButton = (Button) findViewById(R.id.flex_instr_done);

//        rightTest.setVisibility(View.GONE);
//        leftTest.setVisibility(View.GONE);

        leftTest.setEnabled(false);
        rightTest.setEnabled(false);
        doneButton.setVisibility(View.GONE);

        calibrate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FlexInstr.this,FlexCalibrate.class);
                startActivityForResult(intent,CALIBRATE_CODE);
            }
        });

    }

    @Override
    protected void onResume() {

        if(calibrated){
            Log.d("CALIX", "IN ONRESUME CALI");
            leftTest.setEnabled(true);

            leftTest.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(FlexInstr.this,FlexTest.class);
                    intent.putExtra(FlexCalibrate.REST,rest);
                    startActivityForResult(intent,LEFT_CODE);
                }
            });

            rightTest.setEnabled(true);
            rightTest.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(FlexInstr.this,FlexTest.class);
                    intent.putExtra(FlexCalibrate.REST,rest);
                    startActivityForResult(intent,RIGHT_CODE);
                }
            });
        }

        if(rightDone && leftDone){
            doneButton.setVisibility(View.VISIBLE);
            doneButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(FlexInstr.this, Results.class);
                    intent.putExtra(getString(R.string.LEFT),""+leftTime);
                    intent.putExtra(getString(R.string.RIGHT),""+rightTime);
                    startActivity(intent);
                }
            });
        }

        super.onResume();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == CALIBRATE_CODE){
            if(resultCode == RESULT_OK){
                rest = data.getFloatArrayExtra(FlexCalibrate.REST);
                flex = data.getFloatArrayExtra(FlexCalibrate.FLEX);
                calibrated = true;
                leftTest.setEnabled(true);
                rightTest.setEnabled(true);
            }
        }

        else if(requestCode == LEFT_CODE){
            if(resultCode == RESULT_OK){
                leftTime = (data.getLongExtra(FlexTest.TO_REST,0) +
                        data.getLongExtra(FlexTest.TO_FLEX,0))/3;
                if(leftTime == 0){
                    Log.d("TESTX", "LEFT TIME");
                }
                leftDone = true;
                leftTest.setVisibility(View.GONE);
            }
        }
        else if(requestCode == RIGHT_CODE){
            if(resultCode == RESULT_OK){
                rightTime = (data.getLongExtra(FlexTest.TO_REST,0) +
                        data.getLongExtra(FlexTest.TO_FLEX,0))/3;
                if(leftTime == 0){
                    Log.d("TESTX", "RIHT TIME");
                }
                rightDone = true;
                rightTest.setVisibility(View.GONE);
            }
        }
    }
}
