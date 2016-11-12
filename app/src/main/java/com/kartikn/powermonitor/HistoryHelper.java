package com.kartikn.powermonitor;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by kartikn on 11-11-2016.
 */

public class HistoryHelper extends SQLiteOpenHelper {
    public static final String TABLE_HISTORY = "history";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_CAPACITY = "capacity";
    public static final String COLUMN_DATETIME = "datetime";
    public static final String COLUMN_STARTINGCHARGE = "startingcharge";
    public static final String COLUMN_POWER = "power";

    private static final String DATABASE_NAME = "history.db";
    private static final int DATABASE_VERSION = 1;

    // Database creation sql statement
    private static final String DATABASE_CREATE = "create table "
            + TABLE_HISTORY + "( " + COLUMN_ID
            + " integer primary key autoincrement, " + COLUMN_CAPACITY
            + " integer not null, " + COLUMN_DATETIME + " text not null, " + COLUMN_STARTINGCHARGE + " integer not null, " + COLUMN_POWER + " text not null );";

    public HistoryHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(HistoryHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_HISTORY);
        onCreate(db);
    }


}
