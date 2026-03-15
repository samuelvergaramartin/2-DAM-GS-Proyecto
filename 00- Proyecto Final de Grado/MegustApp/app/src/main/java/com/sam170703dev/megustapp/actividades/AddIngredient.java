package com.sam170703dev.megustapp.actividades;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.sam170703dev.megustapp.R;

public class AddIngredient extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.add_ingredient);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.add_ingredient), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        EditText nombreDelIngrediente = findViewById(R.id.nombre_del_ingrediente_add_ingredient);
        CheckBox esAlergeno = findViewById(R.id.esAlergeno_add_ingredient);
        Button botonAgregarIngrediente = findViewById(R.id.boton_agregar_ingrediente_add_ingredient);
        TextView textoVolverAtras = findViewById(R.id.texto_volver_atras_add_ingredient);

        textoVolverAtras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_CANCELED);
                finish();
            }
        });

        botonAgregarIngrediente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent resultado = new Intent();
                resultado.putExtra("nombre_ingrediente", nombreDelIngrediente.getText().toString());
                resultado.putExtra("es_alergeno", esAlergeno.isChecked());
                setResult(RESULT_OK, resultado);
                finish();
            }
        });
    }
}
