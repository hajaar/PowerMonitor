package com.kartikn.powermonitor;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kartikn on 11-11-2016.
 */

public class HistoryDataSource {

    private SQLiteDatabase database;
    private HistoryHelper dbHelper;
    private String[] allColumns = {HistoryHelper.COLUMN_ID,
            HistoryHelper.COLUMN_CAPACITY, HistoryHelper.COLUMN_DATETIME};

    public HistoryDataSource(Context context) {
        dbHelper = new HistoryHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public History createHistory(int capacity, String datetime) {
        ContentValues values = new ContentValues();
        values.put(HistoryHelper.COLUMN_CAPACITY, capacity);
        values.put(HistoryHelper.COLUMN_DATETIME, datetime);
        //Log.d("HistoryDataSource", "createHistory " + capacity + " "+ datetime);
        long insertId = database.insert(HistoryHelper.TABLE_HISTORY, null,
                values);
        Cursor cursor = database.query(HistoryHelper.TABLE_HISTORY,
                allColumns, HistoryHelper.COLUMN_ID + " = " + insertId, null,
                null, null, null);
        cursor.moveToFirst();
        History newHistory = cursorToHistory(cursor);

        cursor.close();
        return newHistory;
    }


    public List<History> getAllHistory() {
        List<History> historylist = new ArrayList<>();

        Cursor cursor = database.query(HistoryHelper.TABLE_HISTORY,
                allColumns, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            History history = cursorToHistory(cursor);
            // Log.d("HistoryDataSource", "getAllHistory: " + history.getId()+" "+history.getCapacity()+" "+history.getDatetime());
            historylist.add(history);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return historylist;
    }

    private History cursorToHistory(Cursor cursor) {
        History history = new History();
        history.setId(cursor.getLong(0));
        history.setCapacity(cursor.getInt(1));
        history.setDatetime(cursor.getString(2));
        Log.d("HistoryDataSource", "cursorToHistory: " + history.getId() + " " + history.getCapacity() + " " + history.getDatetime());
        return history;
    }
}

