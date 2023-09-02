package com.allan.fluxo_de_caixa.modelos.entity;

import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;

/**
 * Classe que define os dados de autoridade dado ao usuario
 */
@Entity
@Table(name = "autoridade_table")
public class Autoridade implements GrantedAuthority {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String nome;


    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    @Override
    public String getAuthority() {
        return this.nome;
    }
}
