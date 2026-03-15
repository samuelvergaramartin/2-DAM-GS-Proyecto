package com.sam170703dev.megustapp.datos_adaptadores;

/**
 * @Deprecated - Usar clase Restaurante del paquete entidades.
 */
@Deprecated
public class Restaurant {
    private String imagen;
    private String nombre;

    @Deprecated
    public Restaurant(String imagen, String nombre) {
        this.imagen = imagen;
        this.nombre = nombre;
    }

    @Deprecated
    public String getImagen() {
        return imagen;
    }

    @Deprecated
    public String getNombre() {
        return nombre;
    }

    @Deprecated
    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    @Deprecated
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
