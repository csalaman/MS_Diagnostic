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
import com.cmsc436.ms_diagnostic.tap_test.TapActivity;

public class TappingInstr extends Activity {

    Button left_hand;
    Button right_hand;
    Button done;

    TextView msg;

    double left_avg = 0;
    double right_avg = 0;
    int testCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tap_instruction_activity);
        left_hand = (Button) findViewById(R.id.left_hand_button);
        right_hand = (Button) findViewById(R.id.right_hand_button);
        done = (Button) findViewById(R.id.done_tap_button);
        msg = (TextView) findViewById(R.id.display_text_msg);

    }



    void startLeftTest(View v){
        Intent intent = new Intent(this,TappingTest.class);
        left_hand.setVisibility(View.GONE);
        testCount++;
        startActivityForResult(intent,1);
    }





    void startRightTest(View v){
        Intent intent = new Intent(this,TappingTest.class);
        right_hand.setVisibility(View.GONE);
        testCount++;
        startActivityForResult(intent,2);
    }

    void showResults(View v ){
        Intent intent = new Intent(this, Results.class);
        intent.putExtra("left_hand",left_avg);
        intent.putExtra("right_hand",right_avg);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("LEFT",""+left_avg);
        Log.d("RIGHT",""+right_avg);
        if (testCount == 2){
            done.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                left_avg = (double)(data.getIntExtra("data", -1))/3;
               //msg.setText(msg.getText() + "[LEFT:" + left_avg + "]");
            }
        } else {
            if (resultCode == Activity.RESULT_OK) {
                right_avg = (double)(data.getIntExtra("data", -1))/3;
                //msg.setText(msg.getText() + "[RIGHT:" + right_avg + "]");
            }
        }

    }
}
