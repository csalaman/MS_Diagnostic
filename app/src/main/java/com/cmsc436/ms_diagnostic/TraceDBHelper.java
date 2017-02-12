package com.cmsc436.ms_diagnostic;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Shubham P.
 * This Database
 */

public class TraceDBHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "trace.db";
    public static final String TABLE_NAME = "trace_table";
    public static int VERSION = 1;

    public static final String ID = "_id";
    public static final String HAND = "hand";
    public static final String TIMESTAMP = "timestamp";
    public static final String TIME = "time";
    public static final String IMAGE = "image";
    public static final String COORDINATES = "coordinates";

    private final String createDB = "create table if not exists  "+TABLE_NAME+"("
            +ID+"integer primary key autoincrement, "
            +HAND+" int2, "
            +TIMESTAMP+"text, "
            +TIME+"int, "
            +IMAGE+"blob, "
            +COORDINATES+"text); ";

    public TraceDBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, VERSION);
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
