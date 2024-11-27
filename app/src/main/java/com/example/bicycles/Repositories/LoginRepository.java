package com.example.bicycles.Repositories;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;

import com.example.bicycles.Models.LoginRequest;
import com.example.bicycles.Networks.ApiService;
import com.example.bicycles.Responses.LoginResponse;
import com.example.bicycles.Singleton.RetrofitClient;
import com.example.bicycles.Token.SharedPreferencesManager;
import com.example.bicycles.Views.Home;
import com.example.bicycles.Views.MainActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginRepository {
    public ApiService apiService;
    public Context context;
    public static String token;

    public LoginRepository(Context context){
        this.apiService = RetrofitClient.getInstance(context).getApiService();
    }

    public MutableLiveData<String> login(String email, String password, Context context) {
        MutableLiveData<String> loginResponse = new MutableLiveData<>();
        LoginRequest request = new LoginRequest(email, password);

        apiService.login(request).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if(response.isSuccessful() && response.body() != null){
                    SharedPreferences pref = context.getSharedPreferences("token", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = pref.edit();
                    editor.putString("token", response.body().getToken());
                    editor.apply();

                    loginResponse.setValue(response.body().getMessage());
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {

                loginResponse.setValue(t.getMessage());
            }
        });

        return loginResponse;
    }
}
