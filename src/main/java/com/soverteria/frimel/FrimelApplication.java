package com.soverteria.frimel;


import com.soverteria.frimel.business.UsuarioBO;
import com.soverteria.frimel.modelos.dto.UsuarioDTO;
import com.soverteria.frimel.modelos.entity.Autoridade;
import com.soverteria.frimel.modelos.entity.Usuario;
import com.soverteria.frimel.repositorios.AutoridadeRepositorio;
import com.soverteria.frimel.repositorios.UsuarioRepositorio;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class FrimelApplication {

    public static void main(String[] args) {
        SpringApplication.run(FrimelApplication.class, args);
    }
}



