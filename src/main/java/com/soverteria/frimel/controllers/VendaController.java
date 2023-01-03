package com.soverteria.frimel.controllers;

import com.soverteria.frimel.business.VendaBO;
import com.soverteria.frimel.business.ProdutoBO;
import com.soverteria.frimel.modelos.dto.VendaDTO;
import com.soverteria.frimel.modelos.entity.Produto;
import com.soverteria.frimel.modelos.entity.Usuario;
import com.soverteria.frimel.security.Filtros.JWTAutenticacao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * esta classe aplica os metodos do DebitoBO
 */
@RestController
@RequestMapping("/api/vendas")
public class VendaController {

    @Autowired
    VendaBO vendaBO;
    @Autowired
    ProdutoBO produtoBO;

      Usuario usuario = JWTAutenticacao.usuario;

        @GetMapping
        public ResponseEntity<Object> pegarTodosOsDados(@RequestParam int pagina,@RequestParam int tamanho)
        {
            try {
                PageRequest pageable =  PageRequest.of(pagina,tamanho);
                return ResponseEntity.status(HttpStatus.OK).body(vendaBO.listarDebitos(pageable,JWTAutenticacao.usuario.getUsername()));
            }
            catch (Exception e){
                 return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("não foi possivel retornar suas vendas pois ocorreu uma falha no servidor");
            }
        }

        @GetMapping("/somados")
        public ResponseEntity<Object> debitosSomados(){

            try {
                return ResponseEntity.status(HttpStatus.OK).body(vendaBO.somarVendas());
            }
            catch (Exception e){
                    e.printStackTrace();
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("não foi possivel retornar suas vendas somadas pois ocorreu uma falha no servidor");
            }
        }


        @GetMapping(value = "/{id}")
        public ResponseEntity<Object> pegarGanhoPeloId(@PathVariable Long id){
            try {
                return ResponseEntity.status(HttpStatus.OK).body(vendaBO.getOne(id));
            }
            catch (Exception e){
                e.printStackTrace();
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("não foi possivel retornar sua venda  pois ocorreu uma falha no servidor");
            }
        }

        @PutMapping
        public ResponseEntity<Object> adicionarGanho(@RequestBody VendaDTO vendaDTO)
        {

            try {
                List<Produto> produtos = produtoBO.findAllSemPaginacao();

               if(usuario.getUsername() != null) {
                   for (int id = 0; id <= produtoBO.findAllSemPaginacao().size(); id++) {
                       Produto produto = produtos.get(id);

                       if (produto.getNome().equals(vendaDTO.getProdutoVendido()) && produto != null)
                           return ResponseEntity.status(HttpStatus.OK).body(vendaBO.save(vendaDTO, produto));

                       else
                           System.out.println("não existe esse produto no produto");
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
        public ResponseEntity<Object> modificarGanho(@PathVariable Long id, @RequestBody VendaDTO vendaDTO){

            try {
                Produto produto = new Produto();
                List<Produto> produtos = produtoBO.findAllSemPaginacao();

                for (int idProduto = 0; idProduto < produtos.size(); idProduto++) {
                    produto = produtos.get(idProduto);

                    if (!vendaDTO.getProdutoVendido().equals(produto.getNome())) {
                        produto = null;
                    }
                }

                return ResponseEntity.status(HttpStatus.OK).body(vendaBO.update(id, vendaDTO, produto));
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
                return ResponseEntity.status(HttpStatus.OK).body(vendaBO.deleteById(id));
            }
            catch (Exception e){
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("não foi possivel deletar sua venda pois ocorreu uma falha no servidor");
            }
        }

    }

