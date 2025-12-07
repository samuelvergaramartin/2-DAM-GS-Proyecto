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
import com.sam170703dev.megustapp.datos_adaptadores.Plato;

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
        final Button botonFinalizarRegistro = findViewById(R.id.boton_finalizar_registro_restaurant_register3);
        final TextView textoVolverAtras = findViewById(R.id.texto_volver_atras_restaurant_register3);

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
        );

        menu.setAdapter(adaptador);

        menu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(position == parent.getCount() - 1) {
                    Intent addDishActivity = new Intent(RestaurantRegister3.this, AddDish.class);
                    resultLauncher.launch(addDishActivity);
                }
            }
        });

        botonFinalizarRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(RestaurantRegister3.this, "Próximamente...", Toast.LENGTH_SHORT).show();
            }
        });

        textoVolverAtras.setOnClickListener(new View.OnClickListener() {
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