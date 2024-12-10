package com.example.bicycles.Networks;

import com.example.bicycles.Models.BicicletaRequest;
import com.example.bicycles.Models.LoginRequest;
import com.example.bicycles.Models.Recorrido;
import com.example.bicycles.Models.ReenviarRequest;
import com.example.bicycles.Models.Usuario;
import com.example.bicycles.Models.VerifyCodeRequest;
import com.example.bicycles.Responses.AllRecorridosUsuarioResponse;
import com.example.bicycles.Responses.BicicletaResponse;
import com.example.bicycles.Responses.EditarBicicletaResponse;
import com.example.bicycles.Responses.EliminarBicicletaResponse;
import com.example.bicycles.Responses.EliminarVelocidadResponse;
import com.example.bicycles.Responses.LoginResponse;
import com.example.bicycles.Responses.LogoutResponse;
import com.example.bicycles.Responses.MisBicicletasResponse;
import com.example.bicycles.Responses.RecorridoInicioResponse;
import com.example.bicycles.Responses.RecorridoResponse;
import com.example.bicycles.Responses.ReenviarResponse;
import com.example.bicycles.Responses.RegisterResponse;
import com.example.bicycles.Responses.SensoresResponse;
import com.example.bicycles.Responses.UsuarioEditResponse;
import com.example.bicycles.Responses.UsuarioResponse;
import com.example.bicycles.Responses.VerifyCodeResponse;
import com.google.gson.JsonObject;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface ApiService {

    // =====[ Auth ]=====

    @POST("login")
    Call<LoginResponse> login (@Body LoginRequest request);

    @POST("register")
    Call<RegisterResponse> register (@Body Usuario usuario);

    @POST("logout")
    Call<LogoutResponse> logout();

    @POST("send")
    Call<VerifyCodeResponse> verifyCode(@Body VerifyCodeRequest request);

    @POST("reenviar")
    Call<ReenviarResponse> reenviar(@Body ReenviarRequest request);


    // =====[ Usuarios ]=====

    @PUT("usuario")
    Call<UsuarioEditResponse> actualizarUsuario(@Body Usuario usuario);

    @GET("usuario")
    Call<UsuarioResponse> getUsuario();


    // =====[ Bicicletas ]=====

//    @Multipart
    @POST("bicicleta")
    Call<BicicletaResponse> addBicicleta(@Body BicicletaRequest nombre);

    @GET("bicicleta")
    Call<MisBicicletasResponse> getBicicletas();

    @DELETE("bicicleta/{id}")
    Call<EliminarBicicletaResponse> eliminarBicicleta(@Path("id") int id);

    @POST("bicicleta/{id}")
    Call<EditarBicicletaResponse> editarBicicleta(@Path("id") int id, @Body BicicletaRequest nombre);

//    @GET("bicicleta/{id}")
//    Call<BicicletaResponse> getBicicleta(@Body Bicicleta bicicleta);


    // =====[ Recorridos ]=====

    @POST("recorrido")
    @FormUrlEncoded
    Call<RecorridoInicioResponse> iniciarRecorrido(@Field("bicicleta_id") int bicicletaId);

    @POST("recorridos")
    Call<RecorridoResponse> addRecorrido(@Body Recorrido recorrido);

    @GET("recorridos/{id}")
    Call<RecorridoResponse> getRecorridoById(@Path("id") int id);

    @GET("recorridos")
    Call<AllRecorridosUsuarioResponse> getRecorridos();

    @GET("recorridos/semana")
    Call<AllRecorridosUsuarioResponse> getRecorridosSemana();

    @GET("recorridos/mes")
    Call<AllRecorridosUsuarioResponse> getRecorridosMes();


    // =====[ Velocidades ]=====

    @POST("velocidades") //es delete, pero no jala con delete ja
    Call<EliminarVelocidadResponse> eliminarVelocidades(@Body JsonObject body);


    // =====[ Sensores ]=====

    @POST("adafruit")
    Call<SensoresResponse> getSensores(@Body JsonObject body);


}
