package com.allan.fluxo_de_caixa.modelos.dto;

public class UsuarioDTO {

    private long id;
    private String username;
    private String email;
    private String senha;
    private String fotoUsuario;

    public UsuarioDTO(String username, String senha, String fotoUsuario) {
        this.id = id;
        this.username = username;
        this.senha = senha;
        this.fotoUsuario = fotoUsuario;
    }

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
