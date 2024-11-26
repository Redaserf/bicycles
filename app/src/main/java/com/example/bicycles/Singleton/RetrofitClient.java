package com.example.bicycles.Singleton;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.bicycles.Networks.ApiService;
import com.example.bicycles.Token.SharedPreferencesManager;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static RetrofitClient instance = null ;
    private ApiService apiService;
    private Context context;

    private static final String BASE_URL = "http://192.168.1.10:8000/api/v1/";


    private RetrofitClient(){
        String token = SharedPreferencesManager.getInstance(null).getData("token");

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request originalRequest = chain.request();
                        Request.Builder builder = originalRequest.newBuilder();

                        if (token != null && !token.isEmpty()) {
                            builder.addHeader("Authorization", "Bearer " + token);
                        }

                        builder.addHeader("Content-Type", "application/json");

                        Request newRequest = builder.build();
                        return chain.proceed(newRequest);
                    }
                })
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        apiService = retrofit.create(ApiService.class);
    }
    public static synchronized RetrofitClient getInstance() {
        if (instance == null) {
            instance = new RetrofitClient();
        }
        return instance;
    }

    public ApiService getApiService() {
        return apiService;
    }
}
