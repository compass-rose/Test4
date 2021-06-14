package com.fro.atmenv_cplxmonitorcase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.provider.ContactsContract;
import android.util.Log;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import java.io.File;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class SQLManager {
    private SQLhelper helper;
    private SQLiteDatabase db;
    private static final String TABLE_NAME = "atmosphere ";
    public SQLManager(Context context){
        helper = new SQLhelper(context);
        db = helper.getWritableDatabase();
    }
    public void insert(int past,Integer tem, Integer hum, Integer sun,Integer pm25){
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("data",getPastDate(past));
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

    public List<Atmosphere> query() {
        List<Atmosphere> atmosphere = null;

        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
        if (cursor != null && cursor.getCount() > 0) {

            atmosphere = new ArrayList<>();

            while (cursor.moveToNext()) {
                Atmosphere atm = new Atmosphere();
                atm.id = cursor.getInt(cursor.getColumnIndex("id"));
                atm.date = cursor.getString(cursor.getColumnIndex("data"));
                atm.tem = cursor.getInt(cursor.getColumnIndex("tem"));
                atm.hum = cursor.getInt(cursor.getColumnIndex("hum"));
                atm.sun = cursor.getInt(cursor.getColumnIndex("sun"));
                atm.pm25 = cursor.getInt(cursor.getColumnIndex("pm25"));
                atmosphere.add(atm);
            }

            cursor.close();
        }

        db.close();

        return atmosphere;
    }

   private class Atmosphere {
        public int id;
        public String date;
        public int tem;
        public int hum;
        public int sun;
        public int pm25;
    }

    public void createExcelHead(HSSFSheet mSheet) {
        HSSFRow headRow = mSheet.createRow(0);
        headRow.createCell(0).setCellValue("id");
        headRow.createCell(1).setCellValue("data");
        headRow.createCell(2).setCellValue("tem");
        headRow.createCell(3).setCellValue("hum");
        headRow.createCell(4).setCellValue("sun");
        headRow.createCell(5).setCellValue("pm25");
    }
    public static void createCell(int id,String date,Integer tem, Integer hum, Integer sun,Integer pm25,HSSFSheet sheet) {
        HSSFRow dataRow = sheet.createRow(sheet.getLastRowNum() + 1);

        dataRow.createCell(0).setCellValue(id);
        dataRow.createCell(1).setCellValue(date);
        dataRow.createCell(2).setCellValue(tem);
        dataRow.createCell(3).setCellValue(hum);
        dataRow.createCell(3).setCellValue(sun);
        dataRow.createCell(3).setCellValue(pm25);
    }

    public void deriveExcel() {
        List<Atmosphere> atmosphere = query();
        HSSFWorkbook mWorkbook = new HSSFWorkbook();
        HSSFSheet mSheet = mWorkbook.createSheet(TABLE_NAME);
        createExcelHead(mSheet);
        for (Atmosphere atm : atmosphere) {
            createCell(atm.id, atm.date, atm.tem, atm.hum, atm.sun, atm.pm25, mSheet);
        }
        File xlsFile = new File(Environment.getExternalStorageDirectory(), "excel.xls");
        try {
            if (!xlsFile.exists()) {
                xlsFile.createNewFile();
            }
            mWorkbook.write(xlsFile);
            mWorkbook.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
