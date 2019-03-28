package com.andrei.examapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.andrei.examapp.model.GenericEntity;

public class AddEntityActivity extends AppCompatActivity {
    public static final String EXTRA_ADD_ENTITY =
            "com.andrei.examapp.EXTRA_ADD_ENTITY";

    private Button btnAdd;
    private EditText etxtName;
    private EditText etxtType;
    private EditText etxtSize;
    private EditText etxtOwner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_entity);
        setTitle("Add bike");

        btnAdd = findViewById(R.id.btn_add_entity);
        etxtName = findViewById(R.id.etxt_name);
        etxtType = findViewById(R.id.etxt_type);
        etxtSize = findViewById(R.id.etxt_size);
        etxtOwner = findViewById(R.id.etxt_owner);

        btnAdd.setOnClickListener(v->{
            String name = etxtName.getText().toString();
            String type = etxtType.getText().toString();
            String size = etxtSize.getText().toString();
            String owner = etxtOwner.getText().toString();

            GenericEntity entity = new GenericEntity();
            entity.setName(name);
            entity.setType(type);
            entity.setSize(size);
            entity.setOwner(owner);

            Intent data = new Intent();
            data.putExtra(EXTRA_ADD_ENTITY,entity);
            setResult(RESULT_OK,data);
            finish();
        });
    }
}
