package andrei.csubb.com.dentistapp.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import andrei.csubb.com.dentistapp.model.Appointment;

@Dao
public interface AppointmentDao {

    @Insert
    void insert(Appointment appointment);

    @Update
    void update(Appointment appointment);

    @Delete
    void delete(Appointment appointment);

    @Query("DELETE FROM Appointments")
    void deleteAll();

    @Query("DELETE FROM Appointments WHERE inSync = 0")
    void deleteAllNotInSync();

    @Query("SELECT * FROM Appointments")
    LiveData<List<Appointment>> getAll();
}
