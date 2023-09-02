package com.allan.fluxo_de_caixa.repositorios;

import com.allan.fluxo_de_caixa.modelos.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Este repositorio gerencia os dados do usuario.
 */
@Repository
public  interface UsuarioRepositorio extends JpaRepository<Usuario,Long> {
      Usuario findByUsername(String nomeUsuario);
}
