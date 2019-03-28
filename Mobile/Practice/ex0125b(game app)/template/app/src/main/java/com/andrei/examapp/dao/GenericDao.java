package com.andrei.examapp.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.andrei.examapp.model.GenericEntity;

import java.util.List;

@Dao
public interface GenericDao {
    @Insert
    void insert(GenericEntity entity);

    @Update
    void update(GenericEntity entity);

    @Delete
    void delete(GenericEntity entity);

    @Query("SELECT * FROM Entities")
    List<GenericEntity> getAll();

    @Query("SELECT * FROM Entities")
    LiveData<List<GenericEntity>> getAllLive();

    @Query("DELETE FROM Entities")
    void deleteAll();
}
