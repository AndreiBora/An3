package com.andrei.examapp.repository;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.os.AsyncTask;
import android.util.Log;

import com.andrei.examapp.dao.GenericDao;
import com.andrei.examapp.db.GenericDatabase;
import com.andrei.examapp.model.GenericEntity;
import com.andrei.examapp.util.BoughtCallback;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
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
    private static final String URL = "http://192.168.43.71:2024";
    private static final String WS = "ws://192.168.43.71:2024";
    private OkHttpClient client = new OkHttpClient.Builder().build();
    private Gson gson = new Gson();
    public MutableLiveData<String> toastMsg;
    private MutableLiveData<Boolean> isGone;
    private GenericDao dao;
    private MutableLiveData<List<GenericEntity>> clientEntities;

    public Repository(Application application) {
        toastMsg = new MutableLiveData<>();
        isGone = new MutableLiveData<>();
        clientEntities = new MutableLiveData<>();

        GenericDatabase db = GenericDatabase.getInstance(application);
        dao = db.genericDao();

        Request request = new Request.Builder()
                .url(WS)
                .build();
//        WebSocket webSocket = client.newWebSocket(request, new WebSockerListener(clientEntities));
    }

    public LiveData<Boolean> getIsGone() {
        return isGone;
    }

    public MutableLiveData<String> getToastMsg() {
        return toastMsg;
    }

    public MutableLiveData<List<GenericEntity>> getClientEntities() {
        String url = URL + "/products";
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
                Log.e("getClientEntities", "Fail to get data from server");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                List<GenericEntity> entities1 = Arrays.asList(gson.fromJson(response.body().string(), GenericEntity[].class));
                clientEntities.postValue(entities1);
                isGone.postValue(true);
                Log.d("getClientEntities", "success retriving data");
            }
        });
        return clientEntities;
    }

    public void delete(GenericEntity entity) {
        //TODO
    }

    public void add(GenericEntity entity) {
        //TODO
    }

    public void buy(GenericEntity entity, BoughtCallback boughtCallback) {
        String url = URL + "/buyProduct";
        JSONObject json = new JSONObject();
        try {
            json.put("id",entity.getId());
            json.put("quantity",1);
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
                Log.e("add","Fail to get data from server");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.code() == 200) {
                    GenericEntity newEntity = gson.fromJson(response.body().string(), GenericEntity.class);
                    List<GenericEntity> cpy = new ArrayList<>();
                    for(GenericEntity entity1:clientEntities.getValue()){
                        if(entity1.getId() != newEntity.getId()){
                            cpy.add(entity1);
                        }else{
                            cpy.add(newEntity);
                        }
                    }
                    clientEntities.postValue(cpy);
                    new InsertAsyncTask(dao).execute(newEntity);
                    boughtCallback.onBought(newEntity);
                    toastMsg.postValue("Product was successfully bought");
                    isGone.postValue(true);
                    Log.d("add", newEntity.toString() + "was saved");
                } else {
                    toastMsg.postValue("Song was not saved");
                    Log.e("add", "Entity was not saved");
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


