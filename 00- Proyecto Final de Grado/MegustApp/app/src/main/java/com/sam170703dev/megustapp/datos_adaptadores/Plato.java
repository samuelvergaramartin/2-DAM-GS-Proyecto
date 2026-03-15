package com.sam170703dev.megustapp.datos_adaptadores;

/**
 * @Deprecated - Usar clase Plato del paquete entidades.
 */
@Deprecated
public class Plato {
    private String nombre;
    private double precio;
    private String ingredientes;

    @Deprecated
    public Plato(String nombre, double precio, String ingredientes) {
        this.nombre = nombre;
        this.precio = precio;
        this.ingredientes = ingredientes;
    }

    @Deprecated
    public String getNombre() {
        return nombre;
    }

    @Deprecated
    public double getPrecio() {
        return precio;
    }

    @Deprecated
    public String getIngredientes() {
        return ingredientes;
    }
}
