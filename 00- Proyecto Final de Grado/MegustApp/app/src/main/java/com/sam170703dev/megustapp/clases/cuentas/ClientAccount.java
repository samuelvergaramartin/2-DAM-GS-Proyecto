package com.sam170703dev.megustapp.clases.cuentas;

import com.sam170703dev.megustapp.clases.UserAccount;
import com.sam170703dev.megustapp.enums.UserAccountTypes;

public class ClientAccount extends UserAccount {

    private int numeroValoracionesRealizadas;
    public ClientAccount(int id, String usuario, String clave) {
        super(id, usuario, clave, UserAccountTypes.CLIENT);
    }


}
