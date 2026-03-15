package com.sam170703dev.megustapp.peticiones_http.post;

import com.sam170703dev.megustapp.api.APIRest;
import com.sam170703dev.megustapp.entidades.Plato;
import com.sam170703dev.megustapp.entidades.Valoracion;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class PeticionCrearRestaurante implements Serializable {
    private String nombre;
    private String telefono;
    private String ciudad;
    private String calle;
    private String email;
    private String clave;
    private String fotoPerfil;
    private List<Plato> platos;
    private List<Valoracion> valoraciones;

    public PeticionCrearRestaurante(String nombre, String telefono, String ciudad, String calle, String email, String clave) {
        setNombre(nombre);
        setTelefono(telefono);
        setCiudad(ciudad);
        setCalle(calle);
        setEmail(email);
        setClave(clave);
        setFotoPerfil(APIRest.URL_API_IMAGENES + "/api/imagenes/default/restaurants/restaurant_default.webp");
        setPlatos(new ArrayList<>());
        setValoraciones(new ArrayList<>());
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
        return "PeticionCrearRestaurante{" +
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
