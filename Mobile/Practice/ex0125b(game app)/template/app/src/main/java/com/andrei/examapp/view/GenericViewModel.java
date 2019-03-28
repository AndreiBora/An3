package com.andrei.examapp.view;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.andrei.examapp.model.GenericEntity;
import com.andrei.examapp.repository.Repository;
import com.andrei.examapp.util.BuyCallback;

import java.util.List;

public class GenericViewModel extends AndroidViewModel {
    private Repository repository;
    public LiveData<Boolean> isGone;
    public MutableLiveData<String> toastMsg;
    public MutableLiveData<List<GenericEntity>> clientEntities;
    public MutableLiveData<List<GenericEntity>> employeeEntities;
    public LiveData<List<GenericEntity>> purchaseEntities;

    public GenericViewModel(@NonNull Application application) {
        super(application);
        repository = new Repository(application);
        isGone = repository.getIsGone();
        toastMsg = repository.getToastMsg();
        clientEntities = repository.getClientEntitites();
        employeeEntities = repository.getEmployeeEntities();
        purchaseEntities = repository.getPurchaseEntities();
    }

    public void delete(GenericEntity entity) {
        repository.delete(entity);
    }

    public void add(GenericEntity entity) {
        repository.add(entity);
    }

    public void purchaseGame(GenericEntity entity, BuyCallback buyCallback) {
        repository.buyGame(entity,buyCallback);
    }
}
