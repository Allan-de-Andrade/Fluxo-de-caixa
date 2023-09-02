package com.allan.fluxo_de_caixa.security;

import com.allan.fluxo_de_caixa.modelos.entity.Usuario;
import com.allan.fluxo_de_caixa.repositorios.UsuarioRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * serviço para carregar o usuario requerido
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    UsuarioRepositorio usuarioRepositorio;

    @Override
    public UserDetails loadUserByUsername(String nomeUsuario) throws UsernameNotFoundException {

          Usuario usuario = this.usuarioRepositorio.findByUsername(nomeUsuario);
          if (usuario == null) {
              System.out.println("O usuario é igual a null");
          }
          return new UsuarioDetails(usuario);


    }
}
