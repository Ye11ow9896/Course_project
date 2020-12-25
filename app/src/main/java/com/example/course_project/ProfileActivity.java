package com.example.course_project;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.ToggleButton;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import java.text.DecimalFormat;
import java.util.Date;

public class ProfileActivity extends Activity implements OnClickListener {

    /*Используемые переменные*/
    private Button alarm, settings;
    private TextView carNumber, odoValue, gpsData, Greeting;
    private WorkWithDB workWithDBProfile;
    private LocationManager manager;
    private boolean flagForCalculate = false;
    public String lathitude, longitude;
    static double oldlong, oldlath;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        /*Привязка переменных к полям XML по ID*/
        carNumber = (TextView) findViewById(R.id.NumberCarText);
        odoValue = (TextView) findViewById(R.id.OdoValueText);
        gpsData = (TextView) findViewById(R.id.Location);
        Greeting = (TextView) findViewById(R.id.HelloText);

        alarm = (Button) findViewById(R.id.BtnAlert);
        alarm.setOnClickListener(this);

        settings = (Button) findViewById(R.id.btnSettings);
        settings.setOnClickListener(this);

        workWithDBProfile = new WorkWithDB(this);
        dataView();//функция отображения данных в активити
    }

    public void onToggleClicked(View view) {

        // включена ли кнопка
        boolean on = ((ToggleButton) view).isChecked();
        if (on) {
            StartGPS();

        } else {

        }
    }

    /*события по нажалию кнопок*/
    @Override
    public void onClick(View v) {
        /*конструкция свитч по ID кнопки*/

        switch (v.getId()) {      //получаем ID кнопки

            case R.id.BtnAlert: //Нажатие алерт - вывод бортового журнала
                Intent intentBoardJournal = new Intent(ProfileActivity.this, BoardJournalActivity.class);
                startActivity(intentBoardJournal);
                break;
            case R.id.btnSettings: //переход в настройки
                Intent intentSettings = new Intent(ProfileActivity.this, SettingsActivity.class);
                startActivity(intentSettings);
                break;
        }
    }

    //Функция отображения данных авторизованного пользователя
    void dataView() {
        carNumber.setText("Номер авто " + workWithDBProfile.getFieldOfDB("mytable", "carNumber",
                workWithDBProfile.getCursorPositionMytable()));//(cur.getString(workWithDBProfile.getFieldOfDB("mytable", "carNumber")));
        odoValue.setText("Пробег " + workWithDBProfile.getFieldOfDB("mytable", "odoValue",
                workWithDBProfile.getCursorPositionMytable())+"км");
        Greeting.setText("Привет, " + workWithDBProfile.getFieldOfDB("mytable", "userName",
                workWithDBProfile.getCursorPositionMytable()) + "!");
    }


    LocationListener locationListener = new LocationListener() {
        /*изменение местоположения. Именно он используется для определения текущих географических координат*/
        @Override
        public void onLocationChanged(@NonNull Location location) {
            if (location != null) {
                //lathitude = (String.valueOf(location.getLatitude()));
                //longitude = (String.valueOf(location.getLongitude()));
                //float distance = location.distanceTo(location);
                double a = Math.round(CalculateDistance(location.getLongitude(), location.getLatitude())*100)/100;
                double b =+ a;
                gpsData.setText(String.valueOf(b));
            } else {
                lathitude = ("Sorry, location");
                longitude = ("unavailable");
            }
        }

        /*изменение состояния поставщика данных о местоположении. В частности приёмника GPS*/
        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        /*получение доступа к поставщику данных о местоположении*/
        @Override
        public void onProviderEnabled(@NonNull String provider) {
            if (ActivityCompat.checkSelfPermission(null, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(null, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }

            formatLocation(manager.getLastKnownLocation(provider));
        }

        /*потеря доступа к поставщику данных о местоположении*/
        @Override
        public void onProviderDisabled(@NonNull String provider) {

        }
    };

    private String formatLocation(Location location) {
        if (location == null)
            return "";
        return String.format(
                "Coordinates: lat = %1$.4f, lon = %2$.4f, time = %3$tF %3$tT",
                location.getLatitude(), location.getLongitude(), new Date(
                        location.getTime()));
    }

    void StartGPS() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Проверка наличия разрешений
            // Если нет разрешения на использование соответсвующих разркешений выполняем какие-то действия
            return;
        }
        manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0,0 , locationListener);
    }

    private double CalculateDistance(double longitude, double lathitude) {
        double radLong, radLath;
        double RADIUS = 6378.16;
        if(!flagForCalculate) {
            oldlong = longitude;
            oldlath = lathitude;
            flagForCalculate = true;
        }
        else {
            oldlong =- longitude;
            oldlath =- lathitude;
        }
        radLong = Math.toRadians(oldlong);
        radLath = Math.toRadians(oldlath);

        double a = Math.sin(radLath/2) * Math.sin(radLath/2) +
                Math.cos(Math.toRadians(lathitude)) * Math.cos(Math.toRadians(oldlath)) *
                        Math.sin(radLong/2) * Math.sin(radLong/2);
        double angle = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return RADIUS*angle;
    }
}