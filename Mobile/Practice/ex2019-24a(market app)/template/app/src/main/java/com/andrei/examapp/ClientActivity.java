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
import android.widget.ProgressBar;

import com.andrei.examapp.model.GenericEntity;
import com.andrei.examapp.util.BoughtCallback;
import com.andrei.examapp.util.MyCallback;
import com.andrei.examapp.util.Util;
import com.andrei.examapp.view.ClientAdapter;
import com.andrei.examapp.view.GenericViewModel;

public class ClientActivity extends AppCompatActivity{
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    GenericViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client);
        setTitle("Client");

        recyclerView = findViewById(R.id.rv_client);
        progressBar = findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.VISIBLE);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        ClientAdapter adapter = new ClientAdapter();
        recyclerView.setAdapter(adapter);

        viewModel = ViewModelProviders.of(this).get(GenericViewModel.class);

        viewModel.isGone.observe(this,flag->{
            if(flag){
                progressBar.setVisibility(View.GONE);
            }else {
                progressBar.setVisibility(View.VISIBLE);
            }
        });

        viewModel.toastMsg.observe(this,msg->{
            Util.toast(ClientActivity.this,msg);
        });

        viewModel.clientEntities.observe(this,genericEntities -> {
            adapter.setEntities(genericEntities);
        });

        adapter.setEventListener(new MyCallback() {
            @Override
            public void onCall(GenericEntity entity) {
                viewModel.buy(entity, new BoughtCallback() {
                    @Override
                    public void onBought(GenericEntity entity) {
                        Intent intent = new Intent(ClientActivity.this,EntityDetailActivity.class);
                        intent.putExtra(EntityDetailActivity.EXTRA_DETAIL_ENTITY,entity);
                        startActivity(intent);
                    }
                });
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_entity,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.item1:
//                Intent intent = new Intent(ClientActivity.this,PurchaseActivity.class);
//                startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
}
