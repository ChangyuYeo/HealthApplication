package com.example.health;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends Activity {

    Button startBtn, stopBtn, endBtn, btnCal, btnMain, addBtn;
    ListView listView;
    ArrayList<String> list = new ArrayList<String>();
    ArrayAdapter<String> adapter;

    private Chronometer chronometer;
    private boolean running;
    private long pauseOffset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startBtn = (Button) findViewById(R.id.start_btn);
        stopBtn = (Button) findViewById(R.id.stop_btn);
        endBtn = (Button) findViewById(R.id.end_btn);
        addBtn = (Button) findViewById(R.id.add_btn);
        btnMain = (Button) findViewById(R.id.btnMain);
        btnCal = (Button) findViewById(R.id.btnCal);
        listView = (ListView) findViewById(R.id.listView);

        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_single_choice, list);
        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        listView.setAdapter(adapter);

        chronometer = findViewById(R.id.chronometer);
        chronometer.setFormat("%s");

        // 현재 날짜 구하기
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy년 MM월 dd일");

        Date date = new Date();
        String timeDay = simpleDateFormat.format(date);

        TextView tvDay = (TextView) findViewById(R.id.tv_day);
        tvDay.setText(timeDay);

        // 운동시작
        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!running) {
                    chronometer.setBase(SystemClock.elapsedRealtime()-pauseOffset);
                    chronometer.start();
                    running = true;
                    startBtn.setVisibility(View.GONE);
                    stopBtn.setVisibility(View.VISIBLE);
                    endBtn.setVisibility(View.VISIBLE);
                }
            }
        });

        // 일시중지
        stopBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (running) {
                    chronometer.stop();
                    pauseOffset = SystemClock.elapsedRealtime()-chronometer.getBase();
                    running = false;
                    startBtn.setVisibility(View.VISIBLE);
                    stopBtn.setVisibility(View.GONE);
                    startBtn.setText("다시시작");
                    startBtn.setTextColor(Color.parseColor("#3b3b3b"));
                    startBtn.setTextSize(20);
                }
            }
        });

        // 운동종료
        endBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("수고하셨습니다!").setMessage("운동을 끝낼까요?");

                builder.setPositiveButton("예", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(getApplicationContext(), "운동을 기록 합니다!", Toast.LENGTH_SHORT).show();
                        chronometer.stop();
                        chronometer.setBase(SystemClock.elapsedRealtime());
                        running = false;
                        startBtn.setVisibility(View.VISIBLE);
                        stopBtn.setVisibility(View.GONE);
                        endBtn.setVisibility(View.GONE);
                        startBtn.setText("운동시작");
                        startBtn.setTextColor(Color.parseColor("#673AB7"));
                        startBtn.setTextSize(25);
                    }
                });

                builder.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(getApplicationContext(), "취소합니다!", Toast.LENGTH_SHORT).show();
                    }
                });

                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, addActivity.class);
                startActivityForResult(intent, 1);
            }
        });

        btnCal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), CalendarActivity.class);
                startActivity(intent);
            }
        });
    }

    // addActivity 에서 값 가져오기
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK) {
            String INPUT_TITLE = data.getStringExtra("INPUT_TITLE");
            String INPUT_SET = data.getStringExtra("INPUT_SET");
            String INPUT_NUM = data.getStringExtra("INPUT_NUM");

            list.add(INPUT_TITLE + "\n" + INPUT_SET + " set" + "          " + INPUT_NUM + " reps");
            adapter.notifyDataSetChanged();
            listView.clearChoices();

            if(adapter.getCount() > 5){
                View item = adapter.getView(0, null, listView);
                item.measure(0, 0);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, (int) (5.5 * item.getMeasuredHeight()));
                listView.setLayoutParams(params);
            }

            final TextView tv1 = (TextView) findViewById(R.id.tv1);
            final Button btnDel = (Button) findViewById(R.id.btn_del);
            tv1.setVisibility(View.GONE);
            btnDel.setVisibility(View.VISIBLE);

            // 리스트 선택해서 삭제하는 리스너
            btnDel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        // 선택된 항목 인덱스 가져오기
                        int pos = listView.getCheckedItemPosition();
                        list.remove(pos);
                        adapter.notifyDataSetChanged();
                        listView.clearChoices();
                    } catch (Exception e) {
                        Toast.makeText(getApplicationContext(), "선택된 항목이 없습니다.", Toast.LENGTH_SHORT).show();
                    }
                    // 삭제하기 눌렀는데 리스트에 아무것도 없었을 때
                    if (adapter.getCount() == 0) {
                        tv1.setVisibility(View.VISIBLE);
                        btnDel.setVisibility(View.GONE);
                    }
                }
            });
        }
    }
}