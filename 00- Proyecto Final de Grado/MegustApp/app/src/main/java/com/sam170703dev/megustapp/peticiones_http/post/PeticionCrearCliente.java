package com.sam170703dev.megustapp.peticiones_http.post;

import com.sam170703dev.megustapp.api.APIRest;
import com.sam170703dev.megustapp.entidades.Valoracion;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class PeticionCrearCliente implements Serializable {
    private String nombre;
    private String ciudad;
    private String telefono;
    private int numeroValoraciones;
    private String email;
    private String clave;
    private String fotoPerfil;
    private List<Valoracion> valoraciones;

    public PeticionCrearCliente(String ciudad, String email, String nombre, String clave, String telefono) {
        setCiudad(ciudad);
        setEmail(email);
        setNombre(nombre);
        setClave(clave);
        setFotoPerfil(APIRest.URL_API_IMAGENES + "/api/imagenes/default/clients/default_user.png");
        setNumeroValoraciones(0);
        setTelefono(telefono);
        setValoraciones(new ArrayList<>());
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public void setFotoPerfil(String fotoPerfil) {
        this.fotoPerfil = fotoPerfil;
    }

    public void setNumeroValoraciones(int numeroValoraciones) {
        this.numeroValoraciones = numeroValoraciones;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getNombre() {
        return nombre;
    }

    public String getCiudad() {
        return ciudad;
    }

    public String getTelefono() {
        return telefono;
    }

    public int getNumeroValoraciones() {
        return numeroValoraciones;
    }

    public String getEmail() {
        return email;
    }

    public String getClave() {
        return clave;
    }

    public String getFotoPerfil() {
        return fotoPerfil;
    }

    public void setValoraciones(List<Valoracion> valoraciones) {
        this.valoraciones = valoraciones;
    }

    public List<Valoracion> getValoraciones() {
        return valoraciones;
    }
}
