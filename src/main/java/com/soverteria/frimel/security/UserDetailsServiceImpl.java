package com.soverteria.frimel.security;

import com.soverteria.frimel.modelos.entity.Usuario;
import com.soverteria.frimel.repositorios.UsuarioRepositorio;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.net.http.HttpRequest;
import java.util.ArrayList;
import java.util.Collection;

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
