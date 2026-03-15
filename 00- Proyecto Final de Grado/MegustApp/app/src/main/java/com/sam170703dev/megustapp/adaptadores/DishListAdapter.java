package com.sam170703dev.megustapp.adaptadores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.sam170703dev.megustapp.R;
import com.sam170703dev.megustapp.entidades.Ingrediente;
import com.sam170703dev.megustapp.entidades.Plato;

import java.util.ArrayList;

public class DishListAdapter extends ArrayAdapter<Plato> {
    private ArrayList<Plato> platos;

    public DishListAdapter(@NonNull Context context, ArrayList<Plato> platos) {
        super(context, R.layout.dish_list_option, platos);
        this.platos = platos;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View elemento = LayoutInflater.from(getContext()).inflate(R.layout.dish_list_option, parent, false);

        TextView plato = elemento.findViewById(R.id.plato_dish_list_option);
        plato.setText(platos.get(position).getNombre());

        TextView precio = elemento.findViewById(R.id.precio_dish_list_option);
        precio.setText(platos.get(position).getPrecio() + " €");

        TextView ingredientes = elemento.findViewById(R.id.ingredientes_dish_list_option);

        String textoIngredientes = "";

        for(Ingrediente ingrediente : platos.get(position).getIngredientes()) {
            textoIngredientes+= ingrediente.getNombre() + ", ";
        }

        textoIngredientes = textoIngredientes.substring(0, textoIngredientes.length() - 2);
        ingredientes.setText(textoIngredientes);

        return elemento;
    }
}
