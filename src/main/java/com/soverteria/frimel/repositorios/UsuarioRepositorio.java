package com.soverteria.frimel.repositorios;

import com.soverteria.frimel.modelos.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public  interface UsuarioRepositorio extends JpaRepository<Usuario,Long> {
      Usuario findByUsername(String nomeUsuario);
}