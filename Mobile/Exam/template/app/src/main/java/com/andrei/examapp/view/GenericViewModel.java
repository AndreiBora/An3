package com.andrei.examapp.view;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.andrei.examapp.model.GenericEntity;
import com.andrei.examapp.repository.Repository;
import com.andrei.examapp.util.LoanCallback;

import java.util.List;

public class GenericViewModel extends AndroidViewModel {
    private Repository repository;
    public LiveData<Boolean> isGone;
    public MutableLiveData<String> toastMsg;
    public MutableLiveData<List<GenericEntity>> userEntities;
    public MutableLiveData<List<GenericEntity>> ownerEntities;
    public LiveData<List<GenericEntity>> loanBikes;

    public GenericViewModel(@NonNull Application application) {
        super(application);
        repository = new Repository(application);
        isGone = repository.getIsGone();
        toastMsg = repository.getToastMsg();
        userEntities = repository.getUserEntities();
        ownerEntities = repository.getOwnerEntities();
        loanBikes =repository.getLoanBikes();
    }

    public void delete(GenericEntity entity) {
        repository.delete(entity);
    }

    public void add(GenericEntity entity) {
        repository.add(entity);
    }

    public void loan(GenericEntity entity, LoanCallback loanCallback) {
        repository.loan(entity,loanCallback);
    }

    public void returnBike(GenericEntity entity) {
        repository.returnBike(entity);
    }
}
