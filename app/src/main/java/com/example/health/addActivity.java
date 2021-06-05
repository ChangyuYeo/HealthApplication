package com.example.health;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.MultiAutoCompleteTextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;

public class addActivity extends Activity {
    Button btnBack, btnReturn;
    EditText edtSet, edtNum;
    String [] itmes;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        btnBack = (Button) findViewById (R.id.btn_back);
        btnReturn = (Button) findViewById (R.id.btn_return);
        edtSet = (EditText) findViewById (R.id.edt_set);
        edtNum = (EditText) findViewById (R.id.edt_num);

        final AutoCompleteTextView edtTitle = (AutoCompleteTextView) findViewById(R.id.edt_title);

        // 텍스트 자동완성
        try {
            InputStream inputS = getResources().openRawResource(R.raw.text_list);
            byte[] txt = new byte[inputS.available()];
            inputS.read(txt);
            String list = new String(txt);
            itmes = list.split("\n");

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, itmes);
            edtTitle.setAdapter(adapter);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // MainActivity로 값 넘겨주기
        btnReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (edtTitle.getText().toString().equals("") || edtSet.getText().toString().equals("") || edtNum.getText().toString().equals("")) {
                    Toast.makeText(getApplicationContext(), "공백 없이 입력해 주세요.", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent();
                    intent.putExtra("INPUT_TITLE", edtTitle.getText().toString());
                    intent.putExtra("INPUT_SET", edtSet.getText().toString());
                    intent.putExtra("INPUT_NUM", edtNum.getText().toString());
                    setResult(Activity.RESULT_OK, intent);
                    finish();
                }
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}

