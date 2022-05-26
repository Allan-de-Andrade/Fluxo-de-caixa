package com.soverteria.frimel.repositorios;

import com.soverteria.frimel.modelos.entity.Despesa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface DespesaRepositorio  extends JpaRepository<Despesa,Long> {

}
