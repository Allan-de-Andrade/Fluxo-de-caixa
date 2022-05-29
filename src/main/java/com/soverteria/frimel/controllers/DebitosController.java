package com.soverteria.frimel.controllers;

import com.soverteria.frimel.business.DebitoBO;
import com.soverteria.frimel.business.EstoqueBO;
import com.soverteria.frimel.modelos.dto.DebitoDTO;
import com.soverteria.frimel.modelos.entity.Debito;
import com.soverteria.frimel.modelos.entity.Estoque;
import com.soverteria.frimel.repositorios.DebitoRepositorio;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/debitos")
public class DebitosController {

    final DebitoBO debitoBO;
    final EstoqueBO estoqueBO;
    public DebitosController(DebitoBO debitoBO,EstoqueBO estoqueBO) {
        this.debitoBO = debitoBO;
        this.estoqueBO = estoqueBO;
    }


    @GetMapping
        public List<Debito> pegarTodosOsDados()
        {
            return  debitoBO.findAll();
        }

        @GetMapping(value = "/{id}")
        public Debito pegarGanhoPeloId(@PathVariable Long id){
            return  debitoBO.getOne(id);
        }

        @PutMapping
        public Debito adicionarGanho(@RequestBody DebitoDTO debitoDTO)
        {

            for(long id = 1;id <= estoqueBO.findAll().size();id++) {
                Estoque estoque = estoqueBO.getOne(id);

                if (estoque.getProduto().equals(debitoDTO.getProdutoVendido()) && estoque != null)
                    return debitoBO.save(debitoDTO, estoque);

                else
                       System.out.println("não existe esse produto no estoque");
            }

            return  null;
        }

        @PostMapping("/{id}")
        public Debito modificarGanho(@PathVariable Long id, @RequestBody DebitoDTO debitoDTO){

            Estoque estoque = estoqueBO.getOne(id);
            return debitoBO.update(id,debitoDTO,estoque);
        }

        @DeleteMapping("/{id}")
        public void deletarGanho(@PathVariable Long id){
        debitoBO.deleteById(id);
        }

    }

