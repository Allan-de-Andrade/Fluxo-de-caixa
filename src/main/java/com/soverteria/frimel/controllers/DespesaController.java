package com.soverteria.frimel.controllers;

import com.soverteria.frimel.business.DespesaBO;
import com.soverteria.frimel.modelos.entity.Despesa;
import com.soverteria.frimel.modelos.dto.DespesaDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Null;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * esta classe executa os metodos da classe DespesaBO
 *
 */

@RestController
@CrossOrigin(origins = "http://192.168.1.197:4200/",maxAge = 3600)
@RequestMapping("/api/despesas")
public class DespesaController extends Despesa {

        final DespesaBO despesaBO;

        public DespesaController(DespesaBO despesaBO) {

            this.despesaBO = despesaBO;
        }

        @GetMapping
        public ResponseEntity<Object> pegarTodosOsDados(){

            try {
                return ResponseEntity.status(HttpStatus.OK).body(despesaBO.findAll());
            }

            catch (Exception e) {
                e.printStackTrace();
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro 500: Ocorreu um erro no servidor ao tentar pegar suas despesas");
            }
        }


        @GetMapping(value = "/{id}")
        public ResponseEntity<Object> pegarDespesaPeloId(@PathVariable Long id) {

            try {
                return ResponseEntity.status(HttpStatus.OK).body(despesaBO.getOne(id));
            }

            catch (Exception e) {
                e.printStackTrace();
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro 500: Ocorreu um erro no servidor ao tentar pegar a despesa");
            }
        }

       @GetMapping(value = "/somadas")
       public ResponseEntity<Object> despesasSomadas() {

           try {
               return ResponseEntity.status(HttpStatus.OK).body(despesaBO.somarDespesas());
           }

           catch (Exception e) {
               e.printStackTrace();
               return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro 500: Ocorreu um erro ao tentar acessar suas despesas somadas");
           }
       }

      @PutMapping
      public ResponseEntity<Object> adicionarDespesa(@RequestBody DespesaDTO despesaDTO) {

          try {
              return ResponseEntity.status(HttpStatus.OK).body(despesaBO.save(despesaDTO));
          }

         catch (NullPointerException e) {
             return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error 400: Não foi possivel salvar  pois faltou as informações da sua despesa");
         }

          catch (Exception e) {
              e.printStackTrace();
              return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro 500: Ocorreu um erro no servidor ao tentar salvar a despesa");
          }
      }

      @PostMapping("/{id}")
      public ResponseEntity<Object> modificarDespesa(@PathVariable Long id,@RequestBody DespesaDTO despesa) {

          try {
              return ResponseEntity.status(HttpStatus.OK).body(despesaBO.update(id,despesa));
          }

          catch (NullPointerException e) {
              return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error 400: Não foi possivel editar  pois faltou as informações da sua despesa");
          }

          catch (Exception e) {
              e.printStackTrace();
              return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro 500: Ocorreu um erro no servidor ao tentar editar a despesa");
          }
        }

      @DeleteMapping("/{id}")
      public ResponseEntity<Object> deletarDespesas(@PathVariable Long id) {
          try {
              return ResponseEntity.status(HttpStatus.OK).body(despesaBO.deleteById(id));
          }

          catch (Exception e) {
              e.printStackTrace();
              return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro 500: Ocorreu um erro no servidor ao tentar editar a despesa");
          }
      }
}