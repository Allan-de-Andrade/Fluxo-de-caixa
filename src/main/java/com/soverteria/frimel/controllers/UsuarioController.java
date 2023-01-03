package com.soverteria.frimel.controllers;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.soverteria.frimel.business.UsuarioBO;
import com.soverteria.frimel.modelos.dto.UsuarioDTO;
import com.soverteria.frimel.modelos.entity.Autoridade;
import com.soverteria.frimel.modelos.entity.Usuario;
import com.soverteria.frimel.repositorios.UsuarioRepositorio;
import com.soverteria.frimel.security.Filtros.JWTAutenticacao;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Arrays.stream;

/**
 * esta classe gerencia as requisições http do usuario
 */
@RestController
@RequestMapping("api/usuario")
public class UsuarioController {

   private  final  UsuarioBO usuarioBO;
   private final UsuarioRepositorio usuarioRepositorio;

    public UsuarioController(UsuarioBO usuarioBO,UsuarioRepositorio usuarioRepositorio) {
        this.usuarioBO = usuarioBO;
        this.usuarioRepositorio = usuarioRepositorio;
    }


    @GetMapping("/listar")
    @PreAuthorize(value = "hasAnyRole('ADMIN')")
    public ResponseEntity<Object> listarUsuarios(@RequestParam int pagina, @RequestParam int quantidade){
        try {
            Pageable paginacao = PageRequest.of(pagina,quantidade);
            return ResponseEntity.status(HttpStatus.OK).body(usuarioBO.listarUsuarios(paginacao));
        }
        catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ocorreu uma falha no servidor ao tentar pegar os usuarios");
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> pegarUsuarioPeloId(@PathVariable Long id){
        try {
            return ResponseEntity.status(HttpStatus.OK).body(usuarioBO.pegarUsuarioPeloId(id));
        }
        catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ocorreu uma falha no servidor ao tentar pegar o usuario");
        }
    }

    @PutMapping(value = "/cadastro")
    public ResponseEntity<Object> salvar(@RequestBody  UsuarioDTO usuarioDTO){

        try {
            return ResponseEntity.status(HttpStatus.OK).body(usuarioBO.salvar(usuarioDTO));
        }

        catch (NullPointerException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error 400: Não foi possivel salvar  pois faltou as informações da sua nova conta");
        }

        catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro 500: Ocorreu um erro ao tentar salvar o usuario");
        }
    }

    @PostMapping(value="/{id}")
    public ResponseEntity<Object> editarUsuario(@RequestBody UsuarioDTO usuarioDTO,@PathVariable long id){

        try {
            return ResponseEntity.status(HttpStatus.OK).body(usuarioBO.editar(id,usuarioDTO));
        }

        catch (NullPointerException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error 400: Não foi possivel editar  pois faltou as informações da sua conta");
        }
    }

    @DeleteMapping(value="/{id}")
    public ResponseEntity<Object> deletarUsuario(@PathVariable Long id){

        try {
            return ResponseEntity.status(HttpStatus.OK).body(usuarioBO.deletarUsuario(id));
        }

        catch (Exception e){
            e.printStackTrace();

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error 500: Ocorreu um erro ao tentar deletar sua conta");
        }
    }
    @PutMapping(value = "/recarregarToken")
    public ResponseEntity<Object> recarregarTokenDeAcesso(HttpServletResponse response, HttpServletRequest request) throws IOException {

        try {
            usuarioBO.recarregarToken(request,response);
           return ResponseEntity.status(HttpStatus.OK).body("token recarregado com sucesso");
        }

        catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error 500: Ocorreu um erro ao tentar deletar sua conta");
        }
    }
}
