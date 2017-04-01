package com.cmsc436.ms_diagnostic.tap_check_test;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.cmsc436.ms_diagnostic.R;
import com.cmsc436.ms_diagnostic.Results;
import com.cmsc436.ms_diagnostic.google_spread_sheets.GoogleSheetManager;
import com.cmsc436.ms_diagnostic.google_spread_sheets.SheetData;

import java.util.ArrayList;

public class TappingInstr extends Activity {

    Button left_hand;
    Button right_hand;
    Button leftFoot;
    Button rightFoot;
    Button done;

    TextView msg;

    private final int LH = 1;
    private final int RH = 2;
    private final int LF = 3;
    private final int RF = 4;

    double left_hand_avg = 0;
    double right_hand_avg = 0;
    double left_foot_avg = 0;
    double right_foot_avg = 0;
    int testCount = 0;
    // ~~ This Object allows to send data to Spreadsheets
    GoogleSheetManager googleSheetManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tap_instruction_activity);
        left_hand = (Button) findViewById(R.id.left_hand_button);
        right_hand = (Button) findViewById(R.id.right_hand_button);
        leftFoot = (Button) findViewById(R.id.left_foot_button);
        rightFoot = (Button) findViewById(R.id.right_foot_button);

        done = (Button) findViewById(R.id.done_tap_button);
        msg = (TextView) findViewById(R.id.display_text_msg);

        googleSheetManager = new GoogleSheetManager(TappingInstr.this);
        // ~~ initializeCommunication makes sure that the
        // the phone can communicate with Google Sheets
        // its onResume because there may times when the
        // communication can be interrupted by other activities or pressing home button
        googleSheetManager.initializeCommunication();
    }



    public void startLeftHandTest(View v){
        Intent intent = new Intent(this,TappingTest.class);
        left_hand.setVisibility(View.GONE);
        testCount++;
        startActivityForResult(intent,LH);
    }

    public void startRightHandTest(View v){
        Intent intent = new Intent(this,TappingTest.class);
        right_hand.setVisibility(View.GONE);
        testCount++;
        startActivityForResult(intent,RH);
    }

    public void startLeftFootTest(View v){
        Intent intent = new Intent(this,TappingTest.class);
        leftFoot.setVisibility(View.GONE);
        testCount++;
        startActivityForResult(intent,LF);
    }

    public void startRightFootTest(View v){
        Intent intent = new Intent(this,TappingTest.class);
        rightFoot.setVisibility(View.GONE);
        testCount++;
        startActivityForResult(intent,RF);
    }



    public void showResults(View v ){
        Intent intent = new Intent(this, Results.class);
        intent.putExtra(getString(R.string.LEFT),""+ left_hand_avg);
        intent.putExtra(getString(R.string.RIGHT),""+ right_hand_avg);

        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();

        Log.d("LEFT",""+ left_hand_avg);
        Log.d("RIGHT",""+ right_hand_avg);
        if (testCount == 4){
            done.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == LH) {
            if (resultCode == Activity.RESULT_OK) {
                left_hand_avg = (double)(data.getIntExtra("data", -1))/3;

                //~~ Sending data to the Spreadsheet
                googleSheetManager.sendData(
                        SheetData.TAPPING_TEST_LH,
                        (ArrayList<Object>)data.getSerializableExtra(TappingTest.DATA_LIST));
               //msg.setText(msg.getText() + "[LEFT:" + left_hand_avg + "]");
            }
        } else if( requestCode == RH) {
            if (resultCode == Activity.RESULT_OK) {
                right_hand_avg = (double)(data.getIntExtra("data", -1))/3;
                googleSheetManager.sendData(
                        SheetData.TAPPING_TEST_RH,
                        (ArrayList<Object>)data.getSerializableExtra(TappingTest.DATA_LIST));
                //msg.setText(msg.getText() + "[RIGHT:" + right_hand_avg + "]");
            }
        } else if(requestCode == LF){
            if(resultCode == RESULT_OK){
                left_foot_avg = (double) data.getIntExtra("data", -1)/3;
                googleSheetManager.sendData(
                        SheetData.TAPPING_TEST_LF,
                        (ArrayList<Object>) data.getSerializableExtra(TappingTest.DATA_LIST)
                );
            }
        } else if(requestCode == RF){
            if(resultCode == RESULT_OK){
                right_foot_avg = (double) data.getIntExtra("data", -1)/3;
                googleSheetManager.sendData(
                        SheetData.TAPPING_TEST_RF,
                        (ArrayList<Object>) data.getSerializableExtra(TappingTest.DATA_LIST)
                );
            }
        }
        else {
            Log.d("REQRES","Request Called");
            // ~~ this because the manager may need to open up popup of choosers
            googleSheetManager.setServices(requestCode,resultCode,data);
        }

    }
}
