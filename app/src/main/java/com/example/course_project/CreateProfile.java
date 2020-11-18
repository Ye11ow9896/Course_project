package com.example.course_project;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class CreateProfile extends Activity implements OnClickListener {
    /*Переменные профиля:
     * userName - имя пользователя
     * carModel - Марка и модель ТС
     * numberOfCar - гос номер автомобиля
     * odoValue - показание одометра одометра*/

    final String LOG_TAG = "myLogs";

    Button btnCreate, btnBack;
    EditText etCarModel, etUserName, etCarNumber, etOdoValue;

    DBHelper dbHelper;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_profile);

        btnCreate = (Button) findViewById(R.id.BtnCreate);
        btnCreate.setOnClickListener(this);

        btnBack = (Button) findViewById(R.id.BtnBack);
        btnBack.setOnClickListener(this);


        etCarModel = (EditText) findViewById(R.id.CarModel);
        etUserName = (EditText) findViewById(R.id.UserName);
        etOdoValue = (EditText) findViewById(R.id.OdoValue);
        etCarNumber = (EditText) findViewById(R.id.NumberOfCar);

        // создаем объект для создания и управления версиями БД
        dbHelper = new DBHelper(this);
    }


    @Override
    public void onClick(View v) {

        // создаем объект для данных
        ContentValues cv = new ContentValues();

        // получаем данные из полей ввода
        String userName = etUserName.getText().toString();
        String carModel = etCarModel.getText().toString();
        Integer odoValue  = new Integer(etOdoValue .getText().toString());
        String carNumber = etCarNumber.getText().toString();

        // подключаемся к БД
        SQLiteDatabase db = dbHelper.getWritableDatabase();


        switch (v.getId()) {
            case R.id.BtnCreate:
                Log.d(LOG_TAG, "--- Insert in mytable: ---");

                // подготовим данные для вставки в виде пар: наименование столбца - значение
                cv.put("userName", userName);
                cv.put("carModel", carModel);
                cv.put("odoValue", odoValue);
                cv.put("carNumber", carNumber);

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
                    int carNumberColIndex = c.getColumnIndex("carNumber");
                    int odoValueColIndex = c.getColumnIndex("odoValue");

                    do {
                        // получаем значения по номерам столбцов и пишем все в лог
                        Log.d(LOG_TAG,
                                "ID = " + c.getInt(idColIndex) +
                                        ", userName = " + c.getString(userNameColIndex) +
                                        ", carModel = " + c.getString(carModelColIndex)+
                                        ", odoValue = " + c.getString(odoValueColIndex) +
                                        ", carNumberColIndex = " + c.getString(carNumberColIndex));
                        // переход на следующую строку
                        // а если следующей нет (текущая - последняя), то false - выходим из цикла
                    } while (c.moveToNext());
                } else
                    Log.d(LOG_TAG, "0 rows");
                c.close();
                break;
            case R.id.BtnBack:
                Intent intent = new Intent( CreateProfile.this,MainActivity.class);
                startActivity(intent);
                break;
        }
        // закрываем подключение к БД
        dbHelper.close();
    }
}
