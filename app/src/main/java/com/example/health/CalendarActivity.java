package com.example.health;

import android.app.Activity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
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
                String fileN = year + "_" + (month + 1) + "_" + dayOfMonth + ".txt";
                calTitle.setText(String.format("%d년 %d월 %d일", year, month + 1, dayOfMonth));
                try {
                    InputStream inputS = openFileInput(fileN);
                    //InputStream inputS = openFileInput("2021_6_8.txt");
                    byte[] txt = new byte[inputS.available()];
                    inputS.read(txt);
                    String str = new String(txt);
                    // 불러온 값에서 특수문자 삭제하기
                    String match = "[^\uAC00-\uD7A3xfe0-9a-zA-Z\\s]";
                    str = str.replaceAll(match, "");
                    calContent.setText(str);
                    inputS.close();
                } catch (IOException e) {
                    calContent.setText("기록이 없습니다 :(");
                }
            }
        });

        // 처음 실행할 때부터 그날의 기록이 있으면 보여주는 부분
        Calendar cal = Calendar.getInstance();
        int cYear = cal.get(Calendar.YEAR);
        int cMonth = cal.get(Calendar.MONTH);
        int cDay = cal.get(Calendar.DAY_OF_MONTH);

        String fileN = cYear + "_" + (cMonth + 1) + "_" + cDay + ".txt";
        calTitle.setText(String.format("%d년 %d월 %d일", cYear, cMonth + 1, cDay));
        try {
            InputStream inputS = openFileInput(fileN);
            byte[] txt = new byte[inputS.available()];
            inputS.read(txt);
            String str = new String(txt);
            String match = "[^\uAC00-\uD7A3xfe0-9a-zA-Z\\s]";
            str = str.replaceAll(match, "");
            calContent.setText(str);
            inputS.close();
        } catch (IOException e) {
            calContent.setText("기록이 없습니다 :(");
        }

        btnMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
