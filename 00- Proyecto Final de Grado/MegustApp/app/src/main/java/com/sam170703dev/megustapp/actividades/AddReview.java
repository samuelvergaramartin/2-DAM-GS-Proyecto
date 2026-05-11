package com.sam170703dev.megustapp.actividades;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.sam170703dev.megustapp.R;
import com.sam170703dev.megustapp.api.API;
import com.sam170703dev.megustapp.api.APIRest;
import com.sam170703dev.megustapp.entidades.Valoracion;
import com.sam170703dev.megustapp.peticiones_http.post.PeticionInsertarValoracion;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddReview extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_review);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.activity_add_review), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        final RatingBar ratingBar = findViewById(R.id.estrellas_activity_add_review);
        final EditText valoracion = findViewById(R.id.valoracion_activity_add_review);
        final Button botonFinalizar = findViewById(R.id.boton_finalizar_activity_add_review);
        final TextView textoVolverAtras = findViewById(R.id.texto_volver_atras_activity_add_review);
        final Bundle bundle = getIntent().getExtras();
        final String tokenCliente = bundle.getString("token_cliente");
        final int idCliente = bundle.getInt("id_cliente");
        final int idRestaurante = bundle.getInt("id_restaurante");
        final API api = APIRest.getAPI();

        botonFinalizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(valoracion.getText().toString().isBlank()) {
                    valoracion.setError("Este campo es obligatorio.");
                }
                else {
                    api.insertarValoracion("Bearer " + tokenCliente, new PeticionInsertarValoracion(
                            Math.round(ratingBar.getRating()),
                            valoracion.getText().toString(),
                            idCliente,
                            idRestaurante
                    )).enqueue(new Callback<Valoracion>() {
                        @Override
                        public void onResponse(Call<Valoracion> call, Response<Valoracion> response) {
                            if(response.isSuccessful()) {
                                Intent resultado = new Intent();
                                resultado.putExtra("numero_estrellas", Math.round(ratingBar.getRating()));
                                resultado.putExtra("valoracion", valoracion.getText().toString());
                                setResult(Activity.RESULT_OK, resultado);
                                Toast.makeText(AddReview.this, "Reseña publicada satisfactoriamente.", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                            else Toast.makeText(AddReview.this, "Se ha producido un error al intentar añadir la reseña.", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onFailure(Call<Valoracion> call, Throwable t) {
                            Toast.makeText(AddReview.this, "Se ha producido un error al intentar añadir la reseña.", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

        textoVolverAtras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(Activity.RESULT_CANCELED);
                finish();
            }
        });
    }
}