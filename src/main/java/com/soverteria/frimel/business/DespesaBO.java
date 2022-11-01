package com.soverteria.frimel.business;

import com.soverteria.frimel.modelos.dto.DespesaDTO;
import com.soverteria.frimel.modelos.entity.Despesa;
import com.soverteria.frimel.repositorios.DespesaRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Esta classe gerencia  todas as despesas da empresa
 */
@Service
public class DespesaBO {

    @Autowired
     DespesaRepositorio despesaRepositorio;
    /**
     * este metodo serve para mostrar todas as despesas
     * @return
     */
    public List<Despesa> findAll(){
        return despesaRepositorio.findAll();
    }

    /**
     * esse metodo serve para pegar a despesa pelo id
     * @param id
     * @return
     */
    public Despesa getOne(Long id){
        return despesaRepositorio.getOne(id);
    }

    /**
     * esse metodo serve para salvar uma nova despesa
     * @param despesa
     * @return
     */
    public Despesa save(DespesaDTO despesa){

        if(despesa != null){
            Despesa despesas = new Despesa();

            despesas.setDescricao(despesa.getDescricao());
            despesas.setValor(despesa.getValor());
            despesas.setData(criarLocalDate(despesa.getData()));

            return despesaRepositorio.save(despesas);
        }

        return null;
    }
    List<Despesa>despesasOrdenadas;
    public ArrayList<Despesa>addValueOfExpensesByMesAndYear(){

        Sort ordenar = Sort.by("data").ascending();
        despesasOrdenadas = despesaRepositorio.findAll(ordenar);
        ArrayList<Despesa> despesasSomadas = new ArrayList<>();
        Despesa despesa = despesasOrdenadas.get(0);

        int i = 0;
        do{
            Despesa despesaSomar = despesasOrdenadas.get(i);

                if(despesa.getData().getYear() == despesaSomar.getData().getYear()){

                   if(despesa.getData().getMonth() == despesaSomar.getData().getMonth()) {
                       despesa.setValor(despesa.getValor().add(despesaSomar.getValor()));
                   }
                   else if(despesa.getData().getMonth() != despesaSomar.getData().getMonth()) {
                       despesasSomadas.add(despesa);
                       despesa = despesaSomar;
                   }
                }
                else if(despesa.getData().getYear() != despesaSomar.getData().getYear()) {
                    despesasSomadas.add(despesa);
                    despesa = despesaSomar;

                }
                if(i + 1 == despesasOrdenadas.size()){
                    Despesa ultimaDespesa = despesasOrdenadas.get(i);
                    despesasSomadas.add(ultimaDespesa);
                }

                 i++;
            }
     while (i < despesasOrdenadas.size());
        return  despesasSomadas;
    }

    /**
     * este metodo serve para atualizar uma despesa
     * @param id
     * @param despesaDTO
     * @return
     */
   public Despesa update(Long id,DespesaDTO despesaDTO){


     if(id != null) {
         Despesa despesa = getOne(id);

         despesa.setDescricao(despesaDTO.getDescricao());
         despesa.setData(criarLocalDate(despesaDTO.getData()));
         despesa.setValor(despesaDTO.getValor());

         despesaRepositorio.save(despesa);
     }
     return null;
   }

    /**
     * este metodo cria um novo LocalDateTime
     * @param data
     * @return
     */
    private LocalDateTime criarLocalDate(String data) {

        if(data != null){

            LocalDateTime dateTime = LocalDate.parse(data).atStartOfDay();

            LocalTime horario = LocalTime.now();
            LocalDate date = dateTime.toLocalDate();

            dateTime = date.atTime(horario);

            return dateTime;
        }
        return null;
    }

    /**
     * este metodo deleta uma despesa pelo id
     * @param id
     * @return
     */
    public Boolean deleteById(Long id){

        try {
            despesaRepositorio.deleteById(id);

            return  Boolean.TRUE;
        }
        catch (Exception e) {
            return  Boolean.FALSE;
        }
    }
}
