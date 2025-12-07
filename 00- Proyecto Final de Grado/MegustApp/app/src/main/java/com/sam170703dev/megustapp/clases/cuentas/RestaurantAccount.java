package com.sam170703dev.megustapp.clases.cuentas;

import com.sam170703dev.megustapp.clases.UserAccount;
import com.sam170703dev.megustapp.enums.UserAccountTypes;

public class RestaurantAccount extends UserAccount {

    public RestaurantAccount(String usuario, String clave) {
        super(usuario, clave, UserAccountTypes.RESTAURANT);
    }
}
