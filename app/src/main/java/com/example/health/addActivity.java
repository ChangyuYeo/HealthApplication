package com.example.health;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class addActivity extends Activity {
    Button btnBack, btnReturn;
    EditText edtTitle, edtSet, edtNum;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        btnBack = (Button) findViewById (R.id.btn_back);
        btnReturn = (Button) findViewById (R.id.btn_return);

        edtTitle = (EditText) findViewById (R.id.edt_title);
        edtSet = (EditText) findViewById (R.id.edt_set);
        edtNum = (EditText) findViewById (R.id.edt_num);

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

