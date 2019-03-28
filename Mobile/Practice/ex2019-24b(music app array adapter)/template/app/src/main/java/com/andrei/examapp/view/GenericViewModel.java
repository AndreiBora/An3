package com.andrei.examapp.view;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.andrei.examapp.model.GenericEntity;
import com.andrei.examapp.repository.Repository;

import java.util.List;

public class GenericViewModel extends AndroidViewModel {
    private Repository repository;
    public LiveData<Boolean> isGone;
    public MutableLiveData<String> toastMsg;
    public MutableLiveData<List<String>> genres;
    public MutableLiveData<List<GenericEntity>> clientEntities;
    public MutableLiveData<List<GenericEntity>> clerkEntities;
    public LiveData<List<GenericEntity>> favoriteEntities;


    public GenericViewModel(@NonNull Application application) {
        super(application);
        repository = new Repository(application);
        isGone = repository.getIsGone();
        toastMsg = repository.getToastMsg();
        genres = repository.getGenres();
        clerkEntities = repository.getClerkEntities();
        favoriteEntities = repository.getFavoriteEntities();
    }

    public void delete(GenericEntity entity) {
        repository.delete(entity);
    }

    public void add(GenericEntity entity) {
        repository.add(entity);
    }

    public void getSongByGenre(String genre) {
        clientEntities = this.repository.getByGenre(genre);
    }

    public void addToFav(GenericEntity entity) {
        repository.addToFav(entity);
    }
}
