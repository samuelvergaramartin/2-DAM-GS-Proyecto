package com.example.megustapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class AddDish extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.add_dish);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        final EditText nombreDelPlato = findViewById(R.id.nombre_del_plato_add_dish);
        final EditText ingredientes = findViewById(R.id.ingredientes_del_plato_add_dish);
        final Button botonRegistrarPlato = findViewById(R.id.boton_registrar_plato_add_dish);
        final TextView textoVolverAtras = findViewById(R.id.texto_volver_atras_add_dish);

        textoVolverAtras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_CANCELED);
                finish();
            }
        });

        botonRegistrarPlato.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent resultado = new Intent();
                resultado.putExtra("nombre_plato", nombreDelPlato.getText().toString());
                resultado.putExtra("ingredientes", ingredientes.getText().toString());
                setResult(RESULT_OK, resultado);
                finish();
            }
        });
    }
}