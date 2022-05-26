package com.soverteria.frimel.business;

import com.soverteria.frimel.modelos.dto.DespesaDTO;
import com.soverteria.frimel.modelos.entity.Despesa;
import com.soverteria.frimel.repositorios.DespesaRepositorio;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class DespesaBO {

    final DespesaRepositorio despesaRepositorio;

    public DespesaBO(DespesaRepositorio despesaRepositorio) {

        this.despesaRepositorio = despesaRepositorio;
    }

    public List<Despesa> findAll(){
        return despesaRepositorio.findAll();
    }

    public Despesa getOne(Long id){
        return despesaRepositorio.getOne(id);
    }

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

    private LocalDateTime criarLocalDate(String data) {

        if(data != null){

            DateTimeFormatter parser = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            LocalDateTime dateTime = LocalDate.parse(data, parser).atStartOfDay();

            LocalTime horario = LocalTime.now();
            LocalDate date = dateTime.toLocalDate();

            dateTime = date.atTime(horario);

            return dateTime;
        }
        return null;
    }

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
