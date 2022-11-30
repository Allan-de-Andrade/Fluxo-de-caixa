package com.soverteria.frimel.controllers;

import com.soverteria.frimel.business.DebitoBO;
import com.soverteria.frimel.business.EstoqueBO;
import com.soverteria.frimel.modelos.dto.DebitoDTO;
import com.soverteria.frimel.modelos.entity.Estoque;
import com.soverteria.frimel.modelos.entity.Usuario;
import com.soverteria.frimel.security.Filtros.JWTAutenticacao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * esta classe aplica os metodos do DebitoBO
 */
@RestController
@CrossOrigin(origins = "http://192.168.1.197:4200",maxAge = 3600)
@RequestMapping("/api/debitos")
public class DebitosController {

    @Autowired
     DebitoBO debitoBO;
    @Autowired
     EstoqueBO estoqueBO;

      Usuario usuario = JWTAutenticacao.usuario;

        @GetMapping
        public ResponseEntity<Object> pegarTodosOsDados()
        {
            try {
                return ResponseEntity.status(HttpStatus.OK).body(debitoBO.findAll());
            }
            catch (Exception e){
                e.printStackTrace();
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("não foi possivel retornar suas vendas pois ocorreu uma falha no servidor");
            }
        }

        @GetMapping("/somados")
        public ResponseEntity<Object> debitosSomados(){
                try {
                return ResponseEntity.status(HttpStatus.OK).body(debitoBO.addValueOfSalesByMesAndYear());
            }
            catch (Exception e){
                    e.printStackTrace();
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("não foi possivel retornar suas vendas somadas pois ocorreu uma falha no servidor");
            }
        }


        @GetMapping(value = "/{id}")
        public ResponseEntity<Object> pegarGanhoPeloId(@PathVariable Long id){
            try {
                return ResponseEntity.status(HttpStatus.OK).body(debitoBO.getOne(id));
            }
            catch (Exception e){
                e.printStackTrace();
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("não foi possivel retornar sua venda  pois ocorreu uma falha no servidor");
            }
        }

        @PutMapping
        public ResponseEntity<Object> adicionarGanho(@RequestBody DebitoDTO debitoDTO)
        {

            try {
                List<Estoque> produtos = estoqueBO.findAll();

               if(usuario.getUsername() != null) {
                   for (int id = 0; id <= estoqueBO.findAll().size(); id++) {
                       Estoque estoque = produtos.get(id);

                       if (estoque.getProduto().equals(debitoDTO.getProdutoVendido()) && estoque != null)
                           return ResponseEntity.status(HttpStatus.OK).body(debitoBO.save(debitoDTO, estoque));

                       else
                           System.out.println("não existe esse produto no estoque");
                   }

               }
               else
                   return ResponseEntity.status(HttpStatus.FORBIDDEN).body("você não estar autorizado a usar esse serviço");
            }

            catch (NullPointerException e) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error 400: Não foi possivel salvar  pois faltou as informações da sua venda");
            }

            catch (Exception e) {
                e.printStackTrace();
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro 500: Ocorreu um erro no servidor ao tentar salvar a venda");
            }

            return  null;
        }

        @PostMapping("/{id}")
        public ResponseEntity<Object> modificarGanho(@PathVariable Long id, @RequestBody DebitoDTO debitoDTO){

            try {
                Estoque produto = new Estoque();
                List<Estoque> produtos = estoqueBO.findAll();

                for (int idProduto = 0; idProduto < produtos.size(); idProduto++) {
                    produto = produtos.get(idProduto);

                    if (!debitoDTO.getProdutoVendido().equals(produto.getProduto()) && produto == null) {
                        produto = null;
                    }
                }

                return ResponseEntity.status(HttpStatus.OK).body(debitoBO.update(id, debitoDTO, produto));
            }

            catch (NullPointerException e){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("não foi possivel editar sua venda pois faltou informação sobre ela");
            }
             catch (Exception e){
                 return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("não foi possivel editar sua venda pois ocorreu uma falha no servidor");
             }
        }

        @DeleteMapping("/{id}")
        public ResponseEntity<Object> deletarGanho(@PathVariable Long id){

            try {
                return ResponseEntity.status(HttpStatus.OK).body(debitoBO.deleteById(id));
            }
            catch (Exception e){
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("não foi possivel deletar sua venda pois ocorreu uma falha no servidor");
            }
        }

    }

