package com.andrei.examapp;

import android.arch.lifecycle.ViewModelProviders;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
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
import com.andrei.examapp.view.ClientAdapter;
import com.andrei.examapp.view.GenericViewModel;

public class ClientActivity extends AppCompatActivity{
    public static final int EDIT_ENTITY_REQUEST = 2;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    GenericViewModel viewModel;
    private FloatingActionButton btnClear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client);
        setTitle("Employee");

        recyclerView = findViewById(R.id.rv_client);
        progressBar = findViewById(R.id.progress_bar);
        btnClear = findViewById(R.id.btn_clear);
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

        viewModel.employeeEntities.observe(this,genericEntities -> {
            adapter.setEntities(genericEntities);
        });

        adapter.setEventListener(new MyCallback() {
            @Override
            public void onCall(GenericEntity entity) {
                Intent intent = new Intent(ClientActivity.this, EditEntityActivity.class);
                intent.putExtra(EditEntityActivity.EXTRA_EDIT_ENTITY,entity);
                startActivityForResult(intent, EDIT_ENTITY_REQUEST);
            }

            @Override
            public void onCall(GenericEntity entity, Integer miles) {
                viewModel.addMoreMiles(entity,miles);
            }

        });

        btnClear.setOnClickListener(v->{
            viewModel.deleteAll();
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == EDIT_ENTITY_REQUEST && resultCode == RESULT_OK){
            GenericEntity entity = (GenericEntity) data.getSerializableExtra(EditEntityActivity.EXTRA_EDIT_ENTITY);
            progressBar.setVisibility(View.VISIBLE);
            viewModel.update(entity);
        }else {
            Util.toast(ClientActivity.this,"Entity was not added");
        }
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

    @Override
    protected void onStart() {
        super.onStart();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkReceiver,intentFilter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(networkReceiver);
    }

    private BroadcastReceiver networkReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(Util.isNetworkAvailable(ClientActivity.this)) {
                viewModel.pushNotSync();
            }
        }
    };
}
