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

    final String LOG_TAG = "myLogs";
    /*Используемые переменные*/
    int odoVal;
    Button start, stop, alarm;
    TextView carNumber, odoValue;
    DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        /*Привязка переменных к полям XML по ID*/
        carNumber = (TextView) findViewById(R.id.NumberCarText);
        odoValue = (TextView) findViewById(R.id.OdoValueText);

        start = (Button) findViewById(R.id.BtnStart);
        start.setOnClickListener(this);

        stop = (Button) findViewById(R.id.BtnStop);
        stop.setOnClickListener(this);

        alarm = (Button) findViewById(R.id.BtnAlert);
        alarm.setOnClickListener(this);

        dataView();//функция отображения данных в активити
    }

    /*события по нажалию кнопок*/
    @Override
    public void onClick(View v) {
        /*конструкция свитч по ID кнопки*/
        switch (v.getId()) {      //получаем ID кнопки

            case R.id.BtnStart: //Нажатие старт - запуск счетчика километража
                break;

            case R.id.BtnStop: //Нажатие стоп - останов счетчика
                break;

            case R.id.BtnAlert: //Нажатие алерт - вывод бортового журнала
                Intent intentBoardJournal = new Intent(ProfileActivity.this, BoardJournalActivity.class);
                intentBoardJournal.putExtra("odoValue", odoVal);
                startActivity(intentBoardJournal);
                break;
        }
    }

    //Функция отображения данных авторизованного пользователя
    void dataView() {
        dbHelper = new DBHelper(this);                  //создаем объект класса DBHelper
        SQLiteDatabase db = dbHelper.getWritableDatabase();     //Метод getWritable... создает БД или, если она создана, то предоставляет доступ на запись/чтение
        Cursor c = db.query("mytable", null, null, null, null, null, null); //создаем таблицу в БД

        /*задаем интовые переменные, которые соответствуют номеру колонок*/
        int carNumberColIndex = c.getColumnIndex("carNumber");
        int odoValueColIndex = c.getColumnIndex("odoValue");
        /*получаем значение id строки из мэйн активити*/
        Intent intent1 = getIntent();
        String row = intent1.getStringExtra("row");
        Integer rowValue = new Integer(row) - 1;//преобразуем string id в int и вычитаем 1 для получения номера строки в таблице БД

        c.moveToPosition(rowValue);//устанавливаем курсор на нужную нам строку
        Log.d(LOG_TAG, c.getString(carNumberColIndex));
        Log.d(LOG_TAG, c.getString(odoValueColIndex));

        //Выводим данные профиля в поля активити (Номер машины и значение одометра)
        carNumber.setText(String.valueOf(c.getString(carNumberColIndex)));
        odoValue.setText(String.valueOf(c.getString(odoValueColIndex)));
        odoVal = c.getInt(odoValueColIndex);
        c.close();//закрываем курсор
    }
}