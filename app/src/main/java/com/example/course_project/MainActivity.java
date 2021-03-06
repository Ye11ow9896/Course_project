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

            case R.id.btnLoginProfile://пытаемся авторизоваться
                if(workWithDBMain.logIn(textName.getText().toString(), textPassword.getText().toString())) {//если введенные имя и пароль есть в БД
                    String carMark = workWithDBMain.getFieldOfDB("mytable", "carMark",
                            workWithDBMain.getCursorPositionMytable());
                    String carModel = workWithDBMain.getFieldOfDB("mytable", "carModel",
                            workWithDBMain.getCursorPositionMytable());
                    workWithDBMain.searchCarInDB(carMark, carModel);//ищем авто в базе данных и берем курсор
                    Intent intent1 = new Intent(MainActivity.this, ProfileActivity.class);//переходим в активити профиля
                    startActivity(intent1);
                } else {//иначе выводим алерт
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                            builder.setTitle("ошибка")
                                    .setMessage("неверно введены данные")
                                    .create();
                            AlertDialog alert = builder.create();
                            alert.show();
                }
        }
    }
}