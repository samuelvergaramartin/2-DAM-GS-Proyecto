package com.sam170703dev.megustapp.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.sam170703dev.megustapp.R;
import com.sam170703dev.megustapp.actividades.RestaurantInfo;
import com.sam170703dev.megustapp.adaptadores.RestaurantListAdapter;
import com.sam170703dev.megustapp.api.API;
import com.sam170703dev.megustapp.api.APIRest;
import com.sam170703dev.megustapp.entidades.Restaurante;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RestaurantListFragment extends Fragment {

    private static final String ARG_ID_CLIENTE = "id_cliente";
    private int idCliente;
    private TextInputLayout searchBar;
    private TextInputEditText searchBarTextInput;
    private MaterialButton botonFiltro;
    private ArrayList<Restaurante> datosRestaurantesAdaptador;
    private ArrayList<String> chipsCheckedNames = new ArrayList<>();

    public RestaurantListFragment() {
        // Constructor vacío obligatorio
    }
    public RestaurantListFragment(Toolbar toolbar, int idCliente) {
        toolbar.setTitle("Restaurantes");
        this.idCliente = idCliente;
    }

    public static RestaurantListFragment newInstance(int idCliente) {
        RestaurantListFragment fragment = new RestaurantListFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_ID_CLIENTE, idCliente);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatActivity activity = (AppCompatActivity) requireActivity();
        if(activity.getSupportActionBar() != null) activity.getSupportActionBar().setTitle("Restaurantes");
        if (getArguments() != null) {
            idCliente = getArguments().getInt(ARG_ID_CLIENTE);
        }

        View view = inflater.inflate(R.layout.fragment_restaurant_list, container, false);

        final RecyclerView recyclerView = view.findViewById(R.id.listado_restaurantes_fragment_restaurant_list);
        final ChipGroup filtros = view.findViewById(R.id.chipGroup);
        final Chip filtroCiudad = view.findViewById(R.id.chip_ciudad);
        final ChipGroup filtrosCiudades = view.findViewById(R.id.chipCityGroup);
        final Chip filtroMalaga = view.findViewById(R.id.chip_malaga);
        final Chip filtroCordoba = view.findViewById(R.id.chip_cordoba);
        final Chip filtroSevilla = view.findViewById(R.id.chip_sevilla);
        final Chip filtroMadrid = view.findViewById(R.id.chip_madrid);
        searchBarTextInput = view.findViewById(R.id.searchBarEditText);
        searchBar = view.findViewById(R.id.searchBar);
        botonFiltro = view.findViewById(R.id.filterButton);

        final ArrayList<Restaurante> datosRestaurantes = new ArrayList<>();
        datosRestaurantesAdaptador = new ArrayList<>();
        final SharedPreferences sharedPreferences = getActivity().getSharedPreferences("Tokens", Context.MODE_PRIVATE);
        final API api = APIRest.getAPI();
        final String tokenCliente = sharedPreferences.getString("token_cliente", "");

        ActivityResultLauncher activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if(result.getResultCode() == Activity.RESULT_OK) {
                            getActivity().recreate();
                        }
                    }
                }
        );

        searchBar.setStartIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String busqueda = searchBarTextInput.getText().toString();
                if(!filtroCiudad.isChecked()) {
                    datosRestaurantesAdaptador = new ArrayList<>(filtrarPorNombre(busqueda, datosRestaurantes));
                }
                else {
                    ArrayList<Restaurante> filtradosPorCiudades = filtrarPorCiudades(chipsCheckedNames, datosRestaurantes);
                    datosRestaurantesAdaptador = new ArrayList<>(filtrarPorNombre(busqueda, filtradosPorCiudades));
                }

                establecerAdaptador(activityResultLauncher, recyclerView, tokenCliente);
            }
        });

        searchBar.setEndIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchBarTextInput.setText("");
                datosRestaurantesAdaptador = new ArrayList<>(datosRestaurantes);
                establecerAdaptador(activityResultLauncher, recyclerView, tokenCliente);
            }
        });

        botonFiltro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final int visible = filtros.getVisibility();

                if(visible == View.VISIBLE) {
                    filtros.setVisibility(View.GONE);
                    filtroCiudad.setChecked(false);
                    filtrosCiudades.setVisibility(View.GONE);
                    filtroMalaga.setChecked(false);
                    filtroCordoba.setChecked(false);
                    filtroSevilla.setChecked(false);
                    filtroMadrid.setChecked(false);
                    botonFiltro.setIconResource(R.drawable.ic_filter_list_24);
                    chipsCheckedNames.clear();
                    datosRestaurantesAdaptador = new ArrayList<>(datosRestaurantes);
                    establecerAdaptador(activityResultLauncher, recyclerView, tokenCliente);
                }
                else {
                    filtros.setVisibility(View.VISIBLE);
                    botonFiltro.setIconResource(R.drawable.outline_filter_alt_off_24);
                }
            }
        });

        filtroCiudad.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(@NonNull CompoundButton buttonView, boolean isChecked) {
                if(isChecked) filtrosCiudades.setVisibility(View.VISIBLE);
                else {
                    filtrosCiudades.setVisibility(View.GONE);
                    filtroMalaga.setChecked(false);
                    filtroCordoba.setChecked(false);
                    filtroSevilla.setChecked(false);
                    filtroMadrid.setChecked(false);
                    chipsCheckedNames.clear();
                    datosRestaurantesAdaptador = new ArrayList<>(datosRestaurantes);
                    establecerAdaptador(activityResultLauncher, recyclerView, tokenCliente);
                }
            }
        });

        filtrosCiudades.setOnCheckedStateChangeListener(new ChipGroup.OnCheckedStateChangeListener() {
            @Override
            public void onCheckedChanged(@NonNull ChipGroup chipGroup, @NonNull List<Integer> list) {
                ArrayList<Integer> chipsIds = new ArrayList<>();
                ArrayList<String> chipsNames = new ArrayList<>();
                chipsCheckedNames.clear();

                for(int i = 0; i < chipGroup.getChildCount(); i++) {
                    chipsIds.add((chipGroup.getChildAt(i)).getId());
                    chipsNames.add(((Chip) chipGroup.getChildAt(i)).getText().toString());
                }

                for(Integer i : list) {
                    chipsCheckedNames.add(chipsNames.get(chipsIds.indexOf(i)));
                }

                if(chipsCheckedNames.isEmpty()) datosRestaurantesAdaptador = new ArrayList<>(datosRestaurantes);
                else datosRestaurantesAdaptador = new ArrayList<>(filtrarPorCiudades(chipsCheckedNames, datosRestaurantes));

                establecerAdaptador(activityResultLauncher, recyclerView, tokenCliente);
            }
        });

        recyclerView.setHasFixedSize(true);

        api.getRestaurantes("Bearer " + tokenCliente).enqueue(new Callback<List<Restaurante>>() {
            @Override
            public void onResponse(Call<List<Restaurante>> call, Response<List<Restaurante>> response) {
                if(response.isSuccessful()) {
                    for(Restaurante restaurante : response.body()) {
                        datosRestaurantes.add(restaurante);
                        datosRestaurantesAdaptador.add(restaurante);

                        establecerAdaptador(activityResultLauncher, recyclerView, tokenCliente);
                        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
                    }
                }
                else Toast.makeText(getContext(), "Error al obtener información de los restaurantes.", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<List<Restaurante>> call, Throwable t) {
                Toast.makeText(getContext(), "Error al obtener información de los restaurantes.", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    private ArrayList<Restaurante> filtrarPorNombre(String busqueda, ArrayList<Restaurante> restaurantes) {
        final ArrayList<Restaurante> resultado = new ArrayList<>();

        for(Restaurante restaurante : restaurantes) {
            if(restaurante.getNombre().contains(busqueda)) {
                resultado.add(restaurante);
            }
        }

        return resultado;
    }

    private ArrayList<Restaurante> filtrarPorCiudades(ArrayList<String> ciudades, ArrayList<Restaurante> restaurantes) {
        final ArrayList<Restaurante> resultado = new ArrayList<>();

        for(Restaurante restaurante : restaurantes) {
            if(ciudades.contains(restaurante.getCiudad())) resultado.add(restaurante);
        }

        return resultado;
    }

    private void establecerAdaptador(ActivityResultLauncher activityResultLauncher, RecyclerView recyclerView, String tokenCliente) {
        final RestaurantListAdapter adaptador = new RestaurantListAdapter(datosRestaurantesAdaptador, getContext());

        adaptador.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent restaurantInfoActivity = new Intent(getContext(), RestaurantInfo.class);
                restaurantInfoActivity.putExtra("id_restaurante", datosRestaurantesAdaptador.get(recyclerView.getChildAdapterPosition(v)).getId());
                restaurantInfoActivity.putExtra("nombre_restaurante", datosRestaurantesAdaptador.get(recyclerView.getChildAdapterPosition(v)).getNombre());
                restaurantInfoActivity.putExtra("imagen_restaurante", datosRestaurantesAdaptador.get(recyclerView.getChildAdapterPosition(v)).getFotoPerfil());
                restaurantInfoActivity.putExtra("ciudad_restaurante", datosRestaurantesAdaptador.get(recyclerView.getChildAdapterPosition(v)).getCiudad());
                restaurantInfoActivity.putExtra("calle_restaurante", datosRestaurantesAdaptador.get(recyclerView.getChildAdapterPosition(v)).getCalle());
                restaurantInfoActivity.putExtra("telefono_restaurante", datosRestaurantesAdaptador.get(recyclerView.getChildAdapterPosition(v)).getTelefono());
                restaurantInfoActivity.putExtra("platos_restaurante", new ArrayList<>(datosRestaurantesAdaptador.get(recyclerView.getChildAdapterPosition(v)).getPlatos()));
                restaurantInfoActivity.putExtra("valoraciones_restaurante", new ArrayList<>(datosRestaurantesAdaptador.get(recyclerView.getChildAdapterPosition(v)).getValoraciones()));
                restaurantInfoActivity.putExtra("token_cliente", tokenCliente);
                restaurantInfoActivity.putExtra("id_cliente", idCliente);
                restaurantInfoActivity.putExtra("posicion_array", recyclerView.getChildAdapterPosition(v));
                activityResultLauncher.launch(restaurantInfoActivity);
            }
        });

        recyclerView.setAdapter(adaptador);
    }
}