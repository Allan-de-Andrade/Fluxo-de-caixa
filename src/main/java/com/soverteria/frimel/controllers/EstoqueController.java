package com.soverteria.frimel.controllers;

import com.soverteria.frimel.business.EstoqueBO;
import com.soverteria.frimel.modelos.dto.EstoqueDTO;
import com.soverteria.frimel.modelos.entity.Estoque;
import com.soverteria.frimel.repositorios.EstoqueRepositorio;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * Esta classe executa as funções da classe EstoqueBO
 */
@RestController
@CrossOrigin(origins = "http://192.168.1.197:4200",maxAge = 3600)
@RequestMapping("/api/estoque")
public class EstoqueController {


    final EstoqueBO estoqueBO;

    public EstoqueController(EstoqueBO estoqueBO) {
        this.estoqueBO = estoqueBO;
    }

    @GetMapping
    public ResponseEntity<Object> pegarTodosOsDados() {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(estoqueBO.findAll());
        }
        catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("não foi possivel retornar seu estoque pois ocorreu uma falha no servidor");
        }
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<Object> pegarProdutoPeloId(@PathVariable Long id){
        try {
            return ResponseEntity.status(HttpStatus.OK).body(estoqueBO.getOne(id));
        }
        catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("não foi possivel retornar seu produto  pois ocorreu uma falha no servidor");
        }
    }

    @PutMapping
    public  ResponseEntity<Object > adicionarProduto(@RequestBody @Validated EstoqueDTO estoque) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(estoqueBO.save(estoque));
        }

        catch (NullPointerException e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("não foi possivel salvar seu produto pois não foi colocada informações sobre ele");
        }

        catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("não foi possivel salvar seu produto pois ocorreu uma falha no servidor");
        }
    }

    @PostMapping("/{id}")
    public ResponseEntity<Object> modificarProduto(@PathVariable Long id, @RequestBody EstoqueDTO estoqueDTO){
        try {
            return ResponseEntity.status(HttpStatus.OK).body(estoqueBO.update(id,estoqueDTO));
        }

        catch (NullPointerException e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("não foi possivel editar seu produto pois não foi colocada informações sobre ele");
        }

        catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("não foi possivel editar seu produto pois ocorreu uma falha no servidor");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deletarProduto(@PathVariable Long id){
        try {
            return ResponseEntity.status(HttpStatus.OK).body(estoqueBO.deleteById(id));
        }
        catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("não foi possivel deletar seu produto pois ocorreu uma falha no servidor");
        }
    }
}
