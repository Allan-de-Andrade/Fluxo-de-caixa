package com.soverteria.frimel.business;

import com.soverteria.frimel.modelos.dto.DebitoDTO;
import com.soverteria.frimel.modelos.dto.EstoqueDTO;
import com.soverteria.frimel.modelos.entity.Debito;
import com.soverteria.frimel.modelos.entity.Estoque;
import com.soverteria.frimel.repositorios.DebitoRepositorio;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * essa classe gerencia todos os debitos da empresa
 */
@Service
public class DebitoBO {


    final DebitoRepositorio debitoRepositorio;
    final EstoqueBO estoqueBO;

    public DebitoBO(DebitoRepositorio debitoRepositorio,EstoqueBO estoqueBO) {
        this.debitoRepositorio = debitoRepositorio;
        this.estoqueBO = estoqueBO;
    }

    /**
     * esse metodo serve para mostrar todos os debitos cadastrados
     * @return
     */
    public  List<Debito> findAll() {
        return debitoRepositorio.findAll();
    }

    /**
     *esse metodo serve para pegar um debito atraves do id
     * @param id
     * @return
     */
    public  Debito getOne(Long id) {
        return debitoRepositorio.getOne(id);
    }

    /**
     * esse metodo serve para registrar um Debito no banco de dados
     * @param debitoDTO
     * @param estoque
     * @return
     */
    public Debito save(DebitoDTO debitoDTO,Estoque estoque) {

        if(debitoDTO != null){
            Debito debito = new Debito();

            debito.setQuantidade(debitoDTO.getQuantidade());
            BigDecimal quantidade =  BigDecimal.valueOf(debito.getQuantidade());

            debito.setValor(estoque.getPreco().multiply(quantidade));
            debito.setProdutoVendido(debitoDTO.getProdutoVendido());
            debito.setData(criarLocalDate(debitoDTO.getData()));

            debitoRepositorio.save(debito);
            estoqueBO.subtrairQuantidadeDoProduto(debito);
            return debito;
        }

        return null;
    }

    /**
     * esse metodo serve para criar um LocalDateTime
     * @param data
     * @return
     */
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

    /**
     * esse metodo serve para deletar um debito especifico pelo id
     * @param id
     * @return
     */
    public Boolean  deleteById(Long id) {

        try {
            debitoRepositorio.deleteById(id);
            return Boolean.TRUE;
        }
        catch (Exception e){
            return Boolean.FALSE;
        }
    }

    /**
     * esse metodo serve para atualizar um Debito com os dados do usuario
     * @param id
     * @param debitoDTO
     * @param estoque
     * @return
     */
    public Debito update(Long id,DebitoDTO debitoDTO,Estoque estoque)
    {
        if(id != null) {

            Debito debito = getOne(id);

            BigDecimal quantidade =  BigDecimal.valueOf(debito.getQuantidade());

            debito.setData(criarLocalDate(debitoDTO.getData()));
            debito.setProdutoVendido(debitoDTO.getProdutoVendido());
            debito.setQuantidade(debitoDTO.getQuantidade());
            debito.setValor(estoque.getPreco().multiply(quantidade));

            return debitoRepositorio.save(debito);
        }
        return null;
    }
}
