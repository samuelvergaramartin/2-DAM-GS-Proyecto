package com.sam170703dev.megustapp.entidades;

import java.io.Serializable;
import java.util.List;

public class Restaurante implements Serializable {
    private int id;
    private String nombre;
    private String telefono;
    private String ciudad;
    private String calle;
    private String email;
    private String clave;
    private String fotoPerfil;
    private List<Plato> platos;
    private List<Valoracion> valoraciones;

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

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public String getCalle() {
        return calle;
    }

    public void setCalle(String calle) {
        this.calle = calle;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public String getFotoPerfil() {
        return fotoPerfil;
    }

    public void setFotoPerfil(String fotoPerfil) {
        this.fotoPerfil = fotoPerfil;
    }

    public void setPlatos(List<Plato> platos) {
        this.platos = platos;
    }

    public List<Plato> getPlatos() {
        return platos;
    }

    public void setValoraciones(List<Valoracion> valoraciones) {
        this.valoraciones = valoraciones;
    }

    public List<Valoracion> getValoraciones() {
        return valoraciones;
    }

    @Override
    public String toString() {
        return "Restaurante{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", telefono='" + telefono + '\'' +
                ", ciudad='" + ciudad + '\'' +
                ", calle='" + calle + '\'' +
                ", email='" + email + '\'' +
                ", clave='" + clave + '\'' +
                ", fotoPerfil='" + fotoPerfil + '\'' +
                ", platos=" + platos +
                ", valoraciones=" + valoraciones +
                '}';
    }
}
