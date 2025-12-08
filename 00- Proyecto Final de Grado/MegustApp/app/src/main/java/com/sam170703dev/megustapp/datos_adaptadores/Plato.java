package com.sam170703dev.megustapp.datos_adaptadores;

public class Plato {
    private String nombre;
    private double precio;
    private String ingredientes;

    public Plato(String nombre, double precio, String ingredientes) {
        this.nombre = nombre;
        this.precio = precio;
        this.ingredientes = ingredientes;
    }

    public String getNombre() {
        return nombre;
    }

    public double getPrecio() {
        return precio;
    }

    public String getIngredientes() {
        return ingredientes;
    }
}
