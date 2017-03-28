package com.cmsc436.ms_diagnostic.google_spread_sheets;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.cmsc436.ms_diagnostic.R;

import java.util.ArrayList;
import java.util.List;

public class TestGoogleComm extends AppCompatActivity {

    TextView textView;
    Button button;
    Button sendButton;
    GoogleSheetManager googleSheetManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_google_comm);

        textView = (TextView) findViewById(R.id.comm_text);
        button = (Button) findViewById(R.id.comm_button);
        sendButton = (Button) findViewById(R.id.comm_send_data);

        googleSheetManager = new GoogleSheetManager(TestGoogleComm.this);
        googleSheetManager.initializeCommunication();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                button.setEnabled(false);
                button.setBackgroundColor(Color.RED);
                List<String> data = googleSheetManager.getData();
                button.setEnabled(true);
                button.setBackgroundColor(Color.GREEN);
//                String s = "";
//
//                for(String st : data){
//                    s+= st+"\n";
//                }
//
//                textView.setText(s);
            }
        });

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendButton.setEnabled(false);
                sendButton.setBackgroundColor(Color.RED);
                List<Object> l = new ArrayList<>();
//                l.add(SheetData.getPID());
//                l.add(1);
                l.add(11);
                l.add(22);
                l.add(33);
                l.add("Testing shit");
                googleSheetManager.sendData(SheetData.TAPPING_TEST_LH,l);
                sendButton.setEnabled(true);
                sendButton.setBackgroundColor(Color.GREEN);
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        googleSheetManager.setServices(requestCode,resultCode,data);
    }


}
