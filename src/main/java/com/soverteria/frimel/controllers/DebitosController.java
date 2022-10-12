package com.soverteria.frimel.controllers;

import com.soverteria.frimel.business.DebitoBO;
import com.soverteria.frimel.business.EstoqueBO;
import com.soverteria.frimel.modelos.dto.DebitoDTO;
import com.soverteria.frimel.modelos.entity.Debito;
import com.soverteria.frimel.modelos.entity.Estoque;
import com.soverteria.frimel.repositorios.DebitoRepositorio;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;


/**
 * esta classe aplica os metodos do DebitoBO
 */
@RestController
@CrossOrigin(origins = "http://192.168.1.197:4200",maxAge = 3600)
@RequestMapping("/api/debitos")
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

        @GetMapping("/somados")
        public ArrayList<Debito> debitosSomados(){
            return debitoBO.addValueOfSalesByMesAndYear();
        }

        @GetMapping(value = "/{id}")
        public Debito pegarGanhoPeloId(@PathVariable Long id){
            return  debitoBO.getOne(id);
        }

        @PutMapping
        public Debito adicionarGanho(@RequestBody DebitoDTO debitoDTO)
        {
             List<Estoque> produtos = estoqueBO.findAll();
            for(int id = 0;id <= estoqueBO.findAll().size();id++) {
                Estoque estoque =produtos.get(id);

                if (estoque.getProduto().equals(debitoDTO.getProdutoVendido()) && estoque != null)
                    return debitoBO.save(debitoDTO, estoque);

                else
                       System.out.println("não existe esse produto no estoque");
            }

            return  null;
        }

        @PostMapping("/{id}")
        public Debito modificarGanho(@PathVariable Long id, @RequestBody DebitoDTO debitoDTO){

            Estoque produto = new Estoque();
            List<Estoque> produtos = estoqueBO.findAll();

            for(int idProduto = 0;idProduto < produtos.size();idProduto++) {
                produto = produtos.get(idProduto);

                if (!debitoDTO.getProdutoVendido().equals(produto.getProduto()) && produto == null){
                    produto = null;
                }
            }

            return debitoBO.update(id,debitoDTO,produto);
        }

        @DeleteMapping("/{id}")
        public void deletarGanho(@PathVariable Long id){
           debitoBO.deleteById(id);
        }

    }

