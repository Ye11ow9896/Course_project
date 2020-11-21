package com.example.course_project;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class ProfileActivity extends Activity implements OnClickListener {

    Button start, stop, alarm;
    TextView carNumber, odoValue;
    DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        carNumber = (TextView) findViewById(R.id.NumberCarText);
        odoValue = (TextView) findViewById(R.id.OdoValueText);

        start = (Button) findViewById(R.id.BtnStart);
        stop = (Button) findViewById(R.id.BtnStop);
        alarm = (Button) findViewById(R.id.BtnAlert);

        DataView();
    }

    @Override
    public void onClick(View v) {

        switch(v.getId()) {
            case R.id.BtnStart :
                break;
            case R.id.BtnStop :
                break;
            case R.id.BtnAlert :
                break;
        }
    }

    //Функция отображения данных
    void DataView() {
        dbHelper = new DBHelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor c = db.query("mytable", null, null, null, null, null, null);

        if (c.moveToFirst()) {

            // определяем номера столбцов по имени в выборке
            int idColIndex = c.getColumnIndex("id");
            int userNameColIndex = c.getColumnIndex("userName");
            int carModelColIndex = c.getColumnIndex("carModel");
            int carNumberColIndex = c.getColumnIndex("carNumber");
            int odoValueColIndex = c.getColumnIndex("odoValue");
            int passwordColIndex = c.getColumnIndex("password");
            do {
                // получаем значения по номерам столбцов и пишем все в лог
                carNumber.setText(String.valueOf(c.getString(carNumberColIndex)));
                odoValue.setText(String.valueOf(c.getString(odoValueColIndex)));
                // переход на следующую строку
                // а если следующей нет (текущая - последняя), то false - выходим из цикла
            } while (c.moveToNext());
        } else
            c.close();
    }
}