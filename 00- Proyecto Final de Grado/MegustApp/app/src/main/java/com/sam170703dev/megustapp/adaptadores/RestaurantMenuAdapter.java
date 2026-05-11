package com.sam170703dev.megustapp.adaptadores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sam170703dev.megustapp.R;
import com.sam170703dev.megustapp.api.APIRest;
import com.sam170703dev.megustapp.entidades.Ingrediente;
import com.sam170703dev.megustapp.entidades.Plato;

import java.util.ArrayList;

public class RestaurantMenuAdapter extends RecyclerView.Adapter<RestaurantMenuViewHolder> implements View.OnClickListener{
    private ArrayList<Plato> platos;
    private View.OnClickListener listener;

    public RestaurantMenuAdapter(ArrayList<Plato> platos) {
        this.platos = platos;
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
    public RestaurantMenuViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View pagina = LayoutInflater.from(parent.getContext()).inflate(R.layout.opc_menu_restaurant, parent, false);
        pagina.setOnClickListener(this);

        RestaurantMenuViewHolder rmvh = new RestaurantMenuViewHolder(pagina);

        return rmvh;
    }

    @Override
    public void onBindViewHolder(@NonNull RestaurantMenuViewHolder holder, int position) {
        Plato plato = platos.get(position);
        holder.bindPlato(plato, holder.itemView.getContext());
    }

    @Override
    public int getItemCount() {
        return platos.size();
    }
}

class RestaurantMenuViewHolder extends RecyclerView.ViewHolder {

    private ImageView imagenPlato;
    private TextView nombrePlato;
    private TextView precioPlato;
    private TextView ingredientesPlato;
    public RestaurantMenuViewHolder(@NonNull View itemView) {
        super(itemView);

        imagenPlato = itemView.findViewById(R.id.imagen_plato_opc_menu_restaurant);
        nombrePlato = itemView.findViewById(R.id.nombre_plato_opc_menu_restaurant);
        precioPlato = itemView.findViewById(R.id.precio_plato_opc_menu_restaurant);
        ingredientesPlato = itemView.findViewById(R.id.ingredientes_plato_opc_menu_restaurant);
    }

    public void bindPlato(Plato plato, Context context) {
        String ingredientes = "";
        APIRest.cargarImagen(context, plato.getImagen(), imagenPlato);
        nombrePlato.setText(plato.getNombre());
        precioPlato.setText(String.valueOf(plato.getPrecio()) + " €");

        for(Ingrediente ingrediente: plato.getIngredientes()) {
            ingredientes+= ingrediente.getNombre() + ", ";
        }

        ingredientes = ingredientes.substring(0, ingredientes.length() - 2);

        ingredientesPlato.setText(ingredientes);
    }
}
