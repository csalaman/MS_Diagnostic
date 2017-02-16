package com.cmsc436.ms_diagnostic;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Shubham P.
 * This Database
 */

public class DBHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "trace.db";
    public static final String TABLE_NAME = "trace_table";
    public static int VERSION = 1;

    public static final short HAND_LEFT = 0;
    public static final short HAND_RIGHT = 1;
    public static final String ID = "_id";
    public static final String HAND = "hand";
    public static final String TIMESTAMP = "timestamp";
    public static final String TIME = "time";
    public static final String IMAGE = "image";
    public static final String COORDINATES = "coordinates";

    public static String[] ALL_COLUMNS = {ID,HAND,TIMESTAMP,TIME,IMAGE,COORDINATES};

    private final String createDB = "create table if not exists  "+TABLE_NAME+"("
            +ID+"integer primary key autoincrement, "
            +HAND+" int2, "
            +TIMESTAMP+"text default current_timestamp, "
            +TIME+"int, "
            +IMAGE+"blob, "
            +COORDINATES+"text); ";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(createDB);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table "+ TABLE_NAME);
    }
}
