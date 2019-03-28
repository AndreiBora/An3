package com.andrei.examapp.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.andrei.examapp.dao.GenericDao;
import com.andrei.examapp.model.GenericEntity;

@Database(entities = {GenericEntity.class},version = 2)
public abstract class GenericDatabase extends RoomDatabase {
    private static GenericDatabase instance;
    public abstract GenericDao genericDao();

    public static synchronized GenericDatabase getInstance(Context context){
        if(instance == null){
            instance = Room.databaseBuilder(context.getApplicationContext(),GenericDatabase.class,"generic_db")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }
}
