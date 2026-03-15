package com.sam170703dev.megustapp.peticiones_http.post;

import com.sam170703dev.megustapp.entidades.Ingrediente;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class PeticionCrearPlato implements Serializable {
    private String nombre;
    private double precio;
    private String imagen;
    private List<Ingrediente> ingredientes = new ArrayList<>();

    private int restauranteId;

    public PeticionCrearPlato(String nombre, double precio, String imagen, List<Ingrediente> ingredientes, int restauranteId) {
        this.nombre = nombre;
        this.precio = precio;
        this.imagen = imagen;
        this.ingredientes = ingredientes;
        this.restauranteId = restauranteId;
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
        return "PeticionCrearPlato{" +
                "nombre='" + nombre + '\'' +
                ", precio=" + precio +
                ", imagen='" + imagen + '\'' +
                ", ingredientes=" + ingredientes +
                ", restauranteId=" + restauranteId +
                '}';
    }
}
