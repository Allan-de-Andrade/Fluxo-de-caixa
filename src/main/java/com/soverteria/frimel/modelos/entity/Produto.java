package com.soverteria.frimel.modelos.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.stereotype.Controller;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * esta classe armazena todos os dados do estoque
 */

@Entity
@Table(name="estoque_table")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Produto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false) //coluna que serve para identificar de qual usuario é esse produto
    private String proprietario = "";

    @Column(nullable = false,unique = true,length = 70)
    private String nome;

    @Column(nullable = false)
    private Integer quantidade;

    @Column(nullable = false)
    private BigDecimal preco =  new BigDecimal(0.0);

    @OneToMany(fetch = FetchType.EAGER)
    private List<Insumo> insumosUsados = new ArrayList<>();

    public BigDecimal getPreco() {
        return preco;
    }

    public void setPreco(BigDecimal preco) {
        this.preco = preco;
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getProprietario(){
        return proprietario;
    }

    public void setProprietario(String proprietario){
     this.proprietario= proprietario;
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

    public List<Insumo> getInsumosUsados() {
        return insumosUsados;
    }

    public void setInsumosUsados(List<Insumo> insumosUsados) {
        this.insumosUsados = insumosUsados;
    }
}
