package andrei.csubb.com.dentistapp.db;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import java.util.Date;

import andrei.csubb.com.dentistapp.converter.DateTypeConverter;
import andrei.csubb.com.dentistapp.dao.AppointmentDao;
import andrei.csubb.com.dentistapp.model.Appointment;

@Database(entities = {Appointment.class},version = 1)
@TypeConverters({DateTypeConverter.class})
public abstract class AppointmentDatabase extends RoomDatabase {

    private static AppointmentDatabase instance;
    public abstract AppointmentDao appointmentDao();

    public static synchronized AppointmentDatabase getInstance(Context context){
        if(instance == null){
            instance = Room.databaseBuilder(context.getApplicationContext(),AppointmentDatabase.class,"appointment_db")
                    .fallbackToDestructiveMigration()
                    .addCallback(roomCallback)
                    .build();
        }
        return instance;
    }

    private static RoomDatabase.Callback roomCallback = new RoomDatabase.Callback(){
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new PopulateDbAsyncTask(instance).execute();
        }
    };

    private static class PopulateDbAsyncTask extends AsyncTask<Void,Void,Void> {
        private AppointmentDao appointmentDao;

        private PopulateDbAsyncTask(AppointmentDatabase db) {
            this.appointmentDao = db.appointmentDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            appointmentDao.insert(new Appointment(new Date(),60,"Andrei","Carie",false));
            appointmentDao.insert(new Appointment(new Date(),90,"Toma","Carie",false));
            return null;
        }
    }
}
