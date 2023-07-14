package com.soverteria.frimel.security.Filtros;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import static java.util.Arrays.stream;

/**
 * Classe que valida o token JWT para enviar as informações requeridas do usuario.
 */
public class ValidarJWT extends BasicAuthenticationFilter {

    public ValidarJWT(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }


    /**
     * Metodo que recebe o token e manda fazer a autenticação do token
     * @param request
     * @param response
     * @param chain
     * @throws IOException
     * @throws ServletException
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {

        String atributoHeader = "Authorization";
        String atributoBearer = "Bearer ";
        try {
            String atributo = request.getHeader(atributoHeader);

            if (atributo == null && !atributo.startsWith(atributoBearer)) {
                chain.doFilter(request, response);
                return;
            }

            String token = atributo.replace(atributoBearer, "");
            UsernamePasswordAuthenticationToken authenticationToken = pegarAutenticacaoToken(token);

            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            chain.doFilter(request, response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * autentica o token do usuario
     * @param token
     * @return
     */
    private UsernamePasswordAuthenticationToken pegarAutenticacaoToken(String token) {

        try {
            String usuario = JWT.require(Algorithm.HMAC256(JWTAutenticacao.senhaToken)).build().verify(token).getSubject();

            DecodedJWT decodificadorJWT = JWT.require(Algorithm.HMAC256(JWTAutenticacao.senhaToken)).build().verify(token);
            String[] autoridadesDTO = decodificadorJWT.getClaim("autoridades").asArray(String.class);

            Collection<SimpleGrantedAuthority> autoridades = new ArrayList<>();
            stream(autoridadesDTO).forEach(autoridade -> {
                autoridades.add(new SimpleGrantedAuthority(autoridade));
            });

            if (usuario == null) {
                return null;
            }
            return new UsernamePasswordAuthenticationToken(usuario, null, autoridades);
        }

        catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
