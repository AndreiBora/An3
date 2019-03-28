package com.andrei.examapp;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;

import com.andrei.examapp.databinding.ActivitySongDetailsBinding;
import com.andrei.examapp.model.GenericEntity;
import com.andrei.examapp.util.Util;

public class SongDetailsActivity extends AppCompatActivity {
    public static final String EXTRA_DETAIL_ENTITY =
            "com.andrei.examapp.EXTRA_DETAIL_ENTITY";
    private Button btnAddFav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivitySongDetailsBinding detailsBinding = DataBindingUtil.setContentView(this,R.layout.activity_song_details);

        Intent intent =getIntent();
        GenericEntity entity = (GenericEntity) intent.getSerializableExtra(EXTRA_DETAIL_ENTITY);
        detailsBinding.setGenericEntity(entity);

        btnAddFav = findViewById(R.id.btn_add_fav);
        btnAddFav.setOnClickListener(v->{
            Intent data = new Intent();
            data.putExtra(EXTRA_DETAIL_ENTITY,entity);
            setResult(RESULT_OK,data);
            finish();
        });
    }
}
