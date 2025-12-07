package com.example.megustapp.adaptadores;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.megustapp.R;
import com.example.megustapp.datos_adaptadores.Restaurant;

import java.util.ArrayList;

public class RestaurantListAdapter extends RecyclerView.Adapter<OpcionViewHolder> implements View.OnClickListener {

    private ArrayList<Restaurant> restaurantes;
    private View.OnClickListener listener;

    public RestaurantListAdapter(ArrayList<Restaurant> restaurantes) {
        this.restaurantes = restaurantes;
    }

    public void setOnClickListener(View.OnClickListener listener) {
        this.listener = listener;
    }

    @Override
    public void onClick(View v) {
        if(listener != null) {
            listener.onClick(v);
        }
    }

    @NonNull
    @Override
    public OpcionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View pagina = LayoutInflater.from(parent.getContext()).inflate(R.layout.opc_restaurant_recview, parent, false);
        pagina.setOnClickListener(this);

        OpcionViewHolder ovh = new OpcionViewHolder(pagina);

        return ovh;
    }

    @Override
    public void onBindViewHolder(@NonNull OpcionViewHolder holder, int position) {
        Restaurant restaurante = restaurantes.get(position);
        holder.bindRestaurante(restaurante);
    }

    @Override
    public int getItemCount() {
        return restaurantes.size();
    }
}

class OpcionViewHolder extends RecyclerView.ViewHolder {

    private ImageView imagen;
    private TextView nombre;
    public OpcionViewHolder(@NonNull View itemView) {
        super(itemView);

        imagen = itemView.findViewById(R.id.imagen_restaurante_opc_restaurant_recview);
        nombre = itemView.findViewById(R.id.nombre_restaurante_opc_restaurant_recview);
    }

    public void bindRestaurante(Restaurant restaurante) {
        imagen.setImageResource(restaurante.getImagen());
        nombre.setText(restaurante.getNombre());
    }
}