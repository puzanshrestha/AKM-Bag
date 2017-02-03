package com.example.pujan.bag.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Pujan on 1/7/2017.
 */
public class DbHelper extends SQLiteOpenHelper {

    static private String dbname = "bagDb";
    static int version =1;

    String createTableSql = "CREATE TABLE IF NOT EXISTS IPPOOL ("
            +" ipid    TEXT,"
            +" ip      TEXT)";

    public DbHelper(Context context) {
        super(context, dbname, null, version);


        getWritableDatabase().execSQL(createTableSql);
        String query2 = "insert into IPPOOL values('1','192.168.1.12')";
        getWritableDatabase().execSQL(query2);


    }

    public void setIP(String ip)
    {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("ip", ip);
        db.update("IPPOOL", values, "ipid" + "=" + "1", null);
        db.close();
    }
    public String getIP()
    {
        String sql = "SELECT ip FROM IPPOOL WHERE ipid=1;";
        Cursor c = getWritableDatabase().rawQuery(sql,null);
        String ip="";
        c.moveToFirst();
        ip=c.getString(c.getColumnIndex("ip"));


        return ip;

    }
    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
