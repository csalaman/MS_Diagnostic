package com.cmsc436.ms_diagnostic;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
    }

    // changes
    public void toTapActivity(View view){
        //startActivity(new Intent(MainActivity.this,TapActivity.class));
        startActivity(new Intent(this,Trace.class));
    }

}
