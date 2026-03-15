package com.example.pruebaapi;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface API {
    @GET("/api/usuarios")
    Call<List<Usuario>> getUsuarios();

    @GET("/api/usuarios/{id}")
    Call<Usuario> getUsuarioById(@Path("id") int id);

    @Multipart
    @POST("/upload")
    Call<ResponseBody> uploadImage(
            @Part MultipartBody.Part image,
            @Part("description") RequestBody description
    );

    @POST("/test")
    Call<ResponseBody> prueba(@Body Cuerpo cuerpo);
}
