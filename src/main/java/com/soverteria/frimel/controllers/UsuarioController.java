package com.soverteria.frimel.controllers;

import com.soverteria.frimel.business.UsuarioBO;
import com.soverteria.frimel.modelos.dto.UsuarioDTO;
import com.soverteria.frimel.modelos.entity.Usuario;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@CrossOrigin(origins = "192.168.1.197:4200/",maxAge = 3600)
@RequestMapping("api/usuario")

public class UsuarioController {
  private  final  UsuarioBO usuarioBO;


    public UsuarioController(UsuarioBO usuarioBO) {
        this.usuarioBO = usuarioBO;
    }


    @GetMapping("/listar")
    @PreAuthorize(value = "hasAnyRole('ADMIN')")
    public ResponseEntity<Object> listarUsuarios(){
        try {
            return ResponseEntity.status(HttpStatus.OK).body(usuarioBO.listarUsuarios());
        }
        catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ocorreu uma falha no servidor ao tentar pegar os usuarios");
        }
    }

    @GetMapping("/{id}")
    @PreAuthorize(value = "hasAnyRole('ADMIN','USUARIO')")
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
    @PreAuthorize(value = "hasAnyRole('USUARIO')")
    public ResponseEntity<Object> editarUsuario(@RequestBody UsuarioDTO usuarioDTO,@PathVariable long id){

        try {
            return ResponseEntity.status(HttpStatus.OK).body(usuarioBO.editar(id,usuarioDTO));
        }

        catch (NullPointerException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error 400: Não foi possivel editar  pois faltou as informações da sua conta");
        }
    }

    @DeleteMapping(value="/{id}")
    @PreAuthorize(value = "hasAnyRole('USUARIO')")
    public ResponseEntity<Object> deletarUsuario(@PathVariable Long id){

        try {
            return ResponseEntity.status(HttpStatus.OK).body(usuarioBO.deletarUsuario(id));
        }

        catch (Exception e){
            e.printStackTrace();

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error 500: Ocorreu um erro ao tentar deletar sua conta");
        }

    }
}
