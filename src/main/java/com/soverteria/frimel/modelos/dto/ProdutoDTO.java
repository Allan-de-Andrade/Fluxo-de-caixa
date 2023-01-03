package com.soverteria.frimel.modelos.dto;

import com.soverteria.frimel.modelos.entity.Insumo;

import java.math.BigDecimal;
import java.util.List;

/**
 * esta classe serve para receber os dados do Produto dado pelo usuario
 */
public class ProdutoDTO {

    private Long id;

    private String nome;

    private Integer quantidade;

    private BigDecimal preco;

    private List<Insumo> insumosUsados;

    public ProdutoDTO(String nome, Integer quantidade, BigDecimal preco, List<Insumo> insumosUsados) {
        this.nome = nome;
        this.quantidade = quantidade;
        this.preco = preco;
        this.insumosUsados = insumosUsados;
    }

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

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }

    public void setPreco(BigDecimal preco) {
        this.preco = preco;
    }

    public List<Insumo> getInsumosUsados() {
        return insumosUsados;
    }

    public void setInsumos(List<Insumo> insumosUsados) {
        this.insumosUsados = insumosUsados;
    }
}
