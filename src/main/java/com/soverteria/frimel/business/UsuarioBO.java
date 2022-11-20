package com.soverteria.frimel.business;

import com.soverteria.frimel.controllers.UsuarioController;
import com.soverteria.frimel.modelos.dto.UsuarioDTO;
import com.soverteria.frimel.modelos.entity.Autoridade;
import com.soverteria.frimel.modelos.entity.Usuario;
import com.soverteria.frimel.repositorios.AutoridadeRepositorio;
import com.soverteria.frimel.repositorios.UsuarioRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
@ComponentScan(basePackageClasses={UsuarioController.class, UsuarioBO.class})
public class UsuarioBO  {

    final UsuarioRepositorio usuarioRepositorio;
    final AutoridadeRepositorio autoridadeRepositorio;


    public UsuarioBO(UsuarioRepositorio usuarioRepositorio,AutoridadeRepositorio autoridadeRepositorio) {
        this.usuarioRepositorio = usuarioRepositorio;
        this.autoridadeRepositorio = autoridadeRepositorio;
    }


    @Bean
    private PasswordEncoder criptografarDado(){
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    public List<Usuario> listarUsuarios(){
        return usuarioRepositorio.findAll();
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

}
