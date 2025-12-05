package com.example.megustapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.megustapp.adaptadores.EditMenuAdapter;
import com.example.megustapp.datos_adaptadores.Plato;

import java.util.ArrayList;

public class RestaurantRegister3 extends AppCompatActivity {

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.edit_menu_context_menu, menu);
    }

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
        final ListView menu = findViewById(R.id.menu_restaurant_register3);
        final EditMenuAdapter adaptador = new EditMenuAdapter(this, platos);

        ActivityResultLauncher resultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult resultado) {
                        if(resultado.getResultCode() == Activity.RESULT_OK) {
                            Intent intent = resultado.getData();
                            if(intent != null) {
                                Bundle datos = intent.getExtras();
                                if(datos != null) {
                                    String nombrePlato = datos.getString("nombre_plato");
                                    double precioPlato = Double.parseDouble(datos.getString("precio_plato"));
                                    String ingredientes = datos.getString("ingredientes");
                                    platos.add(platos.size() - 1, new Plato(nombrePlato, precioPlato));
                                    menu.setAdapter(adaptador);
                                    View plato = adaptador.getView(platos.size() - 2, null, menu);
                                    Toast.makeText(RestaurantRegister3.this, "Plato " + plato , Toast.LENGTH_SHORT).show();
                                    plato.setBackgroundColor(getResources().getColor(R.color.error));
                                    registerForContextMenu(plato);
                                }
                            }
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
                    Intent addDishActivity = new Intent(RestaurantRegister3.this, AddDish.class);
                    resultLauncher.launch(addDishActivity);
                }
            }
        });
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {

        return super.onContextItemSelected(item);
    }
}