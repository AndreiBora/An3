package com.andrei.examapp.repository;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.os.AsyncTask;
import android.util.Log;

import com.andrei.examapp.dao.GenericDao;
import com.andrei.examapp.db.GenericDatabase;
import com.andrei.examapp.model.GenericEntity;
import com.andrei.examapp.view.GenericViewModel;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Entity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;

public class Repository {
    private static final String URL = "http://192.168.43.71:4023";
    private static final String WS = "ws://192.168.43.71:4023";
    private OkHttpClient client = new OkHttpClient.Builder().build();
    private Gson gson = new Gson();
    public MutableLiveData<String> toastMsg;
    private MutableLiveData<Boolean> isGone;
    private GenericDao dao;
    private LiveData<List<GenericEntity>> employeeEntities;
    private MutableLiveData<List<GenericEntity>> managerEntities;


    public Repository(Application application) {
        toastMsg = new MutableLiveData<>();
        isGone = new MutableLiveData<>();

        GenericDatabase db = GenericDatabase.getInstance(application);
        dao = db.genericDao();
        employeeEntities = dao.getAll();
        managerEntities = new MutableLiveData<>();

        Request request = new Request.Builder()
                .url(WS)
                .build();
        WebSocket webSocket = client.newWebSocket(request, new WebSockerListener(managerEntities));
    }

    public LiveData<Boolean> getIsGone() {
        return isGone;
    }

    public MutableLiveData<String> getToastMsg() {
        return toastMsg;
    }

