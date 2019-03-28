package andrei.csubb.com.dentistapp.repository;

import android.app.Activity;
import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import andrei.csubb.com.dentistapp.MainActivity;
import andrei.csubb.com.dentistapp.R;
import andrei.csubb.com.dentistapp.Utils.Util;
import andrei.csubb.com.dentistapp.dao.AppointmentDao;
import andrei.csubb.com.dentistapp.db.AppointmentDatabase;
import andrei.csubb.com.dentistapp.model.Appointment;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class AppointmentRepository {
    private AppointmentDao appointmentDao;
    private LiveData<List<Appointment>> appointments;
    private static final String URL = "http://d7019379.ngrok.io";
    private OkHttpClient client = new OkHttpClient.Builder().build();

    public AppointmentRepository(Application application) {
        AppointmentDatabase database = AppointmentDatabase.getInstance(application);
        appointmentDao = database.appointmentDao();
        appointments = appointmentDao.getAll();
    }

    public void insert(Appointment appointment,Context context) {
        //call to network
        String json = appointment.toJson().toString();
        String url = new StringBuilder().append(URL).append("/api/appointments").toString();
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), json);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Activity mainActivity = (Activity) context;
                mainActivity.runOnUiThread(() -> {
                    Toast.makeText(mainActivity, "Add appointment failed", Toast.LENGTH_SHORT).show();
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                JSONObject json = null;
                try {
                    json = new JSONObject(response.body().string());
                    appointment.setId(json.getInt("id"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Activity mainActivity = (Activity) context;
                mainActivity.runOnUiThread(() -> {
                    Toast.makeText(mainActivity, "Appointment was successfully added", Toast.LENGTH_SHORT).show();
                });
                appointment.setInSync(true);
                new InsertAppointmentAsyncTask(appointmentDao).execute(appointment);
            }
        });
    }

    public void insertOffline(Appointment appointment){
        new InsertAppointmentAsyncTask(appointmentDao).execute(appointment);
    }

    public void update(Appointment appointment, MainActivity context) {
        String jsonObject = appointment.toJson().toString();
        RequestBody body = RequestBody.create(MediaType.parse("application/json"),jsonObject);
        Request request = new Request.Builder()
                .url(URL + "/api/appointments/" + appointment.getId().toString())
                .put(body)
                .build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(response.code() == 200){
                    new UpdateAppointmentAsyncTask(appointmentDao).execute(appointment);

                    Activity mainActivity = (Activity) context;
                    mainActivity.runOnUiThread(() -> {
                        Toast.makeText(mainActivity, "Appointment successfully updated", Toast.LENGTH_SHORT).show();
                    });
                }else{
                    Activity mainActivity = (Activity) context;
                    mainActivity.runOnUiThread(() -> {
                        Toast.makeText(mainActivity, "Appointment was not updated", Toast.LENGTH_SHORT).show();
                        //refresh ui in case of insucess
                        RecyclerView rv = mainActivity.findViewById(R.id.appointments_view);
                        rv.getAdapter().notifyDataSetChanged();
                    });
                }
            }
        });


    }

    public void delete(Appointment appointment,Context context) {
        String url = new StringBuilder().append(URL).append("/api/appointments/" + appointment.getId().toString()).toString();
        Request request = new Request.Builder()
                .url(url)
                .delete()
                .build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Activity mainActivity = (Activity) context;
                mainActivity.runOnUiThread(() -> {
                    Toast.makeText(mainActivity, "Failed to delete appointment", Toast.LENGTH_SHORT).show();
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(response.code() == 204){
                    Activity mainActivity = (Activity) context;
                    mainActivity.runOnUiThread(() -> {
                        Toast.makeText(mainActivity, "Appointment was successfully deleted", Toast.LENGTH_SHORT).show();
                    });
                    new DeleteAppointmentAsyncTask(appointmentDao).execute(appointment);
                }else {
                    Activity mainActivity = (Activity) context;
                    mainActivity.runOnUiThread(() -> {
                        Toast.makeText(mainActivity, response.message(), Toast.LENGTH_SHORT).show();
                        RecyclerView rv = mainActivity.findViewById(R.id.appointments_view);
                        rv.getAdapter().notifyDataSetChanged();
                    });
                }
            }
        });

    }

    public void deleteAll() {
        new DeleteAllAppointmentAsyncTask(appointmentDao).execute();
    }

    public LiveData<List<Appointment>> getAll() {
        return appointments;
    }

    public void pushNotSync(List<Appointment> notInSyncApps) {
        //call to network
        JSONArray jsonArray = new JSONArray();
        for(Appointment appointment: notInSyncApps){
            jsonArray.put(appointment.toJson());
        }

        String url = new StringBuilder().append(URL).append("/api/sync/appointments").toString();
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), jsonArray.toString());
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                JSONArray jsonArray1 = null;
                try {
                    jsonArray1 = new JSONArray(response.body().string());
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                //delete from db the appointments not in sync and add the received ones
                //this is done because I can't update the primary key from sqlite and
                //I want the PK from the server and phone to match
                appointmentDao.deleteAllNotInSync();
                for(int i =0 ;i<jsonArray1.length();i++){
                    try {
                        JSONObject object = jsonArray1.getJSONObject(i);
                        Appointment appointment = Util.appFromJSON(object);
                        appointment.setInSync(true);
                        new InsertAppointmentAsyncTask(appointmentDao).execute(appointment);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }
        });
    }

    private static class InsertAppointmentAsyncTask extends AsyncTask<Appointment, Void, Void> {
        private AppointmentDao appointmentDao;

        public InsertAppointmentAsyncTask(AppointmentDao appointmentDao) {
            this.appointmentDao = appointmentDao;
        }

        @Override
        protected Void doInBackground(Appointment... appointments) {
            appointmentDao.insert(appointments[0]);
            return null;
        }
    }

    private static class UpdateAppointmentAsyncTask extends AsyncTask<Appointment, Void, Void> {
        private AppointmentDao appointmentDao;

        public UpdateAppointmentAsyncTask(AppointmentDao appointmentDao) {
            this.appointmentDao = appointmentDao;
        }

        @Override
        protected Void doInBackground(Appointment... appointments) {
            appointmentDao.update(appointments[0]);
            return null;
        }
    }

    private static class DeleteAppointmentAsyncTask extends AsyncTask<Appointment, Void, Void> {
        private AppointmentDao appointmentDao;

        public DeleteAppointmentAsyncTask(AppointmentDao appointmentDao) {
            this.appointmentDao = appointmentDao;
        }

        @Override
        protected Void doInBackground(Appointment... appointments) {
            appointmentDao.delete(appointments[0]);
            return null;
        }
    }

    private static class DeleteAllAppointmentAsyncTask extends AsyncTask<Void, Void, Void> {
        private AppointmentDao appointmentDao;

        public DeleteAllAppointmentAsyncTask(AppointmentDao appointmentDao) {
            this.appointmentDao = appointmentDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            appointmentDao.deleteAll();
            return null;
        }
    }
}
