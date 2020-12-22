package com.example.course_project;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.view.View.OnClickListener;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

public class ProfileActivity extends Activity implements OnClickListener {

    /*Используемые переменные*/
    private Button alarm, settings;
    private TextView carNumber, odoValue;
    private WorkWithDB workWithDBProfile;
    private ToggleButton toggle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        /*Привязка переменных к полям XML по ID*/
        carNumber = (TextView) findViewById(R.id.NumberCarText);
        odoValue = (TextView) findViewById(R.id.OdoValueText);

        alarm = (Button) findViewById(R.id.BtnAlert);
        alarm.setOnClickListener(this);

        settings = (Button)findViewById(R.id.btnSettings);
        settings.setOnClickListener(this);

        toggle = (ToggleButton)findViewById(R.id.BtnStartStop);

        workWithDBProfile = new WorkWithDB(this);

        dataView();//функция отображения данных в активити
    }

    /*события по нажалию кнопок*/
    @Override
    public void onClick(View v) {
        /*конструкция свитч по ID кнопки*/
        switch (v.getId()) {      //получаем ID кнопки

            case R.id.BtnStartStop:
                toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (buttonView.isChecked()) {
                            Toast.makeText(getApplicationContext(), "Checked", Toast.LENGTH_SHORT).show();
                            //КНОПКА ВКЮСЕНА
                            //КНОПКА ВКЮСЕНА
                            //КНОПКА ВКЮСЕНА
                            //КНОПКА ВКЮСЕНА
                        } else {
                            Toast.makeText(getApplicationContext(), "Not checked", Toast.LENGTH_SHORT).show();
                            //КНОПКА ВЫКЮСЕНА
                            //КНОПКА ВЫКЮСЕНА
                            //КНОПКА ВЫКЮСЕНА
                            //КНОПКА ВЫКЮСЕНА
                        }
                    }
                });
                break;

            case R.id.BtnAlert: //Нажатие алерт - вывод бортового журнала
                Intent intentBoardJournal = new Intent(ProfileActivity.this, BoardJournalActivity.class);
                startActivity(intentBoardJournal);
                break;
            case R.id.btnSettings: //переход в настройки
                Intent intentSettings = new Intent(ProfileActivity.this, SettingsActivity.class);
                startActivity(intentSettings);
                break;
        }
    }

    //Функция отображения данных авторизованного пользователя
    void dataView() {
        Cursor cur = workWithDBProfile.getAccessToDB().query("mytable", null, null, null, null, null, null);//создаем таблицу в БД
        /*получаем значение id строки из мэйн активити*/
        cur.moveToPosition(workWithDBProfile.getCursorPositionMytable());//устанавливаем курсор на нужную нам строку
        //Выводим данные профиля в поля активити (Номер машины и значение одометра)
        carNumber.setText(String.valueOf(cur.getString(workWithDBProfile.getFieldOfDB("mytable", "carNumber"))));
        odoValue.setText(String.valueOf(cur.getString(workWithDBProfile.getFieldOfDB("mytable", "odoValue"))));
        cur.close();//закрываем курсор
    }
}