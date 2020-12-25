package com.example.course_project;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

public class BoardJournalActivity extends Activity implements OnClickListener {

    private ProgressBar progressBar1, progressBar2, progressBar3;
    private TextView brakePadChange, motorOilChange, brakeDiscChange;
    private Button btnBack;
    private WorkWithDB workWithDBBoardJur;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.board_journal_activity);

        progressBar1 = (ProgressBar) findViewById(R.id.progressBar1);
        progressBar2 = (ProgressBar) findViewById(R.id.progressBar2);
        progressBar3 = (ProgressBar) findViewById(R.id.progressBar3);

        brakeDiscChange = (TextView) findViewById(R.id.textView3_2);
        motorOilChange = (TextView) findViewById(R.id.textView2_2);
        brakePadChange = (TextView) findViewById(R.id.textView1_2);

        btnBack = (Button) findViewById(R.id.button);
        btnBack.setOnClickListener(this);

        workWithDBBoardJur = new WorkWithDB(this);
        
        ViewMaintenance();
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){//кнопка назад в профиль
            case R.id.button:
                Intent backIntent = new Intent(BoardJournalActivity.this, ProfileActivity.class);
                startActivity(backIntent);
        }
    }

    void ViewMaintenance() {     //отображжение активити(прогрессбар и значения)
        /*создаем переменные и записываем значения с БД в них для отображения прогрессбар*/
            int brakePadVal = Integer.valueOf(workWithDBBoardJur.getFieldOfDB("maintenance", "brakePadChange",
                    workWithDBBoardJur.getCursorMaintenance())); //cur.getInt(brakePadChangeColIndex);
            int brakeChangeVal = Integer.valueOf(workWithDBBoardJur.getFieldOfDB("maintenance", "brakeDiscChange",
                    workWithDBBoardJur.getCursorMaintenance())); //cur.getInt(brakeDiscChangeColIndex);
            int motorOilVal =  Integer.valueOf(workWithDBBoardJur.getFieldOfDB("maintenance", "motorOilChange",
                    workWithDBBoardJur.getCursorMaintenance()));  //cur.getInt(motorOilChangeColIndex);
            int odoVal = Integer.valueOf(workWithDBBoardJur.getFieldOfDB("mytable", "odoValue",
                workWithDBBoardJur.getCursorPositionMytable()));

            /*Отображаем сколько осталось до работ с помощью прогрессбара*/
            progressBar1.setMax(brakePadVal);//макс значение прогрессбар
            progressBar1.setProgress(odoVal % brakePadVal);//значение для отображения прогресс бар.
            brakePadChange.setText(String.valueOf(brakePadVal - progressBar1.getProgress()));
            
            progressBar2.setMax(brakeChangeVal);
            progressBar2.setProgress(odoVal % brakeChangeVal);
            motorOilChange.setText(String.valueOf(brakeChangeVal - progressBar2.getProgress()));
            
            progressBar3.setMax(motorOilVal);
            progressBar3.setProgress(odoVal % motorOilVal);
            brakeDiscChange.setText(String.valueOf(motorOilVal - progressBar3.getProgress()));
    }
}
