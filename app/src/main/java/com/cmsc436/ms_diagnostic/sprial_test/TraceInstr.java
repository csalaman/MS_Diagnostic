package com.cmsc436.ms_diagnostic.sprial_test;

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


public class TraceInstr extends AppCompatActivity {
    private final int LEFT = 1;
    private final int RIGHT = 2;

    private final String DATA = "DATA";

    Button leftButton;
    Button rightButton;
    Button doneButton;

    private long leftScore = 0;
    private long rightScore = 0;

    private int testCount = 0;

    GoogleSheetManager googleSheetManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trace_instr);

        leftButton = (Button) findViewById(R.id.trace_instr_left);
        rightButton = (Button) findViewById(R.id.trace_instr_right);
        doneButton = (Button) findViewById(R.id.trace_inst_done);

        googleSheetManager = new GoogleSheetManager(TraceInstr.this);
        googleSheetManager.initializeCommunication();
    }

    public void leftTest(View v){
        testCount++;
        leftButton.setVisibility(View.GONE);
        startActivityForResult(new Intent(this,Trace.class),LEFT);
    }

    public void rightTest(View v){
        testCount++;
        rightButton.setVisibility(View.GONE);
        startActivityForResult(new Intent(this,Trace.class),RIGHT);
    }

    public void doneTest(View v){
        Intent intent = new Intent(this, Results.class);
        intent.putExtra(getString(R.string.LEFT),""+leftScore);
        intent.putExtra(getString(R.string.RIGHT),""+rightScore);

        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (testCount == 2){
            doneButton.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == 1){
            if(resultCode == RESULT_OK) {
                leftScore = data.getLongExtra(DATA, 0) / 3;
                googleSheetManager.sendData(
                        SheetData.SPIRAL_TEST_LH,
                        (ArrayList<Object>) data.getSerializableExtra(Trace.DATA_LIST));
            }
        }
        else if(requestCode == 2){
            if(resultCode == RESULT_OK) {
                rightScore = data.getLongExtra(DATA, 0);
                googleSheetManager.sendData(
                        SheetData.SPIRAL_TEST_RH,
                        (ArrayList<Object>) data.getSerializableExtra(Trace.DATA_LIST));
            }
        }
        else {
            googleSheetManager.setServices(requestCode,resultCode,data);
        }
    }
}
