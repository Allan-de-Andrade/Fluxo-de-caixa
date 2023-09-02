package com.allan.fluxo_de_caixa.business;

import com.allan.fluxo_de_caixa.modelos.dto.ProdutoDTO;
import com.allan.fluxo_de_caixa.modelos.entity.Insumo;
import com.allan.fluxo_de_caixa.modelos.entity.Venda;
import com.allan.fluxo_de_caixa.modelos.entity.Produto;
import com.allan.fluxo_de_caixa.modelos.entity.Usuario;
import com.allan.fluxo_de_caixa.repositorios.VendaRepositorio;
import com.allan.fluxo_de_caixa.repositorios.ProdutoRepositorio;
import com.allan.fluxo_de_caixa.security.Filtros.JWTAutenticacao;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

/**
 * Esta classe é um serviço para produtos
 */
@Service
public class ProdutoBO {

     private final ProdutoRepositorio produtoRepositorio;
     private final VendaRepositorio vendaRepositorio;

    public ProdutoBO(ProdutoRepositorio produtoRepositorio, VendaRepositorio vendaRepositorio) {
        this.produtoRepositorio = produtoRepositorio;
        this.vendaRepositorio = vendaRepositorio;
    }

    /**
     * Variavel usado para definir o proprietario deste produto
     */
    Usuario usuario = JWTAutenticacao.usuario;

    /**
     * esse metodo serve para salvar um novo produto no estoque
     *
     * @param produtoDTO
     * @return
     */
    public Produto save(ProdutoDTO produtoDTO) {

        if (produtoDTO != null) {
            Produto produto = new Produto();

            produto.setProprietario(usuario.getUsername());
            produto.setNome(produtoDTO.getNome());
            produto.setQuantidade(produtoDTO.getQuantidade());
            produto.setPreco(definirValorDoProduto(produto,produto.getInsumosUsados()));
            produto.setInsumosUsados(produtoDTO.getInsumosUsados());

            return produtoRepositorio.save(produto);
        }
        return null;
    }

    /**
     * metodo usado para definir o preço dos produtos com os insumos
     * @param produto
     * @param insumos
     * @return
     */
    public BigDecimal definirValorDoProduto(Produto produto,List<Insumo>insumos){

      for(int index =0;index < insumos.size();index++){
          int indexRapido = index + 1;
          Insumo insumo = insumos.get(index);
          Insumo insumoSomar = (indexRapido == insumos.size())?new Insumo():insumos.get(indexRapido);

          if(!insumoSomar.getNome().equals("")){
              insumo.setValor(insumo.getValor().add(insumoSomar.getValor()));
              produto.setPreco(insumo.getValor());
          }
      }

      BigDecimal preco =  new BigDecimal(produto.getPreco().toString());
      return preco;
    }

    /**
     * esse metodo serve para pegar todos os produtos do estoque do usuario com paginação
     *
     * @return
     */
    public Page<Produto> findAllComPaginacao(Pageable pageable,String proprietario) {
      return produtoRepositorio.findByProprietario(pageable,proprietario);
    }

    public List<Produto> findAllSemPaginacao() {
        return produtoRepositorio.findByProprietario(JWTAutenticacao.usuario.getUsername());
    }

    /**
     * esse metodo serve para pegar um produto do estoque pelo id
     *
     * @param id
     * @return
     */
    public Produto getOne(Long id) {
        return produtoRepositorio.getOne(id);
    }

    /**
     * esse metodo serve para deletar um produto do estoque pelo id
     *
     * @param id
     * @return
     */
    public Boolean deleteById(Long id) {

        try {
            Produto produto = produtoRepositorio.getOne(id);
            List<Venda> vendas = vendaRepositorio.findByProprietario(JWTAutenticacao.usuario.getUsername());
            Boolean podeDeletar = true;

            for (Venda venda : vendas) {

                if (venda.getProdutoVendido().equals(produto.getNome())) {
                    podeDeletar = false;
                    System.out.println("não é possivel deletar esse produto pois uma venda estar usando ele");
                }
             }

            if(podeDeletar) {
                produtoRepositorio.deleteById(id);
                return Boolean.TRUE;
            }
            else
                return Boolean.FALSE;
        }

        catch (Exception e){
            e.printStackTrace();
            return Boolean.FALSE;
        }
    }

    /**
     * esse metodo serve para atualizar o estoque com os dados do usuario
     *
     * @param id
     * @param produtoDTO
     * @return
     */
    public Produto update(Long id, ProdutoDTO produtoDTO) {

        if (id != null) {

            Produto produto = getOne(id);

            produto.setQuantidade(produtoDTO.getQuantidade());
            produto.setNome(produtoDTO.getNome());
            produto.setPreco(produtoDTO.getPreco());
            produto.setInsumosUsados(produtoDTO.getInsumosUsados());

            return produtoRepositorio.save(produto);
        }
        return null;
    }

    /**
     * - esse metodo serve para subtrair a quantidade do produto do estoque, com o venda que foi adicionado
     *
     * @param venda
     */
    public void subtrairQuantidadeDoProduto(Venda venda) {

        Produto produto;
        List<Produto> produtos =  produtoRepositorio.findByProprietario(JWTAutenticacao.usuario.getUsername());
        for (int id = 0; id < produtoRepositorio.findAll().size(); id++) {
            produto =  produtos.get(id);

            if (venda.getProdutoVendido().equals(produto.getNome())) {

                produto.setQuantidade(produto.getQuantidade() - venda.getQuantidade());
                produtoRepositorio.save(produto);
            }

        }
    }

    /**
     * metodo que deve somar a quantidade do produto vendido do venda com a quantidade que tem no estoque ao deletar um venda
     * @param venda
     */
    public void aumentarQuantidadeDoProduto(Venda venda) {

        Produto produto;
        List<Produto> produtos;

        produtos =  produtoRepositorio.findByProprietario(JWTAutenticacao.usuario.getUsername());
        for (int id = 0; id < produtoRepositorio.findAll().size(); id++) {
            produto = produtos.get(id);

             if (venda.getProdutoVendido().equals(produto.getNome())) {

                produto.setQuantidade(produto.getQuantidade() + venda.getQuantidade());

                produtoRepositorio.save(produto);
            }
        }
    }
}

