package com.cmsc436.ms_diagnostic.balloon_test;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.cmsc436.ms_diagnostic.R;
import com.cmsc436.ms_diagnostic.Results;
import com.cmsc436.ms_diagnostic.google_spread_sheets.GoogleSheetManager;
import com.cmsc436.ms_diagnostic.google_spread_sheets.SheetData;

import java.util.ArrayList;
import java.util.List;

public class BubbleInstr extends AppCompatActivity {

    final int LEFT_CODE = 1;
    final int RIGHT_CODE = 2;

    Button leftButton;
    Button rightButton;
    Button doneButton;

    // number of trials done
    private long leftTrial;
    private long rightTrial;

    boolean leftDone, rightDone = false;

//    // data to be sent
//    List<Object> dataToSend;

    GoogleSheetManager googleSheetManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bubble_instr);

        leftButton = (Button) findViewById(R.id.bubble_instr_left);
        rightButton = (Button) findViewById(R.id.bubble_instr_right);
        doneButton = (Button) findViewById(R.id.bubble_instr_done);


        googleSheetManager = new GoogleSheetManager(BubbleInstr.this);
        googleSheetManager.initializeCommunication();


        leftButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BubbleInstr.this,BubbleActivity.class);
                startActivityForResult(intent,LEFT_CODE);
            }
        });
        rightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BubbleInstr.this,BubbleActivity.class);
                startActivityForResult(intent,RIGHT_CODE);
            }
        });

        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BubbleInstr.this, Results.class);
                intent.putExtra(getString(R.string.LEFT),""+leftTrial);
                intent.putExtra(getString(R.string.RIGHT),""+rightTrial);
                startActivity(intent);
            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
        if(leftDone && rightDone){
            doneButton.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == LEFT_CODE){
            if (resultCode == RESULT_OK){
                leftDone = true;
                leftButton.setVisibility(View.GONE);
                leftTrial = data.getLongExtra(BubbleActivity.SCORE,-1);
                googleSheetManager.sendData(
                        SheetData.Balloon_TEST_LH,
                        (ArrayList<Object>)data.getSerializableExtra(BubbleActivity.DATA));
            }

        }else if(requestCode == RIGHT_CODE){
            if (resultCode == RESULT_OK){
                rightDone = true;
                rightButton.setVisibility(View.GONE);
                rightTrial = data.getLongExtra(BubbleActivity.SCORE,-1);
                googleSheetManager.sendData(
                        SheetData.Balloon_TEST_RH,
                        (ArrayList<Object>)data.getSerializableExtra(BubbleActivity.DATA));
            }

        }else{
            googleSheetManager.setServices(requestCode,resultCode,data);
        }
    }
}
