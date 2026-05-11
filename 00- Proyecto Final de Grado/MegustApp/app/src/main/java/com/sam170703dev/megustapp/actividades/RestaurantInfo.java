package com.sam170703dev.megustapp.actividades;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sam170703dev.megustapp.R;
import com.sam170703dev.megustapp.adaptadores.RestaurantMenuAdapter;
import com.sam170703dev.megustapp.adaptadores.ReviewsListAdapter;
import com.sam170703dev.megustapp.api.API;
import com.sam170703dev.megustapp.api.APIRest;
import com.sam170703dev.megustapp.entidades.Cliente;
import com.sam170703dev.megustapp.entidades.Plato;
import com.sam170703dev.megustapp.entidades.Valoracion;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RestaurantInfo extends AppCompatActivity {

    private int posicionArray;
    private ArrayList<Valoracion> valoracionesRestaurante;

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
        final int idRestaurante = datos.getInt("id_restaurante");
        final String nombreRestaurante = datos.getString("nombre_restaurante");
        final String fotoRestaurante = datos.getString("imagen_restaurante");
        final String ciudadRestaurante = datos.getString("ciudad_restaurante");
        final String calleRestaurante = datos.getString("calle_restaurante");
        final String telefonoRestaurante = datos.getString("telefono_restaurante");
        final ArrayList<Plato> platosRestaurante = new ArrayList<>(datos.getParcelableArrayList("platos_restaurante"));
        valoracionesRestaurante = new ArrayList<>(datos.getParcelableArrayList("valoraciones_restaurante"));
        final String tokenCliente = datos.getString("token_cliente");
        final int idCliente = datos.getInt("id_cliente");
        posicionArray = datos.getInt("posicion_array");
        final ImageView imagen = findViewById(R.id.imagen_restaurante_data_restaurant_info);
        final TextView ciudad = findViewById(R.id.ciudad_data_restaurant_info);
        final TextView calle = findViewById(R.id.calle_data_restaurant_info);
        final TextView telefono = findViewById(R.id.telefono_data_restaurant_info);
        final RecyclerView listadoValoraciones = findViewById(R.id.valoraciones_data_restaurant_reviews);
        final TextView textoSinValoraciones = findViewById(R.id.texto_sin_valoraciones_data_restaurant_reviews);
        final Toolbar toolbar = findViewById(R.id.toolbar_restaurant_info);
        final Button botonAgregarValoracion = findViewById(R.id.boton_agregar_valoracion_data_restaurant_reviews);

        toolbar.setTitle(nombreRestaurante);
        setSupportActionBar(toolbar);
        ciudad.setText("Ciudad: " + ciudadRestaurante);
        calle.setText(calleRestaurante);
        telefono.setText("Teléfono: " + telefonoRestaurante);

        APIRest.cargarImagen(this, fotoRestaurante, imagen);

        final Button botonVerUbicacion = findViewById(R.id.boton_ver_ubicacion_data_restaurant_info);
        final Button botonLlamar = findViewById(R.id.boton_llamar_data_restaurant_info);
        final RecyclerView menu = findViewById(R.id.menu_data_restaurant_menu);
        final RestaurantMenuAdapter restaurantMenuAdapter = new RestaurantMenuAdapter(platosRestaurante);
        final API api = APIRest.getAPI();

        listadoValoraciones.setHasFixedSize(true);
        menu.setHasFixedSize(true);

        menu.setAdapter(restaurantMenuAdapter);
        menu.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        ActivityResultLauncher activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if(result.getResultCode() == RESULT_OK) {
                            Intent intent = result.getData();

                            if(intent != null) {
                                Bundle bundle = intent.getExtras();

                                if(bundle != null) {
                                    int numeroEstrellas = bundle.getInt("numero_estrellas");
                                    String valoracion = bundle.getString("valoracion");

                                    api.getClienteById("Bearer " + tokenCliente, idCliente).enqueue(new Callback<Cliente>() {
                                        @Override
                                        public void onResponse(Call<Cliente> call, Response<Cliente> response) {
                                            if(response.isSuccessful()) {
                                                api.getClientes("Bearer " + tokenCliente).enqueue(new Callback<List<Cliente>>() {
                                                    @Override
                                                    public void onResponse(Call<List<Cliente>> call, Response<List<Cliente>> response2) {
                                                        if(response2.isSuccessful()) {
                                                            Valoracion nuevaValoracion = new Valoracion();
                                                            nuevaValoracion.setNota(numeroEstrellas);
                                                            nuevaValoracion.setComentario(valoracion);
                                                            nuevaValoracion.setClienteId(idCliente);
                                                            valoracionesRestaurante.add(nuevaValoracion);

                                                            final ArrayList<String> clientes = new ArrayList<>();
                                                            final ArrayList<Cliente> clientesAPI = new ArrayList<>(response2.body());
                                                            ReviewsListAdapter adaptador;
                                                            for(Valoracion valoracion : valoracionesRestaurante) {
                                                                clientes.add(getClientNameById(valoracion.getClienteId(), clientesAPI));
                                                            }
                                                            adaptador = new ReviewsListAdapter(valoracionesRestaurante, clientes);
                                                            listadoValoraciones.setAdapter(adaptador);
                                                            textoSinValoraciones.setVisibility(View.GONE);
                                                        }
                                                        else Toast.makeText(RestaurantInfo.this, "Error al obtener información de los clientes", Toast.LENGTH_SHORT).show();
                                                    }

                                                    @Override
                                                    public void onFailure(Call<List<Cliente>> call, Throwable t) {
                                                        Toast.makeText(RestaurantInfo.this, "Error al obtener información de los clientes", Toast.LENGTH_SHORT).show();
                                                    }
                                                });

                                            }
                                            else Toast.makeText(RestaurantInfo.this, "Error al obtener los datos del cliente.", Toast.LENGTH_SHORT).show();
                                        }

                                        @Override
                                        public void onFailure(Call<Cliente> call, Throwable t) {
                                            Toast.makeText(RestaurantInfo.this, "Error al obtener los datos del cliente.", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            }
                        }
                    }
                }
        );

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

        botonAgregarValoracion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent actividadAgregarValoracion = new Intent(RestaurantInfo.this, AddReview.class);
                actividadAgregarValoracion.putExtra("token_cliente", tokenCliente);
                actividadAgregarValoracion.putExtra("id_cliente", idCliente);
                actividadAgregarValoracion.putExtra("id_restaurante", idRestaurante);

                activityResultLauncher.launch(actividadAgregarValoracion);
            }
        });

        api.getClientes("Bearer " + tokenCliente).enqueue(new Callback<List<Cliente>>() {
            @Override
            public void onResponse(Call<List<Cliente>> call, Response<List<Cliente>> response) {
                if(response.isSuccessful()) {
                    if(response.body() != null) {
                        final ArrayList<String> clientes = new ArrayList<>();
                        final ArrayList<Cliente> clientesAPI = new ArrayList<>(response.body());
                        ReviewsListAdapter adaptador;
                        for(Valoracion valoracion : valoracionesRestaurante) {
                            clientes.add(getClientNameById(valoracion.getClienteId(), clientesAPI));
                        }
                        adaptador = new ReviewsListAdapter(valoracionesRestaurante, clientes);
                        listadoValoraciones.setAdapter(adaptador);
                        listadoValoraciones.setLayoutManager(new LinearLayoutManager(RestaurantInfo.this, LinearLayoutManager.VERTICAL, false));
                        if(valoracionesRestaurante.isEmpty()) textoSinValoraciones.setVisibility(View.VISIBLE);
                        else textoSinValoraciones.setVisibility(View.GONE);
                    }
                }
                else Toast.makeText(RestaurantInfo.this, "Error al obtener informacion de los clientes.", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<List<Cliente>> call, Throwable t) {
                Toast.makeText(RestaurantInfo.this, "Error al obtener informacion de los clientes.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        setResult(Activity.RESULT_OK);
        finish();
        return super.onOptionsItemSelected(item);
    }

    private String getClientNameById(int id, ArrayList<Cliente> clientes) {
        int i = 0;
        String nombre = "";

        while(i < clientes.size() && nombre.isBlank()) {
            if(clientes.get(i).getId() == id) {
                nombre = clientes.get(i).getNombre();
            }

            i++;
        }

        return nombre;
    }
}