package com.fro.atmenv_cplxmonitorcase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.ContactsContract;
import android.util.Log;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class  SQLManager {
    private SQLhelper helper;
    private SQLiteDatabase db;
    private static final String TABLE_NAME = "atmosphere ";
    public SQLManager(Context context){
        helper = new SQLhelper(context);
        db = helper.getWritableDatabase();
    }
    public void insert( String data,Float tem, Float hum, Float sun, Float pm25){
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("data",data);
        values.put("tem",tem);
        values.put("hum",hum);
        values.put("sun",sun);
        values.put("pm25",pm25);
        db.insert(TABLE_NAME,"id",values);
        db.close();
    }//将数据插入到数据库中


    public  String getPastDate(int past) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) - past);
        Date today = calendar.getTime();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String result = format.format(today);
        Log.e(null, result);
        return result;

    }               //通过past得到past前的日期

    public Cursor getCursor(int past){
        String[] strArray={"1"};
        String d = getPastDate(past);
        strArray[0] = d;
        Cursor c = db.rawQuery("select * from " +
                TABLE_NAME + " where data=?",strArray);
        return c;
    }
}
