package com.andrei.examapp.repository;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.os.AsyncTask;
import android.util.Log;

import com.andrei.examapp.dao.GenericDao;
import com.andrei.examapp.db.GenericDatabase;
import com.andrei.examapp.model.GenericEntity;
import com.andrei.examapp.util.LoanCallback;
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
    private static final String URL = "http://192.168.43.71:2028";
    private static final String WS = "ws://192.168.43.71:2028";
    private OkHttpClient client = new OkHttpClient.Builder().build();
    private Gson gson = new Gson();
    public MutableLiveData<String> toastMsg;
    private MutableLiveData<Boolean> isGone;
    private GenericDao dao;
    private MutableLiveData<List<GenericEntity>> userEntities;
    private MutableLiveData<List<GenericEntity>> ownerEntities;
    private LiveData<List<GenericEntity>> loanBikes;

    public Repository(Application application) {
        toastMsg = new MutableLiveData<>();
        isGone = new MutableLiveData<>();
        userEntities = new MutableLiveData<>();
        ownerEntities = new MutableLiveData<>();
        GenericDatabase db = GenericDatabase.getInstance(application);
        dao = db.genericDao();
        loanBikes = dao.getAll();

        Request request = new Request.Builder()
                .url(WS)
                .build();
//        WebSocket webSocket = client.newWebSocket(request, new WebSockerListener(entities));
    }

    public LiveData<Boolean> getIsGone() {
        return isGone;
    }

    public MutableLiveData<String> getToastMsg() {
        return toastMsg;
    }

    public MutableLiveData<List<GenericEntity>> getUserEntities() {
        String url = URL + "/bikes";
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
                Log.e("getUserEntities", "Fail to get data from server");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                List<GenericEntity> entities1 = Arrays.asList(gson.fromJson(response.body().string(), GenericEntity[].class));
                userEntities.postValue(entities1);
                isGone.postValue(true);
                Log.d("getUserEntities", "success retriving bikes");
            }
        });
        return userEntities;
    }

    public void delete(GenericEntity entity) {
        String url = URL + "/bike/" + entity.getId().toString();

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
                    for (GenericEntity s : ownerEntities.getValue()) {
                        if (s.getId() != entityDel.getId()) {
                            cpy.add(s);
                        }
                    }
                    ownerEntities.postValue(cpy);
                    toastMsg.postValue("Bike successfully remove");
                    isGone.postValue(true);
                    Log.d("delete", entityDel.toString() + "was removed");
                } else {
                    toastMsg.postValue("Bike was not removed");
                    Log.e("delete", "Bike was not removed");
                    isGone.postValue(true);
                }
            }
        });
    }

    public void add(GenericEntity entity) {
        String url = URL + "/bike";
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
                Log.e("add","Fail to get data from server");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.code() == 200) {
                    GenericEntity newEntity = gson.fromJson(response.body().string(), GenericEntity.class);
                    List<GenericEntity> cpy = new ArrayList<>(ownerEntities.getValue());
                    cpy.add(newEntity);
                    ownerEntities.postValue(cpy);
                    toastMsg.postValue("Bike was successfully added");
                    isGone.postValue(true);
                    Log.d("add", newEntity.toString() + "was saved");
                } else {
                    toastMsg.postValue("Bike was not saved");
                    Log.e("add", "Bike was not saved");
                    isGone.postValue(true);
                }
            }
        });
    }

    public void loan(GenericEntity entity, LoanCallback loanCallback) {
        String url = URL + "/loan";
        JSONObject json = new JSONObject();
        try {
            json.put("id",entity.getId());
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
                Log.e("loan", "Fail to get data from server");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.code() == 200) {
                    GenericEntity newEntity = gson.fromJson(response.body().string(), GenericEntity.class);
                    new InsertAsyncTask(dao).execute(newEntity);
                    toastMsg.postValue("Bike was successfully loan");
                    isGone.postValue(true);
                    Log.d("loan", newEntity.toString() + "was loaned");
                    loanCallback.onLoan(newEntity);
                } else {
                    toastMsg.postValue("Bike was not loaned");
                    Log.e("loan", "Bike was not loaned");
                    isGone.postValue(true);
                }
            }
        });
    }

    public MutableLiveData<List<GenericEntity>> getOwnerEntities() {
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
                Log.e("getOwnerEntities", "Fail to get data from server");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                List<GenericEntity> entities1 = Arrays.asList(gson.fromJson(response.body().string(), GenericEntity[].class));
                Comparator<GenericEntity> comparator = (e1, e2)-> e2.getType().compareTo(e1.getType());
                Collections.sort(entities1,comparator);
                ownerEntities.postValue(entities1);
                isGone.postValue(true);
                Log.d("getOwnerEntities", "success retriving all bikes");
            }
        });
        return ownerEntities;
    }

    public LiveData<List<GenericEntity>> getLoanBikes() {
        isGone.postValue(true);
        return loanBikes;
    }

    public void returnBike(GenericEntity entity) {
        String url = URL + "/return";
        JSONObject json = new JSONObject();
        try {
            json.put("id",entity.getId());
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
                Log.e("loan", "Fail to get data from server");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.code() == 200) {
                    GenericEntity newEntity = gson.fromJson(response.body().string(), GenericEntity.class);
                    new DeleteAsyncTask(dao).execute(newEntity);
                    toastMsg.postValue("Bike was successfully returned");
                    isGone.postValue(true);
                    Log.d("loan", newEntity.toString() + "was returned");
                } else {
                    toastMsg.postValue("Bike was not not returned");
                    Log.e("loan", "Bike was not returned");
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

    private static class DeleteAsyncTask extends AsyncTask<GenericEntity, Void, Void> {
        private GenericDao dao;

        public DeleteAsyncTask(GenericDao dao) {
            this.dao = dao;
        }

        @Override
        protected Void doInBackground(GenericEntity... genericEntities) {
            dao.delete(genericEntities[0]);
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


