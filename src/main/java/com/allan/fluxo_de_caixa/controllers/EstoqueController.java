package com.allan.fluxo_de_caixa.controllers;

import com.allan.fluxo_de_caixa.business.ProdutoBO;
import com.allan.fluxo_de_caixa.modelos.dto.ProdutoDTO;
import com.allan.fluxo_de_caixa.security.Filtros.JWTAutenticacao;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


/**
 * Esta classe executa as funções da classe ProdutoBO
 */
@RestController
@RequestMapping("/api/estoque")
public class EstoqueController {


    final ProdutoBO produtoBO;

    public EstoqueController(ProdutoBO produtoBO) {
        this.produtoBO = produtoBO;
    }

    @GetMapping
    public ResponseEntity<Object> pegarTodosOsDados(@RequestParam int pagina,@RequestParam  int tamanho) {
        try {
            Pageable page = PageRequest.of(pagina,tamanho);
            return ResponseEntity.status(HttpStatus.OK).body(produtoBO.findAllComPaginacao(page, JWTAutenticacao.usuario.getUsername()));
        }
        catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("não foi possivel retornar seu estoque pois ocorreu uma falha no servidor");
        }
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<Object> pegarProdutoPeloId(@PathVariable Long id){
        try {
            return ResponseEntity.status(HttpStatus.OK).body(produtoBO.getOne(id));
        }
        catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("não foi possivel retornar seu produto  pois ocorreu uma falha no servidor");
        }
    }

    @PutMapping
    public  ResponseEntity<Object > adicionarProduto(@RequestBody @Validated ProdutoDTO estoque) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(produtoBO.save(estoque));
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
    public ResponseEntity<Object> modificarProduto(@PathVariable Long id, @RequestBody ProdutoDTO produtoDTO){
        try {
            return ResponseEntity.status(HttpStatus.OK).body(produtoBO.update(id, produtoDTO));
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
            return ResponseEntity.status(HttpStatus.OK).body(produtoBO.deleteById(id));
        }
        catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("não foi possivel deletar seu produto pois ocorreu uma falha no servidor");
        }
    }
}
