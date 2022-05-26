package com.soverteria.frimel.repositorios;

import com.soverteria.frimel.modelos.entity.Estoque;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EstoqueRepositorio extends JpaRepository<Estoque,Long> {
}
