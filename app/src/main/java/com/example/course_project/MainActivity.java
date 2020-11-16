package com.example.course_project;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private Button BtnLoginProfile, BtnCreateProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        addListenerOnButton();
    }

    public void addListenerOnButton(){
        //привязываем кнопки по айди
        BtnLoginProfile = (Button) findViewById(R.id.BtnLoginProfile);
        BtnCreateProfile = (Button) findViewById(R.id.BtnCreateProfile);

        //По нажатию кнопки переходим в активити создание профиля
        BtnCreateProfile.setOnClickListener(
                new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(MainActivity.this, CreateProfile.class);
                        startActivity(intent);
                    }
                }
        );

        //По нажатию кнопки переходим в активити профиля
        BtnCreateProfile.setOnClickListener(
                new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(MainActivity.this, CreateProfile.class);
                        startActivity(intent);
                    }
                }
        );
    }



}