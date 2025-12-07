package com.example.megustapp.clases.cuentas;

import com.example.megustapp.clases.UserAccount;
import com.example.megustapp.enums.UserAccountTypes;

public class ClientAccount extends UserAccount {

    private int numeroValoracionesRealizadas;
    public ClientAccount(String usuario, String clave) {
        super(usuario, clave, UserAccountTypes.CLIENT);
    }


}
