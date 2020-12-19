package com.example.course_project;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class SettingsActivity extends AppCompatActivity implements OnClickListener {

    final String LOG_TAG = "myLogs";
    private Button btnName, btnCarNumber, btnOdoValue, btnPassword, btnBack, deleteProfile;
    private int ID;
    final Context context = this;
    String newData;
    WorkWithDB workWithDBSA;

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

        btnBack = (Button)findViewById(R.id.btnBackPA);
        btnBack.setOnClickListener(this);

        deleteProfile = (Button)findViewById(R.id.deleteProfile);
        deleteProfile.setOnClickListener(this);

        workWithDBSA = new WorkWithDB(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.name:
               Dialog("userName");
                break;
            case R.id.password:
               Dialog("password");
                break;
            case R.id.carNumber:
               Dialog("carNumber");
                break;
            case R.id.odoValue:
               Dialog("odoValue");
                break;
            case R.id.deleteProfile:

                break;
            case R.id.btnBackPA:
                Intent intentProfileActivity = new Intent(SettingsActivity.this, ProfileActivity.class);
                startActivity(intentProfileActivity);
                break;


            }
        }
    //функция для вывода алерта, в котором можно менять данные.
    // В качестве аргументов получает наименование колонки, с которой необходимо провести работу
    public void Dialog(String column) {
        LayoutInflater li = LayoutInflater.from(context);
        View promptsView = li.inflate(R.layout.alert_dialog, null);
        //Создаем AlertDialog
        AlertDialog.Builder mDialogBuilder = new AlertDialog.Builder(context);
        //Настраиваем prompt.xml для нашего AlertDialog:
        mDialogBuilder.setView(promptsView);
        //Настраиваем отображение поля для ввода текста в открытом диалоге:
        final EditText userInput = (EditText) promptsView.findViewById(R.id.input_text);
        mDialogBuilder
                .setCancelable(false)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                newData = userInput.getText().toString();//получаем данные в переменную из тестового поля
                                ID = workWithDBSA.idValueMytable;
                                ContentValues cv = new ContentValues();
                                if (column.equals("odoValue")) {
                                    Integer ov = new Integer(newData);
                                    cv.put(column, ov);
                                    workWithDBSA.setOdoValue(ov);
                                }
                                else
                                     cv.put(column, newData);
                                workWithDBSA.getAccessToDB().update("mytable", cv, "ID = " + ID, null);
                                logs();
                            }
                        })
                .setNegativeButton("Отмена",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                dialog.cancel();
                            }
                        });
        AlertDialog alertDialog = mDialogBuilder.create();
        alertDialog.show();
    }
    private void logs() {
        Cursor c = workWithDBSA.getAccessToDB().query("mytable", null, null, null, null, null, null);//создаем таблицу в БД

        if (c.moveToFirst()) {
            // определяем номера столбцов по имени в выборке
            int idColIndex = c.getColumnIndex("id");
            int nameColIndex = c.getColumnIndex("carNumber");
            int emailColIndex = c.getColumnIndex("odoValue");
            int emailColInde = c.getColumnIndex("userName");
            int emailColInd = c.getColumnIndex("carModel");
            int emailColIn = c.getColumnIndex("carMark");
            int emailColI = c.getColumnIndex("password");

            do {
                // получаем значения по номерам столбцов и пишем все в лог
                Log.d(LOG_TAG,
                        "ID = " + c.getInt(idColIndex) + ",carnum = "
                                + c.getString(nameColIndex) + ", odoval = "
                                + c.getString(emailColIndex)+ ", email = "
                                + c.getString(emailColInde)+ ", email = "
                                + c.getString(emailColIn)+ ", email = "
                                + c.getString(emailColI)+ ", email = "
                                + c.getString(emailColInd)
                );
                // переход на следующую строку
                // а если следующей нет (текущая - последняя), то false -
                // выходим из цикла
            } while (c.moveToNext());
        }
    }
}
