package com.example.pujan.bag.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.pujan.bag.orderDetailsFragment.printPackageFragment.FragmentPrintDemo;

/**
 * Created by Pujan on 1/7/2017.
 */
/*saajan added the recipt no and date for the receipt no*/

public class DbHelper extends SQLiteOpenHelper {

    static private String dbname = "bagDb";
    static int version = 1;

    String createTableSql = "CREATE TABLE IF NOT EXISTS IPPOOL ("
            +" ipid    TEXT,"
            +" ip      TEXT)";
    String createShopTableSql = "CREATE TABLE IF NOT EXISTS SHOP ("
            + "shop_id  TEXT,"
            +" shop_number  TEXT)";
    String createShopReceipt ="CREATE TABLE IF NOT EXISTS Receipts ("    /*saajan*/
            + "receipt_id  TEXT,"
            + "date_today TEXT)";


    public DbHelper(Context context) {
        super(context, dbname, null, version);

        getWritableDatabase().execSQL(createTableSql);
        String query1 = "insert into IPPOOL values('1','192.168.1.12')";
        getWritableDatabase().execSQL(query1);

        getWritableDatabase().execSQL(createShopTableSql);
        String query2 = "insert into SHOP values('1','1')";
        getWritableDatabase().execSQL(query2);

        getWritableDatabase().execSQL(createShopReceipt);
        String query3 = "insert into Receipts values('1','123')";
        getWritableDatabase().execSQL(query3);
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
    public void setReceipt(String receipt_no,String dateToday,String Receipt_prev_no) /*saajan*/
    {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("receipt_id", receipt_no);
        values.put("date_today",dateToday);
        db.update("Receipts", values, "receipt_id" + "=" + Receipt_prev_no, null);
        db.close();
    }
    public String getReceipt()  /*saajan*/
    {
        String sql = "SELECT * from Receipts";
        Cursor c = getWritableDatabase().rawQuery(sql,null);
        String receipt_id="",dateToday="";
        c.moveToFirst();
        receipt_id = c.getString(0);
        dateToday =  c.getString(1);
        return receipt_id + ',' + dateToday;
    }

    public void setShop(String n)
    {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("shop_number", n);
        db.update("SHOP", values, "shop_id=" + "1" , null);
        db.close();
    }
    public String getShop()
    {
        String sql = "SELECT shop_number FROM SHOP WHERE shop_id=1;";
        Cursor c = getWritableDatabase().rawQuery(sql,null);
        String shop_number=" ";
        c.moveToFirst();
        shop_number=c.getString(c.getColumnIndex("shop_number"));
        return shop_number;

    }
    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
