package com.allan.fluxo_de_caixa.security;

import com.allan.fluxo_de_caixa.modelos.entity.Usuario;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

/**
 * Classe que define os dados de segurança do usuario
 */
public class UsuarioDetails implements UserDetails {

    Usuario usuario;

    public UsuarioDetails() {
    }

    public UsuarioDetails(Usuario usuario) {
        this.usuario = usuario;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return usuario.getAutoridade();
    }

    @Override
    public String getPassword() {
         return usuario.getSenha();
    }

    @Override
    public String getUsername() {
        return usuario.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
