package com.soverteria.frimel.controllers;

import com.soverteria.frimel.business.SaldoBO;
import com.soverteria.frimel.modelos.dto.Saldo;
import com.soverteria.frimel.repositorios.DebitoRepositorio;
import com.soverteria.frimel.repositorios.DespesaRepositorio;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;


/**
 * Esta classe gerencia os metodos do saldo da empresa
 */
@RestController
@CrossOrigin(origins = "http://192.168.1.197:4200",maxAge = 3600)
@RequestMapping("/api/saldo")
public class SaldoController {

    final SaldoBO saldoBO;

    public SaldoController(SaldoBO saldoBO) {
        this.saldoBO = saldoBO;
    }

    @GetMapping
    public ResponseEntity gerarSaldo(){
        try {
            return ResponseEntity.status(HttpStatus.OK).body(saldoBO.criarSaldo());
        }
        catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ocorreu uma falha no servidor ao tentar criar o seu saldo");
        }
    }
}
