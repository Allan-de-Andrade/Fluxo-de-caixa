package com.soverteria.frimel.controllers;

import com.soverteria.frimel.business.SaldoBO;
import com.soverteria.frimel.modelos.dto.Saldo;
import com.soverteria.frimel.repositorios.DebitoRepositorio;
import com.soverteria.frimel.repositorios.DespesaRepositorio;
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

    @PutMapping
    public ArrayList<Saldo> gerarSaldo(){
        return saldoBO.criarSaldo();
    }
}
