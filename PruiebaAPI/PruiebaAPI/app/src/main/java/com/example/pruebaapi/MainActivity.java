package com.example.pruebaapi;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.Permission;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

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
        /*HttpURLConnection
        try {
            URL url = new URL("https://solenoidal-bloodlessly-nakita.ngrok-free.dev/usuarios");
            HttpsURLConnection httpsURLConnection = (HttpsURLConnection) url.openConnection();
            InputStream in = new BufferedInputStream(httpsURLConnection.getInputStream());
            readStream(in)
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }*/
        Retrofit retrofit = new Retrofit.Builder()
                //.baseUrl("https://solenoidal-bloodlessly-nakita.ngrok-free.dev ")
                .baseUrl("http://localhost:8080/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        API api = retrofit.create(API.class);

        /*api.getManufacturers().enqueue(new Callback<List<Manufacturer>>() {
            @Override
            public void onResponse(Call<List<Manufacturer>> call, Response<List<Manufacturer>> response) {
                Log.i("i", response.body().toString());

                ((TextView) findViewById(R.id.lbl)).setText(response.body().get(0).getName());
            }

            @Override
            public void onFailure(Call<List<Manufacturer>> call, Throwable t) {
                Log.e("i", "me cago en to'", t);
            }
        });*/

        api.getUsuarios().enqueue(new Callback<List<Usuario>>() {
            @Override
            public void onResponse(Call<List<Usuario>> call, Response<List<Usuario>> response) {
                Log.i("API_RESPONSE", response.body().toString());

                ((TextView) findViewById(R.id.lbl)).setText(response.body().get(0).getEmail());
            }

            @Override
            public void onFailure(Call<List<Usuario>> call, Throwable t) {
                Log.e("API_RESPONSE", "Ha fallado algo", t);
            }
        });
    }
}