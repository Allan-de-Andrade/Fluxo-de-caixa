package com.soverteria.frimel.repositorios;

import com.soverteria.frimel.modelos.entity.Debito;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DebitoRepositorio extends JpaRepository<Debito,Long> {
}
