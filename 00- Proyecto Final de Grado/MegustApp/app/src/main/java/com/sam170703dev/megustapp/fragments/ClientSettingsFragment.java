package com.sam170703dev.megustapp.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
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
import com.sam170703dev.megustapp.entidades.Usuario;

import java.io.File;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ClientSettingsFragment extends Fragment {

    private int idCliente;
    private Cliente cliente;
    public ClientSettingsFragment(int idCliente, Toolbar toolbar) {
        this.idCliente = idCliente;
        cliente = null;
        toolbar.setTitle("Ajustes");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_client_settings, container, false);

        final ImageView fotoPerfil = view.findViewById(R.id.foto_perfil_fragment_client_settings);
        final EditText usuario = view.findViewById(R.id.usuario_fragment_client_settings);
        final EditText correo = view.findViewById(R.id.correo_fragment_client_settings);
        final EditText clave = view.findViewById(R.id.clave_fragment_client_settings);
        final EditText telefono = view.findViewById(R.id.telefono_fragment_client_settings);
        final EditText ciudad = view.findViewById(R.id.ciudad_fragment_client_settings);
        final Button botonGuardarCambios = view.findViewById(R.id.boton_guardar_cambios_fragment_client_settings);
        final SharedPreferences sharedPreferences = getActivity().getSharedPreferences("Tokens", Context.MODE_PRIVATE);
        final API api = APIRest.getAPI();
        final String tokenCliente = sharedPreferences.getString("token_cliente", "");

        ActivityResultLauncher activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if(result.getResultCode() == Activity.RESULT_OK) {
                            final Intent intent = result.getData();
                            if(intent != null) {
                                Uri imageUri = intent.getData();

                                if(imageUri != null) uploadImage(imageUri, fotoPerfil, tokenCliente);
                            }
                        }
                    }
                }
        );

        api.getClienteById("Bearer " + tokenCliente, idCliente).enqueue(new Callback<Cliente>() {
            @Override
            public void onResponse(Call<Cliente> call, Response<Cliente> response) {
                if(response.isSuccessful()) {
                    cliente = response.body();
                    usuario.setText(response.body().getNombre());
                    correo.setText(response.body().getEmail());
                    telefono.setText(response.body().getTelefono());
                    ciudad.setText(response.body().getCiudad());

                    Glide.with(getContext())
                    .load(response.body().getFotoPerfil())
                    .into(fotoPerfil);
                }
                else Toast.makeText(getContext(), "Error al cargar los datos del cliente", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<Cliente> call, Throwable t) {
                Toast.makeText(getContext(), "Error al cargar los datos del cliente", Toast.LENGTH_SHORT).show();
            }
        });

        fotoPerfil.setOnClickListener(new View.OnClickListener() {
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
                cliente.setNombre(usuario.getText().toString());
                cliente.setEmail(correo.getText().toString());
                cliente.setTelefono(telefono.getText().toString());
                cliente.setCiudad(ciudad.getText().toString());
                if(!clave.getText().toString().isBlank()) cliente.setClave(clave.getText().toString());

                api.actualizarCliente(idCliente, "Bearer " + tokenCliente, cliente).enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if(response.isSuccessful()) {
                            Usuario cuentaUsuario = new Usuario();
                            cuentaUsuario.setId(idCliente);
                            cuentaUsuario.setEmail(cliente.getEmail());
                            cuentaUsuario.setClave(cliente.getClave(), false);

                            api.actualizarUsuario(idCliente, "Bearer " + tokenCliente, cuentaUsuario).enqueue(new Callback<ResponseBody>() {
                                @Override
                                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response2) {
                                    if(response2.isSuccessful()) Toast.makeText(getContext(), "Cambios guardados satisfactoriamente", Toast.LENGTH_SHORT).show();
                                    else Toast.makeText(getContext(), "Se ha producido un error al guardar los cambios", Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onFailure(Call<ResponseBody> call, Throwable t) {
                                    Toast.makeText(getContext(), "Se ha producido un error al guardar los cambios", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                        else Toast.makeText(getContext(), "Se ha producido un error al guardar los cambios", Toast.LENGTH_SHORT).show();
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
        MultipartBody.Part body = MultipartBody.Part.createFormData("imagen", idCliente + "-" +  file.getName(), reqFile);

        API apiImagenes = APIRest.getAPIIMagenes();
        API apiRest = APIRest.getAPI();

        apiImagenes.subirImagenCliente(body).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()) {
                    Glide.with(getContext())
                        .load(APIRest.URL_API_IMAGENES + "/api/imagenes/clients/" + idCliente + "-" +  file.getName())
                        .into(fotoPerfil);

                    cliente.setFotoPerfil(APIRest.URL_API_IMAGENES + "/api/imagenes/clients/" + idCliente + "-" +  file.getName());
                    apiRest.actualizarCliente(cliente.getId(), "Bearer " + token, cliente).enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response2) {
                            if(response2.isSuccessful()) Toast.makeText(getContext(), "Foto de perfil actualizada correctamente", Toast.LENGTH_SHORT).show();
                            else {
                                Toast.makeText(getContext(), "Se ha producido un error al intentar actualizar la foto de perfil", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            Toast.makeText(getContext(), "Se ha producido un error al intentar actualizar la foto de perfil", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                else Toast.makeText(getContext(), "Se ha producido un error al intentar actualizar la foto de perfil AQUI: " + response.code(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getContext(), "Se ha producido un error al intentar actualizar la foto de perfil", Toast.LENGTH_SHORT).show();
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