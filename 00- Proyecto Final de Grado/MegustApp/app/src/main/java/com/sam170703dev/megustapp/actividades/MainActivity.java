package com.sam170703dev.megustapp.actividades;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.sam170703dev.megustapp.R;
import com.sam170703dev.megustapp.api.API;
import com.sam170703dev.megustapp.api.APIRest;
import com.sam170703dev.megustapp.peticiones_http.post.PeticionTokenCliente;
import com.sam170703dev.megustapp.peticiones_http.post.PeticionTokenRestaurante;
import com.sam170703dev.megustapp.respuestas_http.RespuestaToken;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        final Button loginButton = findViewById(R.id.boton_iniciar_sesion_activity_main);
        final Button registerButton = findViewById(R.id.boton_registrase_activity_main);

        final API api = APIRest.getAPI();

        final Call<RespuestaToken> callClientToken = api.getClientToken(new PeticionTokenCliente());
        final Call<RespuestaToken> callRestaurantToken = api.getRestaurantToken(new PeticionTokenRestaurante());
        final SharedPreferences sharedPreferences = getSharedPreferences("Tokens", Context.MODE_PRIVATE);

        callClientToken.enqueue(new Callback<RespuestaToken>() {
            @Override
            public void onResponse(Call<RespuestaToken> call, Response<RespuestaToken> response) {
                if (response.isSuccessful()) {
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("token_cliente", response.body().getAccess_token());
                    editor.apply();
                    editor.commit();
                }
            }

            @Override
            public void onFailure(Call<RespuestaToken> call, Throwable t) {
                Log.e("ERROR", t.getMessage());
            }
        });

        callRestaurantToken.enqueue(new Callback<RespuestaToken>() {
            @Override
            public void onResponse(Call<RespuestaToken> call, Response<RespuestaToken> response) {
                if (response.isSuccessful()) {
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("token_restaurante", response.body().getAccess_token());
                    editor.apply();
                    editor.commit();
                }
            }

            @Override
            public void onFailure(Call<RespuestaToken> call, Throwable t) {
                Log.e("ERROR", t.getMessage());
            }
        });


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                loginButton.setVisibility(View.VISIBLE);
                registerButton.setVisibility(View.VISIBLE);
            }
        }, 3000);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent loginActivity = new Intent(MainActivity.this, Login.class);
                startActivity(loginActivity);
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent registerActivity = new Intent(MainActivity.this, Register.class);
                startActivity(registerActivity);
            }
        });
    }
}