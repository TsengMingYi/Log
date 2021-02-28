package com.example.secondlog;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private Button register_btn;
    private Button login_btn;
    private EditText name_tedit;
    private EditText password_tedit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
    }
    private void initViews() {
        register_btn = findViewById(R.id.register_btn);
        login_btn = findViewById(R.id.login_btn);
        password_tedit = findViewById(R.id.password_tedit);
        name_tedit = findViewById(R.id.name_tedit);
        register_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uid = name_tedit.getText().toString();
                String password = password_tedit.getText().toString();

                if (AccountManager.isUidValid(uid) && AccountManager.isPassValid(password)) {
                    AccountManager.getInstance().registerAccountToServer(MainActivity.this,uid, password);
                } else {
                    Toast.makeText(MainActivity.this,
                            "賬號或密碼錯誤", Toast.LENGTH_SHORT).show();
                }
            }
        });
        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uid = name_tedit.getText().toString();
                String password = password_tedit.getText().toString();
                if (AccountManager.isUidValid(uid) && AccountManager.isPassValid(password)) {
                    AccountManager.getInstance().loginAccountToServer(uid, password,MainActivity.this, new Runnable() {
                        @Override
                        public void run() {
                            startActivity(new Intent(MainActivity.this,HomeActivity.class));
                        }
                    });
                }
            }
        });
    }
}