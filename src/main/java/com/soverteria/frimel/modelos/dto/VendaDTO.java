package com.soverteria.frimel.modelos.dto;


/**
 * esta classe serve para receber os dados do Venda dado pelo usuario
 */
public class VendaDTO {

    private  Long id;

    private String produtoVendido;

    private Integer quantidade;

    private String data;

    public Integer getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }



    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProdutoVendido() {
        return produtoVendido;
    }

    public void setProdutoVendido(String produtoVendido) {
        this.produtoVendido = produtoVendido;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
