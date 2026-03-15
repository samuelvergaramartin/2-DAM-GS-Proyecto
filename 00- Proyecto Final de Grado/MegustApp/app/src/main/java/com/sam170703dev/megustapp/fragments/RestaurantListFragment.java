package com.sam170703dev.megustapp.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.sam170703dev.megustapp.R;
import com.sam170703dev.megustapp.actividades.MainActivityClient;
import com.sam170703dev.megustapp.actividades.RestaurantInfo;
import com.sam170703dev.megustapp.adaptadores.RestaurantListAdapter;
import com.sam170703dev.megustapp.api.API;
import com.sam170703dev.megustapp.api.APIRest;
import com.sam170703dev.megustapp.datos_adaptadores.Restaurant;
import com.sam170703dev.megustapp.entidades.Restaurante;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RestaurantListFragment extends Fragment {

    public RestaurantListFragment(Toolbar toolbar) {
        toolbar.setTitle("Restaurantes");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_restaurant_list, container, false);

        final RecyclerView recyclerView = view.findViewById(R.id.listado_restaurantes_fragment_restaurant_list);

        recyclerView.setHasFixedSize(true);

        final ArrayList<Restaurant> restaurantes = new ArrayList<>();
        final ArrayList<Restaurante> datosRestaurantes = new ArrayList<>(); // Arreglo temporal para poder mandar todos los datos
        final SharedPreferences sharedPreferences = getActivity().getSharedPreferences("Tokens", Context.MODE_PRIVATE);
        final API api = APIRest.getAPI();
        final String tokenCliente = sharedPreferences.getString("token_cliente", "");

        api.getRestaurantes("Bearer " + tokenCliente).enqueue(new Callback<List<Restaurante>>() {
            @Override
            public void onResponse(Call<List<Restaurante>> call, Response<List<Restaurante>> response) {
                if(response.isSuccessful()) {
                    for(Restaurante restaurante : response.body()) {
                        restaurantes.add(new Restaurant(restaurante.getFotoPerfil(), restaurante.getNombre()));
                        datosRestaurantes.add(restaurante);

                        final RestaurantListAdapter adaptador = new RestaurantListAdapter(restaurantes, getContext());

                        adaptador.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent restaurantInfoActivity = new Intent(getContext(), RestaurantInfo.class);
                                restaurantInfoActivity.putExtra("nombre_restaurante", restaurantes.get(recyclerView.getChildAdapterPosition(v)).getNombre());
                                restaurantInfoActivity.putExtra("imagen_restaurante", restaurantes.get(recyclerView.getChildAdapterPosition(v)).getImagen());
                                restaurantInfoActivity.putExtra("ciudad_restaurante", datosRestaurantes.get(recyclerView.getChildAdapterPosition(v)).getCiudad());
                                restaurantInfoActivity.putExtra("calle_restaurante", datosRestaurantes.get(recyclerView.getChildAdapterPosition(v)).getCalle());
                                restaurantInfoActivity.putExtra("telefono_restaurante", datosRestaurantes.get(recyclerView.getChildAdapterPosition(v)).getTelefono());
                                restaurantInfoActivity.putExtra("platos_restaurante", new ArrayList<>(datosRestaurantes.get(recyclerView.getChildAdapterPosition(v)).getPlatos()));
                                startActivity(restaurantInfoActivity);
                            }
                        });

                        recyclerView.setAdapter(adaptador);
                        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Restaurante>> call, Throwable t) {

            }
        });

        return view;
    }
}