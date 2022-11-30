package com.soverteria.frimel.business;

import com.soverteria.frimel.modelos.dto.EstoqueDTO;
import com.soverteria.frimel.modelos.entity.Debito;
import com.soverteria.frimel.modelos.entity.Estoque;
import com.soverteria.frimel.modelos.entity.Usuario;
import com.soverteria.frimel.repositorios.DebitoRepositorio;
import com.soverteria.frimel.repositorios.EstoqueRepositorio;
import com.soverteria.frimel.security.Filtros.JWTAutenticacao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * essa classe gerencia os produtos do estoque da empresa
 */
@Service
public class EstoqueBO {

     EstoqueRepositorio estoqueRepositorio;

    public EstoqueBO(EstoqueRepositorio estoqueRepositorio, DebitoRepositorio debitoRepositorio) {
        this.estoqueRepositorio = estoqueRepositorio;
    }

    Usuario usuario = JWTAutenticacao.usuario;
    /**
     * esse metodo serve para salvar um novo produto no estoque
     *
     * @param estoqueDTO
     * @return
     */
    public Estoque save(EstoqueDTO estoqueDTO) {

        if (estoqueDTO != null) {
            Estoque estoque = new Estoque();

            estoque.setProprietario(usuario.getUsername());
            estoque.setProduto(estoqueDTO.getProduto());
            estoque.setQuantidade(estoqueDTO.getQuantidade());
            estoque.setPreco(estoqueDTO.getPreco());

            return estoqueRepositorio.save(estoque);
        }
        return null;
    }

    /**
     * esse metodo serve para pegar todos os produtos do estoque
     *
     * @return
     */
    public List<Estoque> findAll() {
        List<Estoque> estoqueDoProprietario = new ArrayList<>();

        for(int id  = 0;id < estoqueRepositorio.findAll().size();id++){
            Estoque estoque = estoqueRepositorio.findAll().get(id);

            if(estoque.getProprietario().equals(usuario.getUsername())){
                estoqueDoProprietario.add(estoque);
            }
        }
        return estoqueDoProprietario;
    }

    /**
     * esse metodo serve para pegar um produto do estoque pelo id
     *
     * @param id
     * @return
     */
    public Estoque getOne(Long id) {
        return estoqueRepositorio.getOne(id);
    }

    /**
     * esse metodo serve para deletar um produto do estoque pelo id
     *
     * @param id
     * @return
     */
    public Boolean deleteById(Long id) {

        try {

            estoqueRepositorio.deleteById(id);
            return Boolean.TRUE;
        } catch (Exception e) {
            return Boolean.FALSE;
        }
    }

    /**
     * esse metodo serve para atualizar o estoque com os dados do usuario
     *
     * @param id
     * @param estoqueDTO
     * @return
     */
    public Estoque update(Long id, EstoqueDTO estoqueDTO) {

        if (id != null) {

            Estoque estoque = getOne(id);

            estoque.setQuantidade(estoqueDTO.getQuantidade());
            estoque.setProduto(estoqueDTO.getProduto());
            estoque.setPreco(estoqueDTO.getPreco());
            return estoqueRepositorio.save(estoque);
        }
        return null;
    }

    /**
     * - esse metodo serve para subtrair a quantidade do produto do estoque, com o debito que foi adicionado
     *
     * @param debito
     */
    public void subtrairQuantidadeDoProduto(Debito debito) {

        Estoque produto;
        List<Estoque> produtos =  estoqueRepositorio.findAll();
        for (int id = 0; id < estoqueRepositorio.findAll().size(); id++) {
            produto =  produtos.get(id);

            if (debito.getProdutoVendido().equals(produto.getProduto()) && produto.getProprietario().equals(usuario.getUsername())) {

                produto.setProduto(produto.getProduto());
                produto.setQuantidade(produto.getQuantidade() - debito.getQuantidade());
                estoqueRepositorio.save(produto);
            }

        }
    }

    /**
     * metodo que deve somar a quantidade do produto vendido do debito com a quantidade que tem no estoque ao deletar um debito
     * @param debito
     */
    public void aumentarQuantidadeDoProduto(Debito debito) {

        Estoque produto;
        List<Estoque> produtos;

        produtos =  estoqueRepositorio.findAll();
        for (int id = 0; id < estoqueRepositorio.findAll().size(); id++) {
            produto = produtos.get(id);

             if (debito.getProdutoVendido().equals(produto.getProduto()) && produto.getProprietario().equals(usuario.getUsername())) {

                produto.setProduto(produto.getProduto());
                produto.setQuantidade(produto.getQuantidade() + debito.getQuantidade());

                estoqueRepositorio.save(produto);
            }
        }
    }
}

