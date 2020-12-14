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

    DBHelper dbHelper;

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
        dbHelper = new DBHelper(this);
    }


    @Override
    public void onClick(View v) {

        // создаем объект для данных
        ContentValues cv = new ContentValues();
        // подключаемся к БД
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        // получаем данные из полей ввода
        String userName = etUserName.getText().toString();
        String carMark = etCarMark.getText().toString();
        String carModel = etCarModel.getText().toString();
        Integer odoValue  = new Integer(etOdoValue.getText().toString());
        String carNumber = etCarNumber.getText().toString();
        String password = etPassword.getText().toString();

        /*ищем марку и модель авто по БД в таблице мэинтенс*/
        Cursor cur = db.query("maintenance", null, null, null, null, null, null);
        if (cur.moveToFirst()) {
            // определяем номера столбцов по имени в выборке
            int id_ColIndex = cur.getColumnIndex("id");
            int autoColIndex = cur.getColumnIndex("auto");
            int modelColIndex = cur.getColumnIndex("model");
            int yearOfIssueColIndex = cur.getColumnIndex("yearOfIssue");
            int brakePadChangeColIndex = cur.getColumnIndex("brakePadChange");
            int brakeDiscChangeColIndex = cur.getColumnIndex("brakeDiscChange");
            int motorOilChangeColIndex = cur.getColumnIndex("motorOilChange");

            do {
                /*проверяем, есть ли такая модель и марка автомобиля в БД*/
                if (carMark.equals(cur.getString(modelColIndex)) && carModel.equals(cur.getString(autoColIndex))) {
                    checkEndTable = true;//когда нашлись данные.
                }
                // получаем значения по номерам столбцов и пишем все в лог
                Log.d(LOG_TAG,
                        "ID = " + cur.getInt(id_ColIndex) +
                                ", auto = " + cur.getString(autoColIndex) +
                                ", model = " + cur.getString(modelColIndex)+
                                ", year = " + cur.getString(yearOfIssueColIndex)+
                                ", brakePad = " + cur.getString(brakePadChangeColIndex) +
                                ", brakeDisc = " + cur.getString(brakeDiscChangeColIndex) +
                                ", motorOil= " + cur.getString(motorOilChangeColIndex));


                // переход на следующую строку
                // а если следующей нет (текущая - последняя), то false - выходим из цикла
            } while (cur.moveToNext());
        } else
            Log.d(LOG_TAG, "0 rows");
        if (!(cur.moveToNext() || checkEndTable)) { /*Если не нашлось имя, пароль и курсор равен нулю (не ноль), то: */
            AlertDialog.Builder builder = new AlertDialog.Builder(CreateProfile.this);
            builder.setTitle("ошибка")
                    .setMessage("Авто нет в базе данных")
                    .create();
            AlertDialog alert = builder.create();
            alert.show();
        }
        cur.close();
        //dbHelper.close();



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
                    long rowID = db.insert("mytable", null, cv);
                    Log.d(LOG_TAG, "row inserted, ID = " + rowID);

                    Log.d(LOG_TAG, "--- Rows in mytable: ---");
                    // делаем запрос всех данных из таблицы mytable, получаем Cursor
                    Cursor c = db.query("mytable", null, null, null, null, null, null);

                    // ставим позицию курсора на первую строку выборки
                    // если в выборке нет строк, вернется false
                    if (c.moveToFirst()) {
                        // определяем номера столбцов по имени в выборке
                        int idColIndex = c.getColumnIndex("id");
                        int userNameColIndex = c.getColumnIndex("userName");
                        int carModelColIndex = c.getColumnIndex("carModel");
                        int carMarkColIndex = c.getColumnIndex("carMark");
                        int carNumberColIndex = c.getColumnIndex("carNumber");
                        int odoValueColIndex = c.getColumnIndex("odoValue");
                        int passwordColIndex = c.getColumnIndex("password");

                        do {
                            // получаем значения по номерам столбцов и пишем все в лог
                            Log.d(LOG_TAG,
                                    "ID = " + c.getInt(idColIndex) +
                                            ", userName = " + c.getString(userNameColIndex) +
                                            ", carModel = " + c.getString(carModelColIndex) +
                                            ", carMark = " + c.getString(carMarkColIndex) +
                                            ", odoValue = " + c.getString(odoValueColIndex) +
                                            ", carNumber = " + c.getString(carNumberColIndex) +
                                            ", password = " + c.getString(passwordColIndex));

                            Intent intent1 = new Intent(CreateProfile.this, ProfileActivity.class);
                            intent1.putExtra("row", c.getString(idColIndex));//Передача данных id из базы в др активити
                            startActivity(intent1);//переход в активити
                            // переход на следующую строку
                            // а если следующей нет (текущая - последняя), то false - выходим из цикла
                        } while (c.moveToNext());
                    } else
                        Log.d(LOG_TAG, "0 rows");
                    c.close();
                    break;
                case R.id.BtnBack:
                    Intent intent = new Intent(CreateProfile.this, MainActivity.class);
                    startActivity(intent);
                    break;
            }
            // закрываем подключение к БД
            dbHelper.close();
        }
    }
}
