package com.soverteria.frimel.controllers;

import com.soverteria.frimel.business.DespesaBO;
import com.soverteria.frimel.modelos.entity.Despesa;
import com.soverteria.frimel.modelos.dto.DespesaDTO;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * esta classe executa os metodos da classe DespesaBO
 *
 */

@RestController
@RequestMapping("/despesas")
public class DespesaController extends Despesa {

        final DespesaBO despesaBo;

        public DespesaController(DespesaBO despesaBo) {

            this.despesaBo = despesaBo;
        }

        @GetMapping
        public List<Despesa> pegarTodosOsDados() {
            return  despesaBo.findAll();
        }

        @GetMapping(value = "/{id}")
        public Despesa pegarDespesaPeloId(@PathVariable Long id) {
            return despesaBo.getOne(id);
       }

      @PutMapping
      public Despesa adicionarDespesa(@RequestBody DespesaDTO despesas) {

            return despesaBo.save(despesas);
        }

      @PostMapping("/{id}")
      public Despesa modificarDespesa(@PathVariable Long id,@RequestBody DespesaDTO despesa) {

            return despesaBo.update(id,despesa);
      }

      @DeleteMapping("/{id}")
      public void deletarDespesas(@PathVariable Long id) {
          despesaBo.deleteById(id);
      }
}