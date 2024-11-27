package com.example.bicycles.Singleton;

import android.content.Context;
import android.content.SharedPreferences;
<<<<<<< HEAD
import android.widget.Toast;
=======
>>>>>>> a8fc652745ff9f6a9dbb3d9be3168067eaeb5f10

import com.example.bicycles.Networks.ApiService;
import com.example.bicycles.Token.MyAplication;
import com.example.bicycles.Token.SharedPreferencesManager;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static RetrofitClient instance = null;
    private ApiService apiService;
    private Context context;
    private static String token;

    public static final String BASE_URL = "http://192.168.1.10:8000/api/v1/";

<<<<<<< HEAD

=======
//    private RetrofitClient(Context context) {
//        OkHttpClient okHttpClient = new OkHttpClient.Builder()
//                .addInterceptor(new Interceptor() {
//                    @Override
//                    public Response intercept(Chain chain) throws IOException {
//                        SharedPreferences preferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
//                        String token = preferences.getString("auth_token", ""); // Obtén el token almacenado
//                        Request request = chain.request().newBuilder()
//                                .addHeader("Authorization", "Bearer " + token) // Agrega el token al header
//                                .build();
//                        return chain.proceed(request);
//                    }
//                })
//                .build();
    private RetrofitClient(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
//                .client(okHttpClient)
                .build();
>>>>>>> a8fc652745ff9f6a9dbb3d9be3168067eaeb5f10

    private RetrofitClient(Context context){
        this.context = context;
        SharedPreferences pref = context.getSharedPreferences("token", Context.MODE_PRIVATE);
        Toast.makeText(context, "Token: " + pref.getString("token", null), Toast.LENGTH_SHORT).show();
        token = pref.getString("token", null);
        if (token == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            apiService = retrofit.create(ApiService.class);

        }else{
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
    }
<<<<<<< HEAD
    public static synchronized RetrofitClient getInstance(Context context) {
        if (instance == null) {
            instance = new RetrofitClient(context);
=======
    public static synchronized RetrofitClient getInstance() {
//    public static synchronized RetrofitClient getInstance(Context context) {
        if (instance == null) {
            instance = new RetrofitClient();
//            instance = new RetrofitClient(context);
>>>>>>> a8fc652745ff9f6a9dbb3d9be3168067eaeb5f10
        }
        return instance;
    }

    public ApiService getApiService() {
        return apiService;
    }
}
