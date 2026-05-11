package com.sam170703dev.megustapp.actividades;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.sam170703dev.megustapp.R;
import com.sam170703dev.megustapp.entidades.Ingrediente;

import java.util.ArrayList;

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

        final Bundle bundle = getIntent().getExtras();

        final EditText nombreDelIngrediente = findViewById(R.id.nombre_del_ingrediente_add_ingredient);
        final CheckBox esAlergeno = findViewById(R.id.esAlergeno_add_ingredient);
        final Button botonAgregarIngrediente = findViewById(R.id.boton_agregar_ingrediente_add_ingredient);
        final TextView textoVolverAtras = findViewById(R.id.texto_volver_atras_add_ingredient);

        if(bundle != null) {
            Ingrediente ingrediente = ((ArrayList<Ingrediente>) bundle.getSerializable("ingrediente")).get(0);
            botonAgregarIngrediente.setText("Editar ingrediente");
            nombreDelIngrediente.setText(ingrediente.getNombre());
            esAlergeno.setChecked(ingrediente.isEsAlergeno());
        }

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
                if(nombreDelIngrediente.getText().toString().isBlank()) {
                    nombreDelIngrediente.setError("Campo obligatorio");
                    Toast.makeText(AddIngredient.this, "Error: El nombre del ingrediente es obligatorio", Toast.LENGTH_SHORT).show();
                }
                else {
                    Intent resultado = new Intent();
                    resultado.putExtra("nombre_ingrediente", nombreDelIngrediente.getText().toString());
                    resultado.putExtra("es_alergeno", esAlergeno.isChecked());
                    resultado.putExtra("editar_ingrediente", bundle != null);
                    if(bundle != null) resultado.putExtra("id_ingrediente", ((ArrayList<Ingrediente>) bundle.getSerializable("ingrediente")).get(0).getId());
                    setResult(RESULT_OK, resultado);
                    finish();
                }
            }
        });
    }
}
