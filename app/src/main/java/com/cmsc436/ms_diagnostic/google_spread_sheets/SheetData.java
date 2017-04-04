package com.cmsc436.ms_diagnostic.google_spread_sheets;

import com.google.api.services.sheets.v4.model.ValueRange;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Shubham Patel
 * This is a static class for all details about Google Sheets
 */

public class SheetData {

    public static final String SPREADSHEET_NAME = "CMSC436 App Template";
    public static final String SPREADSHEET_ID = "1nxEXLLuyhMz5e2RNfAn-Zs1soCQRG-8nk6aUsnMvKSc";
    public static final String CENTRAL_SPREADSHEET_NAME = "CMSC436 App Template Central";
//    public static final String CENTRAL_SPREADSHEET_ID = "1BTfMpMri4M1cUkyIV5lecTxS9QBOJ1BGN6HJC9SSMkg"; // test
public static final String CENTRAL_SPREADSHEET_ID = "1YvI3CjS4ZlZQDYi5PaiA7WGGcoCsZfLoSFM0IdvdbDU"; // main
    private static final String TEAM_ID = "07";
    private static final String MEMBER_ID = "01"; //Change your ID Here
    /*Sam - 01
    * Henry - 02
    * Carlos - 03
    * Lauren - 04
    * Khanh - 05
    * if someone wants a different one lemme know
    * */

    public static final String TAPPING_TEST_LH = "Tapping Test (LH)";
    public static final String TAPPING_TEST_RH = "Tapping Test (RH)";
    public static final String TAPPING_TEST_LF = "Tapping Test (LF)";
//    public static final String TAPPING_TEST_LF = "a";
    public static final String TAPPING_TEST_RF = "Tapping Test (RF)";


    public static final String SPIRAL_TEST_LH = "Spiral Test (LH)";
    public static final String SPIRAL_TEST_RH = "Spiral Test (RH)";

    public static final String Balloon_TEST_LH = "Balloon Test (LH)";
    public static final String Balloon_TEST_RH = "Balloon Test (RH)";

    public static final String LEVEL_TEST_LH = "Level Test (LH)";
    public static final String LEVEL_TEST_RH = "Level Test (RH)";

    public static final String FLEX_TEST_LH = "Curling Test (LH)";
    public static final String FLEX_TEST_RH = "Curling Test (RH)";

    public static String getPID(){
//        ValueRange v = new ValueRange();

        return "t"+TEAM_ID+"p"+MEMBER_ID;
    }


    public static String getRange(String testID){
        if(testID.contains("Tap")){
            return testID+"!A:G";
        }
        else if(testID.contains("Spi")){
            return testID+"!A:F";
        }
        else if(testID.contains("Ball")){
            return testID+"!A:F";
        }
        else if(testID.contains("Lev")){
            return testID+"!A:G";
        }
        else if(testID.contains("Cur")){
            return testID+"!A:I";
        }
        return " ";
    }

    public static String getTimeStamp(){
        Calendar c = Calendar.getInstance();
//        Date d = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        return sdf.format(c.getTime());
    }

    public static String getCentralRange(String id, int row){
        return id+"!"+row+":"+row;
    }

    public static List<Object> getPreFix(){
        List<Object> list = new ArrayList<>();
        list.add(getPID());
        list.add(getTimeStamp());
        list.add(1);//data
        return list;
    }

    public static String getHyperTextLinkURL(String SheetID,int sheetNumID){
        return "https://docs.google.com/spreadsheets/d/"+SheetID+"/edit#grid="+sheetNumID;
    }

    public static String getHyperLink(String sheetID, Object data,int sheetNumID){
        return "=HYPERLINK(\""+getHyperTextLinkURL(sheetID,sheetNumID)+"\",\""+data+"\")";
    }



    public static String getLastTestTimeStampPrefrenceID(String ID){
        return ID+"TIME";
    }

    public static String getTestDayPrefrenceID(String ID){
        return ID+"DAY";
    }




}
