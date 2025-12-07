package com.example.megustapp.clases.cuentas;

import com.example.megustapp.clases.UserAccount;
import com.example.megustapp.enums.UserAccountTypes;

public class RestaurantAccount extends UserAccount {

    public RestaurantAccount(String usuario, String clave) {
        super(usuario, clave, UserAccountTypes.RESTAURANT);
    }
}
