package com.cmsc436.ms_diagnostic.google_spread_sheets;

import com.google.api.services.sheets.v4.model.ValueRange;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Shubham Patel
 * This is a static class for all details about Google Sheets
 */

public class SheetData {

    public static final String SPREADSHEET_NAME = "CMSC436 App Template";
    public static final String SPREADSHEET_ID = "1nxEXLLuyhMz5e2RNfAn-Zs1soCQRG-8nk6aUsnMvKSc";
    private static final String TEAM_ID = "07";
    private static final String MEMBER_ID = "01";


    public static final String TAPPING_TEST_LH = "Tapping Test (LH)";
    public static final String TAPPING_TEST_RH = "Tapping Test (RH)";
    public static final String TAPPING_TEST_LF = "Tapping Test (LF)";
    public static final String TAPPING_TEST_RF = "Tapping Test (RF)";

    public static final String SPIRAL_TEST_LH = "Spiral Test (LH)";
    public static final String SPIRAL_TEST_RH = "Spiral Test (RH)";

    public static final String Balloon_TEST_LH = "Balloon Test (LH)";
    public static final String Balloon_TEST_RH = "Balloon Test (RH)";

    public static final String LEVEL_TEST_LH = "Level Test (LH)";
    public static final String LEVEL_TEST_RH = "Level Test (RH)";



    public static String getPID(){
        ValueRange v = new ValueRange();

        return "t"+TEAM_ID+"p"+MEMBER_ID;
    }


    public static String getRange(String testID){
        if(testID.contains("Tap")){
            return testID+"!A:G";
        }
        else if(testID.contains("Spi")){
            return testID+"!A:F";
        }
        if(testID.contains("Ball")){
            return testID+"!A:F";
        }
        if(testID.contains("Lev")){
            return testID+"!A:G";
        }
        return " ";
    }

    public static String getTimeStamp(){
            Date d = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("dd/mm/yyyy HH:mm:ss");
            return sdf.format(d);
    }



    public static String getLastTestTimeStampPrefrenceID(String ID){
        return ID+"TIME";
    }

    public static String getTestDayPrefrenceID(String ID){
        return ID+"DAY";
    }




}
