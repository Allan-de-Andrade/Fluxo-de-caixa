package com.soverteria.frimel.repositorios;

import com.soverteria.frimel.modelos.entity.Venda;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface VendaRepositorio extends JpaRepository<Venda,Long> {

    Page<Venda> findByProprietario(Pageable pageable, String proprietario);
    List<Venda> findByProprietario(String proprietario);
}
