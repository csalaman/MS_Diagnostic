package com.cmsc436.ms_diagnostic;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.transition.Explode;
import android.view.View;
import android.view.Window;

import com.cmsc436.ms_diagnostic.balloon_test.BubbleActivity;
import com.cmsc436.ms_diagnostic.balloon_test.BubbleInstr;
import com.cmsc436.ms_diagnostic.flex_test.FlexCalibrate;
import com.cmsc436.ms_diagnostic.flex_test.FlexInstr;
import com.cmsc436.ms_diagnostic.google_spread_sheets.TestGoogleAPI;
import com.cmsc436.ms_diagnostic.google_spread_sheets.TestGoogleComm;
import com.cmsc436.ms_diagnostic.level_test.Balancer;
import com.cmsc436.ms_diagnostic.level_test.BalancerInstr;
import com.cmsc436.ms_diagnostic.sprial_test.Trace;
import com.cmsc436.ms_diagnostic.sprial_test.TraceInstr;
import com.cmsc436.ms_diagnostic.tap_check_test.TappingInstr;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        getWindow().setExitTransition(new Explode());
        setContentView(R.layout.activity_main);
    }

    // changes
    public void toTapActivity(View view){
       // startActivity(new Intent(MainActivity.this,TapActivity.class));
        startActivity(new Intent(MainActivity.this,TappingInstr.class));


    }


    public void startSpiralTest(View v){
        Intent spiral_intent = new Intent(this,TraceInstr.class);
        startActivity(spiral_intent);
    }

    public void startBalancer(View v){
        Intent intent = new Intent(this,BalancerInstr.class);
        startActivity(intent);
    }

    public void startBubble(View v) {
        Intent intent = new Intent(this, BubbleInstr.class);
        startActivity(intent);
    }

    public void startGoogle(View v){
        Intent intent = new Intent(this,FlexInstr.class);
        startActivity(intent);
    }

//    public void Circle_Activity(View view){
//        Intent circle_intent = new Intent(this, CircleActivity.class);
//        startActivity(circle_intent);
//    }
//    public void startBallMover(View v) {startActivity(new Intent(this, Balancer.class));}

//    public void startBallActivity(View v) {
//        startActivity(new Intent(this, BallActivity.class));
//    }




}
