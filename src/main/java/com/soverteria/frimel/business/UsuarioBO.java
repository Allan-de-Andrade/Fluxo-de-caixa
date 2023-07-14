package com.soverteria.frimel.business;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.soverteria.frimel.controllers.UsuarioController;
import com.soverteria.frimel.modelos.dto.UsuarioDTO;
import com.soverteria.frimel.modelos.entity.Autoridade;
import com.soverteria.frimel.modelos.entity.Usuario;
import com.soverteria.frimel.repositorios.AutoridadeRepositorio;
import com.soverteria.frimel.repositorios.UsuarioRepositorio;
import com.soverteria.frimel.security.Filtros.JWTAutenticacao;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

/**
 * esta classe gerencia os dados de um usuario
 */
@Service
@ComponentScan(basePackageClasses={UsuarioController.class, UsuarioBO.class})
public class UsuarioBO  {

    final UsuarioRepositorio usuarioRepositorio;
    final AutoridadeRepositorio autoridadeRepositorio;


    public UsuarioBO(UsuarioRepositorio usuarioRepositorio,AutoridadeRepositorio autoridadeRepositorio) {
        this.usuarioRepositorio = usuarioRepositorio;
        this.autoridadeRepositorio = autoridadeRepositorio;
    }

    /**
     * metodo para criptografar dados importantes  e que devem ser protegidos
     * @return
     */
    @Bean
    private PasswordEncoder criptografarDado(){
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    public Page<Usuario> listarUsuarios(Pageable page){
        return usuarioRepositorio.findAll(page);
    }


    public Usuario pegarUsuarioPeloNome(String username){
        return  usuarioRepositorio.findByUsername(username);
    }
    public Usuario salvar(UsuarioDTO usuarioDTO){

        Usuario usuario = new Usuario();

        if(usuarioDTO != null) {
         usuario.setFotoUsuario(usuarioDTO.getFotoUsuario());
         usuario.setSenha(criptografarDado().encode(usuarioDTO.getSenha()));
         usuario.setUsername(usuarioDTO.getUsername());
         usuario.setAutoridade(aplicarAutoridadeUsuario(usuario,"ROLE_USUARIO"));
        }

     return usuarioRepositorio.save(usuario);
    }

    public Collection<Autoridade> aplicarAutoridadeUsuario(Usuario usuario,String nomeAutoridade){
       Autoridade autoridade = autoridadeRepositorio.findByNome(nomeAutoridade);
       usuario.getAutoridade().add(autoridade);
       return usuario.getAutoridade();
    }

    public Usuario editar(long id ,UsuarioDTO usuarioDTO){

        Usuario usuario = usuarioRepositorio.getOne(id);
         if(usuarioDTO != null){
             usuario.setUsername(criptografarDado().encode(usuarioDTO.getUsername()));
             usuario.setSenha(criptografarDado().encode(usuarioDTO.getSenha()));
             usuario.setFotoUsuario(usuario.getFotoUsuario());
         }

         return usuarioRepositorio.save(usuario);
    }
    public Boolean deletarUsuario(long id){

        try{
            return deletarUsuario(id);
        }

        catch (Exception e){
            e.printStackTrace();
            return Boolean.FALSE;
        }
    }


  public Usuario pegarUsuarioPeloId(long id){
    return usuarioRepositorio.getOne(id);
  }


  public void recarregarToken(HttpServletRequest request, HttpServletResponse response){

      String atributoHeader = request.getHeader("Authorization");
      String atributoBearer = "Bearer ";

      if(atributoHeader != null && atributoHeader.startsWith("Bearer ")) {
          try {
              Algorithm algorithm = Algorithm.HMAC256(JWTAutenticacao.senhaToken);
              String refreshToken = atributoHeader.replace(atributoBearer, "");

              String username = JWT.require(Algorithm.HMAC256(JWTAutenticacao.senhaToken)).build().verify(refreshToken).getSubject();
              Usuario usuario = usuarioRepositorio.findByUsername(username);

              Collection<SimpleGrantedAuthority> autoridades = new ArrayList<>();

              usuario.getAutoridade().stream().forEach(autoridade -> {
                  autoridades.add(new SimpleGrantedAuthority(autoridade.getNome()));
              });

              UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(usuario, null, autoridades);
              SecurityContextHolder.getContext().setAuthentication(authenticationToken);

              String token_acesso = JWT.create().withSubject(usuario.getUsername()).withIssuer(request.getRequestURL().toString()).
                      withExpiresAt(new Date(System.currentTimeMillis() + 900_000)).
                      withClaim("autoridades", usuario.getAutoridade().stream().map(Autoridade::getNome).collect(Collectors.toList())).sign(algorithm);

              response.setHeader("token_acesso", token_acesso);
          }
          catch (Exception e) {
              response.setStatus(HttpStatus.FORBIDDEN.value());
              e.printStackTrace();
          }
      }

      else{
          throw new RuntimeException("você precisa colocar o refresh token pra ter um novo access token");
      }
  }
}
