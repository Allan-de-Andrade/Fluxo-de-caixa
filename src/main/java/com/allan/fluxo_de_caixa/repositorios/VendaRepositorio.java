package com.allan.fluxo_de_caixa.repositorios;

import com.allan.fluxo_de_caixa.modelos.entity.Venda;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repositorio gerencia os dados de vendas do usuario.
 */
@Repository
public interface VendaRepositorio extends JpaRepository<Venda,Long> {

    Page<Venda> findByProprietario(Pageable pageable, String proprietario);
    List<Venda> findByProprietario(String proprietario);
}
