package com.andrei.examapp;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.andrei.examapp.databinding.ActivityEntityDetailBinding;
import com.andrei.examapp.model.GenericEntity;

public class EntityDetailActivity extends AppCompatActivity {
    public static final String EXTRA_DETAIL_ENTITY =
            "com.andrei.examapp.EXTRA_DETAIL_ENTITY";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityEntityDetailBinding detailBinding = DataBindingUtil.setContentView(this,R.layout.activity_entity_detail);

        Intent intent = getIntent();
        GenericEntity entity = (GenericEntity) intent.getSerializableExtra(EXTRA_DETAIL_ENTITY);

        detailBinding.setGenericEntity(entity);
    }
}
