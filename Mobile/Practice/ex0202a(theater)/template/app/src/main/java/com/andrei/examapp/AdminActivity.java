package com.andrei.examapp;

import android.arch.lifecycle.ViewModelProviders;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.ProgressBar;

import com.andrei.examapp.model.GenericEntity;
import com.andrei.examapp.util.MyCallback;
import com.andrei.examapp.util.Util;
import com.andrei.examapp.view.ClerkAdapter;
import com.andrei.examapp.view.GenericViewModel;

public class AdminActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    GenericViewModel viewModel;
    private FloatingActionButton btnClear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        setTitle("Admin");


        recyclerView = findViewById(R.id.rv_admin);
        progressBar = findViewById(R.id.progress_bar);
        btnClear = findViewById(R.id.btn_clear);
        progressBar.setVisibility(View.VISIBLE);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        ClerkAdapter adapter = new ClerkAdapter();
        recyclerView.setAdapter(adapter);

        viewModel = ViewModelProviders.of(this).get(GenericViewModel.class);


        viewModel.toastMsg.observe(this,msg->{
            Util.toast(AdminActivity.this,msg);
        });
//
//        viewModel.theaterEntities.observe(this,seats->{
//            adapter.setEntities(seats);
//        });

        viewModel.isGone.observe(this,flag->{
            if(flag){
                progressBar.setVisibility(View.GONE);
            }else {
                progressBar.setVisibility(View.VISIBLE);
            }
        });
        adapter.setEventListener(new MyCallback() {
            @Override
            public void onConfirm(GenericEntity entity) {
                viewModel.confirm(entity);
            }

            @Override
            public void onReserved(GenericEntity entity) {

            }
        });

        //delete item
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder viewHolder1) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
                viewModel.delete(adapter.getEntityAt(viewHolder.getAdapterPosition()));
            }
        }).attachToRecyclerView(recyclerView);

        //add new entity

        btnClear.setOnClickListener(v->{
            viewModel.deleteAll();
        });

    }
}
