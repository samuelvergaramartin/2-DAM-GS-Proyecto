package com.sam170703dev.megustapp.actividades;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.sam170703dev.megustapp.R;
import com.sam170703dev.megustapp.fragments.FragmentRestaurantMenu;
import com.sam170703dev.megustapp.fragments.SoonFragment;

public class MainActivityRestaurant extends AppCompatActivity {

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main_restaurant_options_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main_restaurant);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.restaurant_main_activity), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        final Toolbar toolbar = findViewById(R.id.toolbar_activity_main_restaurant);
        setSupportActionBar(toolbar);

        final BottomNavigationView navbar = findViewById(R.id.navbar_activity_main_restaurant);
        navbar.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                Fragment fragment;

                if(menuItem.getItemId() == R.id.opc_menu_navbar_restaurant) {
                    fragment = new FragmentRestaurantMenu();
                }
                else {
                    fragment = new SoonFragment();
                }

                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.framelayout_activity_main_restaurant, fragment)
                        .commit();

                return true;
            }
        });

        getSupportFragmentManager().beginTransaction()
                .add(R.id.framelayout_activity_main_restaurant, new FragmentRestaurantMenu())
                .commit();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Intent resultado = new Intent();
        resultado.putExtra("cerrar_sesion", true);
        setResult(RESULT_OK, resultado);
        finish();
        return super.onOptionsItemSelected(item);
    }
}