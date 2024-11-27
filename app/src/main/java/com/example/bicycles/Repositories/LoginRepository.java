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
<<<<<<< HEAD
    public ApiService apiService;
    public Context context;
    public static String token;

    public LoginRepository(Context context){
        this.apiService = RetrofitClient.getInstance(context).getApiService();
=======
    private final ApiService apiService;

    public LoginRepository() {
        this.apiService = RetrofitClient.getInstance().getApiService();
>>>>>>> a8fc652745ff9f6a9dbb3d9be3168067eaeb5f10
    }

    public MutableLiveData<String> login(String email, String password, Context context) {
        MutableLiveData<String> loginResponse = new MutableLiveData<>();
        LoginRequest request = new LoginRequest(email, password);

        apiService.login(request).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    // Mensaje de éxito
                    Toast.makeText(context, "Bienvenido " + response.body().getMessage(), Toast.LENGTH_SHORT).show();

<<<<<<< HEAD
                    if(response.body().getToken() != null){

                        SharedPreferences pref = context.getSharedPreferences("token", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = pref.edit();
                        editor.putString("token", response.body().getToken());
                        editor.apply();

                        Intent intent = new Intent(context, Home.class);
=======
                    // Guardar el token en SharedPreferences
                    String token = response.body().getToken();
                    if (token != null && !token.isEmpty()) {
                        SharedPreferences preferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putString("auth_token", token); // Nombre consistente con RetrofitClient
                        editor.apply();

                        // Redirigir al usuario a MainActivity
                        Intent intent = new Intent(context, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
>>>>>>> a8fc652745ff9f6a9dbb3d9be3168067eaeb5f10
                        context.startActivity(intent);
                    }

                    loginResponse.setValue(response.body().getMessage());
                } else {
                    // Mensaje de error del servidor
                    String errorMessage = response.body() != null ? response.body().getMessage() : "Credenciales inválidas";
                    Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show();
                    loginResponse.setValue(errorMessage);
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                // Mensaje de error de red o fallo de conexión
                Toast.makeText(context, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                loginResponse.setValue(t.getMessage());
            }
        });

        return loginResponse;
    }
}
