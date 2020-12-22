package com.example.course_project;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class CreateProfile extends Activity implements OnClickListener {

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
            switch (v.getId()) {
                case R.id.BtnCreate://по нажатию кнопки добавляем введенные данные, если находим совпадения модели и марки авто в БД
                    /*метод класса searchCarInDB принимает значения полей марки и модели авто и сравнивает их с данными в БД
                    * если данные совпали - метод возвращает тру*/
                    if (workWithDBCreateProf.searchCarInDB(etCarMark.getText().toString(), etCarModel.getText().toString())) {
                        ContentValues cv = new ContentValues();

                        // подготовим данные для вставки в виде пар: наименование столбца - значение
                        cv.put("userName", etUserName.getText().toString());
                        cv.put("carModel", etCarModel.getText().toString());
                        cv.put("carMark", etCarMark.getText().toString());
                        cv.put("odoValue", new Integer(etOdoValue.getText().toString()));
                        cv.put("carNumber", etCarNumber.getText().toString());
                        cv.put("password", etPassword.getText().toString());
                        workWithDBCreateProf.getAccessToDB().insert("mytable",null, cv);//записываем сл стоку в таблицу пользователей
                        workWithDBCreateProf.logIn(etUserName.getText().toString(), etPassword.getText().toString());//авторизуемся
                        /*переходим в др активити*/
                        Intent intent1 = new Intent(CreateProfile.this, ProfileActivity.class);
                        startActivity(intent1);//переход в активити

                    } else {//если метод searchCarInDB вернул фолс, вызываем алерт с сообщением
                        AlertDialog.Builder builder = new AlertDialog.Builder(CreateProfile.this);
                               builder.setTitle("ошибка")
                                       .setMessage("Авто нет в базе данных")
                                       .create();
                               AlertDialog alert = builder.create();
                               alert.show();
                    }
                    workWithDBCreateProf.close();
                    break;

                    case R.id.BtnBack://возврат мэин активити
                    Intent intent = new Intent(CreateProfile.this, MainActivity.class);
                    startActivity(intent);
                        break;
        }
    }
}
