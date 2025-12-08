package com.sam170703dev.megustapp.actividades;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.sam170703dev.megustapp.R;

public class RestaurantInfo extends AppCompatActivity {

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.restaurant_info_options_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.restaurant_info);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.restaurantInfoActivity), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        final Bundle datos = getIntent().getExtras();
        final String nombreRestaurante = datos.getString("nombre_restaurante");
        final Toolbar toolbar = findViewById(R.id.toolbar_restaurant_info);
        toolbar.setTitle(nombreRestaurante);
        setSupportActionBar(toolbar);
        final Button botonVerUbicacion = findViewById(R.id.boton_ver_ubicacion_data_restaurant_info);
        final Button botonLlamar = findViewById(R.id.boton_llamar_data_restaurant_info);

        botonVerUbicacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ubicacion = new Intent(Intent.ACTION_VIEW);
                ubicacion.setData(Uri.parse("geo:0,0?q=Avenida de Isaac Peral, 16, Málaga"));
                startActivity(ubicacion);
            }
        });

        botonLlamar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent marcarTelefono = new Intent(Intent.ACTION_DIAL);
                marcarTelefono.setData(Uri.parse("tel:722688078"));
                startActivity(marcarTelefono);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        finish();
        return super.onOptionsItemSelected(item);
    }
}