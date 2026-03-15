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

import java.util.ArrayList;

public class IngredientAdapter extends ArrayAdapter<Ingrediente> {
    private ArrayList<Ingrediente> ingredientes;
    private ArrayList<View> views = new ArrayList<>();

    public IngredientAdapter(@NonNull Context context, ArrayList<Ingrediente> ingredientes) {
        super(context, R.layout.ingrediente, ingredientes);
        ingredientes.add(new Ingrediente());
        this.ingredientes = ingredientes;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.ingrediente, parent, false);

        TextView nombreIngrediente = view.findViewById(R.id.nombre_del_ingrediente);
        TextView texto = view.findViewById(R.id.texto_adiccional_ingrediente);

        views.add(view);

        if((ingredientes.size() >= 1 && position < ingredientes.size() - 1)) {
            nombreIngrediente.setText(ingredientes.get(position).getNombre());
        }
        else {
            nombreIngrediente.setText("Añadir ingrediente");
            texto.setText("+");
            views.remove(view);
        }


        return view;
    }

    public ArrayList<View> getViews() {
        return views;
    }
}
