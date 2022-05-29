package com.soverteria.frimel.business;

import com.soverteria.frimel.modelos.dto.DebitoDTO;
import com.soverteria.frimel.modelos.dto.EstoqueDTO;
import com.soverteria.frimel.modelos.entity.Debito;
import com.soverteria.frimel.modelos.entity.Estoque;
import com.soverteria.frimel.repositorios.DebitoRepositorio;
import com.soverteria.frimel.repositorios.EstoqueRepositorio;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EstoqueBO {

    final EstoqueRepositorio estoqueRepositorio;
    final DebitoRepositorio debitoRepositorio;

    public EstoqueBO(EstoqueRepositorio estoqueRepositorio,DebitoRepositorio debitoRepositorio) {
        this.estoqueRepositorio = estoqueRepositorio;
        this.debitoRepositorio = debitoRepositorio;
    }

    public Estoque save(EstoqueDTO estoqueDTO){

     if(estoqueDTO != null) {
         Estoque estoque = new Estoque();

         estoque.setProduto(estoqueDTO.getProduto());
         estoque.setQuantidade(estoqueDTO.getQuantidade());
         estoque.setPreco(estoqueDTO.getPreco());

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

    /**
     *  - esse metodo serve para subtrair a quantidade do produto do estoque, com o debito que foi adicionado
     * @param debito
     */
    public void subtrairQuantidadeDoProduto(Debito debito){

        Estoque estoque;
          for(long id = 1;id <= debitoRepositorio.findAll().size();id++){
              estoque = estoqueRepositorio.findById(id).get();

              if(debito.getProdutoVendido().equals(estoque.getProduto())){

                  estoque.setProduto(estoque.getProduto());
                  estoque.setQuantidade(estoque.getQuantidade() - debito.getQuantidade());

                   estoqueRepositorio.save(estoque);
              }
          }
    }
}
