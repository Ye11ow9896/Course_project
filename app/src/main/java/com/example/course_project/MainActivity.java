package com.example.course_project;

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
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements OnClickListener {

    DBHelper dbHelper;

    private Button BtnLoginProfile, BtnCreateProfile;
    private EditText textName, textPassword;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BtnLoginProfile = (Button)findViewById(R.id.btnLoginProfile);
        BtnLoginProfile.setOnClickListener(this);

        BtnCreateProfile = (Button)findViewById(R.id.btnCreateProfile);
        BtnCreateProfile.setOnClickListener(this);

        dbHelper = new DBHelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor c = db.query("mytable", null, null, null, null, null, null);
    }
    @Override
    public void onClick(View v) {

        //привязываем кнопки по айди
        BtnLoginProfile = (Button) findViewById(R.id.btnLoginProfile);
        BtnCreateProfile = (Button) findViewById(R.id.btnCreateProfile);
        textName = (EditText) findViewById(R.id.NumberOfCar);
        textPassword = (EditText) findViewById(R.id.Password);


        switch (v.getId()) {

            //По нажатию кнопки переходим в активити создание профиля
            case R.id.btnCreateProfile:
                Intent intent = new Intent(MainActivity.this, CreateProfile.class);
                startActivity(intent);
                break;
            //По нажатию кнопки переходим в активити профиля
            case R.id.btnLoginProfile:
                //searchData();
                Intent intent1 = new Intent(MainActivity.this, ProfileActivity.class);
                startActivity(intent1);
                break;
            }
        }


        //BtnCreateProfile.setOnClickListener(
        //        new View.OnClickListener(){
        //            @Override
        //
        //        }
        //);


   //    BtnCreateProfile.setOnClickListener(

   //        new View.OnClickListener(){
   //            @Override
   //            public void onClick(View v) {
   //
   //            }
   //        }
   //    );
   //}
   //void searchData() {//функция поиска данных пароля и мени для авторизаци
   //

   //    if (c.moveToFirst()) {

   //        // определяем номера столбцов по имени в выборке
   //        int idColIndex = c.getColumnIndex("id");
   //        int userNameColIndex = c.getColumnIndex("userName");
   //        int carModelColIndex = c.getColumnIndex("carModel");
   //        int carNumberColIndex = c.getColumnIndex("carNumber");
   //        int odoValueColIndex = c.getColumnIndex("odoValue");
   //        int passwordColIndex = c.getColumnIndex("password");

   //        do {
   //            // получаем значения по номерам столбцов и пишем все в лог
   //             textName.setText(String.valueOf(c.getString(userNameColIndex)));
   //             textPassword.setText(String.valueOf(c.getString(passwordColIndex)));
   //            // переход на следующую строку
   //            // а если следующей нет (текущая - последняя), то false - выходим из цикла
   //        } while (c.moveToNext());
   //    } else
   //    c.close();

   //}


}