package com.soverteria.frimel.business;

import com.soverteria.frimel.modelos.dto.DebitoDTO;
import com.soverteria.frimel.modelos.entity.Debito;
import com.soverteria.frimel.repositorios.DebitoRepositorio;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class DebitoBO {


    final DebitoRepositorio debitoRepositorio;

    public DebitoBO(DebitoRepositorio debitoRepositorio) {
        this.debitoRepositorio = debitoRepositorio;
    }

    public  List<Debito> findAll() {
        return debitoRepositorio.findAll();
    }

    public  Debito getOne(Long id) {
        return debitoRepositorio.getOne(id);
    }
    public Debito save(DebitoDTO debitoDTO) {

        if(debitoDTO != null){

            Debito debito = new Debito();

            debito.setValor(debitoDTO.getValor());
            debito.setProdutoVendido(debitoDTO.getProdutoVendido());
            debito.setData(criarLocalDate(debitoDTO.getData()));
            debito.setQuantidade(debitoDTO.getQuantidade());

           return debitoRepositorio.save(debito);
        }

        return null;
    }

    private LocalDateTime criarLocalDate(String data) {

        if (data != null) {

            DateTimeFormatter parser = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            LocalDateTime dateTime = LocalDate.parse(data, parser).atStartOfDay();

            LocalDate date = dateTime.toLocalDate();
            LocalTime horario = LocalTime.now();

            dateTime = date.atTime(horario);

            return dateTime;
        }
        return null;
    }

    public Boolean  deleteById(Long id) {

        try {
            debitoRepositorio.deleteById(id);
            return Boolean.TRUE;
        }
        catch (Exception e){
            return Boolean.FALSE;
        }
    }
    public Debito update(Long id,DebitoDTO debitoDTO)
    {
        if(id != null) {

            Debito debito = getOne(id);

            debito.setData(criarLocalDate(debitoDTO.getData()));
            debito.setProdutoVendido(debitoDTO.getProdutoVendido());
            debito.setValor(debitoDTO.getValor());
            debito.setQuantidade(debitoDTO.getQuantidade());

            return debitoRepositorio.save(debito);
        }
        return null;
    }

}
