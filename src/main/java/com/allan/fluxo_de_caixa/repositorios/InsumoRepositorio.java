package com.allan.fluxo_de_caixa.repositorios;

import com.allan.fluxo_de_caixa.modelos.entity.Insumo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Este repositorio gerencia os dados de insumos dos usuarios..
 */
public interface InsumoRepositorio  extends JpaRepository<Insumo,Long> {

    Page<Insumo> findByIndentificadorUsuario(Pageable pageable,String identificadorUsuario);
    Insumo findByNome(String nome);
}
