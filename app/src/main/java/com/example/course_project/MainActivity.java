package com.example.course_project;


import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.view.View.OnClickListener;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements OnClickListener {

    private WorkWithDB workWithDBMain;
    /*Переменные класса*/
    private Button BtnLoginProfile, BtnCreateProfile;
    private EditText textName, textPassword;
    final String LOG_TAG = "myLogs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);//привязка в лойауту

        /*привязка к кнопкам и полям для заполнения по ID*/
        BtnLoginProfile = (Button) findViewById(R.id.btnLoginProfile);
        BtnLoginProfile.setOnClickListener(this);

        BtnCreateProfile = (Button) findViewById(R.id.btnCreateProfile);
        BtnCreateProfile.setOnClickListener(this);

        textName = (EditText) findViewById(R.id.editTextTextPersonName);
        textPassword = (EditText) findViewById(R.id.editTextTextPassword);

        workWithDBMain = new WorkWithDB(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            //По нажатию кнопки переходим в активити создание профиля
            case R.id.btnCreateProfile:
                Intent intent = new Intent(MainActivity.this, CreateProfile.class);
                startActivity(intent);
                break;
            //По нажатию кнопки переходим в активити профиля
            case R.id.btnLoginProfile:
                boolean checkEndTable = false;//переменная проверки конца списка. Если тру, то данные нашлись в таблице, если нет, то остается фолс
                /*переменные для сравнения при поиске данных пароля и номера авто при авторизации*/
                String name = textName.getText().toString();
                String pass = textPassword.getText().toString();
                /*инициализируем курсор*/
                Cursor cur = workWithDBMain.getAccessToDB().query("mytable", null, null, null, null, null, null);//создаем таблицу в БД

                /*задаем интовые переменные, которые соответствуют номеру колонок*/
                int odoValueColIndex = cur.getColumnIndex("odoValue");
                int carNumberColIndex = cur.getColumnIndex("carNumber");
                int passwordColIndex = cur.getColumnIndex("password");
                int idColIndex = cur.getColumnIndex("id");
                /*Авторизация*/

                if ( cur!= null) {//курсор не нулл
                    if (cur.moveToFirst()) {//если таблица не пустая - двигаем курсор на первую строку таблицы

                        do
                        {//проверяем есть ли введенные данные в нашей БД и прогоняем по все базе, пока не найдем совпадения
                            Log.d(LOG_TAG, cur.getString(carNumberColIndex));
                            Log.d(LOG_TAG, cur.getString(passwordColIndex));
                            //если пароль и номер авто совпадают с данными из БД то переходим в активити профиля
                            if (name.equals(cur.getString(carNumberColIndex)) &&
                                    pass.equals(cur.getString(passwordColIndex))) {
                                checkEndTable = true;//когда нашлись данные.
                                workWithDBMain.setValueIdMytable(cur.getInt(idColIndex));//передаем значение айди в переменную класса дбхелпер
                                workWithDBMain.setOdoValue((cur.getInt(odoValueColIndex)));
                                /*переход в др активити*/
                                Intent intent1 = new Intent(MainActivity.this, ProfileActivity.class);
                                startActivity(intent1);//переход в активити
                                break;
                            }
                        } while (cur.moveToNext());//конец таблица БД
                    }
                    if (!(cur.moveToNext() || checkEndTable)) { /*Если не нашлось имя, пароль и курсор равен нулю (не ноль), то: */
                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                        builder.setTitle("ошибка")
                                .setMessage("неверно введены данные")
                                .create();
                        AlertDialog alert = builder.create();
                        alert.show();
                        checkEndTable = false;
                    }
                    cur.close();//закрытие курсора
                } else
                    Log.d(LOG_TAG, "Cursor is null");
                cur.close();//закрываем объект БД
                break;
        }
    }
}