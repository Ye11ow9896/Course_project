package com.example.course_project;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class WorkWithDB extends DBHelper {

    static int idValueMytable, idValueMaintenace, odoValue;//переменная для айди таблицы mytable и maintenance
    DBHelper dbHelper;

    public WorkWithDB(Context context) {
        super(context);
        dbHelper = new DBHelper(context);
    }


    /*сеттеры*/
    public void setValueIdMaintenance(int idValueMaintenace) {//метод получения айди после регистрации и авторизации
        this.idValueMaintenace = idValueMaintenace-1;
    }
    public void setValueIdMytable(int idValueMytable) {
        this.idValueMytable = idValueMytable-1;
    }
    public void setOdoValue(int odoValue) {
        this.odoValue = odoValue;
    }
    /*геттеры*/
    public SQLiteDatabase getAccessToDB() {
        SQLiteDatabase db = dbHelper.getWritableDatabase(); //Метод getWritable... создает БД или, если она создана, то предоставляет доступ на запись/чтение
        return db;
    }
    public int getIdMaintenance() {
        return idValueMytable;
    }
    public int getIdMytable() {
        return idValueMytable;
    }
}
