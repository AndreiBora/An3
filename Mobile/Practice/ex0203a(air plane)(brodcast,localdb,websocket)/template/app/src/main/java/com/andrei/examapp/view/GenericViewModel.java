package com.andrei.examapp.view;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.andrei.examapp.model.GenericEntity;
import com.andrei.examapp.repository.Repository;

import java.util.ArrayList;
import java.util.List;

public class GenericViewModel extends AndroidViewModel {
    private Repository repository;
    public LiveData<Boolean> isGone;
    public MutableLiveData<String> toastMsg;
    public LiveData<List<GenericEntity>> employeeEntities;
    public MutableLiveData<List<GenericEntity>> managerEntities;

    public GenericViewModel(@NonNull Application application) {
        super(application);
        repository = new Repository(application);
        isGone = repository.getIsGone();
        toastMsg = repository.getToastMsg();
        employeeEntities = repository.getEmployeeEntities();
        managerEntities = repository.getManagerEntities();
    }

    public void delete(GenericEntity entity) {
        repository.delete(entity);
    }

    public void add(GenericEntity entity) {
        repository.add(entity);
    }

    public void update(GenericEntity entity) {
        repository.update(entity);
    }

    public void pushNotSync() {
        List<GenericEntity> entitiesNotSync =new ArrayList<>();
        try {
            for (GenericEntity entity : employeeEntities.getValue()) {
                if (!entity.getInSync()) {
                    entitiesNotSync.add(entity);
                }
            }
            if (entitiesNotSync.size() > 0) {
                repository.pushNotSync(entitiesNotSync);
            }
        }catch (NullPointerException e){

        }
    }

    public void deleteAll() {
        repository.deleteAll();
    }

    public void addMoreMiles(GenericEntity entity, Integer miles) {
        repository.addMiles(entity,miles);
    }
}
