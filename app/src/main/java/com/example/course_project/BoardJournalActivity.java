package com.example.course_project;


import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View.OnClickListener;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.IOException;

public class BoardJournalActivity extends AppCompatActivity implements OnClickListener {

    int odoValue;
    ProgressBar progressBar1, progressBar2, progressBar3;
    TextView brakePadChange, motorOilChange, brakeDiscChange;
    DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.board_journal_activity);

        progressBar1 = (ProgressBar) findViewById(R.id.progressBar1);
        progressBar2 = (ProgressBar) findViewById(R.id.progressBar2);
        progressBar3 = (ProgressBar) findViewById(R.id.progressBar3);

        brakeDiscChange = (TextView) findViewById(R.id.textView3);
        motorOilChange = (TextView) findViewById(R.id.textView2);
        brakePadChange = (TextView) findViewById(R.id.textView1);

        /*получаем значение одометра с профиль активити*/
        Intent intentBoardJournal = getIntent();
        odoValue = intentBoardJournal.getIntExtra("odoValue", 0);

        ViewMaintenance();
    }

    @Override
    public void onClick(View v) {

    }

    void ViewMaintenance() {     //отображжение активити(прогрессбар и значения)

        int brakePadChange, brakeDiscChange, motorOilChange;

        dbHelper = new DBHelper(this);                  //создаем объект класса DBHelper
        SQLiteDatabase db = dbHelper.getWritableDatabase();     //Метод getWritable... создает БД или, если она создана, то предоставляет доступ на запись/чтение
        Cursor c = db.query("maintenance", null, null, null, null, null, null); //создаем таблицу в БД

        if (c.moveToFirst()) {
            // определяем номера столбцов по имени в выборке
            int autoColIndex = c.getColumnIndex("auto");
            int modelColIndex = c.getColumnIndex("model");
            int yearOfIssueColIndex = c.getColumnIndex("yearOfIssue");
            int brakePadChangeColIndex = c.getColumnIndex("brakePadChange");
            int brakeDiscChangeColIndex = c.getColumnIndex("brakeDiscChange");
            int motorOilChangeColIndex = c.getColumnIndex("motorOilChange");
            do {
                // получаем значения по номерам
                brakePadChange = c.getInt(brakePadChangeColIndex);
                brakeDiscChange = c.getInt(brakeDiscChangeColIndex);
                motorOilChange = c.getInt(motorOilChangeColIndex);
                // а если следующей нет (текущая - последняя), то false - выходим из цикла
            } while (c.moveToNext());
            c.close();

            //Выводим данные профиля в поля активити (Номер машины и значение одометра)
            progressBar1.setMax(brakePadChange);//макс значение прогрессбар
            progressBar1.setProgress(odoValue % brakePadChange);//значение для отображения прогресс бар. Далее по аналогии
            progressBar2.setMax(motorOilChange);
            progressBar2.setProgress(odoValue % motorOilChange);
            progressBar3.setMax(brakeDiscChange);
            progressBar3.setProgress(odoValue % brakeDiscChange);

        }
    }
}
