package com.sam170703dev.megustapp.actividades;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.sam170703dev.megustapp.R;
import com.sam170703dev.megustapp.adaptadores.RestaurantMenuAdapter;
import com.sam170703dev.megustapp.api.API;
import com.sam170703dev.megustapp.api.APIRest;
import com.sam170703dev.megustapp.entidades.Plato;

import java.util.ArrayList;

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
        final String fotoRestaurante = datos.getString("imagen_restaurante");
        final String ciudadRestaurante = datos.getString("ciudad_restaurante");
        final String calleRestaurante = datos.getString("calle_restaurante");
        final String telefonoRestaurante = datos.getString("telefono_restaurante");
        final ArrayList<Plato> platosRestaurante = new ArrayList<>(datos.getParcelableArrayList("platos_restaurante"));
        final ImageView imagen = findViewById(R.id.imagen_restaurante_data_restaurant_info);
        final TextView ciudad = findViewById(R.id.ciudad_data_restaurant_info);
        final TextView calle = findViewById(R.id.calle_data_restaurant_info);
        final TextView telefono = findViewById(R.id.telefono_data_restaurant_info);
        final Toolbar toolbar = findViewById(R.id.toolbar_restaurant_info);

        toolbar.setTitle(nombreRestaurante);
        setSupportActionBar(toolbar);
        ciudad.setText("Ciudad: " + ciudadRestaurante);
        calle.setText(calleRestaurante);
        telefono.setText("Teléfono: " + telefonoRestaurante);

        APIRest.cargarImagen(this, fotoRestaurante, imagen);

        final Button botonVerUbicacion = findViewById(R.id.boton_ver_ubicacion_data_restaurant_info);
        final Button botonLlamar = findViewById(R.id.boton_llamar_data_restaurant_info);
        final ListView menu = findViewById(R.id.menu_data_restaurant_menu);
        final RestaurantMenuAdapter restaurantMenuAdapter = new RestaurantMenuAdapter(this, platosRestaurante);

        menu.setAdapter(restaurantMenuAdapter);

        botonVerUbicacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ubicacion = new Intent(Intent.ACTION_VIEW);
                ubicacion.setData(Uri.parse("geo:0,0?q=" + calleRestaurante + ", " + ciudadRestaurante));
                startActivity(ubicacion);
            }
        });

        botonLlamar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent marcarTelefono = new Intent(Intent.ACTION_DIAL);
                marcarTelefono.setData(Uri.parse("tel:" + telefonoRestaurante));
                startActivity(marcarTelefono);
            }
        });

        API api = APIRest.getAPI();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        finish();
        return super.onOptionsItemSelected(item);
    }
}