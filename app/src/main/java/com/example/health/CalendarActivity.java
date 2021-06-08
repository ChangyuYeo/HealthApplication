package com.example.health;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;

public class CalendarActivity extends Activity {

    Button btnMain;
    CalendarView cal;
    TextView calTitle, calContent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        btnMain = (Button) findViewById(R.id.btnMain);
        cal = (CalendarView) findViewById(R.id.cal);
        calTitle = (TextView) findViewById(R.id.cal_title);
        calContent = (TextView) findViewById(R.id.cal_content);

        cal.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int year, int month, int dayOfMonth) {
                String fileN = year + "_" + (month + 1) + "_" + dayOfMonth;
                calTitle.setText(String.format("%d년 %d월 %d일", year, month + 1, dayOfMonth));
                try {
                    InputStream inputS = openFileInput(fileN);
                    byte[] txt = new byte[50];
                    inputS.read(txt);
                    String str = new String(txt);
                    calContent.setText(str);
                    inputS.close();
                } catch (IOException e) {   }
            }
        });

        btnMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
