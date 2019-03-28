package com.andrei.examapp.repository;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.os.AsyncTask;
import android.util.Log;

import com.andrei.examapp.dao.GenericDao;
import com.andrei.examapp.db.GenericDatabase;
import com.andrei.examapp.model.GenericEntity;
import com.google.gson.Gson;

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
    private static final String URL = "http://192.168.43.71:2224";
    private static final String WS = "ws://192.168.43.71:2224";
    private OkHttpClient client = new OkHttpClient.Builder().build();
    private Gson gson = new Gson();
    public MutableLiveData<String> toastMsg;
    private MutableLiveData<Boolean> isGone;
    private GenericDao dao;
    private MutableLiveData<List<GenericEntity>> entitiesClient;
    private MutableLiveData<List<GenericEntity>> entitiesClerk;
    private LiveData<List<GenericEntity>> favoriteEntitites;
    private MutableLiveData<List<String>> genres;

    public Repository(Application application) {
        toastMsg = new MutableLiveData<>();
        isGone = new MutableLiveData<>();
        entitiesClient = new MutableLiveData<>();
        genres = new MutableLiveData<>();
        entitiesClerk = new MutableLiveData<>();

        GenericDatabase db = GenericDatabase.getInstance(application);
        dao = db.genericDao();
        favoriteEntitites = dao.getAll();

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


    public void delete(GenericEntity entity) {
        String url = URL + "/song/" + entity.getId().toString();

        Request request = new Request.Builder()
                .url(url)
                .delete()
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
                    for (GenericEntity s : entitiesClerk.getValue()) {
                        if (s.getId() != entityDel.getId()) {
                            cpy.add(s);
                        }
                    }
                    entitiesClerk.postValue(cpy);
                    toastMsg.postValue("Song successfully remove");
                    isGone.postValue(true);
                    Log.d("delete", entityDel.toString() + "was removed");
                } else {
                    toastMsg.postValue("Song was not removed");
                    Log.e("delete", "Enity was not removed");
                    isGone.postValue(true);
                }
            }
        });
    }

    public void add(GenericEntity entity) {
        String url = URL + "/song";
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
                    List<GenericEntity> cpy = new ArrayList<>(entitiesClerk.getValue());
                    cpy.add(newEntity);
                    entitiesClerk.postValue(cpy);
                    toastMsg.postValue("Car was successfully added");
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

    public MutableLiveData<List<String>> getGenres() {
        String url = URL + "/genres";
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
                List<String> genres1 = Arrays.asList(gson.fromJson(response.body().string(), String[].class));
                genres.postValue(genres1);
                isGone.postValue(true);
                Log.d("getEntities", "success retriving data");
            }
        });
        return genres;
    }

    public MutableLiveData<List<GenericEntity>> getByGenre(String genre) {
        String url = URL + "/songs/" + genre;
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
                Log.e("getByGenre", "Fail to get data from server");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                List<GenericEntity> entities = Arrays.asList(gson.fromJson(response.body().string(), GenericEntity[].class));
                entitiesClient.postValue(entities);
                isGone.postValue(true);
                Log.d("getByGenre", "success retriving data");
            }
        });
        return entitiesClient;
    }

    public MutableLiveData<List<GenericEntity>> getClerkEntities() {
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
                Log.e("getClerkEntities","Fail to get data from server");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                List<GenericEntity> entities = Arrays.asList(gson.fromJson(response.body().string(), GenericEntity[].class));
                Comparator<GenericEntity> comparator = (e1,e2)->{
                    String e11 = e1.getAlbum() + e1.getTitle();
                    String e12 = e2.getAlbum() + e2.getTitle();
                    return e11.compareTo(e12);
                };
                Collections.sort(entities,comparator);
                entitiesClerk.postValue(entities);
                isGone.postValue(true);
                Log.d("getClerkEntities", "success retriving data");
            }
        });
        return entitiesClerk;
    }

    public void addToFav(GenericEntity entity) {
        Log.d("addToFav", entity.toString() + "was saved in db");
        new InsertAsyncTask(dao).execute(entity);
        isGone.postValue(true);
        toastMsg.postValue("Added to Favorite");
    }

    public LiveData<List<GenericEntity>> getFavoriteEntities() {
        isGone.postValue(true);
        return favoriteEntitites;
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


