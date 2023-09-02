package com.allan.fluxo_de_caixa.controllers;

import com.allan.fluxo_de_caixa.business.SaldoBO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


/**
 * Esta classe gerencia os metodos do saldo da empresa
 */
@RestController
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
