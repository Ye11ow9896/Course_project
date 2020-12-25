package com.example.course_project;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class WorkWithDB extends DBHelper {

    private static int idValueMaintenace;
    private static int cursorPositionMytable;
    private DBHelper dbHelper;

    public WorkWithDB(Context context) {
        super(context);
        dbHelper = new DBHelper(context);
    }

/*------------------------------сеттеры-------------------------------------------*/
    public void setIdMaintenance(int idValueMaintenace) {//метод получения айди после регистрации и авторизации
        this.idValueMaintenace = idValueMaintenace;
    }

    public void setCursorMytable(String userName) {
        int ctnCursor = 0;
        Cursor c = getAccessToDB().query("mytable", null, null, null, null, null, null);//создаем таблицу в БД
        int userNameColIndex = c.getColumnIndex("userName");
        if (c.moveToFirst()) {

           do {
               if (userName.equals(c.getString(userNameColIndex))) {
                    cursorPositionMytable = ctnCursor;
                    c.close();
                    break;
                }
               ctnCursor++;
           } while (c.moveToNext());
           c.close();
        }
    }


/*-------------------------------геттеры--------------------------------------------*/
    public SQLiteDatabase getAccessToDB() {
        SQLiteDatabase db = dbHelper.getWritableDatabase(); //Метод getWritable... создает БД или, если она создана, то предоставляет доступ на запись/чтение
        return db;
    }

    public int getCursorPositionMytable() {
        return cursorPositionMytable;
    }

    public String getFieldOfDB (String nameOfTable, String columnName, int cursorPosition) {
        Cursor cursor = getAccessToDB().query(nameOfTable, null, null, null, null, null, null);//создаем таблицу в БД
        cursor.moveToPosition(cursorPosition);
        int ColIndex = cursor.getColumnIndex(columnName);
        String f = cursor.getString(ColIndex);
        cursor.close();
        return f;
    }
    public int getCursorMaintenance(){
        return idValueMaintenace - 1;
    }

    //метод возвращает индекс столбца. Принимает в качестве аргументов название таблицы
    //и название столбца
    private int getFieldOfDBForClass(String tableName, String columnName) {
        Cursor cursor = getAccessToDB().query(tableName, null, null, null, null, null, null);//создаем таблицу в БД
        int colIndex = cursor.getColumnIndex(columnName);
        cursor.close();
        return colIndex;
    }


    /*------------------остальные методы: посик марки авто по БД, авторизация, вывод логов, закрыть БД-------------------*/

    public boolean searchCarInDB(String carMark, String carModel) {
        boolean result = false;
        Cursor cur1 = getAccessToDB().query("maintenance", null, null, null, null, null, null);//создаем таблицу в БД

        /*ищем марку и модель авто по БД в таблице мэинтенс*/
        if (cur1.moveToFirst()) {
            do {
                /*проверяем, есть ли такая модель и марка автомобиля в БД*/
                if (carMark.equals(cur1.getString(getFieldOfDBForClass("maintenance", "mark")))
                        && carModel.equals(cur1.getString(getFieldOfDBForClass("maintenance", "model")))) {
                    result = true;//когда нашлись данные.
                    setIdMaintenance(cur1.getInt(getFieldOfDBForClass("maintenance", "id")));//передаем значение айди таблици мэинтенс
                    int c = cur1.getInt(getFieldOfDBForClass("maintenance", "id"));
                }
                // переход на следующую строку
                // а если следующей нет (текущая - последняя), то false - выходим из цикла
            } while (cur1.moveToNext());
        }
        if (!(cur1.moveToNext() || result)) { //*Если не нашлась авто из БД
            result = false;
        }
        cur1.close();
        return result;
    }

    public boolean logIn(String name, String password) {//Передаем в качестве аргументов имя и пароль. Возвращает тру если нашлись введенные данные
        boolean coincidence = false;//флаг. тру - если данные нашлись
        Cursor cur = getAccessToDB().query("mytable", null, null, null, null, null, null);//создаем курсор таблицы майтэйбл

        if (cur != null) {//курсор не нулл
            if (cur.moveToFirst()) {//если таблица не пустая - двигаем курсор на первую строку таблицы
                do
                {//проверяем есть ли введенные данные в нашей БД и прогоняем по все базе, пока не найдем совпадения
                    //если пароль и номер авто совпадают с данными из БД то переходим в активити профиля
                    if (name.equals(cur.getString(getFieldOfDBForClass("mytable", "userName"))) &&
                            password.equals(cur.getString(getFieldOfDBForClass("mytable", "password")))) {
                        coincidence = true;//когда нашлись данные.
                        setCursorMytable(cur.getString(getFieldOfDBForClass("mytable", "userName")));//запись курсора в класс
                        logs();
                        break;
                    }
                } while (cur.moveToNext());//конец таблица БД
            }
            if (!(cur.moveToNext() || coincidence)) { /*Если не нашлось имя, пароль и курсор равен нулю (не ноль), то: */
                coincidence = false;
            }
        }
        cur.close();//закрываем объект БД
        logs();
        return coincidence;
    }

    private void logs() {
        Cursor c = getAccessToDB().query("mytable", null, null, null, null, null, null);//создаем таблицу в БД

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
                                + c.getString(emailColIndex) + ", email = "
                                + c.getString(emailColInde) + ", email = "
                                + c.getString(emailColIn) + ", email = "
                                + c.getString(emailColI) + ", email = "
                                + c.getString(emailColInd)
                );
                // переход на следующую строку
                // а если следующей нет (текущая - последняя), то false -
                // выходим из цикла
            } while (c.moveToNext());
        }
    }
}
