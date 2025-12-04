package com.example.megustapp;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
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
        final ListView menu = findViewById(R.id.menu_restaurant_register3);
        final EditMenuAdapter adaptador = new EditMenuAdapter(this, platos);

        ActivityResultLauncher resultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult resultado) {
                        if(resultado.getResultCode() == Activity.RESULT_OK) {
                            
                        }
                    }
                }
        );

        menu.setAdapter(adaptador);

        menu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(position != parent.getCount() - 1) {

                }
                else {
                    platos.add(position,new Plato("Nuevo plato", 10));
                    menu.setAdapter(adaptador);
                    Toast.makeText(RestaurantRegister3.this, "Añadido", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}