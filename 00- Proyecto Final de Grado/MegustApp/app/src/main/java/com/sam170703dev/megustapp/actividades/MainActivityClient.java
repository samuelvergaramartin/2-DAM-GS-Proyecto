package com.sam170703dev.megustapp.actividades;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

import com.sam170703dev.megustapp.R;
import com.sam170703dev.megustapp.fragments.ClientSettingsFragment;
import com.sam170703dev.megustapp.fragments.RestaurantListFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivityClient extends AppCompatActivity {

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.restautant_list_options_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main_client);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.mainActivityClient), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        final Bundle bundle = getIntent().getExtras();
        Toolbar toolbar = findViewById(R.id.toolbar_activity_main_client);
        setSupportActionBar(toolbar);

        if(savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.framelayout_activity_main_client, new RestaurantListFragment(toolbar))
                    .commit();
        }

        final BottomNavigationView navbar = findViewById(R.id.navbar_activity_main_client);

        navbar.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                Fragment fragment = null;
                int idCliente = 0;

                if(bundle != null) {
                    idCliente = bundle.getInt("id_cliente");
                }

                if(menuItem.getItemId() == R.id.buscar_navbar_opc) {
                    fragment = new RestaurantListFragment(toolbar);
                }
                else if(menuItem.getItemId() == R.id.cuenta_navbar_opc) {
                    fragment = new ClientSettingsFragment(idCliente, toolbar);
                }

                if(fragment == null) return false;

                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.framelayout_activity_main_client, fragment)
                        .commit();

                return true;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(item.getItemId() == R.id.opc_buscar_restaurant_list_menu) {
            Toast.makeText(this, "Próximamente...", Toast.LENGTH_SHORT).show();
        }

        if(item.getItemId() == R.id.opc_recargar_pagina_restaurant_list_menu) {
            Toast.makeText(this, "Página recargada con éxito", Toast.LENGTH_SHORT).show();
        }

        if(item.getItemId() == R.id.opc_cerrar_sesion_restaurant_list_menu) {
            Intent resultado = new Intent();
            resultado.putExtra("cerrar_sesion", true);
            setResult(RESULT_OK, resultado);
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}