package com.cmsc436.ms_diagnostic.level_test;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.cmsc436.ms_diagnostic.R;
import com.cmsc436.ms_diagnostic.Results;
import com.cmsc436.ms_diagnostic.dialog_comment.CommentDialog;
import com.cmsc436.ms_diagnostic.google_spread_sheets.GoogleSheetManager;
import com.cmsc436.ms_diagnostic.google_spread_sheets.SheetData;

import org.w3c.dom.Comment;

import java.util.ArrayList;
import java.util.List;

/*
    Created by Shubham Patel
    This activity like all ___Instr activity will let the user interact with the level test
    and send data to the Google Sheets
 */
public class BalancerInstr extends AppCompatActivity {
    // codes for intent extra
    final static String GET_SCORE = "GETSCORE";
    final static String HAND = "HAND";
    final static String TRIAL = "TRIAL";

    final static int LEFT_CODE = 1;
    final static int RIGHT_CODE = 2;

    CommentDialog new_comment;
    Button leftButton;
    Button rightButton;
    Button doneButton;
    Button feedbackButton;

    // number of trials done
    private int leftTrial;
    private int rightTrial;

    // data to be sent
    List<Object> leftData;
    List<Object> rightData;

    // avrage data to be sent to results
    double leftAvg = 0.0;
    double rightAvg = 0.0;

    GoogleSheetManager googleSheetManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_balancer_instr);

        leftButton = (Button) findViewById(R.id.balancer_instr_left);
        rightButton = (Button) findViewById(R.id.balancer_inst_right);
        doneButton = (Button) findViewById(R.id.balancer_inst_done);
        feedbackButton = (Button) findViewById(R.id.trace_feedback);

        leftTrial = 1;
        rightTrial = 1;

        leftData = new ArrayList<>();
        rightData = new ArrayList<>();

        leftButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BalancerInstr.this,Balancer_Test.class);
                intent.putExtra(HAND,"Left");
                intent.putExtra(TRIAL,leftTrial);
                leftTrial++;
                startActivityForResult(intent,LEFT_CODE);
            }
        });

        rightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BalancerInstr.this,Balancer_Test.class);
                intent.putExtra(HAND,"Right");
                intent.putExtra(TRIAL,rightTrial);
                rightTrial++;
                startActivityForResult(intent,RIGHT_CODE);
            }
        });

        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BalancerInstr.this, Results.class);
                intent.putExtra(getString(R.string.LEFT),""+leftAvg);
                intent.putExtra(getString(R.string.RIGHT),""+rightAvg);
//                Toast.makeText(BalancerInstr.this,new_comment.getTextComment(), Toast.LENGTH_LONG).show();
                startActivity(intent);
            }
        });

        googleSheetManager = new GoogleSheetManager(BalancerInstr.this);
        googleSheetManager.initializeCommunication();
    }

    @Override
    protected void onResume() {

        leftButton.setText("Hand: Left  Trial: "+ leftTrial);
        rightButton.setText("Hand: Right  Trial: "+ rightTrial);

        if(leftTrial > 3){
            leftButton.setVisibility(View.GONE);
        }
        if(rightTrial > 3){
            rightButton.setVisibility(View.GONE);
        }
        if(leftTrial > 3 && rightTrial > 3){
            doneButton.setVisibility(View.VISIBLE);
            feedbackButton.setVisibility(View.VISIBLE);
        }

        super.onResume();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == LEFT_CODE){
            if(resultCode == RESULT_OK){
                double l = data.getDoubleExtra(GET_SCORE,0.0);
                leftAvg+=(l/3);
                leftData.add(l);
                if(leftTrial > 3){
                    googleSheetManager.sendData(SheetData.LEVEL_TEST_LH,leftData);
                }
            }
        }else if (requestCode == RIGHT_CODE){
            if (resultCode == RESULT_OK){
                double r = data.getDoubleExtra(GET_SCORE,0.0);
                rightAvg+=(r/3);
                rightData.add(r);
                if(rightTrial > 3){
                    googleSheetManager.sendData(SheetData.LEVEL_TEST_RH,rightData);
                }
            }
        }else{
            googleSheetManager.setServices(requestCode,resultCode,data);
        }
    }

    public void balancerFeedback(View view) {
        new_comment  = new CommentDialog(this);
        new_comment.create().show();

    }
}
