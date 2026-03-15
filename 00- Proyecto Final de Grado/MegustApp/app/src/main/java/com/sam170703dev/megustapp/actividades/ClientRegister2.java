package com.sam170703dev.megustapp.actividades;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
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
import com.sam170703dev.megustapp.entidades.Cliente;
import com.sam170703dev.megustapp.entidades.Usuario;
import com.sam170703dev.megustapp.peticiones_http.post.PeticionCrearCliente;
import com.sam170703dev.megustapp.peticiones_http.post.PeticionCrearUsuario;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ClientRegister2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.client_register2);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.client_register2_activity), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        final EditText telefonoInput = findViewById(R.id.telefono_client_register2);
        final EditText ciudadInput = findViewById(R.id.ciudad_client_register2);
        final Button botonFinalizarRegistro = findViewById(R.id.boton_finalizar_registro_client_register2);
        final TextView textoVolverAtras = findViewById(R.id.texto_volver_atras_client_register2);

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

        botonFinalizarRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean completado = true;

                if(telefonoInput.getText().toString().isBlank()) {
                    telefonoInput.setError("Este campo es obligatorio.");
                    completado = false;
                }
                if(ciudadInput.getText().toString().isBlank()) {
                    ciudadInput.setError("Este campo es obligatorio.");
                    completado = false;
                }

                if(completado) {
                    Set<Character> caracteres = new HashSet<>(Arrays.asList('1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '+'));
                    boolean caracteresInvalidosTel, caracteresInvalidosCiudad;
                    caracteresInvalidosTel = tieneCaracteresInvalidos(telefonoInput.getText().toString(), caracteres, true);
                    if(caracteresInvalidosTel) {
                        telefonoInput.setError("El número de telefono contiene caracteres inválidos");
                    }
                    caracteresInvalidosCiudad = tieneCaracteresInvalidos(ciudadInput.getText().toString(), caracteres, false);
                    if(caracteresInvalidosCiudad) {
                        ciudadInput.setError("La ciudad introducida tiene caracteres inválidos.");
                    }

                    if(!caracteresInvalidosTel && !caracteresInvalidosCiudad) {
                        API api = APIRest.getAPI();
                        SharedPreferences sharedPreferences = getSharedPreferences("Tokens", Context.MODE_PRIVATE);
                        Bundle bundle = getIntent().getExtras();
                        String usuario, correo, clave, token;

                        token = sharedPreferences.getString("token_cliente", "");
                        usuario = bundle.getString("Usuario");
                        correo = bundle.getString("Correo");
                        clave = bundle.getString("Clave");



                        api.crearCliente("Bearer " + token, new PeticionCrearCliente(
                                ciudadInput.getText().toString(),
                                correo,
                                usuario,
                                clave,
                                telefonoInput.getText().toString()
                        )).enqueue(new Callback<Cliente>() {
                            @Override
                            public void onResponse(Call<Cliente> call, Response<Cliente> response) {
                                if(response.isSuccessful()) {
                                    api.crearUsuario("Bearer " + token, new PeticionCrearUsuario(correo, clave)).enqueue(new Callback<Usuario>() {
                                        @Override
                                        public void onResponse(Call<Usuario> call, Response<Usuario> response2) {
                                            if(response2.isSuccessful()) {
                                                Toast.makeText(ClientRegister2.this, "Cuenta creada satisfactoriamente", Toast.LENGTH_SHORT).show();
                                                Intent mainActivityClient = new Intent(ClientRegister2.this, MainActivityClient.class);
                                                mainActivityClient.putExtra("id_usuario", response2.body().getId());
                                                resultLauncher.launch(mainActivityClient);
                                            }
                                            else {
                                                Toast.makeText(ClientRegister2.this, "Ocurrió un error al intentar crear la cuenta", Toast.LENGTH_SHORT).show();
                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<Usuario> call, Throwable t) {
                                            Toast.makeText(ClientRegister2.this, "Ocurrió un error al intentar crear la cuenta", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                                else {
                                    Toast.makeText(ClientRegister2.this, "Fue aqui y estado: " + response.code(), Toast.LENGTH_SHORT).show();
                                    //Toast.makeText(ClientRegister2.this, "Ocurrió un error al intentar crear la cuenta", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<Cliente> call, Throwable t) {
                                Toast.makeText(ClientRegister2.this, "Ocurrió un error al intentar crear la cuenta", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
                else {
                    Toast.makeText(ClientRegister2.this, "Todos los campos son obligatorios.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        textoVolverAtras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_CANCELED);
                finish();
            }
        });
    }

    private boolean tieneCaracteresInvalidos(String cadena, Set<Character> caracteres, boolean validos) {
        boolean caracteresInvalidos = false;
        int i = 0;
        if(validos) {
          while (!caracteresInvalidos && i < cadena.length()) {
              if(!caracteres.contains(cadena.charAt(i))) caracteresInvalidos = true;
              i++;
          }
        }
        else {
            while (!caracteresInvalidos && i < cadena.length()) {
                if(caracteres.contains(cadena.charAt(i))) caracteresInvalidos = true;
                i++;
            }
        }

        return caracteresInvalidos;
    }
}