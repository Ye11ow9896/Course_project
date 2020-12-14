package com.example.course_project;


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

    final String LOG_TAG = "myLogs";
    MainAlertDialog mainAlertDialog = new MainAlertDialog();//объект всплывающего окна ошибки ввода данных при авторизации
    /*Переменные класса*/
    DBHelper dbHelper;
    private Button BtnLoginProfile, BtnCreateProfile;
    private EditText textName, textPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);//привязка в лойауту

        /*привязка к кнопкам и полям для заполнения по ID*/
        BtnLoginProfile = (Button) findViewById(R.id.btnLoginProfile);
        BtnLoginProfile.setOnClickListener(this);//возвращает обратку при нажатии

        BtnCreateProfile = (Button) findViewById(R.id.btnCreateProfile);
        BtnCreateProfile.setOnClickListener(this);//возвращает обратку при нажатии

        textName = (EditText) findViewById(R.id.editTextTextPersonName);
        textPassword = (EditText) findViewById(R.id.editTextTextPassword);


        dbHelper = new DBHelper(this);//создаем объекс класса

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
                SQLiteDatabase db = dbHelper.getWritableDatabase(); //Метод getWritable... создает БД или, если она создана, то предоставляет доступ на запись/чтение
                Cursor c = db.query("mytable", null, null, null, null, null, null);//создаем таблицу в БД
                /*переменные для сравнения при поиске данных пароля и номера авто при авторизации*/
                String name = textName.getText().toString();
                String pass = textPassword.getText().toString();
                /*задаем интовые переменные, которые соответствуют номеру колонок*/
                int carNumberColIndex = c.getColumnIndex("carNumber");
                int passwordColIndex = c.getColumnIndex("password");
                int idColIndex = c.getColumnIndex("id");
                /*Авторизация*/
                if (c != null) {//курсор не нулл
                    if (c.moveToFirst()) {//если таблица не пустая - двигаем курсор на первую строку таблицы

                        do
                        {//проверяем есть ли введенные данные в нашей БД и прогоняем по все базе, пока не найдем совпадения
                            Log.d(LOG_TAG, c.getString(carNumberColIndex));
                            Log.d(LOG_TAG, c.getString(passwordColIndex));
                            //если пароль и номер авто совпадают с данными из БД то переходим в активити профиля
                            if (name.equals(c.getString(carNumberColIndex)) && pass.equals(c.getString(passwordColIndex))) {
                                checkEndTable = true;//когда нашлись данные.
                                Intent intent1 = new Intent(MainActivity.this, ProfileActivity.class);
                                intent1.putExtra("row", c.getString(idColIndex));//Передача данных id из базы в др активити
                                startActivity(intent1);//переход в активити
                                break;
                            }

                        } while (c.moveToNext());//конец таблица БД
                    }
                    if (!(c.moveToNext() || checkEndTable)) { /*Если не нашлось имя, пароль и курсор равен нулю (не ноль), то: */
                        mainAlertDialog.show(getSupportFragmentManager(), "custom");
                        checkEndTable = false;
                    }
                    c.close();//закрытие курсора
                } else
                    Log.d(LOG_TAG, "Cursor is null");
                dbHelper.close();//закрываем объект БД
                break;
        }
    }
}