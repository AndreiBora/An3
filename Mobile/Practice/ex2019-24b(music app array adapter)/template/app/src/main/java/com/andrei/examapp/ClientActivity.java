package com.andrei.examapp;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.andrei.examapp.util.MyCallback;
import com.andrei.examapp.util.Util;
import com.andrei.examapp.view.ClientAdapter;
import com.andrei.examapp.view.GenericViewModel;

import java.util.ArrayList;
import java.util.List;

public class ClientActivity extends AppCompatActivity {
    private ListView listView;
    private ProgressBar progressBar;
    GenericViewModel viewModel;
    List<String> items;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client);
        setTitle("Client");

        items = new ArrayList<>();
        listView = findViewById(R.id.lv_client);
        progressBar = findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.VISIBLE);

        viewModel = ViewModelProviders.of(this).get(GenericViewModel.class);


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, items);
        listView.setAdapter(adapter);

        viewModel.genres.observe(this, genericEntities -> {
            items.addAll(genericEntities);
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
            Util.toast(ClientActivity.this, msg);
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String genre = (String) parent.getItemAtPosition(position);
                if (Util.isNetworkAvailable(ClientActivity.this)) {
                    Intent intent = new Intent(ClientActivity.this, ClientSongActivity.class);
                    intent.putExtra(ClientSongActivity.EXTRA_GENRE, genre);
                    startActivity(intent);
                }else{
                    Util.toast(ClientActivity.this,"Device offline");
                }
            }
        });

    }


}
