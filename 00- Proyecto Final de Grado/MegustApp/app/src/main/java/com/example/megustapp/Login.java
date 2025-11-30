package com.example.megustapp;

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

        final EditText usernameInput = findViewById(R.id.usuario_login);
        final EditText passwordInput = findViewById(R.id.clave_login);
        final Button loginButton = findViewById(R.id.boton_iniciar_sesion_login);
        final TextView textoRegistrarse = findViewById(R.id.texto_registrarse_login);

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
                if(usernameInput.getText().toString().isEmpty()) {
                    usernameInput.setError("Este campo es obligatorio.");
                    credencialesValidas = false;
                }
                else {
                    if(!usernameInput.getText().toString().equals("root")) {
                        usernameInput.setError("Nombre de usuario inválido");
                        credencialesValidas = false;
                    }
                }

                if(passwordInput.getText().toString().isEmpty()) {
                    passwordInput.setError("Este campo es obligatorio.");
                    credencialesValidas = false;
                }
                else {
                    if(!passwordInput.getText().toString().equals("root")) {
                        passwordInput.setError("Contraseña incorrecta.");
                        credencialesValidas = false;
                    }
                }

                if(!credencialesValidas) Toast.makeText(Login.this, "Credenciales inválidas", Toast.LENGTH_SHORT).show();
                else {
                    Toast.makeText(Login.this, "Iniciando sesión...", Toast.LENGTH_SHORT).show();
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