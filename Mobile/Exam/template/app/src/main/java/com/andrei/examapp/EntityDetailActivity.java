package com.andrei.examapp;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;

import com.andrei.examapp.databinding.ActivityEntityDetailBinding;
import com.andrei.examapp.model.GenericEntity;
import com.andrei.examapp.util.Util;
import com.andrei.examapp.view.GenericViewModel;

public class EntityDetailActivity extends AppCompatActivity {
    public static final String EXTRA_DETAIL_ENTITY =
            "com.andrei.examapp.EXTRA_DETAIL_ENTITY";

    private Button btnReturn;
    GenericViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityEntityDetailBinding detailBinding = DataBindingUtil.setContentView(this,R.layout.activity_entity_detail);

        btnReturn = findViewById(R.id.btn_return);

        Intent intent = getIntent();
        GenericEntity entity = (GenericEntity) intent.getSerializableExtra(EXTRA_DETAIL_ENTITY);
        detailBinding.setGenericEntity(entity);
        viewModel = ViewModelProviders.of(this).get(GenericViewModel.class);

        btnReturn.setOnClickListener(v->{
            viewModel.returnBike(entity);
        });

    }
}
