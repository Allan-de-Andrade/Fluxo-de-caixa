package com.soverteria.frimel.controllers;

import com.soverteria.frimel.business.InsumoBO;
import com.soverteria.frimel.modelos.dto.InsumoDTO;
import com.soverteria.frimel.security.Filtros.JWTAutenticacao;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


/**
 * classe que gerencia as requisições http para insumos
 */
@RestController
@RequestMapping(value = "/api/insumos")
public class InsumoController {

    private final InsumoBO insumoBO;

    public InsumoController(InsumoBO insumoBO) {
        this.insumoBO = insumoBO;
    }

    @GetMapping
    public ResponseEntity<Object> listarInsumos(@RequestParam int pagina,@RequestParam int tamanho){
        try {
            PageRequest pageable =  PageRequest.of(pagina,tamanho);
            return ResponseEntity.status(HttpStatus.OK).body(insumoBO.listarInsumos(pageable, JWTAutenticacao.usuario.getUsername()));
        }
        catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Não conseguimos  listar os insumos pois um  erro ocorreu no servidor, tente novamente mais tarde");
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> buscarInsumoPeloId(@PathVariable Long  id){
        try {
            return ResponseEntity.status(HttpStatus.OK).body(insumoBO.pegarInsumoPeloId(id));
        }

        catch (NullPointerException e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Não foi possivel buscar o insumo pois falta informações sobre ele");
        }

        catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Não conseguimos  buscar o insumo pois   ocorreu um erro no servidor, tente novamente mais tarde");
        }
    }

    @PutMapping
    public ResponseEntity<Object> salvarInsumo(@RequestBody InsumoDTO insumoDTO){
        try {
            return ResponseEntity.status(HttpStatus.OK).body(insumoBO.salvarInsumo(insumoDTO));
        }

        catch (NullPointerException e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Não foi possivel salvar o insumo pois falta informações sobre ele");
        }

        catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Não foi possivel salvar o insumo pois ocorreu um erro no servidor,tente novamente mais tarde");
        }
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Object> deletarInsumo(@PathVariable Long id){
        try{
            return ResponseEntity.status(HttpStatus.OK).body(insumoBO.deletarInsumo(id));
        }
        catch (NullPointerException e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Não foi possivel deletar o insumo pois falta informações sobre ele");
        }
        catch (Exception e){
            e.printStackTrace();
            return  ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Não foi possivel deletar o insumo pois ocorreu um erro no servidor, tente novamente mais tarde");
        }
    }
}
