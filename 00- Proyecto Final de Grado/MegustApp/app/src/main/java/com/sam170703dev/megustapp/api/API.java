package com.sam170703dev.megustapp.api;

import com.sam170703dev.megustapp.entidades.Usuario;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
public interface API {
    @GET("/usuarios")
    Call<List<Usuario>> getUsuarios();

    @GET("/usuarios/{id}")
    Call<Usuario> getUsuarioById(@Path("id") int id);
}
