package com.soverteria.frimel.business;

import com.soverteria.frimel.modelos.dto.Saldo;
import com.soverteria.frimel.modelos.entity.Debito;
import com.soverteria.frimel.modelos.entity.Despesa;
import com.soverteria.frimel.repositorios.DebitoRepositorio;
import com.soverteria.frimel.repositorios.DespesaRepositorio;
import net.bytebuddy.TypeCache;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

/**
 * Esta classe contem os metodos para o saldo
 */
@Service
public class SaldoBO {

    final DespesaRepositorio despesaRepositorio;
    final DebitoRepositorio debitoRepositorio;

    public SaldoBO(DespesaRepositorio despesaRepositorio, DebitoRepositorio debitoRepositorio) {
        this.despesaRepositorio = despesaRepositorio;
        this.debitoRepositorio = debitoRepositorio;
    }

    /**
     * Este metodo serve para somar todas as Despesas e guardar em um ArrayList
     * @return ArrayList</Despesas>
     */
    private ArrayList<Despesa> somarDespesas() {

        ArrayList<Despesa> despesasSomadas = new ArrayList<Despesa>();
        Sort ordenar = Sort.by("data").descending();
        ;
        List<Despesa> todasAsDespesasOrdenadas = despesaRepositorio.findAll(ordenar);


        int id = 0;
        int idComparar = id + 1;

        do{

            Despesa despesa = todasAsDespesasOrdenadas.get(id);
            Despesa despesaComparada = todasAsDespesasOrdenadas.get(idComparar);

            if (despesa.getData().getMonth().equals(despesaComparada.getData().getMonth())) {

                despesa.setValor(despesa.getValor().add(despesaComparada.getValor()));
                 despesasSomadas.add(despesa);
            }

            else {
                despesasSomadas.add(despesa);
                id++;
            }
            idComparar++;
        }
         while (idComparar< despesaRepositorio.findAll().size());

        return despesasSomadas;
    }

    /**
     * Este metodo soma todos os Debitos e guarda em um ArrayList
     * @return ArrayList<Debito>
     */
    private ArrayList<Debito> somarDebitos() {

        ArrayList<Debito> debitosSomados = new ArrayList<Debito>();
        Sort ordenar = Sort.by("data").descending();
        List<Debito> todosOsDebitosOrdenardos = debitoRepositorio.findAll(ordenar);

        int id = 0;
        int idComparar = id + 1;

        do{

            Debito debito = todosOsDebitosOrdenardos.get(id);
            Debito debitoComparado = todosOsDebitosOrdenardos.get(idComparar);

            if (debito.getData().getMonth().equals(debitoComparado.getData().getMonth())) {

                debito.setValor(debito.getValor().add(debitoComparado.getValor()));


                if (debitoRepositorio.findAll().contains(debito.getData().getMonth()) == false && debitoRepositorio.findAll().contains(debito.getData().getYear()) == false) {
                    debitosSomados.add(debito);
                }
            }

            else {
                debitosSomados.add(debito);
                id++;
            }
            idComparar++;
        }

        while (idComparar<debitoRepositorio.findAll().size());

        return debitosSomados;
    }


    private ArrayList<Saldo> saldos = new ArrayList<Saldo>();

    /**
     * Este metodo cria a lista de Saldos
     * @return ArrayList<Saldo>
     */
    public ArrayList<Saldo> criarSaldo() {

        ArrayList<Debito> debitosSomados = somarDebitos();
        ArrayList<Despesa> despesasSomadas = somarDespesas();

        Debito debito;
        Despesa despesa;

            for (int id = 0; id < despesasSomadas.size() && id < debitosSomados.size(); id++) {

                despesa = despesasSomadas.get(id);
                debito = debitosSomados.get(id);

                Saldo saldo = new Saldo();

                if (debito.getData().getYear() == despesa.getData().getYear() && debito.getData().getMonth().equals(despesa.getData().getMonth())) {

                    saldo.setDateTime(criarLocalDateTime(debito, saldo));
                    saldo.setValor(debito.getValor().subtract(despesa.getValor()));

                    saldos.add(saldo);
                }

                else {
                    saldo.setValor(debito.getValor());
                    saldo.setDateTime(debito.getData());
                    saldos.add(saldo);

                    saldo = new Saldo();

                    saldo.setValor(despesa.getValor().negate());
                    saldo.setDateTime(despesa.getData());
                    saldos.add(saldo);
                }
            }

        return saldos;
    }

    /**
     * metodo que cria o LocalDateTime do Saldo
     * @param debito
     * @param saldo
     * @return LocalDateTime
     */
    private LocalDateTime criarLocalDateTime(Debito debito, Saldo saldo) {

        LocalDate dataDoSaldo = debito.getData().toLocalDate();
        LocalTime horarioCriado = LocalTime.now();

        LocalDateTime dateTime = dataDoSaldo.atTime(horarioCriado);
        saldo.setDateTime(dateTime);
        return saldo.getDateTime();
    }
}