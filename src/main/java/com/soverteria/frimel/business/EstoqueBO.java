package com.soverteria.frimel.business;

import com.soverteria.frimel.modelos.dto.EstoqueDTO;
import com.soverteria.frimel.modelos.entity.Estoque;
import com.soverteria.frimel.repositorios.EstoqueRepositorio;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EstoqueBO {

    final EstoqueRepositorio estoqueRepositorio;

    public EstoqueBO(EstoqueRepositorio estoqueRepositorio) {
        this.estoqueRepositorio = estoqueRepositorio;
    }

    public Estoque save(EstoqueDTO estoqueDTO){

     if(estoqueDTO != null) {
         Estoque estoque = new Estoque();

         estoque.setProduto(estoqueDTO.getProduto());
         estoque.setQuantidade(estoqueDTO.getQuantidade());

         return estoqueRepositorio.save(estoque);
        }
         return null;
    }

    public List<Estoque> findAll(){
        return estoqueRepositorio.findAll();
    }

    public Estoque getOne(Long id){
      return estoqueRepositorio.getOne(id);
    }

    public Boolean deleteById(Long id){

        try {

            estoqueRepositorio.deleteById(id);
             return Boolean.TRUE;
        }
        catch (Exception e){
            return Boolean.FALSE;
        }
    }
    public Estoque update(Long id,EstoqueDTO estoqueDTO){

        if(id != null){

            Estoque estoque = getOne(id);

             estoque.setQuantidade(estoqueDTO.getQuantidade());
             estoque.setProduto(estoqueDTO.getProduto());

             return estoqueRepositorio.save(estoque);
        }
        return null;
    }
}
