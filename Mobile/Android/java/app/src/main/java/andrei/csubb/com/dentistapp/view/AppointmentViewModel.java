package andrei.csubb.com.dentistapp.view;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.content.Context;
import android.net.wifi.WifiManager;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import andrei.csubb.com.dentistapp.MainActivity;
import andrei.csubb.com.dentistapp.model.Appointment;
import andrei.csubb.com.dentistapp.repository.AppointmentRepository;

public class AppointmentViewModel extends AndroidViewModel {
    private AppointmentRepository repository;
    private LiveData<List<Appointment>> appointments;

    public AppointmentViewModel(@NonNull Application application) {
        super(application);
        repository = new AppointmentRepository(application);
        appointments = repository.getAll();
    }

    public void insert(Appointment appointment, Context context, WifiManager wifiManager) {
        if (wifiManager.isWifiEnabled()) {
            repository.insert(appointment, context);
        } else {
            repository.insertOffline(appointment);
        }
    }

    public void update(Appointment appointment, MainActivity context) {
        repository.update(appointment,context);
    }

    public void delete(Appointment appointment,Context context) {
        repository.delete(appointment,context);
    }

    public void deleteAll(Appointment appointment) {
        repository.deleteAll();
    }

    public LiveData<List<Appointment>> getAppointments() {
        return appointments;
    }

    public void pushNotSync() {
        List<Appointment> notInSyncApps = new ArrayList<>();
        try {
            for (Appointment appointment : appointments.getValue()) {
                if (appointment.getInSync() == false) {
                    notInSyncApps.add(appointment);
                }
            }
            if(!notInSyncApps.isEmpty()) {
                repository.pushNotSync(notInSyncApps);
            }
        }catch (NullPointerException e){
            //do nothing
        }
    }
}
