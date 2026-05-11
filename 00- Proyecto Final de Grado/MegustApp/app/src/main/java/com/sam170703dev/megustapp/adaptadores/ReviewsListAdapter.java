package com.sam170703dev.megustapp.adaptadores;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sam170703dev.megustapp.R;
import com.sam170703dev.megustapp.entidades.Valoracion;

import java.util.ArrayList;

public class ReviewsListAdapter extends RecyclerView.Adapter<ReviewsListViewHolder> implements View.OnClickListener {
    private ArrayList<Valoracion> valoraciones;
    private ArrayList<String> clientes;
    private View.OnClickListener listener;

    public ReviewsListAdapter(ArrayList<Valoracion> valoraciones, ArrayList<String> clientes) {
        this.valoraciones = valoraciones;
        this.clientes = clientes;
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
    public ReviewsListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View pagina = LayoutInflater.from(parent.getContext()).inflate(R.layout.valoracion, parent, false);
        pagina.setOnClickListener(this);

        ReviewsListViewHolder rlvh = new ReviewsListViewHolder(pagina);

        return rlvh;
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewsListViewHolder holder, int position) {
        Valoracion valoracion = valoraciones.get(position);
        String cliente = clientes.get(position);
        holder.bindValoracion(valoracion, cliente);
    }

    @Override
    public int getItemCount() {
        return valoraciones.size();
    }
}

class ReviewsListViewHolder extends RecyclerView.ViewHolder {

    private RatingBar ratingBar;
    private TextView usuario;
    private TextView valoracionText;
    public ReviewsListViewHolder(@NonNull View itemView) {
        super(itemView);

        ratingBar = itemView.findViewById(R.id.rating_bar_valoracion);
        usuario = itemView.findViewById(R.id.usuario_valoracion);
        valoracionText = itemView.findViewById(R.id.valoracion_valoracion);
    }

    public void bindValoracion(Valoracion valoracion, String nombreCliente) {
        ratingBar.setRating(valoracion.getNota());
        usuario.setText(nombreCliente);
        valoracionText.setText(valoracion.getComentario());
    }
}
