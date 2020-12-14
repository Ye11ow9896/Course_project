package com.example.course_project;

import android.content.ContentValues;
import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

class DBHelper extends SQLiteOpenHelper {


    public DBHelper(Context context) {
        // конструктор суперкласса
        super(context, DB_NAME, null, SCHEMA);
        this.myContext = context;
        DB_PATH = context.getFilesDir().getPath() + DB_NAME;
    }

    final String LOG_TAG = "myLogs";

    private boolean mNeedUpdate = false;
    private static String DB_PATH;
    private static String DB_NAME = "myDB.db";
    private static final int SCHEMA = 1; // версия базы данных
    //static final String TABLE = "Maintenance"; // название таблицы в бд
    //// названия столбцов
    //static final String COLUMN_AUTO = "Auto";
    //static final String COLUMN_MODEL = "Model";
    //static final String COLUMN_YEAR = "YearOfIssue";
    //static final String COLUMN_PADCHANGE = "BrakePadChange";
    //static final String COLUMN_DISCCHANGE = "BrakeDiscChange";
    //static final String COLUMN_MOILCHANGE = "MotorOilChange";
    private Context myContext;



    @Override
    public void onCreate(SQLiteDatabase db) {
       Log.d(LOG_TAG, "--- onCreate database ---");
       // создаем таблицу с полями
       db.execSQL("create table mytable ("
               + "id integer primary key autoincrement,"
               + "carNumber text,"
               + "odoValue INTEGER,"
               + "userName text,"
               + "carModel text,"
               + "carMark text,"
               + "password text"
               + ");");

        ContentValues cv = new ContentValues();
        // создаем таблицу с полями по ТО автомобилей
        db.execSQL("create table maintenance ("
                + "id integer primary key autoincrement,"
                + "auto text,"
                + "model text,"
                + "yearOfIssue text,"
                + "brakePadChange INTEGER,"
                + "brakeDiscChange INTEGER,"
                + "motorOilChange INTEGER"
                + ");");
        cv.clear();
        cv.put("auto", "Volkswagen");
        cv.put("model", "Polo");
        cv.put("yearOfIssue", 2018);
        cv.put("brakePadChange", 15000);
        cv.put("brakeDiscChange", 25000);
        cv.put("motorOilChange", 10000);
        db.insert("maintenance", null, cv);
        cv.put("auto", "Volkswagen");
        cv.put("model", "Golf");
        cv.put("yearOfIssue", 2019);
        cv.put("brakePadChange", 16000);
        cv.put("brakeDiscChange", 28000);
        cv.put("motorOilChange", 11000);
        db.insert("maintenance", null, cv);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }


}
