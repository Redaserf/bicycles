package com.example.bicycles.Networks;



import android.content.SharedPreferences;

import com.example.bicycles.Models.Bicicleta;
import com.example.bicycles.Models.LoginRequest;
import com.example.bicycles.Models.Recorrido;
import com.example.bicycles.Models.Usuario;
import com.example.bicycles.Responses.BicicletaResponse;
import com.example.bicycles.Responses.LoginResponse;
<<<<<<< HEAD
import com.example.bicycles.Responses.MisBicicletasResponse;
=======
import com.example.bicycles.Responses.RecorridoResponse;
>>>>>>> a8fc652745ff9f6a9dbb3d9be3168067eaeb5f10
import com.example.bicycles.Responses.RegisterResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApiService {


    @POST("login")
    Call<LoginResponse> login (@Body LoginRequest request);

    @POST("register")
    Call<RegisterResponse> register (@Body Usuario usuario);

    @POST("bicicleta")
    Call<BicicletaResponse> addBicicleta(@Body Bicicleta bicicleta);

<<<<<<< HEAD
    @GET("bicicleta")
    Call<MisBicicletasResponse> getBicicletas();
=======
    @GET("bicicleta/{id}")
    Call<BicicletaResponse> getBicicleta(@Body Bicicleta bicicleta);

    @POST("recorridos")
    Call<RecorridoResponse> addRecorrido(@Body Recorrido recorrido);

    @GET("recorridos/{id}")
    Call<RecorridoResponse> getRecorridoById(@Path("id") int id); // por ver

>>>>>>> a8fc652745ff9f6a9dbb3d9be3168067eaeb5f10
}
