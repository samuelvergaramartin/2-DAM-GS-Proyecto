package com.sam170703dev.megustapp.entidades;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Plato implements Serializable {
    private int id;
    private String nombre;
    private double precio;
    private String imagen;
    private List<Ingrediente> ingredientes = new ArrayList<>();

    private int restauranteId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public List<Ingrediente> getIngredientes() {
        return ingredientes;
    }

    public void addIngrediente(Ingrediente ingrediente) {
        ingredientes.add(ingrediente);
    }

    public void removeIngrediente(Ingrediente ingrediente) {
        ingredientes.remove(ingrediente);
    }

    public void setRestauranteId(int restauranteId) {
        this.restauranteId = restauranteId;
    }

    public int getRestauranteId() {
        return restauranteId;
    }

    @Override
    public String toString() {
        return "Plato{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", precio=" + precio +
                ", imagen='" + imagen + '\'' +
                ", ingredientes=" + ingredientes +
                ", restauranteId=" + restauranteId +
                '}';
    }
}
