package com.sam170703dev.megustapp.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.loader.content.CursorLoader;

import com.bumptech.glide.Glide;
import com.sam170703dev.megustapp.R;
import com.sam170703dev.megustapp.api.API;
import com.sam170703dev.megustapp.api.APIRest;
import com.sam170703dev.megustapp.entidades.Cliente;
import com.sam170703dev.megustapp.entidades.Restaurante;
import com.sam170703dev.megustapp.entidades.Usuario;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RestaurantSettingsFragment extends Fragment {
    private int idRestaurante;
    private int idUsuario;
    private Restaurante restaurante;

    public RestaurantSettingsFragment(int idRestaurante, int idUsuario, Toolbar toolbar) {
        this.idRestaurante = idRestaurante;
        this.idUsuario = idUsuario;
        restaurante = null;
        toolbar.setTitle("Ajustes");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_restaurant_settings, container, false);

        final ImageView fotoRestaurante = view.findViewById(R.id.foto_restaurante_fragment_restaurant_settings);
        final EditText usuario = view.findViewById(R.id.usuario_fragment_restaurant_settings);
        final EditText correo = view.findViewById(R.id.correo_fragment_restaurant_settings);
        final EditText clave = view.findViewById(R.id.clave_fragment_restaurant_settings);
        final EditText telefono = view.findViewById(R.id.telefono_fragment_restaurant_settings);
        final EditText ciudad = view.findViewById(R.id.ciudad_fragment_restaurant_settings);
        final EditText calle = view.findViewById(R.id.calle_fragment_restaurant_settings);
        final Button botonGuardarCambios = view.findViewById(R.id.boton_guardar_cambios_fragment_restaurant_settings);
        final SharedPreferences sharedPreferences = getActivity().getSharedPreferences("Tokens", Context.MODE_PRIVATE);
        final API api = APIRest.getAPI();
        final String tokenRestaurante = sharedPreferences.getString("token_restaurante", "");

        ActivityResultLauncher activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if(result.getResultCode() == Activity.RESULT_OK) {
                            final Intent intent = result.getData();
                            if(intent != null) {
                                Uri imageUri = intent.getData();

                                if(imageUri != null) uploadImage(imageUri, fotoRestaurante, tokenRestaurante);
                            }
                        }
                    }
                }
        );

        api.getRestauranteById("Bearer " + tokenRestaurante, idRestaurante).enqueue(new Callback<Restaurante>() {
            @Override
            public void onResponse(Call<Restaurante> call, Response<Restaurante> response) {
                if(response.isSuccessful()) {
                    restaurante = response.body();
                    usuario.setText(response.body().getNombre());
                    correo.setText(response.body().getEmail());
                    telefono.setText(response.body().getTelefono());
                    ciudad.setText(response.body().getCiudad());
                    calle.setText(response.body().getCalle());

                    Glide.with(getContext())
                            .load(response.body().getFotoPerfil())
                            .into(fotoRestaurante);
                }
                else Toast.makeText(getContext(), "Error al cargar los datos del restaurante " + idRestaurante, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<Restaurante> call, Throwable t) {
                Toast.makeText(getContext(), "Error al cargar los datos del restaurante", Toast.LENGTH_SHORT).show();
            }
        });

        fotoRestaurante.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                activityResultLauncher.launch(intent);
            }
        });

        botonGuardarCambios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                restaurante.setNombre(usuario.getText().toString());
                restaurante.setEmail(correo.getText().toString());
                restaurante.setTelefono(telefono.getText().toString());
                restaurante.setCiudad(ciudad.getText().toString());
                restaurante.setCalle(calle.getText().toString());
                if(!clave.getText().toString().isBlank()) restaurante.setClave(clave.getText().toString());

                api.actualizarRestaurante(idRestaurante, "Bearer " + tokenRestaurante, restaurante).enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if(response.isSuccessful()) {
                            Usuario cuentaUsuario = new Usuario();
                            cuentaUsuario.setId(idRestaurante);
                            cuentaUsuario.setEmail(restaurante.getEmail());
                            cuentaUsuario.setClave(restaurante.getClave(), false);

                            api.actualizarUsuario(idUsuario, "Bearer " + tokenRestaurante, cuentaUsuario).enqueue(new Callback<ResponseBody>() {
                                @Override
                                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response2) {
                                    if(response2.isSuccessful()) Toast.makeText(getContext(), "Cambios guardados satisfactoriamente", Toast.LENGTH_SHORT).show();
                                    else Toast.makeText(getContext(), "Se ha producido un error al guardar los cambios.", Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onFailure(Call<ResponseBody> call, Throwable t) {
                                    Toast.makeText(getContext(), "Se ha producido un error al guardar los cambios", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                        else Toast.makeText(getContext(), "Se ha producido un error al guardar los cambios.", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Toast.makeText(getContext(), "Se ha producido un error al guardar los cambios", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        return view;
    }

    private void uploadImage(Uri imageUri, ImageView fotoPerfil, String token) {
        File file = new File(getRealPathFromURI(imageUri));
        RequestBody reqFile = RequestBody.create(MediaType.parse("image/*"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("imagen", idRestaurante + "-" +  file.getName(), reqFile);

        API apiImagenes = APIRest.getAPIIMagenes();
        API apiRest = APIRest.getAPI();

        apiImagenes.subirImagenRestaurante(body).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()) {
                    Glide.with(getContext())
                            .load(APIRest.URL_API_IMAGENES + "/api/imagenes/restaurants/" + idRestaurante + "-" +  file.getName())
                            .into(fotoPerfil);

                    restaurante.setFotoPerfil(APIRest.URL_API_IMAGENES + "/api/imagenes/restaurants/" + idRestaurante + "-" +  file.getName());
                    apiRest.actualizarRestaurante(restaurante.getId(), "Bearer " + token, restaurante).enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response2) {
                            if(response2.isSuccessful()) Toast.makeText(getContext(), "Foto del restaurante actualizada correctamente", Toast.LENGTH_SHORT).show();
                            else {
                                Toast.makeText(getContext(), "Se ha producido un error al intentar actualizar la foto del restaurante", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            Toast.makeText(getContext(), "Se ha producido un error al intentar actualizar la foto del restaurante", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                else Toast.makeText(getContext(), "Se ha producido un error al intentar actualizar la foto del restaurante: " + response.code(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getContext(), "Se ha producido un error al intentar actualizar la foto del restaurante", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private String getRealPathFromURI(Uri uri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        CursorLoader loader = new CursorLoader(getContext(), uri, proj, null, null, null);
        Cursor cursor = loader.loadInBackground();
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String result = cursor.getString(column_index);
        cursor.close();
        return result;
    }
}
