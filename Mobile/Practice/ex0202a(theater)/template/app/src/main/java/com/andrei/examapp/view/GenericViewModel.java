package com.andrei.examapp.view;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.andrei.examapp.model.GenericEntity;
import com.andrei.examapp.repository.Repository;
import com.andrei.examapp.util.ShowDetailCallback;

import java.util.List;

public class GenericViewModel extends AndroidViewModel {
    private Repository repository;
    public LiveData<Boolean> isGone;
    public MutableLiveData<String> toastMsg;
    public MutableLiveData<List<GenericEntity>> theaterEntities;
    public MutableLiveData<List<GenericEntity>> clientEntities;

    public GenericViewModel(@NonNull Application application) {
        super(application);
        repository = new Repository(application);
        isGone = repository.getIsGone();
        toastMsg = repository.getToastMsg();
        theaterEntities = repository.getTheaterEntities();
        clientEntities = repository.getClientEntities();
    }

    public void delete(GenericEntity entity) {
        repository.delete(entity);
    }

    public void add(GenericEntity entity) {
        repository.add(entity);
    }

    public void confirm(GenericEntity entity) {
        repository.confirm(entity);
    }

    public void markAvailable() {
        this.repository.markAvailable();
    }

    public void deleteAll() {
        repository.deleteAll();
    }

    public void reserve(GenericEntity entity, ShowDetailCallback showDetailCallback) {
        repository.reserve(entity,showDetailCallback);
    }
}
