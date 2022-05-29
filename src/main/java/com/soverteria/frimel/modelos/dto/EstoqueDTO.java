package com.soverteria.frimel.modelos.dto;

import java.math.BigDecimal;

/**
 * esta classe serve para receber os dados do Estoque dado pelo usuario
 */
public class EstoqueDTO {

    private Long id;

    private String produto;

    private Integer quantidade;

    private BigDecimal preco;

    public BigDecimal getPreco() {
        return preco;
    }

    public void setPreço(BigDecimal preco) {
        this.preco = preco;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProduto() {
        return produto;
    }

    public void setProduto(String produto) {
        this.produto = produto;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }
}
