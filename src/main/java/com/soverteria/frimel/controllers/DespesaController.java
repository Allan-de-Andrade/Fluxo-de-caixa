package com.soverteria.frimel.controllers;

import com.auth0.jwt.JWT;
import com.soverteria.frimel.business.DespesaBO;
import com.soverteria.frimel.modelos.entity.Despesa;
import com.soverteria.frimel.modelos.dto.DespesaDTO;
import com.soverteria.frimel.security.Filtros.JWTAutenticacao;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * esta classe executa os metodos da classe DespesaBO
 *
 */

@RestController
@RequestMapping("/api/despesas")
public class DespesaController extends Despesa {

        final DespesaBO despesaBO;

        public DespesaController(DespesaBO despesaBO) {

            this.despesaBO = despesaBO;
        }

        @GetMapping
        public ResponseEntity<Object> listarDespesas(@RequestParam int pagina,@RequestParam int tamanho){

            try {
                PageRequest page = PageRequest.of(pagina, tamanho);
                return ResponseEntity.status(HttpStatus.OK).body(despesaBO.listarDespesas(page, JWTAutenticacao.usuario.getUsername()));
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