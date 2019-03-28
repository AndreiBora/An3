package com.andrei.examapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.andrei.examapp.model.GenericEntity;

public class AddEntityActivity extends AppCompatActivity {
    public static final String EXTRA_ADD_ENTITY =
            "com.andrei.examapp.EXTRA_ADD_ENTITY";

    private Button btnAdd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_entity);
        setTitle("Add");

        btnAdd = findViewById(R.id.btn_add_entity);

        btnAdd.setOnClickListener(v->{
            //String name = etxName.getText().toString();
//            GenericEntity entity = new GenericEntity();
//            Intent data = new Intent();
//            data.putExtra(EXTRA_ADD_ENTITY,entity);
//            setResult(RESULT_OK,data);
//            finish();
        });
    }
}
