package com.soverteria.frimel.repositorios;

import com.soverteria.frimel.modelos.entity.Despesa;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Este repositorio define os dados de despesas do usuario.
 */
@Repository
public interface DespesaRepositorio  extends JpaRepository<Despesa,Long> {
    Page<Despesa> findByProprietario(org.springframework.data.domain.Pageable pageable, String proprietario);
    List<Despesa> findByProprietario(String proprietario);

}
