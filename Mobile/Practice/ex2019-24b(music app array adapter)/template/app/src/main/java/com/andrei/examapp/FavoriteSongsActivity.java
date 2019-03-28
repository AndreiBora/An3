package com.andrei.examapp;

import android.arch.lifecycle.ViewModelProviders;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.andrei.examapp.model.GenericEntity;
import com.andrei.examapp.util.Util;
import com.andrei.examapp.view.GenericViewModel;

import java.util.ArrayList;
import java.util.List;

public class FavoriteSongsActivity extends AppCompatActivity {
    private ListView listView;
    private ProgressBar progressBar;
    GenericViewModel viewModel;
    List<String> items;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite_songs);
        setTitle("Favorite songs title");

        items = new ArrayList<>();
        listView = findViewById(R.id.lv_client);
        progressBar = findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.VISIBLE);

        viewModel = ViewModelProviders.of(this).get(GenericViewModel.class);


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, items);
        listView.setAdapter(adapter);

        viewModel.favoriteEntities.observe(this, genericEntities -> {
            List<String> titles = new ArrayList<>();
            for(GenericEntity entity:genericEntities){
                titles.add(entity.getTitle());
            }
            items.addAll(titles);
            adapter.notifyDataSetChanged();
        });

        viewModel.isGone.observe(this, flag -> {
            if (flag) {
                progressBar.setVisibility(View.GONE);
            } else {
                progressBar.setVisibility(View.VISIBLE);
            }
        });

        viewModel.toastMsg.observe(this, msg -> {
            Util.toast(FavoriteSongsActivity.this, msg);
        });
    }
}
