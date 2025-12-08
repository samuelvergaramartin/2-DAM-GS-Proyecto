package com.sam170703dev.megustapp.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.sam170703dev.megustapp.R;
import com.sam170703dev.megustapp.adaptadores.DishListAdapter;
import com.sam170703dev.megustapp.datos_adaptadores.Plato;

import java.util.ArrayList;


public class FragmentRestaurantMenu extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View fragment = inflater.inflate(R.layout.fragment_restaurant_menu, container, false);

        final ListView listadoPlatos = fragment.findViewById(R.id.listado_platos_fragment_restaurant_menu);
        final ImageView imagenEditar = fragment.findViewById(R.id.imagen_editar_fragment_restaurant_menu);

        // Creamos platos asi ya que por el momento no vamos a recoger la información de ningún sitio
        final ArrayList<Plato> platos = new ArrayList<>();
        platos.add(new Plato("Carne a la brasa", 20, "Chuleta de cerdo, sal, patatas a lo pobre y limón."));
        platos.add(new Plato("Espetos de sardinas", 4, "8 sardinas, sal y limón"));
        platos.add(new Plato("Pizza hawaiana", 10, "Pizza, piña, tomate, queso y jamón york"));
        platos.add(new Plato("Ensalada", 12, "Lechuga, tomate, cebolla, maíz, aceite de oliva, y vinagre"));

        final DishListAdapter adaptador = new DishListAdapter(getContext(), platos);

        listadoPlatos.setAdapter(adaptador);

        imagenEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Próximamente...", Toast.LENGTH_SHORT).show();
            }
        });

        return fragment;
    }
}