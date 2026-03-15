package com.sam170703dev.megustapp.peticiones_http.post;

import java.io.Serializable;

public class PeticionEliminarImagenPlato implements Serializable {
    private String image;

    public PeticionEliminarImagenPlato(String image) {
        this.image = image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getImage() {
        return image;
    }
}
