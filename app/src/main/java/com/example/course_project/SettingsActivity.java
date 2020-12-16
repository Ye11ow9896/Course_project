package com.example.course_project;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class SettingsActivity extends AppCompatActivity implements OnClickListener {

    private Button btnName, btnCarNumber, btnOdoValue, btnPassword;
    private DBHelper dbHelper;
    final Context context = this;
    private TextView final_text;
    private int rowValue;
    String newPassword;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_actyvity);//привязка в лойауту

        btnName = (Button)findViewById(R.id.name);
        btnName.setOnClickListener(this);

        btnCarNumber = (Button)findViewById(R.id.carNumber);
        btnCarNumber.setOnClickListener(this);

        btnOdoValue = (Button)findViewById(R.id.odoValue);
        btnOdoValue.setOnClickListener(this);

        btnPassword = (Button)findViewById(R.id.password);
        btnPassword.setOnClickListener(this);

        dbHelper = new DBHelper(this);


    }

    @Override
    public void onClick(View v) {
        ChangeOfBD();
        switch (v.getId())
        {
            case R.id.name:

                //Получаем вид с файла prompt.xml, который применим для диалогового окна:
                LayoutInflater li = LayoutInflater.from(context);
                View promptsView = li.inflate(R.layout.alert_dialog, null);

                //Создаем AlertDialog
                AlertDialog.Builder mDialogBuilder = new AlertDialog.Builder(context);

                //Настраиваем prompt.xml для нашего AlertDialog:
                mDialogBuilder.setView(promptsView);

                //Настраиваем отображение поля для ввода текста в открытом диалоге:
                final EditText userInput = (EditText) promptsView.findViewById(R.id.input_text);

                //Настраиваем сообщение в диалоговом окне:
                mDialogBuilder
                        .setCancelable(false)
                        .setPositiveButton("OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,int id) {

                                        newPassword = userInput.getText().toString();//получаем данные в переменную из тестового поля

                                    }
                                })
                        .setNegativeButton("Отмена",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,int id) {
                                        dialog.cancel();
                                    }
                                });

                //Создаем AlertDialog:
                AlertDialog alertDialog = mDialogBuilder.create();

                //и отображаем его:
                alertDialog.show();

                break;

            case R.id.password:
                AlertDialog.Builder builder1 = new AlertDialog.Builder(SettingsActivity.this);
                builder1.setTitle("ошибка")
                        .setMessage("Авто нет в базе данных")
                        .create();
                AlertDialog alert1 = builder1.create();
                alert1.show();
                break;
            case R.id.carNumber:
                AlertDialog.Builder builder2 = new AlertDialog.Builder(SettingsActivity.this);
                builder2.setTitle("ошибка")
                        .setMessage("Авто нет в базе данных")
                        .create();
                AlertDialog alert2 = builder2.create();
                alert2.show();
                break;
            case R.id.odoValue:
                AlertDialog.Builder builder3 = new AlertDialog.Builder(SettingsActivity.this);
                builder3.setTitle("ошибка")
                        .setMessage("Авто нет в базе данных")
                        .create();
                AlertDialog alert3 = builder3.create();
                alert3.show();
                break;
        }
    }

    int getId() {
        int rowValue;
        Intent intentSettings = getIntent();
        rowValue = intentSettings.getIntExtra("rowValue", 0);
        return rowValue;
    }

    void ChangeOfBD() {
        SQLiteDatabase db = dbHelper.getWritableDatabase(); //Метод getWritable... создает БД или, если она создана, то предоставляет доступ на запись/чтение
        Cursor c = db.query("mytable", null, null, null, null, null, null);//создаем таблицу в БД

        c.moveToPosition(getId());
        /*индексы значений в БД*/
        int idColIndex = c.getColumnIndex("id");
        int userNameColIndex = c.getColumnIndex("userName");
        int carModelColIndex = c.getColumnIndex("carModel");
        int carMarkColIndex = c.getColumnIndex("carMark");
        int carNumberColIndex = c.getColumnIndex("carNumber");
        int odoValueColIndex = c.getColumnIndex("odoValue");
        int passwordColIndex = c.getColumnIndex("password");
    }
}
