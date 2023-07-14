package com.soverteria.frimel.modelos.dto;

import java.math.BigDecimal;

/**
 * Classe que recebe os dados do insumo e os define para a clase Insumo.
 */
public class InsumoDTO {

    private String nome;
    private BigDecimal valor;
    private String indentificadorUsuario;     //Essa coluna serve para indentificar qual usuario usa este insumo para mostrar os insumos desse usuario

    public InsumoDTO(String nome, BigDecimal valor, String indentificadorUsuario) {
        this.nome = nome;
        this.valor = valor;
        this.indentificadorUsuario = indentificadorUsuario;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public String getIndentificadorUsuario() {
        return indentificadorUsuario;
    }

    public void setIndentificadorUsuario(String indentificadorUsuario) {
        this.indentificadorUsuario = indentificadorUsuario;
    }
}
