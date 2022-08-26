package com.soverteria.frimel.controllers;

import com.soverteria.frimel.business.EstoqueBO;
import com.soverteria.frimel.modelos.dto.EstoqueDTO;
import com.soverteria.frimel.modelos.entity.Estoque;
import com.soverteria.frimel.repositorios.EstoqueRepositorio;
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
    public List<Estoque> pegarTodosOsDados()
    {
        return  estoqueBO.findAll();
    }

    @GetMapping(value = "/{id}")
    public Estoque pegarProdutoPeloId(@PathVariable Long id){
        return estoqueBO.getOne(id);
    }

    @PutMapping
    public  Estoque adicionarProduto(@RequestBody @Validated EstoqueDTO estoque)
    {
        return estoqueBO.save(estoque);
    }

    @PostMapping("/{id}")
    public Estoque modificarProduto(@PathVariable Long id, @RequestBody EstoqueDTO estoqueDTO){
     return  estoqueBO.update(id, estoqueDTO);
    }

    @DeleteMapping("/{id}")
    public void deletarProduto(@PathVariable Long id){
        estoqueBO.deleteById(id);
    }
}
