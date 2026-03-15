package com.sam170703dev.megustapp.actividades;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
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
import com.sam170703dev.megustapp.peticiones_http.post.PeticionEliminarImagenPlato;

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

        final EditText nombreDelPlato = findViewById(R.id.nombre_del_plato_add_dish);
        final EditText precioDelPlato = findViewById(R.id.precio_add_dish);
        final ListView listadoIngredientes = findViewById(R.id.listado_ingredientes_add_dish);
        final ArrayList<Ingrediente> ingredientes = new ArrayList<>();
        final IngredientAdapter adaptador = new IngredientAdapter(this, ingredientes);
        final Button botonRegistrarPlato = findViewById(R.id.boton_registrar_plato_add_dish);
        final TextView textoVolverAtras = findViewById(R.id.texto_volver_atras_add_dish);
        final Button botonSubirImagen = findViewById(R.id.boton_subir_imagen_plato_add_dish);
        final ImageView imagenDelPlato = findViewById(R.id.imagen_del_plato_add_dish);

        ActivityResultLauncher activityResultLauncher = registerForActivityResult(
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
                                    Ingrediente nuevoIngrediente = new Ingrediente();
                                    nuevoIngrediente.setNombre(nombreIngrediente);
                                    nuevoIngrediente.setEsAlergeno(esAlergeno);
                                    ingredientes.add(ingredientes.size() - 1, nuevoIngrediente);
                                    listadoIngredientes.setAdapter(adaptador);
                                    adaptador.getViews().clear();

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
                else Toast.makeText(context, "Se ha producido un error al intentar subir la imagen del plato", Toast.LENGTH_SHORT).show();
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
}