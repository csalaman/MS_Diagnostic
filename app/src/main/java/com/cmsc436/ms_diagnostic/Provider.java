package com.cmsc436.ms_diagnostic;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;

/**
 * Created by Shubham Patel
 *
 * This is the Content Provider for Data Saved
 *
 * IMPORTANT NOTICE:
 *      IF FUTURE TABLES ARE TO BE ADDED
 *      WE MUST UPDATE ALL OF THE METHODS TO ACCOMMODATE IT
 *
 * PLEASE NAME IT PROPERLY ----> TABLE_NAME_DESCRIPTION
 */

public class Provider extends ContentProvider {

    private static final String AUTHORITY = "com.cmsc436.ms_diagnostics.provider";
    private static final String TRACE_PATH = "trace_table";
    public static final Uri TRACE_URI = Uri.parse("content://"+AUTHORITY+"/"+TRACE_PATH);

    private static final int TRACE_ID = 1;

    private static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static{
        uriMatcher.addURI(AUTHORITY,TRACE_PATH,TRACE_ID);
    }


    SQLiteDatabase database;
    @Override
    public boolean onCreate() {
        DBHelper helper = new DBHelper(getContext());
        database = helper.getWritableDatabase();
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        if(uriMatcher.match(uri) == TRACE_ID){
            selection = DBHelper.ID+ "="+uri.getLastPathSegment();
            return database.query(DBHelper.TABLE_NAME,DBHelper.ALL_COLUMNS,selection,null,null,null,DBHelper.ID);
        }

        return null;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {

        return null;
    }


    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        if(uriMatcher.match(uri) == TRACE_ID){
            long id = database.insert(DBHelper.TABLE_NAME,null,values);
            return Uri.parse(TRACE_PATH+"/"+id);
        }

        return null;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        if(uriMatcher.match(uri) == TRACE_ID) {
            return database.delete(DBHelper.TABLE_NAME, selection, selectionArgs);
        }
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        if(uriMatcher.match(uri) == TRACE_ID) {
            return database.update(DBHelper.TABLE_NAME, values, selection, selectionArgs);
        }
        return 0;
    }
}
