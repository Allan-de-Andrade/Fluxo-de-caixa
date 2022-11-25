package com.soverteria.frimel.modelos.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.soverteria.frimel.modelos.dto.UsuarioDTO;
import org.hibernate.annotations.Fetch;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "UsuarioTable", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"username"}),
        @UniqueConstraint(columnNames = {"senha"})
})
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Usuario implements Serializable {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY )
    private String senha;

    @Column
    private String fotoUsuario;

    @ManyToMany(fetch = FetchType.EAGER)
    private Collection<Autoridade> autoridade = new ArrayList<>();

    @ManyToMany(fetch = FetchType.EAGER)
    private ArrayList<Despesa> dadosDespesa = new ArrayList<Despesa>() {
    };

    @ManyToMany(fetch = FetchType.EAGER)
    private ArrayList<Debito> dadosDebito;

    @ManyToMany(fetch = FetchType.EAGER)
    private List<Estoque> dadosEstoque;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String nomeUsuario) {
        this.username = nomeUsuario;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getFotoUsuario() {
        return fotoUsuario;
    }

    public void setFotoUsuario(String fotoUsuario) {
        this.fotoUsuario = fotoUsuario;
    }

    public Collection<Autoridade> getAutoridade() {
        return autoridade;
    }

    public void setAutoridade(Collection<Autoridade> autoridade) {
        this.autoridade = autoridade;
    }

    public List<Despesa> getDadosDespesa() {
        return dadosDespesa;
    }


    public List<Debito> getDadosDebito() {
        return dadosDebito;
    }


    public List<Estoque> getDadosEstoque() {
        return dadosEstoque;
    }

    public void setDadosEstoque(List<Estoque> dadosEstoque) {
        this.dadosEstoque = dadosEstoque;
    }
}


