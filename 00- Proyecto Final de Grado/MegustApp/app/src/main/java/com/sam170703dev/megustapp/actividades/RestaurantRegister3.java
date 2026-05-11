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

    private Plato platoSeleccionado;
    private EditMenuAdapter adaptador;
    private ArrayList<Plato> platos = new ArrayList<>();
    private ListView menu;
    private ActivityResultLauncher resultLauncher;
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

        menu = findViewById(R.id.menu_restaurant_register3);
        adaptador = new EditMenuAdapter(this, platos);
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

        resultLauncher = registerForActivityResult(
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
                                    else if(datos.getBoolean("editar_plato")) {
                                        String nombrePlato = datos.getString("nombre_plato");
                                        double precioPlato = Double.parseDouble(datos.getString("precio_plato"));
                                        ArrayList<Ingrediente> ingredientes = (ArrayList<Ingrediente>) datos.getSerializable("ingredientes_plato");
                                        String imagenPlato = datos.getString("imagen_plato");
                                        Plato plato = obtenerPlatoPorCodigo(datos.getInt("id_plato"));
                                        int pos = platos.indexOf(plato);

                                        if(plato != null) {
                                            plato.setNombre(nombrePlato);
                                            plato.setPrecio(precioPlato);
                                            plato.getIngredientes().clear();
                                            ingredientes.forEach(ingrediente -> plato.addIngrediente(ingrediente));
                                            plato.setImagen(imagenPlato);

                                            platos.set(pos, plato);
                                            menu.setAdapter(adaptador);
                                            adaptador.getViews().clear();
                                            new Handler().postDelayed(new Runnable() {
                                                @Override
                                                public void run() {
                                                    for(int i = 0; i < adaptador.getViews().size(); i++) {
                                                        View platoView = adaptador.getViews().get(i);
                                                        Plato plato = adaptador.getItem(i);

                                                        platoView.setOnLongClickListener(new View.OnLongClickListener() {
                                                            @Override
                                                            public boolean onLongClick(View v) {
                                                                platoSeleccionado = plato;

                                                                return platoView.showContextMenu();
                                                            }
                                                        });

                                                        registerForContextMenu(platoView);
                                                    }
                                                }
                                            }, 1000);
                                        }
                                    }
                                    else {
                                        String nombrePlato = datos.getString("nombre_plato");
                                        double precioPlato = Double.parseDouble(datos.getString("precio_plato"));
                                        ArrayList<Ingrediente> ingredientes = (ArrayList<Ingrediente>) datos.getSerializable("ingredientes_plato");
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
                                                for(int i = 0; i < adaptador.getViews().size(); i++) {
                                                    View platoView = adaptador.getViews().get(i);
                                                    Plato plato = adaptador.getItem(i);

                                                    platoView.setOnLongClickListener(new View.OnLongClickListener() {
                                                        @Override
                                                        public boolean onLongClick(View v) {
                                                            platoSeleccionado = plato;

                                                            return platoView.showContextMenu();
                                                        }
                                                    });

                                                    registerForContextMenu(platoView);
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
                            if(response.isSuccessful() && response.body() != null) {
                                api.crearUsuario("Bearer " + tokenRestaurante, new PeticionCrearUsuario(correoRestaurante, claveRestaurante)).enqueue(new Callback<Usuario>() {
                                    @Override
                                    public void onResponse(Call<Usuario> call, Response<Usuario> response2) {
                                        if(response2.isSuccessful()) {
                                            crearPlatosRegistro(api, tokenRestaurante, platos, response.body().getId(), new CompletionCallback() {
                                                @Override
                                                public void onComplete() {
                                                    Toast.makeText(RestaurantRegister3.this, "Cuenta creada satisfactoriamente", Toast.LENGTH_SHORT).show();
                                                    Intent restaurantMainActivity = new Intent(RestaurantRegister3.this, MainActivityRestaurant.class);
                                                    restaurantMainActivity.putExtra("id_usuario", response2.body().getId());
                                                    restaurantMainActivity.putExtra("id_restaurante", response.body().getId());
                                                    restaurantMainActivity.putExtra("nombre_restaurante", response.body().getNombre());
                                                    resultLauncher.launch(restaurantMainActivity);
                                                }
                                            });
                                        }
                                        else {
                                            Toast.makeText(RestaurantRegister3.this, "Se ha producido un error al crear la cuenta.", Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<Usuario> call, Throwable t) {
                                        Toast.makeText(RestaurantRegister3.this, "Se ha producido un error al crear la cuenta.", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                            else {
                                Toast.makeText(RestaurantRegister3.this, "Se ha producido un error al crear la cuenta", Toast.LENGTH_SHORT).show();
                            }
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

        if(item.getItemId() == R.id.opc_editar_plato) {
            Intent addDishActivity = new Intent(RestaurantRegister3.this, AddDish.class);
            ArrayList<Plato> plato = new ArrayList<>();
            plato.add(platoSeleccionado);
            addDishActivity.putExtra("plato", plato);
            resultLauncher.launch(addDishActivity);
        }
        else {
            ArrayList<Plato> temp = new ArrayList<>();
            platos.forEach(plato -> {
                if(!plato.equals(platoSeleccionado) && plato.getNombre() != null) temp.add(plato);
            });
            platos = new ArrayList<>(temp);
            adaptador = new EditMenuAdapter(this, platos);
            menu.setAdapter(adaptador);
            adaptador.getViews().clear();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    for(int i = 0; i < adaptador.getViews().size(); i++) {
                        View platoView = adaptador.getViews().get(i);
                        Plato plato = adaptador.getItem(i);

                        platoView.setOnLongClickListener(new View.OnLongClickListener() {
                            @Override
                            public boolean onLongClick(View v) {
                                platoSeleccionado = plato;

                                return platoView.showContextMenu();
                            }
                        });

                        registerForContextMenu(platoView);
                    }
                }
            }, 1000);
            Toast.makeText(this, "Plato eliminado satisfactoriamente", Toast.LENGTH_SHORT).show();
        }

        return super.onContextItemSelected(item);
    }


    private interface CompletionCallback {
        void onComplete();
    }

    private void crearPlatosRegistro(API api, String tokenRestaurante, ArrayList<Plato> platos, int idRestaurante, CompletionCallback completionCallback) {
        guardarPlatosRegistro(api, tokenRestaurante, platos, idRestaurante, 0, completionCallback);
    }

    private void guardarPlatosRegistro(API api, String tokenRestaurante, ArrayList<Plato> platos, int idRestaurante, int index, CompletionCallback completionCallback) {
        if(index >= platos.size()) {
            completionCallback.onComplete();
            return;
        }

        Plato plato = platos.get(index);
        if(plato == null || plato.getNombre() == null) {
            guardarPlatosRegistro(api, tokenRestaurante, platos, idRestaurante, index + 1, completionCallback);
            return;
        }

        sincronizarIngredientesRegistro(api, tokenRestaurante, plato, 0, new CompletionCallback() {
            @Override
            public void onComplete() {
                ArrayList<Ingrediente> ingredientesValidos = new ArrayList<>();
                for(Ingrediente ingrediente : plato.getIngredientes()) {
                    if(ingrediente != null && ingrediente.getNombre() != null) {
                        ingredientesValidos.add(ingrediente);
                    }
                }

                plato.getIngredientes().clear();
                plato.getIngredientes().addAll(ingredientesValidos);

                PeticionCrearPlato peticionCrearPlato = new PeticionCrearPlato(
                        plato.getNombre(),
                        plato.getPrecio(),
                        plato.getImagen(),
                        ingredientesValidos,
                        idRestaurante);

                api.crearPlato("Bearer " + tokenRestaurante, peticionCrearPlato).enqueue(new Callback<Plato>() {
                    @Override
                    public void onResponse(Call<Plato> call, Response<Plato> response) {
                        if(response.isSuccessful() && response.body() != null) {
                            plato.setId(response.body().getId());
                            Log.i("CREAR-PLATO", "se ha creado plato");
                        }
                        else {
                            Log.i("CREAR-PLATO", "No se ha creado el plato " + response.code());
                        }

                        guardarPlatosRegistro(api, tokenRestaurante, platos, idRestaurante, index + 1, completionCallback);
                    }

                    @Override
                    public void onFailure(Call<Plato> call, Throwable t) {
                        Log.i("CREAR-PLATO", "Error al crear plato: " + t.getMessage());
                        guardarPlatosRegistro(api, tokenRestaurante, platos, idRestaurante, index + 1, completionCallback);
                    }
                });
            }
        });
    }

    private void sincronizarIngredientesRegistro(API api, String tokenRestaurante, Plato plato, int index, CompletionCallback completionCallback) {
        if(index >= plato.getIngredientes().size()) {
            completionCallback.onComplete();
            return;
        }

        Ingrediente ingrediente = plato.getIngredientes().get(index);

        if(ingrediente == null || ingrediente.getNombre() == null) {
            plato.getIngredientes().remove(index);
            sincronizarIngredientesRegistro(api, tokenRestaurante, plato, index, completionCallback);
            return;
        }

        api.obtenerIngredientes("Bearer " + tokenRestaurante).enqueue(new Callback<List<Ingrediente>>() {
            @Override
            public void onResponse(Call<List<Ingrediente>> call, Response<List<Ingrediente>> response) {
                if(!response.isSuccessful() || response.body() == null) {
                    sincronizarIngredientesRegistro(api, tokenRestaurante, plato, index + 1, completionCallback);
                    return;
                }

                Ingrediente ingredienteExistente = buscarIngrediente(response.body(), ingrediente);
                if(ingredienteExistente != null) {
                    plato.getIngredientes().set(index, ingredienteExistente);
                    sincronizarIngredientesRegistro(api, tokenRestaurante, plato, index + 1, completionCallback);
                    return;
                }

                api.crearIngrediente("Bearer " + tokenRestaurante, new PeticionCrearIngrediente(
                        ingrediente.getNombre(),
                        ingrediente.isEsAlergeno()
                )).enqueue(new Callback<Ingrediente>() {
                    @Override
                    public void onResponse(Call<Ingrediente> call, Response<Ingrediente> responseIngrediente) {
                        if(responseIngrediente.isSuccessful() && responseIngrediente.body() != null) {
                            plato.getIngredientes().set(index, responseIngrediente.body());
                        }
                        else {
                            Toast.makeText(RestaurantRegister3.this, "code: " + responseIngrediente.code(), Toast.LENGTH_SHORT).show();
                        }

                        sincronizarIngredientesRegistro(api, tokenRestaurante, plato, index + 1, completionCallback);
                    }

                    @Override
                    public void onFailure(Call<Ingrediente> call, Throwable t) {
                        sincronizarIngredientesRegistro(api, tokenRestaurante, plato, index + 1, completionCallback);
                    }
                });
            }

            @Override
            public void onFailure(Call<List<Ingrediente>> call, Throwable t) {
                sincronizarIngredientesRegistro(api, tokenRestaurante, plato, index + 1, completionCallback);
            }
        });
    }

    private Ingrediente buscarIngrediente(List<Ingrediente> ingredientes, Ingrediente ingrediente) {
        if(ingredientes == null || ingrediente == null || ingrediente.getNombre() == null) {
            return null;
        }

        for(Ingrediente ingredienteRegistrado : ingredientes) {
            if(ingredienteRegistrado.getNombre().equals(ingrediente.getNombre())
                    && ingredienteRegistrado.isEsAlergeno() == ingrediente.isEsAlergeno()) {
                return ingredienteRegistrado;
            }
        }

        return null;
    }

    private Plato obtenerPlatoPorCodigo(int codigo) {
        Plato plato = null;
        int i = 0;

        while (i < platos.size() && plato == null) {
            if(platos.get(i).getId() == codigo) plato = platos.get(i);

            i++;
        }

        return plato;
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