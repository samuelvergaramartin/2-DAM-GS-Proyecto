package com.example.pruebaapi;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface API {
    @GET("manufacturer")
    Call<List<Manufacturer>> getManufacturers();
    @GET("usuarios")
    Call<List<Usuario>> getUsuarios();
}
