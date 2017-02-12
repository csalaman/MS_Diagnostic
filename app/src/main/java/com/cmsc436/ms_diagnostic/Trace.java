package com.cmsc436.ms_diagnostic;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

public class Trace extends AppCompatActivity {

    private Chronometer timer;
    private Button start_button;
    private Button stop_button;

    public static final String STATE_LH = "Left_Hand_Sec";
    public static final String STATE_RH = "Right_Hand_Sec";
    private long left_h_time = 0;
    private long right_h_time = 0;

    private int testCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trace);
        timer = (Chronometer) findViewById(R.id.timer_object);

        start_button = (Button) findViewById(R.id.start_button );
        stop_button = (Button) findViewById(R.id.stop_button);

        start_button.setEnabled(true);
        stop_button.setEnabled(false);
    }


    public void startTimer(View view){
        timer.setBase(SystemClock.elapsedRealtime());
        timer.start();
        testCount++;
        start_button.setEnabled(false);
        stop_button.setEnabled(true);





    }

    public void stopTimer(View view){
        timer.stop();
        start_button.setEnabled(true);
        stop_button.setEnabled(false);
        long elapsedTime = (SystemClock.elapsedRealtime() - timer.getBase() )/1000;

        DrawView traceView = (DrawView)findViewById(R.id.trace_draw_view);
        traceView.setDrawingCacheEnabled(true);
        traceView.buildDrawingCache();
        Bitmap bm = traceView.getDrawingCache();



        String path = Environment.getExternalStorageDirectory().getPath();
        String imageName = (testCount == 1) ? "/left.jpg" : "/right.jpg";
        //File file = new File("/mnt" + path + imageName);

        try {
            //FileOutputStream outputStream = new FileOutputStream(new File("/mnt" + path + imageName));
            bm.compress(Bitmap.CompressFormat.JPEG, 100, new FileOutputStream(new File(path + imageName)));
            //bm.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
            //outputStream.flush();
            //outputStream.close();
        } catch (FileNotFoundException e) {
            Log.e("File not found", e.toString());
        }

        if(testCount == 1){
            left_h_time = elapsedTime;
        }else{
            right_h_time = elapsedTime;
            Intent myIntent = new Intent(this,Results.class);
            myIntent.putExtra(STATE_LH,left_h_time);
            myIntent.putExtra(STATE_RH,right_h_time);
            startActivity(myIntent);
        }



        Toast.makeText(this, "Ellapsed Time: " + elapsedTime +" seconds.", Toast.LENGTH_LONG).show();

    }

}
