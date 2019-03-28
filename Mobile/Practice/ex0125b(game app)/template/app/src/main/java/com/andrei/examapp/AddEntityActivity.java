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
    private EditText etxtQuantity;
    private EditText etxtType;
    private EditText etxtStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_entity);
        setTitle("Add");

        btnAdd = findViewById(R.id.btn_add_entity);
        etxtName = findViewById(R.id.etxt_name);
        etxtQuantity = findViewById(R.id.etxt_quantity);
        etxtType = findViewById(R.id.etxt_type);
        etxtStatus = findViewById(R.id.etxt_status);

        btnAdd.setOnClickListener(v->{
            String name = etxtName.getText().toString();
            Integer quantity = Integer.parseInt(etxtQuantity.getText().toString());
            String type = etxtType.getText().toString();
            String status = etxtStatus.getText().toString();

            GenericEntity entity = new GenericEntity(name,quantity,type,status);
            Intent data = new Intent();
            data.putExtra(EXTRA_ADD_ENTITY,entity);
            setResult(RESULT_OK,data);
            finish();
        });
    }
}
