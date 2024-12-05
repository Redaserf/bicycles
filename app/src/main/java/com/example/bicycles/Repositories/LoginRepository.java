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
import com.example.bicycles.Views.Fragments.MasFragment;
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
        this.context = context;
        this.apiService = RetrofitClient.getInstance(context).getApiService();
    }

    public MutableLiveData<String> login(String email, String password) {
        MutableLiveData<String> loginResponse = new MutableLiveData<>();
        LoginRequest request = new LoginRequest(email, password);

        apiService.login(request).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    // Guardar el token en SharedPreferences
                    SharedPreferences pref = context.getSharedPreferences("token", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = pref.edit();
                    editor.putString("token", response.body().getToken());
                    editor.apply();

                    // Actualizar el LiveData con el mensaje
                    loginResponse.setValue(response.body().getMessage());

                    // Redirigir a la vista de activity_mas
                    Toast.makeText(context, "Se inicio sesion correctamente", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(context, Home.class); // Cambiar "Home" por la actividad deseada
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // Necesario para iniciar actividad desde un contexto
                    context.startActivity(intent);

                } else {
                    // Manejar error en la respuesta
                    loginResponse.setValue("Error: " + response.message());
                    Toast.makeText(context, "Inicio de sesión fallido: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }


            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
                loginResponse.setValue(t.getMessage());
            }
        });

        return loginResponse;
    }
}
