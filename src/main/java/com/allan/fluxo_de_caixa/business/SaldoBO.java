package com.allan.fluxo_de_caixa.business;

import com.allan.fluxo_de_caixa.modelos.dto.Saldo;
import com.allan.fluxo_de_caixa.modelos.entity.Venda;
import com.allan.fluxo_de_caixa.modelos.entity.Despesa;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    VendaBO vendaBO;

    @Autowired
    DespesaBO despesaBO;

    private ArrayList<Saldo> saldos = new ArrayList<Saldo>();

    /**
     * Este metodo cria a lista de Saldos
     * @return ArrayList<Saldo>
     */
    public List<Saldo> criarSaldo() {

        List<Venda> debitosSomados = vendaBO.somarVendas();
        List<Despesa> despesasSomadas = despesaBO.somarDespesas();

        Venda venda;
        Despesa despesa;

         if(saldos.size() == 0) {

             for (int id = 0; id < despesasSomadas.size() && id < debitosSomados.size(); id++) {

                 despesa = despesasSomadas.get(id);
                 venda = debitosSomados.get(id);

                 Saldo saldo = new Saldo();

                 if (venda.getData().getYear() == despesa.getData().getYear() && venda.getData().getMonth().equals(despesa.getData().getMonth())) {

                     saldo.setDateTime(criarLocalDateTime(venda, saldo));
                     saldo.setValor(venda.getValor().subtract(despesa.getValor()));

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
     * @param venda
     * @param saldo
     * @return LocalDateTime
     */
    private LocalDateTime criarLocalDateTime(Venda venda, Saldo saldo) {

        LocalDate dataDoSaldo = venda.getData().toLocalDate();
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
    private List<Saldo> atualizarSaldos(List<Saldo> saldos, List<Venda> debitosSomados, List<Despesa> despesasSomadas){

        List<Venda> debitosSomadosAtual = vendaBO.somarVendas();
        List<Despesa> despesasSomadasAtual = despesaBO.somarDespesas();

        Despesa despesaAtual;
        Venda vendaAtual;

        Venda debitoAntes;
        Despesa despesaAntes;

        for(int id = 0;id < debitosSomados.size() && id < despesasSomadas.size();id++){

            despesaAtual = despesasSomadasAtual.get(id);
            despesaAntes = despesasSomadas.get(id);

            debitoAntes = debitosSomados.get(id);
            vendaAtual = debitosSomadosAtual.get(id);

            Saldo saldo = saldos.get(id);

            if(debitoAntes != vendaAtual && despesaAntes != despesaAtual)
            {
                if (vendaAtual.getData().getYear() == despesaAtual.getData().getYear() && vendaAtual.getData().getMonth().equals(despesaAtual.getData().getMonth())) {

                    saldo.setDateTime(criarLocalDateTime(vendaAtual, saldo));
                    saldo.setValor(vendaAtual.getValor().subtract(despesaAtual.getValor()));
                }

                else {
                    saldo.setValor(vendaAtual.getValor());
                    saldo.setDateTime(vendaAtual.getData());

                    saldo = saldos.get(id + 1);

                    saldo.setValor(despesaAtual.getValor().negate());
                    saldo.setDateTime(despesaAtual.getData());
                }
            }
        }
        return  saldos;
    }




}
