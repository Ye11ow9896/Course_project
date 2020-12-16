package com.example.course_project;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class ProfileActivity extends Activity implements OnClickListener {

    final String LOG_TAG = "myLogs";
    /*Используемые переменные*/
    private Button start, stop, alarm, settings;
    private TextView carNumber, odoValue;
    private WorkWithDB workWithDBProfile;

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

        settings = (Button)findViewById(R.id.btnSettings);
        settings.setOnClickListener(this);

        workWithDBProfile = new WorkWithDB(this);

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
                //intentBoardJournal.putExtra("odoValue", odoVal);
                startActivity(intentBoardJournal);
                break;
            case R.id.btnSettings: //по нажатию передаем значение айдишника строки с данными из таблицы
                Intent intentSettings = new Intent(ProfileActivity.this, SettingsActivity.class);
                //intentSettings.putExtra("rowValue", rowValue);
                startActivity(intentSettings);
                break;
        }
    }

    //Функция отображения данных авторизованного пользователя
    void dataView() {
        Cursor cur = workWithDBProfile.getAccessToDB().query("mytable", null, null, null, null, null, null);//создаем таблицу в БД

        /*задаем интовые переменные, которые соответствуют номеру колонок*/
        int carNumberColIndex = cur.getColumnIndex("carNumber");
        int odoValueColIndex = cur.getColumnIndex("odoValue");
        int idColIndex = cur.getColumnIndex("id");

        /*получаем значение id строки из мэйн активити*/
        cur.moveToPosition(workWithDBProfile.getIdMytable());//устанавливаем курсор на нужную нам строку
        Log.d(LOG_TAG, cur.getString(carNumberColIndex));
        Log.d(LOG_TAG, cur.getString(odoValueColIndex));
        Log.d(LOG_TAG, cur.getString(idColIndex));

        //Выводим данные профиля в поля активити (Номер машины и значение одометра)
        carNumber.setText(String.valueOf(cur.getString(carNumberColIndex)));
        odoValue.setText(String.valueOf(cur.getString(odoValueColIndex)));
        cur.close();//закрываем курсор
    }
}