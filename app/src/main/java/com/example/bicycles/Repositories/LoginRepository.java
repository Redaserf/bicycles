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
import com.example.bicycles.Views.MainActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginRepository {
    public ApiService apiService;
    public Context context;

    public LoginRepository(){
        this.apiService = RetrofitClient.getInstance().getApiService();
    }

    public MutableLiveData<String> login(String email, String password, Context context){
        MutableLiveData<String> loginResponse = new MutableLiveData<>();
        LoginRequest request = new LoginRequest(email, password);
        apiService.login(request).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Toast.makeText(context, "Bienvenido " + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    //logica para guardar el token con SharedPreferences

                    if(response.body().getToken() != null){

                        SharedPreferences token = context.getSharedPreferences("token", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = token.edit();
                        editor.putString("token", response.body().getToken());
                        editor.apply();

                        Intent intent = new Intent(context, MainActivity.class);
                        context.startActivity(intent);
                    }
                    loginResponse.setValue(response.body().getMessage());

                } else {
                    Toast.makeText(context, "Credenciales inválidas", Toast.LENGTH_SHORT).show();
                    loginResponse.setValue(response.body().getMessage());
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Toast.makeText(context, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                loginResponse.setValue(t.getMessage());

            }
        });
        return loginResponse;
    }

}
