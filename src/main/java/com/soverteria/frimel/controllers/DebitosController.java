package com.soverteria.frimel.controllers;

import com.soverteria.frimel.business.DebitoBO;
import com.soverteria.frimel.modelos.dto.DebitoDTO;
import com.soverteria.frimel.modelos.entity.Debito;
import com.soverteria.frimel.repositorios.DebitoRepositorio;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/debitos")
public class DebitosController {

    final DebitoBO debitoBO;

    public DebitosController(DebitoBO debitoBO) {
        this.debitoBO = debitoBO;
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
            return debitoBO.save(debitoDTO);
        }

        @PostMapping("/{id}")
        public Debito modificarGanho(@PathVariable Long id, @RequestBody DebitoDTO debitoDTO){

            return debitoBO.update(id,debitoDTO);
        }

        @DeleteMapping("/{id}")
        public void deletarGanho(@PathVariable Long id){
        debitoBO.deleteById(id);
        }

    }

