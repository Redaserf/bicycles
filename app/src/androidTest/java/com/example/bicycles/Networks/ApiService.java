package com.example.bicycles.Networks;



import com.example.bicycles.Models.LoginRequest;
import com.example.bicycles.Models.RegisterRequest;
import com.example.bicycles.Responses.LoginResponse;
import com.example.bicycles.Responses.RegisterResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiService {

    @POST("api/v1/login")
    Call<LoginResponse> login (@Body LoginRequest request);

    @POST("api/v1/register")
    Call<RegisterResponse> register (@Body RegisterRequest request);
}
