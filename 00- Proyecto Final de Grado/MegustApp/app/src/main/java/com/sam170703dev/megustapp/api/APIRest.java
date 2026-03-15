package com.sam170703dev.megustapp.api;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class APIRest {

    public static final String URL_API_REST = "http://n1.voriamtechnologies.com:3017";
    public static final String URL_API_IMAGENES = "http://n1.voriamtechnologies.com:3010";
    private static API APIRest, APIIMagenes;
    private static Retrofit retrofitAPIRest, retrofitAPIImagenes;

    static {
        retrofitAPIRest = new Retrofit.Builder()
                .baseUrl(URL_API_REST)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        retrofitAPIImagenes = new Retrofit.Builder()
                .baseUrl(URL_API_IMAGENES)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        APIRest = retrofitAPIRest.create(API.class);
        APIIMagenes = retrofitAPIImagenes.create(API.class);
    }

    public static API getAPI() {
        return APIRest;
    }

    public static API getAPIIMagenes() {
        return APIIMagenes;
    }

    public static void cargarImagen(Context context, String imagen, ImageView imageView) {
        Glide.with(context)
            .load(imagen)
            .into(imageView);
    }
}
