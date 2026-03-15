package com.sam170703dev.megustapp.actividades;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.sam170703dev.megustapp.R;
import com.sam170703dev.megustapp.api.API;
import com.sam170703dev.megustapp.api.APIRest;
import com.sam170703dev.megustapp.clases.UserAccount;
import com.sam170703dev.megustapp.clases.cuentas.ClientAccount;
import com.sam170703dev.megustapp.clases.cuentas.RestaurantAccount;
import com.sam170703dev.megustapp.entidades.Cliente;
import com.sam170703dev.megustapp.entidades.Restaurante;
import com.sam170703dev.megustapp.entidades.Usuario;
import com.sam170703dev.megustapp.enums.UserAccountTypes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Login extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.login_activity), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        final EditText nombreUsuarioInput = findViewById(R.id.usuario_login);
        final EditText claveInput = findViewById(R.id.clave_login);
        final Button loginButton = findViewById(R.id.boton_iniciar_sesion_login);
        final TextView textoRegistrarse = findViewById(R.id.texto_registrarse_login);
        final Map<String, UserAccount> cuentas = new HashMap<>();
        final API api = APIRest.getAPI();
        final SharedPreferences sharedPreferences = getSharedPreferences("Tokens", Context.MODE_PRIVATE);

        final String tokenCliente = sharedPreferences.getString("token_cliente", "");

        api.getUsuarios("Bearer " + tokenCliente).enqueue(new Callback<List<Usuario>>() {
            @Override
            public void onResponse(Call<List<Usuario>> call, Response<List<Usuario>> response) {
                if(response.isSuccessful()) {
                    for(Usuario usuario : response.body()) {
                        api.getClienteById("Bearer " + tokenCliente, usuario.getId()).enqueue(new Callback<Cliente>() {
                            @Override
                            public void onResponse(Call<Cliente> call2, Response<Cliente> response2) {
                                if(response2.isSuccessful()) {
                                    cuentas.put(usuario.getEmail(), new ClientAccount(usuario.getId(), usuario.getEmail(), usuario.getClave()));
                                }
                                else {
                                    cuentas.put(usuario.getEmail(), new RestaurantAccount(usuario.getId(), usuario.getEmail(), usuario.getClave()));
                                }
                            }

                            @Override
                            public void onFailure(Call<Cliente> call, Throwable t) {
                                Log.i("LOGIN-ERROR", "Error");
                                Log.e("ERROR", t.getMessage());
                            }
                        });
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Usuario>> call, Throwable t) {
                Log.i("LOGIN-ERROR", "Error");
                Log.e("LOGIN-ERROR", t.getMessage());
            }
        });

        ActivityResultLauncher resultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if(result.getResultCode() == Activity.RESULT_OK) {
                            Intent intent = result.getData();
                            if(intent != null) {
                                Bundle extras = intent.getExtras();
                                if(extras.getBoolean("cerrar_sesion")) {
                                    Intent resultado = new Intent();
                                    resultado.putExtra("cerrar_sesion", true);
                                    setResult(RESULT_OK, resultado);
                                    finish();
                                }
                            }
                        }
                    }
                }
        );

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean credencialesValidas = true;
                UserAccount cuenta = cuentas.get(nombreUsuarioInput.getText().toString());
                if(nombreUsuarioInput.getText().toString().isEmpty()) {
                    nombreUsuarioInput.setError("Este campo es obligatorio.");
                    credencialesValidas = false;
                }
                else {
                    if(cuenta == null) {
                        nombreUsuarioInput.setError("El usuario introducido no existe.");
                        credencialesValidas = false;
                    }
                }

                if(claveInput.getText().toString().isEmpty()) {
                    claveInput.setError("Este campo es obligatorio.");
                    credencialesValidas = false;
                }
                else {
                    if(cuenta != null && !cuenta.esClaveCorrecta(claveInput.getText().toString())) {
                        claveInput.setError("Contraseña incorrecta.");
                        credencialesValidas = false;
                    }
                }

                if(!credencialesValidas) Toast.makeText(Login.this, "Credenciales inválidas", Toast.LENGTH_SHORT).show();
                else {
                    if(cuenta.getTIPO().equals(UserAccountTypes.CLIENT)) {
                        api.getClientes("Bearer " + tokenCliente).enqueue(new Callback<List<Cliente>>() {
                            @Override
                            public void onResponse(Call<List<Cliente>> call, Response<List<Cliente>> response) {
                                Intent actividad = null;
                                if(response.isSuccessful()) {
                                    int i = 0;
                                    boolean encontrado = false;
                                    while (i < response.body().size() && !encontrado) {
                                        if(response.body().get(i).getEmail().equals(cuenta.getUsuario())) {
                                            actividad = new Intent(Login.this, MainActivityClient.class);
                                            actividad.putExtra("id_cliente", response.body().get(i).getId());
                                            encontrado = true;
                                        }

                                        i++;
                                    }

                                    if(encontrado) resultLauncher.launch(actividad);
                                }
                                else Toast.makeText(Login.this, "Se ha producido un error al intentar iniciar sesión.", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onFailure(Call<List<Cliente>> call, Throwable t) {
                                Toast.makeText(Login.this, "Se ha producido un error al intentar iniciar sesión.", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                    else {
                        api.getRestaurantes("Bearer " + tokenCliente).enqueue(new Callback<List<Restaurante>>() {
                            @Override
                            public void onResponse(Call<List<Restaurante>> call, Response<List<Restaurante>> response) {
                                Intent actividad = null;
                                if(response.isSuccessful()) {
                                    int i = 0;
                                    boolean encontrado = false;
                                    while (i < response.body().size() && !encontrado) {
                                        if(response.body().get(i).getEmail().equals(cuenta.getUsuario())) {
                                            actividad = new Intent(Login.this, MainActivityRestaurant.class);
                                            actividad.putExtra("id_restaurante", response.body().get(i).getId());
                                            actividad.putExtra("nombre_restaurante", response.body().get(i).getNombre());
                                            encontrado = true;
                                        }

                                        i++;
                                    }

                                    if(encontrado) resultLauncher.launch(actividad);
                                }
                                else Toast.makeText(Login.this, "Se ha producido un error al intentar iniciar sesión.", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onFailure(Call<List<Restaurante>> call, Throwable t) {
                                Toast.makeText(Login.this, "Se ha producido un error al intentar iniciar sesión.", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            }
        });

        textoRegistrarse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent registerActivity = new Intent(Login.this, Register.class);
                resultLauncher.launch(registerActivity);
            }
        });
    }
}