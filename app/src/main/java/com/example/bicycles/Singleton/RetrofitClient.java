package com.example.bicycles.Singleton;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.bicycles.Networks.ApiService;

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

    public static final String BASE_URL = "http://3.135.19.191/api/v1/";

    private RetrofitClient(Context context) {
        // Crear OkHttpClient con un interceptor que lea el token dinámicamente
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(chain -> {
                    Request originalRequest = chain.request();
                    Request.Builder builder = originalRequest.newBuilder();

                    // Leer el token dinámicamente desde SharedPreferences
                    SharedPreferences pref = context.getSharedPreferences("token", Context.MODE_PRIVATE);
                    String token = pref.getString("token", null);

                    if (token != null && !token.isEmpty()) {
                        builder.addHeader("Authorization", "Bearer " + token);
                    }

                    builder.addHeader("Content-Type", "application/json");

                    Request newRequest = builder.build();
                    return chain.proceed(newRequest);
                })
                .build();

        // Crear instancia de Retrofit
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        apiService = retrofit.create(ApiService.class);
    }

    public static synchronized RetrofitClient getInstance(Context context) {
        if (instance == null) {
            instance = new RetrofitClient(context.getApplicationContext());
        }
        return instance;
    }

    public ApiService getApiService() {
        return apiService;
    }

    public static synchronized void resetInstance() {
        instance = null; // Forzar la recreación de RetrofitClient
    }
}







// Esto es lo anterior, por si lo nuevo que puse no jala :'v

//package com.example.bicycles.Singleton;
//
//import android.content.Context;
//import android.content.SharedPreferences;
//import android.widget.Toast;
//import com.example.bicycles.Networks.ApiService;
//import com.example.bicycles.Token.MyAplication;
//import com.example.bicycles.Token.SharedPreferencesManager;
//
//import java.io.IOException;
//
//import okhttp3.Interceptor;
//import okhttp3.OkHttpClient;
//import okhttp3.Request;
//import okhttp3.Response;
//import retrofit2.Retrofit;
//import retrofit2.converter.gson.GsonConverterFactory;
//
//public class RetrofitClient {
//    private static RetrofitClient instance = null;
//    private ApiService apiService;
//    private Context context;
//    private static String token;
//
//    public static final String BASE_URL = "http://192.168.100.229:8000/api/v1/";
//
//
//    private RetrofitClient(Context context){
//        this.context = context;
//        SharedPreferences pref = context.getSharedPreferences("token", Context.MODE_PRIVATE);
//        String savedToken = pref.getString("token", "No se encontró el token.");
//        Toast.makeText(context, "Token: " + savedToken, Toast.LENGTH_SHORT).show();
//        token = pref.getString("token", null);
//        if (token == null) {
//            Retrofit retrofit = new Retrofit.Builder()
//                    .baseUrl(BASE_URL)
//                    .addConverterFactory(GsonConverterFactory.create())
//                    .build();
//            apiService = retrofit.create(ApiService.class);
//
//        }else{
//            OkHttpClient client = new OkHttpClient.Builder()
//                    .addInterceptor(new Interceptor() {
//                        @Override
//                        public Response intercept(Chain chain) throws IOException {
//                            Request originalRequest = chain.request();
//                            Request.Builder builder = originalRequest.newBuilder();
//
//                            if (token != null && !token.isEmpty()) {
//                                builder.addHeader("Authorization", "Bearer " + token);
//                            }
//
//                            builder.addHeader("Content-Type", "application/json");
//
//                            Request newRequest = builder.build();
//                            return chain.proceed(newRequest);
//                        }
//                    })
//                    .build();
//
//            Retrofit retrofit = new Retrofit.Builder()
//                    .baseUrl(BASE_URL)
//                    .client(client)
//                    .addConverterFactory(GsonConverterFactory.create())
//                    .build();
//            apiService = retrofit.create(ApiService.class);
//
//        }
//    }
//    public static synchronized RetrofitClient getInstance(Context context) {
//        if (instance == null) {
//            instance = new RetrofitClient(context);
//
//        }
//        return instance;
//    }
//
//    public ApiService getApiService() {
//        return apiService;
//    }
//}
