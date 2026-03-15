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

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class RestaurantRegister2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.restaurant_register2);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.restaurant_register2_activity), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        final EditText telefonoInput = findViewById(R.id.telefono_restaurant_register2);
        final EditText ciudadInput = findViewById(R.id.ciudad_restaurant_register2);
        final EditText calleInput = findViewById(R.id.calle_restaurant_register2);
        final Button botonSiguiente = findViewById(R.id.boton_siguiente_restaurant_register2);
        final TextView textoVolverAtras = findViewById(R.id.texto_volver_atras_restaurant_register2);
        final Bundle datos = getIntent().getExtras();

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

                if(telefonoInput.getText().toString().isBlank()) {
                    telefonoInput.setError("Este campo es obligatorio.");
                    completado = false;
                }
                if(ciudadInput.getText().toString().isBlank()) {
                    ciudadInput.setError("Este campo es obligatorio.");
                    completado = false;
                }
                if(calleInput.getText().toString().isBlank()) {
                    calleInput.setError("Este campo es obligatorio.");
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
                        Intent restaurantRegister3Activity = new Intent(RestaurantRegister2.this, RestaurantRegister3.class);
                        restaurantRegister3Activity.putExtra("nombre_restaurante", datos.getString("nombre_restaurante"));
                        restaurantRegister3Activity.putExtra("correo_restaurante", datos.getString("correo_restaurante"));
                        restaurantRegister3Activity.putExtra("clave_restaurante", datos.getString("clave_restaurante"));
                        restaurantRegister3Activity.putExtra("telefono_restaurante", telefonoInput.getText().toString());
                        restaurantRegister3Activity.putExtra("ciudad_restaurante", ciudadInput.getText().toString());
                        restaurantRegister3Activity.putExtra("calle_restaurante", calleInput.getText().toString());
                        resultLauncher.launch(restaurantRegister3Activity);
                    }
                }
                else {
                    Toast.makeText(RestaurantRegister2.this, "Todos los campos son obligatorios.", Toast.LENGTH_SHORT).show();
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