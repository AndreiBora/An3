package com.andrei.examapp.repository;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.os.AsyncTask;
import android.util.Log;

import com.andrei.examapp.dao.GenericDao;
import com.andrei.examapp.db.GenericDatabase;
import com.andrei.examapp.model.GenericEntity;
import com.andrei.examapp.util.BuyCallback;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;

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
    private static final String URL = "http://192.168.43.71:4001";
    private static final String WS = "ws://192.168.43.71:4001";
    private OkHttpClient client = new OkHttpClient.Builder().build();
    private Gson gson = new Gson();
    public MutableLiveData<String> toastMsg;
    private MutableLiveData<Boolean> isGone;
    private GenericDao dao;
    private MutableLiveData<List<GenericEntity>> clientEntities;
    private MutableLiveData<List<GenericEntity>> employeesEntities;
    private LiveData<List<GenericEntity>> purchaseEntities;


    public Repository(Application application) {
        toastMsg = new MutableLiveData<>();
        isGone = new MutableLiveData<>();
        clientEntities = new MutableLiveData<>();
        employeesEntities = new MutableLiveData<>();

        GenericDatabase db = GenericDatabase.getInstance(application);
        dao = db.genericDao();
        purchaseEntities = dao.getAllLive();

        Request request = new Request.Builder()
                .url(WS)
                .build();
        WebSocket webSocket = client.newWebSocket(request, new WebSockerListener(employeesEntities));
    }

    public LiveData<Boolean> getIsGone() {
        return isGone;
    }

    public MutableLiveData<String> getToastMsg() {
        return toastMsg;
    }

    public MutableLiveData<List<GenericEntity>> getClientEntitites() {
        String url = URL + "/games";
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
                Log.e("getClientEntitites", "Fail to get data from server");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                List<GenericEntity> entities1 = Arrays.asList(gson.fromJson(response.body().string(), GenericEntity[].class));
                clientEntities.postValue(entities1);
                isGone.postValue(true);
                Log.d("getClientEntitites", "success retriving data");
            }
        });
        return clientEntities;
    }

    public void delete(GenericEntity entity) {
        String url = URL + "/removeGame";

        JSONObject json = new JSONObject();
        try {
            json.put("id", entity.getId());
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
                toastMsg.postValue("Server error");
                Log.e("delete", "Server error");
                isGone.postValue(true);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.code() == 200) {
                    GenericEntity entityDel = gson.fromJson(response.body().string(), GenericEntity.class);
                    List<GenericEntity> cpy = new ArrayList<>();
                    for (GenericEntity s : employeesEntities.getValue()) {
                        if (s.getId() != entityDel.getId()) {
                            cpy.add(s);
                        }
                    }
                    employeesEntities.postValue(cpy);
                    toastMsg.postValue("Game successfully remove");
                    isGone.postValue(true);
                    Log.d("delete", entityDel.toString() + "was removed");
                } else {
                    toastMsg.postValue("Game was not removed");
                    Log.e("delete", "Game was not removed");
                    isGone.postValue(true);
                }
            }
        });
    }

    public void add(GenericEntity entity) {
        Log.e("test", entity.toString());
        String url = URL + "/addGame";
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
                    List<GenericEntity> cpy = new ArrayList<>(employeesEntities.getValue());
                    cpy.add(newEntity);
                    employeesEntities.postValue(cpy);
                    toastMsg.postValue("Game was successfully added");
                    isGone.postValue(true);
                    Log.d("add", newEntity.toString() + "was saved");
                } else {
                    toastMsg.postValue("Game was not saved");
                    Log.e("add", "Game was not saved");
                    isGone.postValue(true);
                }
            }
        });
    }

    public void buyGame(GenericEntity entity, BuyCallback buyCallback) {
        String url = URL + "/buyGame";
        JSONObject json = new JSONObject();
        try {
            json.put("id", entity.getId().toString());
            json.put("quantity", entity.getQuantity().toString());
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
                    List<GenericEntity> cpy = new ArrayList<>();
                    for (GenericEntity s : clientEntities.getValue()) {
                        if (s.getId() != newEntity.getId()) {
                            cpy.add(s);
                        } else {
                            cpy.add(newEntity);
                        }
                    }
                    clientEntities.postValue(cpy);
                    new InsertAsyncTask(dao).execute(newEntity);
                    toastMsg.postValue("Game was successfully bougth");
                    List<GenericEntity> purchaseGames = null;
                    try {
                        purchaseGames = new GetAsyncTask(dao).execute().get();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    buyCallback.onBuy(purchaseGames);
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

    public MutableLiveData<List<GenericEntity>> getEmployeeEntities() {
        String url = URL + "/all";
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
                Log.e("getEmployeeEntities", "Fail to get data from server");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                List<GenericEntity> entities1 = Arrays.asList(gson.fromJson(response.body().string(), GenericEntity[].class));
                employeesEntities.postValue(entities1);
                isGone.postValue(true);
                Log.d("getEmployeeEntities", "success retriving data");
            }
        });
        return employeesEntities;
    }

    public LiveData<List<GenericEntity>> getPurchaseEntities() {
        return purchaseEntities;
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

    private static class DeleteAllAsyncTask extends AsyncTask<Void, Void, Void> {
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

    private static class GetAsyncTask extends AsyncTask<Void, Void, List<GenericEntity>> {
        private GenericDao dao;

        public GetAsyncTask(GenericDao dao) {
            this.dao = dao;
        }

        @Override
        protected List<GenericEntity> doInBackground(Void... voids) {
            return this.dao.getAll();
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


