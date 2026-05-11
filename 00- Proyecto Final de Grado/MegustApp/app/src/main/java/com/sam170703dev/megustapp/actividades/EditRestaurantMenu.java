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
import com.sam170703dev.megustapp.entidades.Ingrediente;
import com.sam170703dev.megustapp.entidades.Plato;
import com.sam170703dev.megustapp.peticiones_http.post.PeticionCrearIngrediente;
import com.sam170703dev.megustapp.peticiones_http.post.PeticionCrearPlato;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditRestaurantMenu extends AppCompatActivity {

    private int idRestaurante = 0;
    private String nombreDelPlato = "";
    private ArrayList<Plato> platos = new ArrayList<>();
    private ArrayList<Plato> platosEliminados = new ArrayList<>();
    private ListView menu = null;
    private ActivityResultLauncher resultLauncher;
    private EditMenuAdapter adaptador;
    private Plato platoSeleccionado;

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.edit_menu_context_menu, menu);

        final TextView nombrePlato = v.findViewById(R.id.nombre_del_plato);
        nombreDelPlato = nombrePlato.getText().toString();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_edit_restaurant_menu);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.activity_edit_restaurant_menu), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        menu = findViewById(R.id.menu_activity_edit_restaurant_menu);
        final Button botonAplicarCambios = findViewById(R.id.boton_activity_edit_restaurant_menu);
        final TextView textoCancelar = findViewById(R.id.texto_cancelar_activity_edit_restaurant_menu);

        final API api = APIRest.getAPI();
        final SharedPreferences sharedPreferences = getSharedPreferences("Tokens", Context.MODE_PRIVATE);
        final String tokenRestaurante = sharedPreferences.getString("token_restaurante", "");

        final Bundle bundle = getIntent().getExtras();

        if(bundle != null) {
            platos = new ArrayList<>(bundle.getParcelableArrayList("platos"));
            idRestaurante = bundle.getInt("id_restaurante");
        }
        else platos = new ArrayList<>();

        adaptador = new EditMenuAdapter(this, platos);

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
                                        /*new Handler().postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                for(View plato : adaptador.getViews()) {
                                                    registerForContextMenu(plato);
                                                }
                                            }
                                        }, 1000);*/
                                    }
                                }
                            }
                        }
                    }
                }
        );

        menu.setAdapter(adaptador);
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
        /*new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                for(View plato : adaptador.getViews()) {
                    registerForContextMenu(plato);
                }
            }
        }, 1000);*/

        menu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(position == parent.getCount() - 1) {
                    Intent addDishActivity = new Intent(EditRestaurantMenu.this, AddDish.class);
                    resultLauncher.launch(addDishActivity);
                }
            }
        });

        botonAplicarCambios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                guardarCambiosMenu(api, tokenRestaurante, new CompletionCallback() {
                    @Override
                    public void onComplete() {
                        Intent resultado = new Intent();
                        resultado.putExtra("platos", platos);
                        setResult(RESULT_OK, resultado);
                        finish();
                    }
                });
            }
        });

        textoCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_CANCELED);
                finish();
            }
        });
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {

        if(item.getTitle().equals("Eliminar plato")) {

            if(platos.size() == 2) Toast.makeText(this, "Error: No puedes eliminar el unico plato que hay.", Toast.LENGTH_LONG).show();
            else {
                int i = 0;
                boolean encontrado = false;

                while (i < platos.size() && !encontrado) {
                    if(platos.get(i).getNombre().equals(nombreDelPlato)) {
                        platosEliminados.add(platos.get(i));
                        platos.remove(i);
                        encontrado = true;
                    }
                    i++;
                }

                ArrayList<Plato> platosRestantes = new ArrayList<>();

                for(Plato plato : platos) {
                    if(plato.getNombre() != null) platosRestantes.add(plato);
                }

                if(encontrado) {
                    Log.i("PLATOS", "platos: " + platosRestantes);
                    final EditMenuAdapter adaptador = new EditMenuAdapter(this, platosRestantes);
                    menu.setAdapter(adaptador);
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
        else {
            Intent addDishActivity = new Intent(EditRestaurantMenu.this, AddDish.class);
            ArrayList<Plato> plato = new ArrayList<>();
            plato.add(platoSeleccionado);
            addDishActivity.putExtra("plato", plato);
            addDishActivity.putExtra("editar_menu", true);
            resultLauncher.launch(addDishActivity);
        }
        return super.onContextItemSelected(item);
    }


    private interface CompletionCallback {
        void onComplete();
    }

    private void guardarCambiosMenu(API api, String tokenRestaurante, CompletionCallback completionCallback) {
        eliminarPlatosPendientes(api, tokenRestaurante, 0, new CompletionCallback() {
            @Override
            public void onComplete() {
                guardarPlatos(api, tokenRestaurante, 0, completionCallback);
            }
        });
    }

    private void eliminarPlatosPendientes(API api, String tokenRestaurante, int index, CompletionCallback completionCallback) {
        if(index >= platosEliminados.size()) {
            completionCallback.onComplete();
            return;
        }

        Plato plato = platosEliminados.get(index);
        if(plato == null || plato.getId() == 0) {
            eliminarPlatosPendientes(api, tokenRestaurante, index + 1, completionCallback);
            return;
        }

        api.eliminarPlato(plato.getId(), "Bearer " + tokenRestaurante).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                eliminarPlatosPendientes(api, tokenRestaurante, index + 1, completionCallback);
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                eliminarPlatosPendientes(api, tokenRestaurante, index + 1, completionCallback);
            }
        });
    }

    private void guardarPlatos(API api, String tokenRestaurante, int index, CompletionCallback completionCallback) {
        if(index >= platos.size()) {
            completionCallback.onComplete();
            return;
        }

        Plato plato = platos.get(index);
        if(plato == null || plato.getNombre() == null) {
            guardarPlatos(api, tokenRestaurante, index + 1, completionCallback);
            return;
        }

        sincronizarIngredientesPlato(api, tokenRestaurante, plato, 0, new CompletionCallback() {
            @Override
            public void onComplete() {
                guardarPlato(api, tokenRestaurante, plato, new CompletionCallback() {
                    @Override
                    public void onComplete() {
                        guardarPlatos(api, tokenRestaurante, index + 1, completionCallback);
                    }
                });
            }
        });
    }

    private void sincronizarIngredientesPlato(API api, String tokenRestaurante, Plato plato, int index, CompletionCallback completionCallback) {
        if(index >= plato.getIngredientes().size()) {
            completionCallback.onComplete();
            return;
        }

        Ingrediente ingrediente = plato.getIngredientes().get(index);

        if(ingrediente == null) {
            plato.getIngredientes().remove(index);
            sincronizarIngredientesPlato(api, tokenRestaurante, plato, index, completionCallback);
            return;
        }

        if(ingrediente.getNombre() == null) {
            plato.getIngredientes().remove(index);
            sincronizarIngredientesPlato(api, tokenRestaurante, plato, index, completionCallback);
            return;
        }

        api.obtenerIngredientes("Bearer " + tokenRestaurante).enqueue(new Callback<List<Ingrediente>>() {
            @Override
            public void onResponse(Call<List<Ingrediente>> call, Response<List<Ingrediente>> response) {
                if(!response.isSuccessful() || response.body() == null) {
                    sincronizarIngredientesPlato(api, tokenRestaurante, plato, index + 1, completionCallback);
                    return;
                }

                Ingrediente ingredienteExistente = buscarIngrediente(response.body(), ingrediente);
                if(ingredienteExistente != null) {
                    plato.getIngredientes().set(index, ingredienteExistente);
                    sincronizarIngredientesPlato(api, tokenRestaurante, plato, index + 1, completionCallback);
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
                            Toast.makeText(EditRestaurantMenu.this, "code: " + responseIngrediente.code(), Toast.LENGTH_SHORT).show();
                        }

                        sincronizarIngredientesPlato(api, tokenRestaurante, plato, index + 1, completionCallback);
                    }

                    @Override
                    public void onFailure(Call<Ingrediente> call, Throwable t) {
                        sincronizarIngredientesPlato(api, tokenRestaurante, plato, index + 1, completionCallback);
                    }
                });
            }

            @Override
            public void onFailure(Call<List<Ingrediente>> call, Throwable t) {
                sincronizarIngredientesPlato(api, tokenRestaurante, plato, index + 1, completionCallback);
            }
        });
    }

    private void guardarPlato(API api, String tokenRestaurante, Plato plato, CompletionCallback completionCallback) {
        ArrayList<Ingrediente> ingredientesValidos = new ArrayList<>();

        for(Ingrediente ingrediente : plato.getIngredientes()) {
            if(ingrediente != null && ingrediente.getNombre() != null) {
                ingredientesValidos.add(ingrediente);
            }
        }

        plato.getIngredientes().clear();
        plato.getIngredientes().addAll(ingredientesValidos);

        if(plato.getId() != 0) {
            api.actualizarPlato(plato.getId(), "Bearer " + tokenRestaurante, plato).enqueue(new Callback<Plato>() {
                @Override
                public void onResponse(Call<Plato> call, Response<Plato> response) {
                    completionCallback.onComplete();
                }

                @Override
                public void onFailure(Call<Plato> call, Throwable t) {
                    completionCallback.onComplete();
                }
            });
            return;
        }

        plato.setRestauranteId(idRestaurante);
        api.crearPlato("Bearer " + tokenRestaurante, new PeticionCrearPlato(
                plato.getNombre(),
                plato.getPrecio(),
                plato.getImagen(),
                ingredientesValidos,
                idRestaurante
        )).enqueue(new Callback<Plato>() {
            @Override
            public void onResponse(Call<Plato> call, Response<Plato> response) {
                if(response.isSuccessful() && response.body() != null) {
                    plato.setId(response.body().getId());
                }
                completionCallback.onComplete();
            }

            @Override
            public void onFailure(Call<Plato> call, Throwable t) {
                completionCallback.onComplete();
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
            if(ingredientes.get(i).getNombre().equals(ingrediente.getNombre()) && ingredientes.get(i).isEsAlergeno() == ingrediente.isEsAlergeno()) {
                registrado = true;
                if(ingrediente.getId() == 0) ingrediente.setId(ingredientes.get(i).getId());
            }

            i++;
        }

        return registrado;
    }
}