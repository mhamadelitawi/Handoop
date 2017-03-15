package com.handoop.mhamad.handoop.Tools;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.handoop.mhamad.handoop.Models.Result;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by Mhamad on 19-Jan-17.
 */

public class MySQLLite extends SQLiteOpenHelper {


    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "handoop";



    public MySQLLite(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            String createTableQuery =
            "CREATE TABLE IF NOT EXISTS history (guid varchar(100) NOT NULL PRIMARY KEY, task varchar(50) NOT NULL, title varchar(100) NOT NULL, preview text NOT NULL, pathresult varchar(100) NOT NULL, date varchar(50) NOT NULL, time varchar(50) NOT NULL )";
            db.execSQL(createTableQuery);
        }catch (Exception e) {}
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
      db.execSQL("DROP TABLE IF EXISTS history" );
        onCreate(db);
    }





    public void addHistory(Result result)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("guid", result.guid);
        values.put("task", result.task);
        values.put("title", result.title);
        values.put("preview", result.preview);
        values.put("pathresult", result.pathresult);
        values.put("date", result.date);
        values.put("time", result.time);
        db.insert("history" , null, values);
        db.close();
    }



    public ArrayList<Result> getAllHistories()
    {
        ArrayList <Result> ar = new ArrayList<>();
        try {
            String selectQuery = "SELECT  * FROM history";
            SQLiteDatabase db = this.getWritableDatabase();

            Cursor cursor = db.rawQuery(selectQuery, null);


            // looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                do {
                    ar.add(0 ,cursorToResult(cursor));
                } while (cursor.moveToNext());
            }
            db.close();
        }catch (Exception e)
        {
            String sx = "";
        }
        return ar;
    }



    public static Result cursorToResult(Cursor cursor)
    {

        try {
            return new  Result(cursor.getString(0) , cursor.getString(1) , cursor.getString(2) , cursor.getString(3)   , cursor.getString(4) , cursor.getString(5) , cursor.getString(6));
        }catch (Exception e){}
        return null;
    }





}