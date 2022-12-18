package com.soverteria.frimel.business;

import com.soverteria.frimel.modelos.dto.Saldo;
import com.soverteria.frimel.modelos.entity.Debito;
import com.soverteria.frimel.modelos.entity.Despesa;
import com.soverteria.frimel.repositorios.DebitoRepositorio;
import com.soverteria.frimel.repositorios.DespesaRepositorio;
import net.bytebuddy.TypeCache;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
     DebitoBO debitoBO;

    @Autowired
    DespesaBO despesaBO;

    private ArrayList<Saldo> saldos = new ArrayList<Saldo>();

    /**
     * Este metodo cria a lista de Saldos
     * @return ArrayList<Saldo>
     */
    public ArrayList<Saldo> criarSaldo() {

        ArrayList<Debito> debitosSomados = debitoBO.somarDebitos();
        ArrayList<Despesa> despesasSomadas = despesaBO.somarDespesas();

        Debito debito;
        Despesa despesa;

         if(saldos.size() == 0) {

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
                     saldo = new Saldo();

                     saldo.setValor(despesa.getValor().negate());
                     saldo.setDateTime(despesa.getData());
                     saldos.add(saldo);
                 }
             }
         }

        else {
             atualizarSaldos(saldos, debitosSomados, despesasSomadas);
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

    /**
     * este metodo atualiza a lista de saldos
     * @param saldos
     * @param debitosSomados
     * @param despesasSomadas
     * @return ArrayList<Saldo>
     */
    private ArrayList<Saldo> atualizarSaldos(ArrayList<Saldo> saldos, ArrayList<Debito> debitosSomados, ArrayList<Despesa> despesasSomadas){

        ArrayList<Debito> debitosSomadosAtual = debitoBO.somarDebitos();
        ArrayList<Despesa> despesasSomadasAtual = despesaBO.somarDespesas();

        Despesa despesaAtual;
        Debito debitoAtual;

        Debito debitoAntes;
        Despesa despesaAntes;

        for(int id = 0;id < debitosSomados.size() && id < despesasSomadas.size();id++){

            despesaAtual = despesasSomadasAtual.get(id);
            despesaAntes = despesasSomadas.get(id);

            debitoAntes = debitosSomados.get(id);
            debitoAtual = debitosSomadosAtual.get(id);

            Saldo saldo = saldos.get(id);

            if(debitoAntes != debitoAtual && despesaAntes != despesaAtual)
            {
                if (debitoAtual.getData().getYear() == despesaAtual.getData().getYear() && debitoAtual.getData().getMonth().equals(despesaAtual.getData().getMonth())) {

                    saldo.setDateTime(criarLocalDateTime(debitoAtual, saldo));
                    saldo.setValor(debitoAtual.getValor().subtract(despesaAtual.getValor()));
                }

                else {
                    saldo.setValor(debitoAtual.getValor());
                    saldo.setDateTime(debitoAtual.getData());

                    saldo = saldos.get(id + 1);

                    saldo.setValor(despesaAtual.getValor().negate());
                    saldo.setDateTime(despesaAtual.getData());
                }
            }
        }

        return  saldos;
    }




}