    public LiveData<List<GenericEntity>> getEmployeeEntities() {
        String url = URL + "/airplanes";
        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();
        Call call = client.newCall(request);

        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                toastMsg.postValue("Fail to get data from server");
                isGone.postValue(true);
                Log.e("getEntities", "Fail to get data from server");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                List<GenericEntity> entities1 = Arrays.asList(gson.fromJson(response.body().string(), GenericEntity[].class));
                new DeleteAllAsyncTask(dao).execute();
                for(GenericEntity entity:entities1){
                    entity.setInSync(true);
                    new InsertAsyncTask(dao).execute(entity);
                }
                isGone.postValue(true);
                Log.d("getEntities", "success retriving data");
            }
        });
        return employeeEntities;
    }

    public void delete(GenericEntity entity) {
        String url = URL + "/airplane/" + entity.getId().toString();

        Request request = new Request.Builder()
                .url(url)
                .delete()
                .build();

        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                toastMsg.postValue("Server error");
                Log.e("delete","Server error");
                isGone.postValue(true);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.code() == 200) {
                    GenericEntity entityDel = gson.fromJson(response.body().string(), GenericEntity.class);
                    List<GenericEntity> cpy = new ArrayList<>();
                    for (GenericEntity s : managerEntities.getValue()) {
                        if (s.getId() != entityDel.getId()) {
                            cpy.add(s);
                        }
                    }
                    managerEntities.postValue(cpy);
                    toastMsg.postValue("Airplane successfully remove");
                    isGone.postValue(true);
                    Log.d("delete", entityDel.toString() + "was removed");
                } else {
                    toastMsg.postValue("Airplane was not removed");
                    Log.e("delete", "Airplane was not removed");
                    isGone.postValue(true);
                }
            }
        });
    }

    public void add(GenericEntity entity) {
        String url = URL + "/add";
        Gson gson = new Gson();
        String json = gson.toJson(entity);
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), json);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();

        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                toastMsg.postValue("Fail to get data from server");
                isGone.postValue(true);
                Log.e("add", "Fail to get data from server");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.code() == 200) {
                    GenericEntity newEntity = gson.fromJson(response.body().string(), GenericEntity.class);
                    List<GenericEntity> cpy = new ArrayList<>(managerEntities.getValue());
                    cpy.add(newEntity);
                    managerEntities.postValue(cpy);
                    toastMsg.postValue("Airplane was successfully added");
                    isGone.postValue(true);
                    Log.d("add", newEntity.toString() + "was saved");
                } else {
                    toastMsg.postValue("Airplane was not saved");
                    Log.e("add", "Airplane was not saved");
                    isGone.postValue(true);
                }
            }
        });
    }

    public MutableLiveData<List<GenericEntity>> getManagerEntities() {
        String url = URL + "/airplanes";
        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();
        Call call = client.newCall(request);

        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                toastMsg.postValue("Fail to get data from server");
                isGone.postValue(true);
                Log.e("getGenres", "Fail to get data from server");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                List<GenericEntity> entities = Arrays.asList(gson.fromJson(response.body().string(), GenericEntity[].class));
                Comparator<GenericEntity> comparator = (e1, e2)-> e2.getMiles() - e1.getMiles();
                Collections.sort(entities,comparator);
                managerEntities.postValue(entities);
                isGone.postValue(true);
                Log.d("getEntities", "success retriving data");
            }
        });
        return managerEntities;

    }

    public void update(GenericEntity entity) {
        String url = URL + "/update";
        Gson gson = new Gson();
        String json = gson.toJson(entity);
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), json);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();

        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                entity.setInSync(false);
                new UpdateAsyncTask(dao).execute(entity);
                toastMsg.postValue("Server connection failed. Saved locally");
                isGone.postValue(true);
                Log.e("update", "Server connection failed. Saved locally");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.code() == 200) {
                    GenericEntity editEntity = gson.fromJson(response.body().string(), GenericEntity.class);
                    editEntity.setInSync(true);
                    new UpdateAsyncTask(dao).execute(editEntity);
                    toastMsg.postValue("Airplane was successfully updated");
                    isGone.postValue(true);
                    Log.d("add", entity.toString() + "was updated");
                } else {
                    toastMsg.postValue("Airplane was not saved");
                    Log.e("add", "Airplane was not saved");
                    isGone.postValue(true);
                }
            }
        });
    }

    public void pushNotSync(List<GenericEntity> entitiesNotSync) {
        for(GenericEntity entity:entitiesNotSync){
            update(entity);
        }
    }

    public void deleteAll() {
        new DeleteAllAsyncTask(dao).execute();
    }

    public void addMiles(GenericEntity entity, Integer miles) {
        String url = URL + "/miles";
        JSONObject json = new JSONObject();
        try {
            json.put("id",entity.getId());
            json.put("miles",miles);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestBody body = RequestBody.create(MediaType.parse("application/json"), json.toString());
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();

        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                toastMsg.postValue("Fail to get data from server");
                isGone.postValue(true);
                Log.e("add", "Fail to get data from server");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.code() == 200) {
                    GenericEntity newEntity = gson.fromJson(response.body().string(), GenericEntity.class);
                    new UpdateAsyncTask(dao).execute(newEntity);
                    toastMsg.postValue("Airplane was successfully added");
                    isGone.postValue(true);
                    Log.d("add", newEntity.toString() + "was saved");
                } else {
                    toastMsg.postValue("Airplane was not saved");
                    Log.e("add", "Airplane was not saved");
                    isGone.postValue(true);
                }
            }
        });
    }


    private static class InsertAsyncTask extends AsyncTask<GenericEntity, Void, Void> {
        private GenericDao dao;

        public InsertAsyncTask(GenericDao dao) {
            this.dao = dao;
        }

        @Override
        protected Void doInBackground(GenericEntity... genericEntities) {
            dao.insert(genericEntities[0]);
            return null;
        }
    }
    private static class UpdateAsyncTask extends AsyncTask<GenericEntity, Void, Void> {
        private GenericDao dao;

        public UpdateAsyncTask(GenericDao dao) {
            this.dao = dao;
        }

        @Override
        protected Void doInBackground(GenericEntity... genericEntities) {
            dao.update(genericEntities[0]);
            return null;
        }
    }

    private static class DeleteAllAsyncTask extends AsyncTask<Void,Void,Void>{
        private GenericDao dao;

        public DeleteAllAsyncTask(GenericDao dao) {
            this.dao = dao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            this.dao.deleteAll();
            return null;
        }
    }
}

class WebSockerListener extends WebSocketListener {
    public static final Integer NORMAL_CLOSURE_STATUS = 100;
    private MutableLiveData<List<GenericEntity>> enities;

    public WebSockerListener(MutableLiveData<List<GenericEntity>> enities) {
        this.enities = enities;
    }

    @Override
    public void onOpen(WebSocket webSocket, Response response) {
        super.onOpen(webSocket, response);
    }

    @Override
    public void onMessage(WebSocket webSocket, String text) {
        Gson gson = new Gson();
        GenericEntity entity = gson.fromJson(text, GenericEntity.class);
        List<GenericEntity> enitiesCpy = new ArrayList<>(enities.getValue());
        enitiesCpy.add(entity);
        enities.postValue(enitiesCpy);
        super.onMessage(webSocket, text);
    }

    @Override
    public void onMessage(WebSocket webSocket, ByteString bytes) {
        super.onMessage(webSocket, bytes);
    }

    @Override
    public void onClosing(WebSocket webSocket, int code, String reason) {
        super.onClosing(webSocket, code, reason);
    }

    @Override
    public void onClosed(WebSocket webSocket, int code, String reason) {
        super.onClosed(webSocket, code, reason);
        webSocket.close(NORMAL_CLOSURE_STATUS, "Goodbye!");
    }

}


