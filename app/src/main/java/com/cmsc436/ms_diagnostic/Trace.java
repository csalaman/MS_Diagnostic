package com.cmsc436.ms_diagnostic;

import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.transition.Explode;
import android.transition.Fade;
import android.util.Log;
import android.view.View;
import android.view.Window;
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

    private DrawView draw_event;

    public static final String STATE_LH = "Left_Hand_Sec";
    public static final String STATE_RH = "Right_Hand_Sec";
    private long left_h_time = 0;
    private long right_h_time = 0;

    private int testCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Transition of screen
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        setContentView(R.layout.activity_trace);

        timer = (Chronometer) findViewById(R.id.timer_object);

        start_button = (Button) findViewById(R.id.start_button );
        stop_button = (Button) findViewById(R.id.stop_button);
        draw_event = (DrawView) findViewById(R.id.trace_draw_view);


        start_button.setEnabled(true);
        stop_button.setEnabled(false);

        draw_event.setVisibility(View.GONE);

    }

    @Override
    protected void onStart() {
        testCount = 0;
        super.onStart();
    }

    public void startTimer(View view){
        timer.setBase(SystemClock.elapsedRealtime());
        timer.start();
        testCount++;
        start_button.setEnabled(false);
        stop_button.setEnabled(true);
        draw_event.setVisibility(View.VISIBLE);





    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void stopTimer(View view){
        timer.stop();
        start_button.setEnabled(true);
        stop_button.setEnabled(false);
        draw_event.setVisibility(View.GONE);
        draw_event.clearDrawing();
        long elapsedTime = (SystemClock.elapsedRealtime() - timer.getBase() )/1000;

        DrawView traceView = (DrawView)findViewById(R.id.trace_draw_view);
        traceView.setDrawingCacheEnabled(true);
        traceView.buildDrawingCache();
        Bitmap bm = traceView.getDrawingCache();

        String root = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString();
        File myDir = new File(root + "/saved_images");
        myDir.mkdirs();

        String imageName = (testCount == 1) ? "/left.jpg" : "/right.jpg";
        File file = new File(myDir, imageName);




        //String path = Environment.getExternalStorageDirectory().getPath();
        //String path = getExternalStoragePublicDirectory(DIRECTORY_PICTURES).getPath();

        //File file = new File("/mnt" + path + imageName);
        if (file.exists ()) file.delete ();
        try {
            //FileOutputStream outputStream = new FileOutputStream(new File("/mnt" + path + imageName));
            FileOutputStream out = new FileOutputStream(file);
            bm.compress(Bitmap.CompressFormat.JPEG, 100, out);
            //bm.compress(Bitmap.CompressFormat.JPEG, 100, new FileOutputStream(new File(path + imageName)));

            //bm.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
            //outputStream.flush();
            //outputStream.close();
        } catch (FileNotFoundException e) {
            Log.e("File not found", e.toString());
        }

        MediaScannerConnection.scanFile(this, new String[] { file.toString() }, null,
                new MediaScannerConnection.OnScanCompletedListener() {
                    public void onScanCompleted(String path, Uri uri) {
                        Log.i("ExternalStorage", "Scanned " + path + ":");
                        Log.i("ExternalStorage", "-> uri=" + uri);
                    }
                });

        if(testCount == 1){
            left_h_time = elapsedTime;
        }else{
            right_h_time = elapsedTime;
            // Exit transition
            getWindow().setExitTransition(new Fade());
            Intent myIntent = new Intent(this,Results.class);
            myIntent.putExtra(STATE_LH,left_h_time);
            myIntent.putExtra(STATE_RH,right_h_time);
            startActivity(myIntent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
            //startActivity(myIntent);
        }



        Toast.makeText(this, "Ellapsed Time: " + elapsedTime +" seconds.", Toast.LENGTH_LONG).show();

    }

}
