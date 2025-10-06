package com.example.megustapp;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
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

        final EditText usernameInput = findViewById(R.id.username_input_login);
        final EditText passwordInput = findViewById(R.id.password_input_login);
        final Button loginButton = findViewById(R.id.login_button_login_activity);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(usernameInput.getText().toString().isEmpty()) usernameInput.setError("Este campo es obligatorio.");
                else {
                    if(!usernameInput.getText().toString().equals("root")) usernameInput.setError("Nombre de usuario inválido");
                }

                if(passwordInput.getText().toString().isEmpty()) passwordInput.setError("Este campo es obligatorio.");
                else {
                    if(!passwordInput.getText().toString().equals("root")) passwordInput.setError("Contraseña incorrecta.");
                }
            }
        });
    }
}