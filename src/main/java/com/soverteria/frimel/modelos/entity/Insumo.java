package com.soverteria.frimel.modelos.entity;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "insumos_table")
public class Insumo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false,unique =  true)
    private String nome = "";

    @Column(nullable = false)
    private BigDecimal valor = BigDecimal.ZERO;

    @Column(nullable = false) //essa coluna serve para indentificar de qual usuario é esse insumo
    private String indentificadorUsuario = "";

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
