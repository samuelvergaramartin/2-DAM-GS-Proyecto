package com.sam170703dev.megustapp.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.sam170703dev.megustapp.R;
import com.sam170703dev.megustapp.actividades.AddDish;
import com.sam170703dev.megustapp.actividades.EditRestaurantMenu;
import com.sam170703dev.megustapp.adaptadores.DishListAdapter;
import com.sam170703dev.megustapp.api.API;
import com.sam170703dev.megustapp.api.APIRest;
import com.sam170703dev.megustapp.entidades.Ingrediente;
import com.sam170703dev.megustapp.entidades.Plato;
import com.sam170703dev.megustapp.entidades.Restaurante;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class FragmentRestaurantMenu extends Fragment {

    private final int idRestaurante;
    private ArrayList<Plato> platos;
    private DishListAdapter adaptador;

    public FragmentRestaurantMenu(int idRestaurante) {
        this.idRestaurante = idRestaurante;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View fragment = inflater.inflate(R.layout.fragment_restaurant_menu, container, false);

        final ListView listadoPlatos = fragment.findViewById(R.id.listado_platos_fragment_restaurant_menu);
        final ImageView imagenEditar = fragment.findViewById(R.id.imagen_editar_fragment_restaurant_menu);

        final SharedPreferences sharedPreferences = getContext().getSharedPreferences("Tokens", Context.MODE_PRIVATE);
        final String tokenRestaurante = sharedPreferences.getString("token_restaurante", "");

        final API api = APIRest.getAPI();

        ActivityResultLauncher activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                            Bundle bundle = result.getData().getExtras();
                            if (bundle != null) {
                                platos = new ArrayList<>(bundle.getParcelableArrayList("platos"));

                                for (int i = platos.size() - 1; i >= 0; i--) {
                                    Plato plato = platos.get(i);

                                    if (plato == null || plato.getNombre() == null) {
                                        platos.remove(i);
                                    } else {
                                        for (int j = plato.getIngredientes().size() - 1; j >= 0; j--) {
                                            Ingrediente ingrediente = plato.getIngredientes().get(j);
                                            if (ingrediente == null || ingrediente.getNombre() == null) {
                                                plato.getIngredientes().remove(j);
                                            }
                                        }
                                    }
                                }

                                adaptador = new DishListAdapter(getContext(), platos);
                                listadoPlatos.setAdapter(adaptador);
                            }
                        }
                    }
                }
        );

        api.getRestauranteById("Bearer " + tokenRestaurante, idRestaurante).enqueue(new Callback<Restaurante>() {
            @Override
            public void onResponse(Call<Restaurante> call, Response<Restaurante> response) {
                if(response.isSuccessful()) {
                    platos = new ArrayList<>(response.body().getPlatos());
                    adaptador = new DishListAdapter(getContext(), platos);

                    listadoPlatos.setAdapter(adaptador);

                    imagenEditar.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent activity = new Intent(getContext(), EditRestaurantMenu.class);
                            activity.putExtra("id_restaurante", idRestaurante);
                            activity.putExtra("platos", platos);
                            activityResultLauncher.launch(activity);
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<Restaurante> call, Throwable t) {

            }
        });

        return fragment;
    }
}