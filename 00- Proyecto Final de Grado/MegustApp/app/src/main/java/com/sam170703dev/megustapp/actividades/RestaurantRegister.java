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

import java.util.ArrayList;
import java.util.List;

public class RestaurantRegister extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.restaurant_register);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.restaurant_register_activity), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        final EditText usuarioInput = findViewById(R.id.nombre_restaurant_register);
        final EditText correoInput = findViewById(R.id.correo_restaurant_register);
        final EditText claveInput = findViewById(R.id.clave_restaurant_register);
        final EditText confirmarClaveInput = findViewById(R.id.confirmar_clave_restaurant_register);
        final Button botonSiguiente = findViewById(R.id.boton_siguiente_restaurant_register);
        final TextView textoVolverAtras = findViewById(R.id.texto_volver_atras_restaurant_register);
        final ArrayList<Character> caracteresNoValidosCorreo = new ArrayList<>();

        caracteresNoValidosCorreo.add('!');
        caracteresNoValidosCorreo.add('#');
        caracteresNoValidosCorreo.add('^');
        caracteresNoValidosCorreo.add('&');
        caracteresNoValidosCorreo.add('*');
        caracteresNoValidosCorreo.add('=');
        caracteresNoValidosCorreo.add('/');
        caracteresNoValidosCorreo.add(';');
        caracteresNoValidosCorreo.add('?');
        caracteresNoValidosCorreo.add(':');

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

        botonSiguiente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean completado = true;
                if(usuarioInput.getText().toString().isBlank()) {
                    usuarioInput.setError("Este campo es obligatorio.");
                    completado = false;
                }
                if(correoInput.getText().toString().isBlank()) {
                    correoInput.setError("Este campo es obligatorio.");
                    completado = false;
                }
                if(claveInput.getText().toString().isBlank()) {
                    claveInput.setError("Este campo es obligatorio.");
                    completado = false;
                }
                if(confirmarClaveInput.getText().toString().isBlank()) {
                    confirmarClaveInput.setError("Este campo es obligatorio.");
                    completado = false;
                }

                if(completado) {
                    String cadena = correoInput.getText().toString().split("@")[0];
                    boolean contieneCaracteresInvalidos = false;
                    for(Character caracter : cadena.toCharArray()) {
                        if(caracteresNoValidosCorreo.contains(caracter)) contieneCaracteresInvalidos = true;
                    }

                    if(contieneCaracteresInvalidos) {
                        correoInput.setError("El correo introducido contiene caracteres inválidos.");
                    }
                    else {
                        List<Character> caracteresCorreo = new ArrayList<>();
                        for(Character caracter : correoInput.getText().toString().toCharArray()) {
                            caracteresCorreo.add(caracter);
                        }
                        if(!caracteresCorreo.contains('@') || !caracteresCorreo.contains('.')) {
                            correoInput.setError("El correo introducido no tiene un formato válido.");
                        }
                        else {
                            if(claveInput.getText().toString().equals(confirmarClaveInput.getText().toString())) {
                                Intent clientRegister2Activity = new Intent(RestaurantRegister.this, RestaurantRegister2.class);
                                resultLauncher.launch(clientRegister2Activity);
                            }
                            else {
                                Toast.makeText(RestaurantRegister.this, "Los campos de las contraseñas deben ser iguales.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }
                else {
                    Toast.makeText(RestaurantRegister.this, "Todos los campos son obligatorios.", Toast.LENGTH_SHORT).show();
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
}