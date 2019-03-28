package com.andrei.examapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.andrei.examapp.model.GenericEntity;

public class EditEntityActivity extends AppCompatActivity {
    public static final String EXTRA_EDIT_ENTITY =
            "com.andrei.examapp.EXTRA_EDIT_ENTITY";

    private EditText etxtName;
    private EditText etxtStatus;
    private EditText etxtYear;
    private Button btnUpdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_entity);
        setTitle("Update");

        btnUpdate = findViewById(R.id.btn_update_entity);
        etxtName = findViewById(R.id.etxt_name);
        etxtStatus = findViewById(R.id.etxt_status);
        etxtYear = findViewById(R.id.etxt_year);

        Intent intent = getIntent();
        GenericEntity entity = (GenericEntity) intent.getSerializableExtra(EXTRA_EDIT_ENTITY);

        etxtName.setText(entity.getName());
        etxtStatus.setText(entity.getStatus());
        etxtYear.setText(entity.getYear().toString());

        btnUpdate.setOnClickListener(v->{
            String name = etxtName.getText().toString();
            String status = etxtStatus.getText().toString();
            Integer year = Integer.parseInt(etxtYear.getText().toString());

            entity.setName(name);
            entity.setStatus(status);
            entity.setYear(year);
            Intent data = new Intent();
            data.putExtra(EXTRA_EDIT_ENTITY,entity);
            setResult(RESULT_OK,data);
            finish();

        });
    }
}
