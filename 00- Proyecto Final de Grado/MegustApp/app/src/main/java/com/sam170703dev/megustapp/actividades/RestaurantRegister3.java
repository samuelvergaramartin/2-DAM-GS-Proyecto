package com.sam170703dev.megustapp.actividades;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.sam170703dev.megustapp.R;
import com.sam170703dev.megustapp.adaptadores.EditMenuAdapter;
import com.sam170703dev.megustapp.api.API;
import com.sam170703dev.megustapp.api.APIRest;
import com.sam170703dev.megustapp.entidades.Cliente;
import com.sam170703dev.megustapp.entidades.Ingrediente;
import com.sam170703dev.megustapp.entidades.Plato;
import com.sam170703dev.megustapp.entidades.Restaurante;
import com.sam170703dev.megustapp.entidades.Usuario;
import com.sam170703dev.megustapp.peticiones_http.post.PeticionCrearIngrediente;
import com.sam170703dev.megustapp.peticiones_http.post.PeticionCrearPlato;
import com.sam170703dev.megustapp.peticiones_http.post.PeticionCrearRestaurante;
import com.sam170703dev.megustapp.peticiones_http.post.PeticionCrearUsuario;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RestaurantRegister3 extends AppCompatActivity {

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.edit_menu_context_menu, menu);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.restaurant_register3);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.restaurant_register3_activity), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        final ArrayList<Plato> platos = new ArrayList<>();
        final ListView menu = findViewById(R.id.menu_restaurant_register3);
        final EditMenuAdapter adaptador = new EditMenuAdapter(this, platos);
        final Button botonFinalizarRegistro = findViewById(R.id.boton_finalizar_registro_restaurant_register3);
        final TextView textoVolverAtras = findViewById(R.id.texto_volver_atras_restaurant_register3);

        final Bundle datos = getIntent().getExtras();
        final String nombreRestaurante = datos.getString("nombre_restaurante");
        final String correoRestaurante = datos.getString("correo_restaurante");
        final String claveRestaurante = datos.getString("clave_restaurante");
        final String telefonoRestaurante = datos.getString("telefono_restaurante");
        final String ciudadRestaurante = datos.getString("ciudad_restaurante");
        final String calleRestaurante = datos.getString("calle_restaurante");
        final API api = APIRest.getAPI();
        final SharedPreferences sharedPreferences = getSharedPreferences("Tokens", Context.MODE_PRIVATE);
        final String tokenRestaurante = sharedPreferences.getString("token_restaurante", "");

        ActivityResultLauncher resultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult resultado) {
                        if(resultado.getResultCode() == Activity.RESULT_OK) {
                            Intent intent = resultado.getData();
                            if(intent != null) {
                                Bundle datos = intent.getExtras();
                                if(datos != null) {
                                    if(datos.getBoolean("cerrar_sesion")) {
                                        Intent resultadoActividad = new Intent();
                                        resultadoActividad.putExtra("cerrar_sesion", true);
                                        setResult(RESULT_OK, resultadoActividad);
                                        finish();
                                    }
                                    else {
                                        String nombrePlato = datos.getString("nombre_plato");
                                        double precioPlato = Double.parseDouble(datos.getString("precio_plato"));
                                        ArrayList<Ingrediente> ingredientes = new ArrayList<>(datos.getParcelableArrayList("ingredientes_plato"));
                                        String imagenPlato = datos.getString("imagen_plato");
                                        Plato nuevoPlato = new Plato();
                                        nuevoPlato.setNombre(nombrePlato);
                                        nuevoPlato.setPrecio(precioPlato);
                                        for(Ingrediente ingrediente: ingredientes) {
                                            nuevoPlato.addIngrediente(ingrediente);
                                        }
                                        nuevoPlato.setImagen(imagenPlato);
                                        platos.add(platos.size() - 1, nuevoPlato);
                                        menu.setAdapter(adaptador);
                                        adaptador.getViews().clear();
                                        new Handler().postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                for(View plato : adaptador.getViews()) {
                                                    registerForContextMenu(plato);
                                                }
                                            }
                                        }, 1000);
                                    }
                                }
                            }
                        }
                    }
                }
        );

        menu.setAdapter(adaptador);

        menu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(position == parent.getCount() - 1) {
                    Intent addDishActivity = new Intent(RestaurantRegister3.this, AddDish.class);
                    resultLauncher.launch(addDishActivity);
                }
            }
        });

        botonFinalizarRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(platos.size() == 1) {
                    Toast.makeText(RestaurantRegister3.this, "Debes añadir mínimo un plato.", Toast.LENGTH_SHORT).show();
                }
                else {
                    platos.remove(platos.size() - 1);
                    PeticionCrearRestaurante peticionCrearRestaurante = new PeticionCrearRestaurante(
                            nombreRestaurante,
                            telefonoRestaurante,
                            ciudadRestaurante,
                            calleRestaurante,
                            correoRestaurante,
                            claveRestaurante
                    );

                    api.crearRestaurante("Bearer " + tokenRestaurante, peticionCrearRestaurante).enqueue(new Callback<Restaurante>() {
                        @Override
                        public void onResponse(Call<Restaurante> call, Response<Restaurante> response) {
                            if(response.isSuccessful()) {
                                api.crearUsuario("Bearer " + tokenRestaurante, new PeticionCrearUsuario(correoRestaurante, claveRestaurante)).enqueue(new Callback<Usuario>() {
                                    @Override
                                    public void onResponse(Call<Usuario> call, Response<Usuario> response2) {
                                        if(response2.isSuccessful()) {
                                            int delay = 200;
                                            for(Plato plato : platos) {
                                                for(Ingrediente ingrediente : plato.getIngredientes()) {
                                                    new Handler().postDelayed(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            api.obtenerIngredientes("Bearer " + tokenRestaurante).enqueue(new Callback<List<Ingrediente>>() {
                                                                @Override
                                                                public void onResponse(Call<List<Ingrediente>> call, Response<List<Ingrediente>> response3) {
                                                                    if(response.isSuccessful() && ingrediente != null && ingrediente.getNombre() != null && !ingredienteRegistrado(response3.body(), ingrediente)) {
                                                                        Log.i("LOG-INGREDIENTE", "contains?: " + response3.body().contains(ingrediente));
                                                                        Log.i("LOG-INGREDIENTE-2", "listado: " + response3.body().toString());
                                                                        Log.i("LOG-INGREDIENTE-3", "nombre ingrediente: " + ingrediente.getNombre());
                                                                        api.crearIngrediente("Bearer " + tokenRestaurante, new PeticionCrearIngrediente(
                                                                                ingrediente.getNombre(),
                                                                                ingrediente.isEsAlergeno()
                                                                        )).enqueue(new Callback<Ingrediente>() {
                                                                            @Override
                                                                            public void onResponse(Call<Ingrediente> call, Response<Ingrediente> response4) {
                                                                                if(response4.isSuccessful()) {
                                                                                    Ingrediente refIngrediente = new Ingrediente();
                                                                                    refIngrediente.setNombre(response4.body().getNombre());
                                                                                    refIngrediente.setEsAlergeno(response4.body().isEsAlergeno());
                                                                                    for(int i = 0; i < plato.getIngredientes().size(); i++) {
                                                                                        if(plato.getIngredientes().get(i).equals(refIngrediente)) plato.getIngredientes().set(i, response4.body());
                                                                                    }
                                                                                    //plato.getIngredientes().set(plato.getIngredientes().indexOf(refIngrediente), response4.body());
                                                                                }
                                                                                else Toast.makeText(RestaurantRegister3.this, "code: " + response4.code(), Toast.LENGTH_SHORT).show();
                                                                            }

                                                                            @Override
                                                                            public void onFailure(Call<Ingrediente> call, Throwable t) {}
                                                                        });
                                                                    }
                                                                    else if(ingrediente != null && ingrediente.getNombre() != null) {
                                                                        Ingrediente ingredienteCompleto = new Ingrediente();
                                                                        ingredienteCompleto.setNombre(ingrediente.getNombre());
                                                                        ingredienteCompleto.setEsAlergeno(ingrediente.isEsAlergeno());
                                                                        int i = 0;
                                                                        boolean encontrado = false;

                                                                        while (i < response3.body().size() && !encontrado) {
                                                                            if(response3.body().get(i).getNombre().equals(ingrediente.getNombre()) && response3.body().get(i).isEsAlergeno() == ingrediente.isEsAlergeno()) {
                                                                                ingredienteCompleto.setId(response3.body().get(i).getId());
                                                                                encontrado = true;
                                                                            }

                                                                            i++;
                                                                        }

                                                                        i = 0;
                                                                        encontrado = false;

                                                                        while (i < plato.getIngredientes().size() && !encontrado) {
                                                                            if(plato.getIngredientes().get(i).getNombre().equals(ingredienteCompleto.getNombre()) && plato.getIngredientes().get(i).isEsAlergeno() == ingredienteCompleto.isEsAlergeno()) {
                                                                                plato.getIngredientes().get(i).setId(ingredienteCompleto.getId());
                                                                                encontrado = true;
                                                                            }

                                                                            i++;
                                                                        }
                                                                    }

                                                                    if(ingrediente != null && ingrediente.getNombre() == null) {
                                                                        plato.getIngredientes().remove(ingrediente);
                                                                    }
                                                                }

                                                                @Override
                                                                public void onFailure(Call<List<Ingrediente>> call, Throwable t) {}
                                                            });
                                                        }
                                                    }, delay);
                                                    delay+= 200;
                                                }
                                                new Handler().postDelayed(new Runnable() {
                                                    PeticionCrearPlato peticionCrearPlato = new PeticionCrearPlato(
                                                            plato.getNombre(),
                                                            plato.getPrecio(),
                                                            plato.getImagen(),
                                                            plato.getIngredientes(),
                                                            response.body().getId());
                                                    @Override
                                                    public void run() {
                                                        api.crearPlato("Bearer " + tokenRestaurante, new PeticionCrearPlato(
                                                                plato.getNombre(),
                                                                plato.getPrecio(),
                                                                plato.getImagen(),
                                                                plato.getIngredientes(),
                                                                response.body().getId()
                                                        )).enqueue(new Callback<Plato>() {
                                                            @Override
                                                            public void onResponse(Call<Plato> call, Response<Plato> response3) {
                                                                Log.i("PETICION", "Peticion crear plato: " + peticionCrearPlato);
                                                                if(!response3.isSuccessful()) Log.i("TROLL", "La app te esta trolleando silenciosamente");
                                                            }

                                                            @Override
                                                            public void onFailure(Call<Plato> call, Throwable t) {
                                                                throw new Error("ERROR_PLATOS " + t.getMessage());
                                                            }
                                                        });
                                                    }
                                                }, delay);
                                                delay+= 200;
                                            }

                                            new Handler().postDelayed(new Runnable() {
                                                @Override
                                                public void run() {
                                                    Toast.makeText(RestaurantRegister3.this, "Cuenta creada satisfactoriamente", Toast.LENGTH_SHORT).show();
                                                    Intent restaurantMainActivity = new Intent(RestaurantRegister3.this, MainActivityRestaurant.class);
                                                    restaurantMainActivity.putExtra("id_usuario", response2.body().getId());
                                                    resultLauncher.launch(restaurantMainActivity);
                                                }
                                            }, delay);
                                        }
                                        else Toast.makeText(RestaurantRegister3.this, "Se ha producido un error al crear la cuenta.", Toast.LENGTH_SHORT).show();
                                    }

                                    @Override
                                    public void onFailure(Call<Usuario> call, Throwable t) {
                                        Toast.makeText(RestaurantRegister3.this, "Se ha producido un error al crear la cuenta.", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                            else Toast.makeText(RestaurantRegister3.this, "Se ha producido un error al crear la cuenta", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onFailure(Call<Restaurante> call, Throwable t) {
                            Toast.makeText(RestaurantRegister3.this, "Se ha producido un error al crear la cuenta", Toast.LENGTH_SHORT).show();
                        }
                    });
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

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        Toast.makeText(this, item.getTitle(), Toast.LENGTH_SHORT).show();

        Toast.makeText(this, "View: " + item.getActionView(), Toast.LENGTH_SHORT).show();
        return super.onContextItemSelected(item);
    }

    private boolean ingredienteRegistrado(List<Ingrediente> ingredientes, Ingrediente ingrediente) {
        boolean registrado = false;
        int i = 0;

        while (i < ingredientes.size() && !registrado) {
            if(ingredientes.get(i).getNombre().equals(ingrediente.getNombre()) && ingredientes.get(i).isEsAlergeno() == ingrediente.isEsAlergeno()) registrado = true;

            i++;
        }

        return registrado;
    }
}