package com.andrei.examapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;

import com.andrei.examapp.util.Util;

public class MainActivity extends AppCompatActivity {
    private Button btnClient;
    private Button btnClerk;
    private Button btnAdmin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setTitle("Theater app");
        btnClient = findViewById(R.id.btn_client);
        btnClerk = findViewById(R.id.btn_clerk);
        btnAdmin = findViewById(R.id.btn_admin);

        btnClient.setOnClickListener(v->{
            Intent intent = new Intent(MainActivity.this, ClientActivity.class);
            startActivity(intent);
        });

        btnClerk.setOnClickListener(v->{
            if(Util.isNetworkAvailable(this)){
                Intent intent = new Intent(MainActivity.this, ClerkActivity.class);
                startActivity(intent);
            }else{
                Util.toast(this,"Device is offline");
            }
        });

        btnAdmin.setOnClickListener(v->{
            if(Util.isNetworkAvailable(this)){
                Intent intent = new Intent(MainActivity.this, AdminActivity.class);
                startActivity(intent);
            }else{
                Util.toast(this,"Device is offline");
            }
        });
    }
}
