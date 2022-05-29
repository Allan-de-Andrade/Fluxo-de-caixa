package com.soverteria.frimel.controllers;

import com.soverteria.frimel.repositorios.DebitoRepositorio;
import com.soverteria.frimel.repositorios.DespesaRepositorio;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * Esta classe gerencia os saldo da empresa
 */
@RestController
@RequestMapping("/saldo")
public class SaldoController {

    final DespesaRepositorio despesaRepositorio;
    final DebitoRepositorio debitoRepositorio;

    SaldoController(DespesaRepositorio despesaRepositorio, DebitoRepositorio debitoRepositorio){
        this.despesaRepositorio = despesaRepositorio;
        this.debitoRepositorio = debitoRepositorio;
    }

    
}
