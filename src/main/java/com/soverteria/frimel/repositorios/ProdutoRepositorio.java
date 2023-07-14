package com.soverteria.frimel.repositorios;

import com.soverteria.frimel.modelos.entity.Produto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Este repositorio gerencia os dados dos produtos referente aos usuarios.
 */
@Repository
public interface ProdutoRepositorio extends JpaRepository<Produto,Long> {
    Page<Produto> findByProprietario(Pageable pageable, String proprietario);
    List<Produto> findByProprietario(String proprietario);

}
