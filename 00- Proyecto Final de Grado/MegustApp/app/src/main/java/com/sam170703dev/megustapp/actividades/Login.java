package com.sam170703dev.megustapp.actividades;

import android.app.Activity;
import android.content.Intent;
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
import com.sam170703dev.megustapp.clases.UserAccount;
import com.sam170703dev.megustapp.clases.cuentas.ClientAccount;
import com.sam170703dev.megustapp.clases.cuentas.RestaurantAccount;
import com.sam170703dev.megustapp.enums.UserAccountTypes;

import java.util.HashMap;
import java.util.Map;

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
        final ClientAccount cliente = new ClientAccount("samuel", "root");
        final RestaurantAccount restaurante = new RestaurantAccount("restaurante", "root");
        final Map<String, UserAccount> cuentas = new HashMap<>();

        cuentas.put("samuel", cliente);
        cuentas.put("restaurante", restaurante);

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
                    if(cuenta != null && !cuenta.getClave().equals(claveInput.getText().toString())) {
                        claveInput.setError("Contraseña incorrecta.");
                        credencialesValidas = false;
                    }
                }

                if(!credencialesValidas) Toast.makeText(Login.this, "Credenciales inválidas", Toast.LENGTH_SHORT).show();
                else {
                    Intent actividad;
                    if(cuenta.getTIPO().equals(UserAccountTypes.CLIENT)) {
                        actividad = new Intent(Login.this, MainActivityClient.class);
                    }
                    else {
                        actividad = new Intent(Login.this, MainActivityRestaurant.class);
                    }

                    resultLauncher.launch(actividad);
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