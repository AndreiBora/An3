package com.andrei.examapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import com.andrei.examapp.model.GenericEntity;

public class ShowDetailActivity extends AppCompatActivity {
    public static final String EXTRA_SHOW_ENTITY =
            "com.andrei.examapp.EXTRA_SHOW_ENTITY";
    private EditText etxtName;
    private EditText etxtType;
    private EditText etxtStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_detail);
        setTitle("Seat details");

        etxtName = findViewById(R.id.etxt_name);
        etxtType = findViewById(R.id.etxt_type);
        etxtStatus = findViewById(R.id.etxt_status);

        Intent intent = getIntent();
        GenericEntity entity = (GenericEntity) intent.getSerializableExtra(EXTRA_SHOW_ENTITY);

        etxtName.setText(entity.getName());
        etxtType.setText(entity.getType());
        etxtStatus.setText(entity.getStatus());

    }
}
