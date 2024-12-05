package com.example.bicycles.Networks;



import com.example.bicycles.Models.Bicicleta;
import com.example.bicycles.Models.LoginRequest;
import com.example.bicycles.Models.Recorrido;
import com.example.bicycles.Models.Usuario;
import com.example.bicycles.Responses.AllRecorridosUsuarioResponse;
import com.example.bicycles.Responses.BicicletaResponse;
import com.example.bicycles.Responses.LoginResponse;
import com.example.bicycles.Responses.LogoutResponse;
import com.example.bicycles.Responses.MisBicicletasResponse;
import com.example.bicycles.Responses.RecorridoResponse;
import com.example.bicycles.Responses.RegisterResponse;
import com.example.bicycles.Responses.SensoresResponse;
import com.example.bicycles.Responses.UsuarioEditResponse;
import com.example.bicycles.Responses.UsuarioResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ApiService {

    // =====[ Auth ]=====

    @POST("login")
    Call<LoginResponse> login (@Body LoginRequest request);

    @POST("register")
    Call<RegisterResponse> register (@Body Usuario usuario);

    @POST("logout")
    Call<LogoutResponse> logout();


    // =====[ Usuarios ]=====

    @PUT("usuario")
    Call<UsuarioEditResponse> actualizarUsuario(@Body Usuario usuario);

    @GET("usuario")
    Call<UsuarioResponse> getUsuario();


    // =====[ Bicicletas ]=====

    @POST("bicicleta")
    Call<BicicletaResponse> addBicicleta(@Body Bicicleta bicicleta);

    @GET("bicicleta")
    Call<MisBicicletasResponse> getBicicletas();

    @GET("bicicleta/{id}")
    Call<BicicletaResponse> getBicicleta(@Body Bicicleta bicicleta);


    // =====[ Recorridos ]=====

    @POST("recorridos")
    Call<RecorridoResponse> addRecorrido(@Body Recorrido recorrido);

    @GET("recorridos/{id}")
    Call<RecorridoResponse> getRecorridoById(@Path("id") int id);

    @GET("recorridos")
    Call<AllRecorridosUsuarioResponse> getRecorridos();


    // =====[ Sensores ]=====

    @GET("adafruit")
    Call<SensoresResponse> getSensores();

}
