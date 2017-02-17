package com.cmsc436.ms_diagnostic;

import android.app.Activity;

import android.os.Bundle;





public class CircleActivity extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(new Circles(this));
    }
}
