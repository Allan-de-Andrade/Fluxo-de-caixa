package com.soverteria.frimel.modelos.dto;

import com.soverteria.frimel.modelos.entity.Debito;
import com.soverteria.frimel.modelos.entity.Despesa;
import com.soverteria.frimel.modelos.entity.Estoque;

import java.util.Collection;
import java.util.List;

public class UsuarioDTO {

    private long id;
    private String username;
    private String email;
    private String senha;
    private String fotoUsuario;


    public String getFotoUsuario() {
        return fotoUsuario;
    }

    public long getId() {
        return id;
    }

    public String getUsername() {
        return  username;
    }


    public String getSenha() {
        return senha;
    }

}
