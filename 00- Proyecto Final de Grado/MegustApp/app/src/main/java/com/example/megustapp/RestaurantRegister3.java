package com.example.megustapp;

import android.os.Bundle;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.megustapp.adaptadores.EditMenuAdapter;
import com.example.megustapp.datos_adaptadores.Plato;

import java.util.ArrayList;

public class RestaurantRegister3 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.restaurant_register3);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.restaurant_register3_activity), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        final ArrayList<Plato> platos = new ArrayList<>();
        platos.add(new Plato("Espetos de sardinas", 12));
        final ListView menu = findViewById(R.id.menu);
        final EditMenuAdapter adaptador = new EditMenuAdapter(this, platos);

        menu.setAdapter(adaptador);
    }
}