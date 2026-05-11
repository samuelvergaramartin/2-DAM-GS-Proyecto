package com.sam170703dev.megustapp.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sam170703dev.megustapp.R;
import com.sam170703dev.megustapp.adaptadores.ReviewsListAdapter;
import com.sam170703dev.megustapp.api.API;
import com.sam170703dev.megustapp.api.APIRest;
import com.sam170703dev.megustapp.entidades.Cliente;
import com.sam170703dev.megustapp.entidades.Restaurante;
import com.sam170703dev.megustapp.entidades.Valoracion;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReviewsListFragment extends Fragment {
    private int idRestaurante;
    private ArrayList<String> clientes = new ArrayList<>();

    public ReviewsListFragment(int idRestaurante) {
        this.idRestaurante = idRestaurante;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_reviews_list, container, false);
        final RecyclerView listadoValoraciones = view.findViewById(R.id.valoraciones_fragment_reviews_list);
        final TextView textoSinValoraciones = view.findViewById(R.id.texto_sin_valoraciones_fragment_reviews_list);
        final SharedPreferences sharedPreferences = getActivity().getSharedPreferences("Tokens", Context.MODE_PRIVATE);
        final API api = APIRest.getAPI();
        final String tokenRestaurante = sharedPreferences.getString("token_restaurante", "");
        final String tokenCliente = sharedPreferences.getString("token_cliente", "");

        listadoValoraciones.setHasFixedSize(true);

        api.getRestauranteById("Bearer " + tokenRestaurante, idRestaurante).enqueue(new Callback<Restaurante>() {
            @Override
            public void onResponse(Call<Restaurante> call, Response<Restaurante> response) {
                if(response.isSuccessful()) {
                    ArrayList<Valoracion> valoraciones;
                    if(response.body().getValoraciones() != null) {
                        valoraciones = new ArrayList<>(response.body().getValoraciones());
                        api.getClientes("Bearer " + tokenCliente).enqueue(new Callback<List<Cliente>>() {
                            @Override
                            public void onResponse(Call<List<Cliente>> call, Response<List<Cliente>> response2) {
                                if(response2.isSuccessful()) {
                                    ArrayList<Cliente> clientesAPI;
                                    if(response2.body() != null) {
                                        clientesAPI = new ArrayList<>(response2.body());
                                    }
                                    else clientesAPI = new ArrayList<>();
                                    for(Valoracion valoracion : valoraciones) {
                                        clientes.add(getClientNameById(valoracion.getClienteId(), clientesAPI));
                                    }
                                    final ReviewsListAdapter adaptador = new ReviewsListAdapter(valoraciones, clientes);
                                    listadoValoraciones.setAdapter(adaptador);
                                    listadoValoraciones.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

                                    if(valoraciones.isEmpty()) textoSinValoraciones.setVisibility(View.VISIBLE);
                                }
                                else Toast.makeText(getContext(), "Error al obtener informacion de los clientes.", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onFailure(Call<List<Cliente>> call, Throwable t) {
                                Toast.makeText(getContext(), "Error al obtener informacion de los clientes.", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                    else {
                        valoraciones = new ArrayList<>();
                    }
                }
                else Toast.makeText(getContext(), "Error al obtener información del restaurante.", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<Restaurante> call, Throwable t) {
                Toast.makeText(getContext(), "Error al obtener información del restaurante.", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    private String getClientNameById(int id, ArrayList<Cliente> clientes) {
        int i = 0;
        String nombre = "";

        while(i < clientes.size() && nombre.isBlank()) {
            if(clientes.get(i).getId() == id) {
                nombre = clientes.get(i).getNombre();
            }

            i++;
        }

        return nombre;
    }
}
