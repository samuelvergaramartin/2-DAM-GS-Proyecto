package com.example.megustapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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

        final Button botonRegistrarse = findViewById(R.id.boton_siguiente_restaurant_register);
        final TextView textoVolverAtras = findViewById(R.id.texto_volver_atras_restaurant_register);

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

        botonRegistrarse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent restaurantRegister2Activity = new Intent(RestaurantRegister.this, RestaurantRegister2.class);
                resultLauncher.launch(restaurantRegister2Activity);
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