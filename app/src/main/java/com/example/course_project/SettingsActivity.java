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
import android.widget.Toast;

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
                Dialog("userName", workWithDBSA.getFieldOfDB("mytable", "userName",
                        workWithDBSA.getCursorPositionMytable()));
                break;
            case R.id.password:
               Dialog("password", workWithDBSA.getFieldOfDB("mytable", "password",
                       workWithDBSA.getCursorPositionMytable()));
                break;
            case R.id.carNumber:
               Dialog("carNumber", workWithDBSA.getFieldOfDB("mytable", "carNumber",
                       workWithDBSA.getCursorPositionMytable()));
                break;
            case R.id.odoValue:
               Dialog("odoValue", workWithDBSA.getFieldOfDB("mytable", "odoValue",
                       workWithDBSA.getCursorPositionMytable()));
                break;
            case R.id.deleteProfile:
               DeleteRowTable();
                break;
            case R.id.btnBackPA:
                Intent intentProfileActivity = new Intent(SettingsActivity.this, ProfileActivity.class);
                startActivity(intentProfileActivity);
                break;
            }
        }
    //функция для вывода алерта, в котором можно менять данные.
    // В качестве аргументов получает наименование колонки, с которой необходимо провести работу
    private void Dialog(String column, String nameOper) {

        LayoutInflater li = LayoutInflater.from(context);
        View promptsView = li.inflate(R.layout.alert_dialog, null);
        //Создаем AlertDialog
        AlertDialog.Builder mDialogBuilder = new AlertDialog.Builder(context);
        //Настраиваем prompt.xml для нашего AlertDialog:
        mDialogBuilder.setView(promptsView);
        //Настраиваем отображение поля для ввода текста в открытом диалоге:
        final TextView nameOperation = (TextView) promptsView.findViewById(R.id.nameOperation);
        nameOperation.setText(nameOper);
        final EditText userInput = (EditText) promptsView.findViewById(R.id.input_text);
        mDialogBuilder
                .setCancelable(false)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Cursor cur = workWithDBSA.getAccessToDB().query("mytable", null, null, null, null, null, null);

                                cur.moveToPosition(workWithDBSA.getCursorPositionMytable());//передаем курсор, т.о. устанавливаем курсор в позиции на нужной строке
                                int c = workWithDBSA.getCursorPositionMytable();
                                ID = Integer.valueOf(cur.getString(cur.getColumnIndex("id")));//берем из БД айди этого профиля и присваиваем переменной
                                newData = userInput.getText().toString();//получаем данные в переменную из тестового поля
                                ContentValues cv = new ContentValues();
                                if (column.equals("odoValue")) {
                                    Integer ov = new Integer(newData);
                                    cv.put(column, ov);
                                }
                                else
                                     cv.put(column, newData);
                                workWithDBSA.getAccessToDB().update("mytable", cv, "ID = " + ID, null);
                                logs();
                                workWithDBSA.close();
                                Toast.makeText(context, "Изменения сохранены", Toast.LENGTH_LONG).show();
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
    private void DeleteRowTable() {
        Cursor cur = workWithDBSA.getAccessToDB().query("mytable", null, null, null, null, null, null);
        cur.moveToPosition(workWithDBSA.getCursorPositionMytable());//передаем курсор, т.о. устанавливаем курсор в позиции на нужной строке
        ID = Integer.valueOf(cur.getString(cur.getColumnIndex("id")));//берем из БД айди этого профиля и присваиваем переменной
        workWithDBSA.getAccessToDB().delete("mytable","ID = " + ID, null);
        logs();
        workWithDBSA.close();
        /*алерт об удалении учетки, переход в мэин активити*/
        AlertDialog.Builder builder = new AlertDialog.Builder(SettingsActivity.this);
        builder.setTitle("учетная запись была удалена")
                .setPositiveButton("ОК",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Intent goToMain = new Intent(SettingsActivity.this, MainActivity.class);
                                startActivity(goToMain);
                            }
                        })
                .create();
        AlertDialog alert = builder.create();
        alert.show();
    }
}
