package com.soverteria.frimel.repositorios;

import com.soverteria.frimel.modelos.entity.Autoridade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AutoridadeRepositorio extends JpaRepository<Autoridade,Long> {
      Autoridade findByNome(String nome);
}
