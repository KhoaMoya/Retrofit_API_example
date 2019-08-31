package com.khoa.retrofit_api_example.Control;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.khoa.retrofit_api_example.Model.SimplePing;

import java.util.ArrayList;

public class MyDataBase extends SQLiteOpenHelper {

    private static String DataBase_Name = "MyDataBase";
    private static String Table_Ping_Name = "PingTable";
    private static String ID_Ping = "id";
    private static String Value_Ping = "value";
    private static String Address_Ping = "address";
    private static String Id_User_Ping = "id_user";
    private static String Time_Ping = "time";
    private static String Save_Online = "save";
    private static long lastId;

    public MyDataBase(Context context) {
        super(context, DataBase_Name, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String creatPingTable = "create table if not exists " + Table_Ping_Name + " ( " +
                ID_Ping + " integer primary key autoincrement, " +
                Value_Ping + " real, " +
                Address_Ping + " text, " +
                Id_User_Ping + " integer, " +
                Time_Ping + " text, " +
                Save_Online + " text )";
        db.execSQL(creatPingTable);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String dropPingTable = "drop table if exists " + Table_Ping_Name;
        db.execSQL(dropPingTable);
        onCreate(db);
    }

    public void savePing(float value, String address, int idUser, boolean saveOnline, String time) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(Value_Ping, value);
        cv.put(Address_Ping, address);
        cv.put(Id_User_Ping, idUser);
        cv.put(Save_Online, String.valueOf(saveOnline));
        cv.put(Time_Ping, time);
        db.insert(Table_Ping_Name, null, cv);
    }

    public ArrayList<SimplePing> getArrayMissPing(String time) {
        SQLiteDatabase db = getReadableDatabase();
        int idMax = 0, idServer = 0;
        ArrayList<SimplePing> arrMissPing = new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT MAX(" + ID_Ping + ") FROM " + Table_Ping_Name, null);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            idMax = cursor.getInt(0);
        } else {
            return null;
        }
        cursor = db.rawQuery("SELECT " + ID_Ping + " FROM " + Table_Ping_Name + " WHERE " + Time_Ping + " = " + time, null);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            idServer = cursor.getInt(0);
        } else {
            return null;
        }
        Log.e("Loi", idMax+"");
        Log.e("Loi", idServer+"");
        if (idServer < idMax) {
            for (int i = idServer; i <= idMax; i++) {
                cursor = db.rawQuery("SELECT * FROM " + Table_Ping_Name + " WHERE " + ID_Ping + " = " + String.valueOf(i), null);
                if (cursor.getCount() > 0) {
                    cursor.moveToFirst();
                    arrMissPing.add(new SimplePing(cursor.getFloat(1), cursor.getString(2), cursor.getString(4)));
                } else {
                    return null;
                }
            }
        }
        cursor.close();
        db.close();
        return arrMissPing;

    }


}
