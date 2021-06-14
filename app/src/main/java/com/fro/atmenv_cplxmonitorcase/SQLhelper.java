package com.fro.atmenv_cplxmonitorcase;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class SQLhelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "environment.db";
    private static final int DATABASE_VERSIOM = 1;
    private static final String TABLE_NAME = "atmosphere";

    public SQLhelper(Context context ) {
        super(context, DATABASE_NAME, null, DATABASE_VERSIOM);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table "+"if not exists "+TABLE_NAME+"("
                +"id integer primary key,"
                +"data varchar,"
                +"tem integer,"
                +"hum integer,"
                +"sun integer,"
                +"pm25 integer)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("drop table if exists "+TABLE_NAME);
        this.onCreate(db);
    }
}
