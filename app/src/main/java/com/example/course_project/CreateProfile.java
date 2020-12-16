package com.example.course_project;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import java.io.IOException;

public class CreateProfile extends Activity implements OnClickListener {
    /*
     * Переменные профиля:
     * userName - имя пользователя
     * carModel - Марка и модель ТС
     * numberOfCar - гос номер автомобиля
     * odoValue - показание одометра одометра
     */


    final String LOG_TAG = "myLogs";

    boolean checkEndTable;

    Button btnCreate, btnBack;
    EditText etCarModel, etUserName, etCarNumber, etOdoValue, etPassword,etCarMark;

    WorkWithDB workWithDBCreateProf;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_profile);

        btnCreate = (Button) findViewById(R.id.BtnCreate);
        btnCreate.setOnClickListener(this);

        btnBack = (Button) findViewById(R.id.BtnBack);
        btnBack.setOnClickListener(this);


        etCarModel = (EditText) findViewById(R.id.CarModel);
        etCarMark = (EditText) findViewById(R.id.CarMark);
        etUserName = (EditText) findViewById(R.id.UserName);
        etOdoValue = (EditText) findViewById(R.id.OdoValue);
        etCarNumber = (EditText) findViewById(R.id.NumberOfCar);
        etPassword = (EditText) findViewById(R.id.Password);

        // создаем объект для создания и управления версиями БД
        workWithDBCreateProf = new WorkWithDB(this);
    }


    @Override
    public void onClick(View v) {

        // создаем объект для данных
        ContentValues cv = new ContentValues();
        // получаем данные из полей ввода
        String userName = etUserName.getText().toString();
        String carMark = etCarMark.getText().toString();
        String carModel = etCarModel.getText().toString();
        Integer odoValue  = new Integer(etOdoValue.getText().toString());
        String carNumber = etCarNumber.getText().toString();
        String password = etPassword.getText().toString();

        Cursor cur1 = workWithDBCreateProf.getAccessToDB().query("maintenance", null, null, null, null, null, null);//создаем таблицу в БД

        /*ищем марку и модель авто по БД в таблице мэинтенс*/
        if (cur1.moveToFirst()) {
            // определяем номера столбцов по имени в выборке
            int id_ColIndex = cur1.getColumnIndex("id");
            int autoColIndex = cur1.getColumnIndex("auto");
            int modelColIndex = cur1.getColumnIndex("model");
            int yearOfIssueColIndex = cur1.getColumnIndex("yearOfIssue");
            int brakePadChangeColIndex = cur1.getColumnIndex("brakePadChange");
            int brakeDiscChangeColIndex = cur1.getColumnIndex("brakeDiscChange");
            int motorOilChangeColIndex = cur1.getColumnIndex("motorOilChange");

            do {
                /*проверяем, есть ли такая модель и марка автомобиля в БД*/
                if (carMark.equals(cur1.getString(modelColIndex))
                        && carModel.equals(cur1.getString(autoColIndex))) {
                    checkEndTable = true;//когда нашлись данные.
                    workWithDBCreateProf.setValueIdMaintenance(cur1.getInt(id_ColIndex));//передаем значение айди таблици мэинтенс
                }
                // получаем значения по номерам столбцов и пишем все в лог
                Log.d(LOG_TAG,
                        "ID = " + cur1.getInt(id_ColIndex) +
                                ", auto = " + cur1.getString(autoColIndex) +
                                ", model = " + cur1.getString(modelColIndex)+
                                ", year = " + cur1.getString(yearOfIssueColIndex)+
                                ", brakePad = " + cur1.getString(brakePadChangeColIndex) +
                                ", brakeDisc = " + cur1.getString(brakeDiscChangeColIndex) +
                                ", motorOil= " + cur1.getString(motorOilChangeColIndex));


                // переход на следующую строку
                // а если следующей нет (текущая - последняя), то false - выходим из цикла
            } while (cur1.moveToNext());
        } else
            Log.d(LOG_TAG, "0 rows");
        if (!(cur1.moveToNext() || checkEndTable)) { //*Если не нашлась авто из БД
            AlertDialog.Builder builder = new AlertDialog.Builder(CreateProfile.this);
            builder.setTitle("ошибка")
                    .setMessage("Авто нет в базе данных")
                    .create();
            AlertDialog alert = builder.create();
            alert.show();
        }
        cur1.close();

        if (checkEndTable) {
            switch (v.getId()) {
                case R.id.BtnCreate:
                    Log.d(LOG_TAG, "--- Insert in mytable: ---");

                    // подготовим данные для вставки в виде пар: наименование столбца - значение
                    cv.put("userName", userName);
                    cv.put("carModel", carModel);
                    cv.put("carMark", carMark);
                    cv.put("odoValue", odoValue);
                    cv.put("carNumber", carNumber);
                    cv.put("password", password);

                    // вставляем запись и получаем ее ID
                    long rowID = workWithDBCreateProf.getAccessToDB().insert("mytable", null, cv);
                    Log.d(LOG_TAG, "row inserted, ID = " + rowID);
                    Log.d(LOG_TAG, "--- Rows in mytable: ---");

                    Cursor cur = workWithDBCreateProf.getAccessToDB().query("maintenance", null, null, null, null, null, null);//создаем таблицу в БД

                    // ставим позицию курсора на первую строку выборки
                    // если в выборке нет строк, вернется false
                    if (cur.moveToFirst()) {
                        // определяем номера столбцов по имени в выборке
                        int idColIndex = cur.getColumnIndex("id");
                        int userNameColIndex = cur.getColumnIndex("userName");
                        int carModelColIndex = cur.getColumnIndex("carModel");
                        int carMarkColIndex = cur.getColumnIndex("carMark");
                        int carNumberColIndex = cur.getColumnIndex("carNumber");
                        int odoValueColIndex = cur.getColumnIndex("odoValue");
                        int passwordColIndex = cur.getColumnIndex("password");

                        do {
                            // получаем значения по номерам столбцов и пишем все в лог
                            Log.d(LOG_TAG,
                                    "ID = " + cur.getInt(idColIndex) +
                                            ", userName = " + cur.getString(userNameColIndex) +
                                            ", carModel = " + cur.getString(carModelColIndex) +
                                            ", carMark = " + cur.getString(carMarkColIndex) +
                                            ", odoValue = " + cur.getString(odoValueColIndex) +
                                            ", carNumber = " + cur.getString(carNumberColIndex) +
                                            ", password = " + cur.getString(passwordColIndex));

                            Intent intent1 = new Intent(CreateProfile.this, ProfileActivity.class);
                            intent1.putExtra("row", cur.getString(idColIndex));//Передача данных id из базы в др активити
                            startActivity(intent1);//переход в активити
                            // переход на следующую строку
                            // а если следующей нет (текущая - последняя), то false - выходим из цикла
                        } while (cur.moveToNext());
                    } else
                        Log.d(LOG_TAG, "0 rows");
                    cur.close();
                    break;
                case R.id.BtnBack:
                    Intent intent = new Intent(CreateProfile.this, MainActivity.class);
                    startActivity(intent);
                    break;
            }
            // закрываем подключение к БД
            workWithDBCreateProf.close();
        }
    }
}
