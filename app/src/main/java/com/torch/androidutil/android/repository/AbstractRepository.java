package com.torch.androidutil.android.repository;

import android.content.Context;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.torch.androidutil.interfaces.OnSuccessListener;


import okhttp3.OkHttpClient;
import okhttp3.Response;
import okhttp3.internal.Util;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public abstract class AbstractRepository<T> {
    protected final Context context;
    protected final T service;

    protected Call currentCall;

    public AbstractRepository(Context context, Class<T> tClass) {
        this.context = context;
        Retrofit build = new Retrofit.Builder()
                .baseUrl("Util.getBaseUrl()")
                .client(new OkHttpClient.Builder().build())
                .addConverterFactory(GsonConverterFactory
                                             .create(new GsonBuilder().setDateFormat("yyyy-MM-dd")
                                                             .create()))
                .build();
        service = build.create(tClass);
    }

    protected void enqueueArray(Call<Response> call,
                                OnSuccessListener<JsonArray> successListener) {
        currentCall = call;
//        call.enqueue(new JsonArrayHttpResponseCallbackAdapter(context, successListener));
    }

    protected void enqueueObject(Call<Response> call,
                                 OnSuccessListener<JsonObject> successListener) {
        currentCall = call;
//        call.enqueue(new JsonObjHttpResponseCallbackAdapter(context, successListener));
    }

    public Call getCurrentCall() {
        return currentCall;
    }

    protected void cancelCurrentCall() {
        if (currentCall != null)
            currentCall.cancel();
    }

//    protected String getDeviceId() {
//        return Util.getDeviceId(context);
//    }

    protected boolean getAsBoolean(String paramName, JsonObject data) {
        return getAsBoolean(paramName, data, false);
    }

    protected boolean getAsBoolean(String paramName, JsonObject data, boolean defaultValue) {
        if (data != null && data.has("success")) {
            return data.get("success").getAsBoolean();
        }
        return defaultValue;
    }
}