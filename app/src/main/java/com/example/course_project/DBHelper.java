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

    int idMytable, idValueMaintenace;//переменная для айди таблицы mytable и maintenance

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
    private Context myContext;

    @Override
    public void onCreate(SQLiteDatabase db) {
       Log.d(LOG_TAG, "--- onCreate database ---");
       // создаем таблицу с полями
        ContentValues cv = new ContentValues();

       db.execSQL("create table mytable ("
               + "id integer primary key autoincrement,"
               + "carNumber text,"
               + "odoValue INTEGER,"
               + "userName text,"
               + "carModel text,"
               + "carMark text,"
               + "password text"
               + ");");
       /*тестовый профиль*/
        cv.clear();
        cv.put("carNumber", "a");
        cv.put("odoValue", 199999);
        cv.put("userName", "Сергей Коклюш");
        cv.put("carModel", "Lada");
        cv.put("carMark", "Granta");
        cv.put("password", "a");
        db.insert("mytable", null, cv);
        /*конец тест профиля*/

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
        /*Подготовка данных для загрузку в БД*/
        String auto [] =            {"Volkswagen", "Volkswagen", "Lada"     };
        String model [] =           {"Polo",       "Golf",       "Granta"   };
        int yearOfIssue[] =         {2018,         2019,          2015      };
        int brakePadChange [] =     {15000,        16000,         14000     };
        int brakeDiscChange [] =    {25000,        28000,         23000     };
        int motorOilChange [] =     {10000,        11000,         10000     };
        /*загрузка данных в таблицу мэинтенс*/
        for( int i = 0;i < auto.length; i++)
        {
            cv.put("auto", auto[i]);
            cv.put("model", model[i]);
            cv.put("yearOfIssue", yearOfIssue[i]);
            cv.put("brakePadChange", brakePadChange[i]);
            cv.put("brakeDiscChange", brakeDiscChange[i]);
            cv.put("motorOilChange", motorOilChange[i]);
        }
        db.insert("maintenance", null, cv);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

}
