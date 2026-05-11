package com.sam170703dev.megustapp.actividades;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
import androidx.loader.content.CursorLoader;

import com.bumptech.glide.Glide;
import com.sam170703dev.megustapp.R;
import com.sam170703dev.megustapp.adaptadores.IngredientAdapter;
import com.sam170703dev.megustapp.api.API;
import com.sam170703dev.megustapp.api.APIRest;
import com.sam170703dev.megustapp.entidades.Ingrediente;
import com.sam170703dev.megustapp.entidades.Plato;

import java.io.File;
import java.util.ArrayList;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddDish extends AppCompatActivity {

    private String urlImagenPlato;
    private String nombreImagen;
    private ArrayList<Ingrediente> ingredientes = new ArrayList<>();
    private Ingrediente ingredienteSeleccionado;
    private ActivityResultLauncher activityResultLauncher;
    private IngredientAdapter adaptador;
    private ListView listadoIngredientes;

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.edit_dish_context_menu, menu);
    }

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

        final Bundle bundle = getIntent().getExtras();

        final EditText nombreDelPlato = findViewById(R.id.nombre_del_plato_add_dish);
        final EditText precioDelPlato = findViewById(R.id.precio_add_dish);
        listadoIngredientes = findViewById(R.id.listado_ingredientes_add_dish);

        final Button botonRegistrarPlato = findViewById(R.id.boton_registrar_plato_add_dish);
        final TextView textoVolverAtras = findViewById(R.id.texto_volver_atras_add_dish);
        final Button botonSubirImagen = findViewById(R.id.boton_subir_imagen_plato_add_dish);
        final ImageView imagenDelPlato = findViewById(R.id.imagen_del_plato_add_dish);

        if(bundle != null) {
            Plato plato = ((ArrayList<Plato>) bundle.getSerializable("plato")).get(0);
            nombreDelPlato.setText(plato.getNombre());
            precioDelPlato.setText(String.valueOf(plato.getPrecio()));
            urlImagenPlato = plato.getImagen();
            Glide.with(this)
                    .load(plato.getImagen())
                    .into(imagenDelPlato);
            imagenDelPlato.setVisibility(View.VISIBLE);
            botonSubirImagen.setVisibility(View.GONE);
            if(bundle.getBoolean("editar_menu", false)) {
                reemplazarIngredientesNulos(plato);
                ingredientes = new ArrayList<>(plato.getIngredientes());
            }
            else ingredientes = new ArrayList<>(plato.getIngredientes().subList(0, plato.getIngredientes().size() - 1));
            nombreImagen = plato.getImagen().split("/")[plato.getImagen().split("/").length - 1];
            botonRegistrarPlato.setText("Editar plato");
        }

        adaptador = new IngredientAdapter(this, ingredientes);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                for(int i = 0; i < adaptador.getViews().size(); i++) {
                    View ingredienteView = adaptador.getViews().get(i);
                    Ingrediente ingrediente = adaptador.getItem(i);

                    ingredienteView.setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View v) {
                            ingredienteSeleccionado = ingrediente;

                            return ingredienteView.showContextMenu();
                        }
                    });

                    registerForContextMenu(ingredienteView);
                }
            }
        }, 1000);

        activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if(result.getResultCode() == Activity.RESULT_OK) {
                            Intent intent = result.getData();
                            if(intent != null) {
                                Bundle datos = intent.getExtras();
                                if(datos != null) {
                                    String nombreIngrediente = datos.getString("nombre_ingrediente");
                                    boolean esAlergeno = datos.getBoolean("es_alergeno");

                                    if(datos.getBoolean("editar_ingrediente")) {
                                        Ingrediente ingrediente = obtenerIngredientePorCodigo(datos.getInt("id_ingrediente"));

                                        if(ingrediente != null) {
                                            int pos = ingredientes.indexOf(ingrediente);
                                            ingredientes.get(pos).setNombre(nombreIngrediente);
                                            ingredientes.get(pos).setEsAlergeno(esAlergeno);
                                        }
                                    }
                                    else {
                                        Ingrediente nuevoIngrediente = new Ingrediente();
                                        nuevoIngrediente.setNombre(nombreIngrediente);
                                        nuevoIngrediente.setEsAlergeno(esAlergeno);
                                        ingredientes.add(ingredientes.size() - 1, nuevoIngrediente);
                                    }

                                    listadoIngredientes.setAdapter(adaptador);
                                    adaptador.getViews().clear();
                                    new Handler().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            for(int i = 0; i < adaptador.getViews().size(); i++) {
                                                View ingredienteView = adaptador.getViews().get(i);
                                                Ingrediente ingrediente = adaptador.getItem(i);

                                                ingredienteView.setOnLongClickListener(new View.OnLongClickListener() {
                                                    @Override
                                                    public boolean onLongClick(View v) {
                                                        ingredienteSeleccionado = ingrediente;

                                                        return ingredienteView.showContextMenu();
                                                    }
                                                });

                                                registerForContextMenu(ingredienteView);
                                            }
                                        }
                                    }, 1000);
                                }
                            }
                        }
                    }
                }
        );

        ActivityResultLauncher activityResultLauncherImagen = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if(result.getResultCode() == Activity.RESULT_OK) {
                            final Intent intent = result.getData();
                            if(intent != null) {
                                Uri imageUri = intent.getData();

                                if(imageUri != null) uploadImage(imageUri, imagenDelPlato, getBaseContext(), botonSubirImagen);
                            }
                        }
                    }
                }
        );

        listadoIngredientes.setAdapter(adaptador);

        listadoIngredientes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(position == parent.getCount() - 1) {
                    Intent addIngredientActivity = new Intent(AddDish.this, AddIngredient.class);
                    activityResultLauncher.launch(addIngredientActivity);
                }
            }
        });
        textoVolverAtras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final API api = APIRest.getAPIIMagenes();

                if(bundle == null && nombreImagen != null) {
                    api.eliminarImagenPlato(nombreImagen).enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            if(response.isSuccessful()) {
                                Toast.makeText(AddDish.this, "Operación cancelada correctamente.", Toast.LENGTH_SHORT).show();
                                setResult(RESULT_CANCELED);
                                finish();
                            }
                            else {
                                Toast.makeText(AddDish.this, "Se ha producido un error al intentar cancelar la operacion", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            Toast.makeText(AddDish.this, "Se ha producido un error al intentar cancelar la operacion", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                else {
                    Toast.makeText(AddDish.this, "Operación cancelada correctamente.", Toast.LENGTH_SHORT).show();
                    setResult(RESULT_CANCELED);
                    finish();
                }
            }
        });

        botonRegistrarPlato.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean completado = true;

                if(nombreDelPlato.getText().toString().isBlank()) {
                    nombreDelPlato.setError("Este campo es obligatorio");
                    completado = false;
                }
                if(precioDelPlato.getText().toString().isBlank()) {
                    precioDelPlato.setError("Este campo es obligatorio");
                    completado = false;
                }

                if(urlImagenPlato == null || urlImagenPlato.isBlank()) {
                    Toast.makeText(AddDish.this, "Debes subir una imagen del plato para continuar.", Toast.LENGTH_SHORT).show();
                    completado = false;
                }

                if(ingredientes.size() == 1) {
                    Toast.makeText(AddDish.this, "El plato debe tener como mínimo un ingrediente.", Toast.LENGTH_SHORT).show();
                    completado = false;
                }

                if(completado) {
                    Intent resultado = new Intent();
                    resultado.putExtra("editar_plato", bundle != null);
                    if(bundle != null) resultado.putExtra("id_plato", ((ArrayList<Plato>) bundle.getSerializable("plato")).get(0).getId());
                    resultado.putExtra("nombre_plato", nombreDelPlato.getText().toString());
                    resultado.putExtra("precio_plato", precioDelPlato.getText().toString());
                    resultado.putExtra("ingredientes_plato", new ArrayList<>(ingredientes));
                    resultado.putExtra("imagen_plato", urlImagenPlato);
                    setResult(RESULT_OK, resultado);
                    finish();
                }
            }
        });

        botonSubirImagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                activityResultLauncherImagen.launch(intent);
            }
        });

        imagenDelPlato.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                activityResultLauncherImagen.launch(intent);
            }
        });
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {

        if(item.getItemId() == R.id.opc_editar_ingrediente) {
            Intent addIngredientActivity = new Intent(AddDish.this, AddIngredient.class);
            ArrayList<Ingrediente> ingrediente = new ArrayList<>();
            ingrediente.add(ingredienteSeleccionado);
            addIngredientActivity.putExtra("ingrediente", ingrediente);
            activityResultLauncher.launch(addIngredientActivity);
        }
        else {
            ArrayList<Ingrediente> temp = new ArrayList<>();
            ingredientes.forEach(ingrediente -> {
                if(!ingrediente.equals(ingredienteSeleccionado) && ingrediente.getNombre() != null) temp.add(ingrediente);
            });
            ingredientes = new ArrayList<>(temp);
            adaptador = new IngredientAdapter(this, ingredientes);
            listadoIngredientes.setAdapter(adaptador);
            adaptador.getViews().clear();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    for(int i = 0; i < adaptador.getViews().size(); i++) {
                        View ingredienteView = adaptador.getViews().get(i);
                        Ingrediente ingrediente = adaptador.getItem(i);

                        ingredienteView.setOnLongClickListener(new View.OnLongClickListener() {
                            @Override
                            public boolean onLongClick(View v) {
                                ingredienteSeleccionado = ingrediente;

                                return ingredienteView.showContextMenu();
                            }
                        });

                        registerForContextMenu(ingredienteView);
                    }
                }
            }, 1000);
            Toast.makeText(this, "Ingrediente eliminado satisfactoriamente", Toast.LENGTH_SHORT).show();
        }

        return super.onContextItemSelected(item);
    }

    private void uploadImage(Uri imageUri, ImageView fotoPerfil, Context context, Button boton) {
        File file = new File(getRealPathFromURI(imageUri, context));
        RequestBody reqFile = RequestBody.create(MediaType.parse("image/*"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("imagen", file.getName(), reqFile);
        RequestBody description = RequestBody.create(MediaType.parse("text/plain"), "Descripción");

        API apiImagenes = APIRest.getAPIIMagenes();

        apiImagenes.subirImagenPlato(body, description).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()) {
                    Glide.with(context)
                            .load(APIRest.URL_API_IMAGENES + "/api/imagenes/dishes/" + file.getName())
                            .into(fotoPerfil);

                    fotoPerfil.setVisibility(View.VISIBLE);
                    boton.setVisibility(View.GONE);
                    urlImagenPlato = APIRest.URL_API_IMAGENES + "/api/imagenes/dishes/" + file.getName();
                    nombreImagen = file.getName();
                }
                else Toast.makeText(context, "Se ha producido un error al intentar subir la imagen del plato.", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(context, "Se ha producido un error al intentar subir la imagen del plato", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private String getRealPathFromURI(Uri uri, Context context) {
        String[] proj = {MediaStore.Images.Media.DATA};
        CursorLoader loader = new CursorLoader(context, uri, proj, null, null, null);
        Cursor cursor = loader.loadInBackground();
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String result = cursor.getString(column_index);
        cursor.close();
        return result;
    }
    private Ingrediente obtenerIngredientePorCodigo(int codigo) {
        Ingrediente ingrediente = null;
        int i = 0;

        while (i < ingredientes.size() && ingrediente == null) {
            if(ingredientes.get(i).getId() == codigo) ingrediente = ingredientes.get(i);

            i++;
        }

        return ingrediente;
    }

    private boolean platoContieneIngredientesNulos(Plato plato) {
        boolean encontrado = false;
        int i = 0;

        while (i < plato.getIngredientes().size() && !encontrado) {
            if(plato.getIngredientes().get(i).getNombre() == null) encontrado = true;
        }

        return encontrado;
    }

    private void reemplazarIngredientesNulos(Plato plato) {
        ArrayList<Ingrediente> ingredientes = new ArrayList<>();
        int i = 0;

        while (i < plato.getIngredientes().size()) {
            if(plato.getIngredientes().get(i) != null && plato.getIngredientes().get(i).getNombre() != null) ingredientes.add(plato.getIngredientes().get(i));

            i++;
        }

        plato.getIngredientes().clear();

        ingredientes.forEach(ingrediente -> plato.addIngrediente(ingrediente));
    }
}