package com.cmsc436.ms_diagnostic;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    // changes
    public void toTapActivity(View view){
        startActivity(new Intent(MainActivity.this,TapActivity.class));
    }

}
// HElo