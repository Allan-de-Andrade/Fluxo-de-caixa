package com.soverteria.frimel.security.Filtros;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.soverteria.frimel.business.UsuarioBO;
import com.soverteria.frimel.modelos.entity.Autoridade;
import com.soverteria.frimel.modelos.entity.Usuario;
import com.soverteria.frimel.repositorios.UsuarioRepositorio;
import com.soverteria.frimel.security.UsuarioDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

public class JWTAutenticacao extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    public static final Usuario usuario = new Usuario();
     public JWTAutenticacao(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;

        setFilterProcessesUrl("/api/usuario/login");
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        try {
            Usuario usuario = new ObjectMapper().readValue(request.getInputStream(),Usuario.class);
            this.usuario.setUsername(usuario.getUsername());

            return authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(usuario.getUsername(), usuario.getSenha(), usuario.getAutoridade())
            );
        }
        catch (IOException e){
            e.printStackTrace();
        }
        return null;
    }

    public static final String senhaToken = "8a42cf2b-b458-4c6e-819d-86df95e968ac";

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {

        UsuarioDetails usuarioDetails = (UsuarioDetails) authResult.getPrincipal();
        Algorithm algorithm = Algorithm.HMAC256(senhaToken);

       String token_acesso = JWT.create().withSubject(usuarioDetails.getUsername()).withIssuer(request.getRequestURL().toString()).
               withExpiresAt(new Date(System.currentTimeMillis() + 180_000)).
               withClaim("autoridades",usuarioDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList())).sign(algorithm);

        String token_refresh = JWT.create().withSubject(usuarioDetails.getUsername()).withIssuer(request.getRequestURL().toString()).
                withExpiresAt(new Date(System.currentTimeMillis() + 360_000)).sign(algorithm);

       response.setHeader("token_acesso", token_acesso);
       response.setHeader("token_refresh",token_refresh);
    }
}
