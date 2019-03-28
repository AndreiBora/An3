package com.andrei.examapp;

import android.arch.lifecycle.ViewModelProviders;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;

import com.andrei.examapp.util.Util;
import com.andrei.examapp.view.ClerkAdapter;
import com.andrei.examapp.view.ClientAdapter;
import com.andrei.examapp.view.GenericViewModel;

public class LoanBikesActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    GenericViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loan_bikes);
        setTitle("Loan bikes");


        recyclerView = findViewById(R.id.rv_loan);
        progressBar = findViewById(R.id.progress_bar);
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
            Util.toast(LoanBikesActivity.this,msg);
        });

        viewModel.loanBikes.observe(this,genericEntities -> {
            adapter.setEntities(genericEntities);
        });

    }
}
