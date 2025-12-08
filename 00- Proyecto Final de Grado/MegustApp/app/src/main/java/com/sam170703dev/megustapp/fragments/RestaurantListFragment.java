package com.sam170703dev.megustapp.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.sam170703dev.megustapp.R;
import com.sam170703dev.megustapp.actividades.MainActivityClient;
import com.sam170703dev.megustapp.actividades.RestaurantInfo;
import com.sam170703dev.megustapp.adaptadores.RestaurantListAdapter;
import com.sam170703dev.megustapp.datos_adaptadores.Restaurant;

import java.util.ArrayList;

public class RestaurantListFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_restaurant_list, container, false);

        final RecyclerView recyclerView = view.findViewById(R.id.listado_restaurantes_fragment_restaurant_list);

        recyclerView.setHasFixedSize(true);

        final ArrayList<Restaurant> restaurantes = new ArrayList<>();

        //Restaurantes de ejemplo, ya que, aún no lo conectaré a la base de datos
        restaurantes.add(new Restaurant(R.drawable.logo, "Restaurante 1"));
        restaurantes.add(new Restaurant(R.drawable.logo, "Restaurante 2"));
        restaurantes.add(new Restaurant(R.drawable.logo, "Restaurante 3"));
        restaurantes.add(new Restaurant(R.drawable.logo, "Restaurante 4"));
        restaurantes.add(new Restaurant(R.drawable.logo, "Restaurante 5"));
        restaurantes.add(new Restaurant(R.drawable.logo, "Restaurante 6"));
        restaurantes.add(new Restaurant(R.drawable.logo, "Restaurante 7"));
        restaurantes.add(new Restaurant(R.drawable.logo, "Restaurante 8"));
        restaurantes.add(new Restaurant(R.drawable.logo, "Restaurante 9"));
        restaurantes.add(new Restaurant(R.drawable.logo, "Restaurante 10"));

        final RestaurantListAdapter adaptador = new RestaurantListAdapter(restaurantes);

        adaptador.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent restaurantInfoActivity = new Intent(getContext(), RestaurantInfo.class);
                restaurantInfoActivity.putExtra("nombre_restaurante", restaurantes.get(recyclerView.getChildAdapterPosition(v)).getNombre());
                startActivity(restaurantInfoActivity);
            }
        });

        recyclerView.setAdapter(adaptador);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

        return view;
    }
}