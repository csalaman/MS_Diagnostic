package com.cmsc436.ms_diagnostic;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.transition.Explode;
import android.transition.Slide;
import android.view.View;
import android.view.Window;

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

        startActivity(new Intent(MainActivity.this,TapActivity.class));


    }

    public void startSpiralTest(View v){
        Intent spiral_intent = new Intent(this,Trace.class);
        startActivity(spiral_intent);
    }




}
