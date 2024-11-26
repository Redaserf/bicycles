package com.example.bicycles.Networks;



import android.content.SharedPreferences;

import com.example.bicycles.Models.Bicicleta;
import com.example.bicycles.Models.LoginRequest;
import com.example.bicycles.Models.Usuario;
import com.example.bicycles.Responses.BicicletaResponse;
import com.example.bicycles.Responses.LoginResponse;
import com.example.bicycles.Responses.RegisterResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiService {


    @POST("login")
    Call<LoginResponse> login (@Body LoginRequest request);

    @POST("register")
    Call<RegisterResponse> register (@Body Usuario usuario);

    @POST("bicicleta")
    Call<BicicletaResponse> addBicicleta(@Body Bicicleta bicicleta);
}
