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
import com.sam170703dev.megustapp.entidades.Plato;

import java.util.ArrayList;

public class EditMenuAdapter extends ArrayAdapter<Plato> {
    private ArrayList<Plato> platos;
    private ArrayList<View> views = new ArrayList<>();

    public EditMenuAdapter(@NonNull Context context, ArrayList<Plato> platos) {
        super(context, R.layout.plato, platos);
        platos.add(new Plato());
        this.platos = platos;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater = LayoutInflater.from(getContext());
        View elemento = layoutInflater.inflate(R.layout.plato, parent, false);

        TextView nombre = elemento.findViewById(R.id.nombre_del_plato);
        TextView precio = elemento.findViewById(R.id.precio_del_plato);

        views.add(elemento);

        if((platos.size() >= 1 && position < platos.size() - 1)) {
            nombre.setText(platos.get(position).getNombre());
            precio.setText(platos.get(position).getPrecio() + " €");
        }
        else {
            nombre.setText("Añadir plato");
            precio.setText("+");
            views.remove(elemento);
        }

        return elemento;
    }

    public ArrayList<View> getViews() {
        return views;
    }
}
