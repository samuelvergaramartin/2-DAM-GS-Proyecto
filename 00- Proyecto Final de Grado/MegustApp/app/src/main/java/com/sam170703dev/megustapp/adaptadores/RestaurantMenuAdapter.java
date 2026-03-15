package com.sam170703dev.megustapp.adaptadores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.sam170703dev.megustapp.R;
import com.sam170703dev.megustapp.api.APIRest;
import com.sam170703dev.megustapp.entidades.Ingrediente;
import com.sam170703dev.megustapp.entidades.Plato;

import java.util.ArrayList;

public class RestaurantMenuAdapter extends ArrayAdapter<Plato> {

    private ArrayList<Plato> platos;
    public RestaurantMenuAdapter(@NonNull Context context, ArrayList<Plato> platos) {
        super(context, R.layout.opc_menu_restaurant, platos);
        this.platos = platos;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View elemento = LayoutInflater.from(getContext()).inflate(R.layout.opc_menu_restaurant, parent, false);
        String ingredientes = "";

        ImageView imagenPlato = elemento.findViewById(R.id.imagen_plato_opc_menu_restaurant);
        APIRest.cargarImagen(getContext(), platos.get(position).getImagen(), imagenPlato);

        TextView nombrePlato = elemento.findViewById(R.id.nombre_plato_opc_menu_restaurant);
        nombrePlato.setText(platos.get(position).getNombre());

        TextView precioPlato = elemento.findViewById(R.id.precio_plato_opc_menu_restaurant);
        precioPlato.setText(String.valueOf(platos.get(position).getPrecio()) + " €");

        TextView ingredientesPlato = elemento.findViewById(R.id.ingredientes_plato_opc_menu_restaurant);

        for(Ingrediente ingrediente: platos.get(position).getIngredientes()) {
            ingredientes+= ingrediente.getNombre() + ", ";
        }

        ingredientes = ingredientes.substring(0, ingredientes.length() - 2);

        ingredientesPlato.setText(ingredientes);

        return elemento;
    }
}
