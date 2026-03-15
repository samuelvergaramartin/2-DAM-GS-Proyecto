package com.sam170703dev.megustapp.actividades;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
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

import com.sam170703dev.megustapp.R;
import com.sam170703dev.megustapp.adaptadores.EditMenuAdapter;
import com.sam170703dev.megustapp.entidades.Ingrediente;
import com.sam170703dev.megustapp.entidades.Plato;

import java.util.ArrayList;

public class EditRestaurantMenu extends AppCompatActivity {

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
        setContentView(R.layout.activity_edit_restaurant_menu);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.activity_edit_restaurant_menu), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        final ListView menu = findViewById(R.id.menu_activity_edit_restaurant_menu);
        final Button botonAplicarCambios = findViewById(R.id.menu_activity_edit_restaurant_menu);
        final TextView textoCancelar = findViewById(R.id.texto_cancelar_activity_edit_restaurant_menu);

        final Bundle bundle = getIntent().getExtras();
        ArrayList<Plato> platos;

        if(bundle != null) {
            platos = new ArrayList<>(bundle.getParcelableArrayList("platos"));
        }
        else platos = new ArrayList<>();

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
                                    if(datos.getBoolean("cerrar_sesion")) {
                                        Intent resultadoActividad = new Intent();
                                        resultadoActividad.putExtra("cerrar_sesion", true);
                                        setResult(RESULT_OK, resultadoActividad);
                                        finish();
                                    }
                                    else {
                                        String nombrePlato = datos.getString("nombre_plato");
                                        double precioPlato = Double.parseDouble(datos.getString("precio_plato"));
                                        ArrayList<Ingrediente> ingredientes = new ArrayList<>(datos.getParcelableArrayList("ingredientes_plato"));
                                        String imagenPlato = datos.getString("imagen_plato");
                                        Plato nuevoPlato = new Plato();
                                        nuevoPlato.setNombre(nombrePlato);
                                        nuevoPlato.setPrecio(precioPlato);
                                        for(Ingrediente ingrediente: ingredientes) {
                                            nuevoPlato.addIngrediente(ingrediente);
                                        }
                                        nuevoPlato.setImagen(imagenPlato);
                                        platos.add(platos.size() - 1, nuevoPlato);
                                        menu.setAdapter(adaptador);
                                        adaptador.getViews().clear();
                                        new Handler().postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                for(View plato : adaptador.getViews()) {
                                                    registerForContextMenu(plato);
                                                }
                                            }
                                        }, 1000);
                                    }
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
                if(position == parent.getCount() - 1) {
                    Intent addDishActivity = new Intent(EditRestaurantMenu.this, AddDish.class);
                    resultLauncher.launch(addDishActivity);
                }
            }
        });

        botonAplicarCambios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                
            }
        });

        textoCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_CANCELED);
                finish();
            }
        });
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        Toast.makeText(this, item.getTitle(), Toast.LENGTH_SHORT).show();

        Toast.makeText(this, "View: " + item.getActionView(), Toast.LENGTH_SHORT).show();
        return super.onContextItemSelected(item);
    }
}