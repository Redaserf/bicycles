package com.example.bicycles.Singleton;

import com.example.bicycles.Networks.ApiService;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static RetrofitClient instance = null ;
    private ApiService apiService;

    private static final String BASE_URL = "AQUI LA IP DE LA API";

    private RetrofitClient(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
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
