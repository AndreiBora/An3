package com.andrei.examapp;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.ProgressBar;

import com.andrei.examapp.model.GenericEntity;
import com.andrei.examapp.util.Util;
import com.andrei.examapp.view.ClerkAdapter;
import com.andrei.examapp.view.GenericViewModel;

public class ClerkActivity extends AppCompatActivity {
    public static final int ADD_ENTITY_REQUEST = 1;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    GenericViewModel viewModel;
    private FloatingActionButton btnAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clerk);
        setTitle("Clerk");

        recyclerView = findViewById(R.id.rv_clerk);
        progressBar = findViewById(R.id.progress_bar);
        btnAdd = findViewById(R.id.btn_add_entity);
        progressBar.setVisibility(View.VISIBLE);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        ClerkAdapter adapter = new ClerkAdapter();
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
            Util.toast(ClerkActivity.this,msg);
        });

        viewModel.clerkEntities.observe(this,genericEntities -> {
            adapter.setEntities(genericEntities);
        });


        //delete item
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder viewHolder1) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
                progressBar.setVisibility(View.VISIBLE);
                viewModel.delete(adapter.getEntityAt(viewHolder.getAdapterPosition()));
            }
        }).attachToRecyclerView(recyclerView);

        //add new entity

        btnAdd.setOnClickListener(v->{
            if(Util.isNetworkAvailable(ClerkActivity.this)) {
                Intent intent = new Intent(ClerkActivity.this, AddEditEntityActivity.class);
                intent.putExtra(AddEditEntityActivity.EXTRA_ADD_EDIT_ENTITY, new GenericEntity());
                startActivityForResult(intent, ADD_ENTITY_REQUEST);
            }else{
                Util.toast(ClerkActivity.this,"Device offline");
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == ADD_ENTITY_REQUEST && resultCode == RESULT_OK){
            GenericEntity entity = (GenericEntity) data.getSerializableExtra(AddEditEntityActivity.EXTRA_ADD_EDIT_ENTITY);
            progressBar.setVisibility(View.VISIBLE);
            viewModel.add(entity);
        }else {
            Util.toast(ClerkActivity.this,"Entity was not added");
        }
    }


}
