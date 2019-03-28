package com.andrei.examapp;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.andrei.examapp.model.GenericEntity;
import com.andrei.examapp.util.MyCallback;
import com.andrei.examapp.util.Util;
import com.andrei.examapp.view.ClerkAdapter;
import com.andrei.examapp.view.ClientAdapter;
import com.andrei.examapp.view.GenericViewModel;

public class ClientSongActivity extends AppCompatActivity {
    public static final String EXTRA_GENRE =
            "com.andrei.examapp.EXTRA_GENRE";
    public static final int ADD_FAV_ENTITY_REQUEST = 2;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    GenericViewModel viewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_song);

        Intent intent = getIntent();
        String genre = intent.getStringExtra(EXTRA_GENRE);
        setTitle(genre);

        recyclerView = findViewById(R.id.rv_client);
        progressBar = findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.VISIBLE);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        ClientAdapter adapter = new ClientAdapter();
        recyclerView.setAdapter(adapter);

        viewModel = ViewModelProviders.of(this).get(GenericViewModel.class);

        viewModel.getSongByGenre(genre);
        viewModel.clientEntities.observe(this,songs->{
            adapter.setEntities(songs);
        });


        viewModel.isGone.observe(this,flag->{
            if(flag){
                progressBar.setVisibility(View.GONE);
            }else {
                progressBar.setVisibility(View.VISIBLE);
            }
        });
        viewModel.toastMsg.observe(this,msg->{
            Util.toast(ClientSongActivity.this,msg);
        });

        adapter.setEventListener(new MyCallback() {
            @Override
            public void onCall(GenericEntity entity) {
                if (Util.isNetworkAvailable(ClientSongActivity.this)) {
                    Intent intent1 = new Intent(ClientSongActivity.this,SongDetailsActivity.class);
                    intent1.putExtra(SongDetailsActivity.EXTRA_DETAIL_ENTITY,entity);
                    startActivityForResult(intent1,ADD_FAV_ENTITY_REQUEST);
                }else{
                    Util.toast(ClientSongActivity.this,"Device offline");
                }
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == ADD_FAV_ENTITY_REQUEST && resultCode == RESULT_OK){
            GenericEntity entity = (GenericEntity) data.getSerializableExtra(SongDetailsActivity.EXTRA_DETAIL_ENTITY);
            progressBar.setVisibility(View.VISIBLE);
            viewModel.addToFav(entity);
        }else {
            Util.toast(ClientSongActivity.this,"Entity was not added");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_entity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item1:
                Intent intent = new Intent(ClientSongActivity.this,FavoriteSongsActivity.class);
                startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
}
