package com.soverteria.frimel.business;

import com.soverteria.frimel.modelos.dto.DebitoDTO;
import com.soverteria.frimel.modelos.entity.Debito;
import com.soverteria.frimel.modelos.entity.Estoque;
import com.soverteria.frimel.modelos.entity.Usuario;
import com.soverteria.frimel.repositorios.DebitoRepositorio;
import com.soverteria.frimel.security.Filtros.JWTAutenticacao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/**
 * essa classe gerencia todos os debitos da empresa
 */
@Service
public class DebitoBO  {

     @Autowired
     DebitoRepositorio debitoRepositorio;
     @Autowired
     EstoqueBO estoqueBO;

     Usuario usuario = JWTAutenticacao.usuario;
    /**
     * esse metodo serve para mostrar todos os debitos cadastrados
     *
     * @return
     */
    public List<Debito> findAll() {
        List<Debito> debitosDoProprietario = new ArrayList<>();

        for(int id  = 0;id < debitoRepositorio.findAll().size();id++){
            Debito debito = debitoRepositorio.findAll().get(id);

            if(debito.getProprietario().equals(usuario.getUsername())){
                debitosDoProprietario.add(debito);
            }
        }
        return debitosDoProprietario;
    }

    /**
     * esse metodo serve para pegar um debito atraves do id
     *
     * @param id
     * @return
     */
    public Debito getOne(Long id) {
        try {
            return debitoRepositorio.findById(id).get();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * esse metodo serve para registrar um Debito no banco de dados
     *
     * @param debitoDTO
     * @param estoque
     * @return
     */
    public Debito save(DebitoDTO debitoDTO, Estoque estoque) {

        if (debitoDTO != null) {
            Debito debito = new Debito();

            debito.setQuantidade(debitoDTO.getQuantidade());
            BigDecimal quantidade = BigDecimal.valueOf(debito.getQuantidade());

            debito.setProprietario(usuario.getUsername());
            debito.setValor(estoque.getPreco().multiply(quantidade));
            debito.setProdutoVendido(debitoDTO.getProdutoVendido());
            debito.setData(criarLocalDate(debitoDTO.getData()));

            debitoRepositorio.save(debito);
            estoqueBO.subtrairQuantidadeDoProduto(debito);
            return debito;
        }

        return null;
    }


    public Boolean deleteById(Long id) {

        try {
            Debito debito = debitoRepositorio.findById(id).get();
            estoqueBO.aumentarQuantidadeDoProduto(debito);
            debitoRepositorio.deleteById(id);
            return Boolean.TRUE;
        }
        catch (Exception e) {
            e.printStackTrace();
            return Boolean.FALSE;
        }
    }

    /**
     * esse metodo serve para criar um LocalDateTime
     *
     * @param data
     * @return
     */

    private LocalDateTime criarLocalDate(String data) {

        if (data != null) {

            LocalDateTime dateTime = LocalDate.parse(data).atStartOfDay();

            LocalDate date = dateTime.toLocalDate();
            LocalTime horario = LocalTime.now();

            dateTime = date.atTime(horario);

            return dateTime;
        }
        return null;
    }

    /**
     * esse metodo serve para deletar um debito especifico pelo id
     *
     * @param id
     * @return
     */


    /**
     * esse metodo serve para atualizar um Debito com os dados do usuario
     *
     * @param id
     * @param debitoDTO
     * @param estoque
     * @return
     */

    public Debito update(Long id, DebitoDTO debitoDTO, Estoque estoque) {
        Debito debito = new Debito();

        if (id != null) {

            debito = getOne(id);
            Debito debitoOperar =  new Debito();

            BigDecimal quantidade = BigDecimal.valueOf(debito.getQuantidade());

            debitoOperar.setProdutoVendido(debitoDTO.getProdutoVendido());
            debitoOperar.setData(criarLocalDate(debitoDTO.getData()));
            debitoOperar.setValor(estoque.getPreco().multiply(quantidade));
            debitoOperar.setQuantidade(debito.getQuantidade());

            debito.setData(criarLocalDate(debitoDTO.getData()));
            debito.setProdutoVendido(debitoDTO.getProdutoVendido());
            debito.setQuantidade(debitoDTO.getQuantidade());
            debito.setValor(estoque.getPreco().multiply(quantidade));

            estoqueBO.aumentarQuantidadeDoProduto(debitoOperar);
            estoqueBO.subtrairQuantidadeDoProduto(debito);
        }

        return debitoRepositorio.save(debito);
    }


    List<Debito> debitosOrdenados;

    public ArrayList<Debito> addValueOfSalesByMesAndYear() {


        Sort ordenar = Sort.by("data").ascending();
        debitosOrdenados = debitoRepositorio.findAll(ordenar);
        ArrayList<Debito> debitosSomados = new ArrayList<>();
        Debito debito= debitosOrdenados.get(0);

        int i = 1;
        do{
            Debito debitoSomar = debitosOrdenados.get(i);

            if(debito.getData().getYear() == debitoSomar.getData().getYear() && debitoSomar.getProprietario().equals(usuario.getUsername()) && debito.getProprietario().equals(usuario.getUsername())){

                if(debito.getData().getMonth() == debitoSomar.getData().getMonth()) {
                    debito.setValor(debito.getValor().add(debitoSomar.getValor()));
                }
                else if(debito.getData().getMonth() != debitoSomar.getData().getMonth()) {
                    debitosSomados.add(debito);
                    debito = debitoSomar;
                }
            }

            if(debito.getData().getYear() != debitoSomar.getData().getYear() || debito.getData().getMonth() != debitoSomar.getData().getMonth()){
                debitosSomados.add(debitoSomar);
            }
            if(debito.getProprietario().equals(debitoSomar.getProprietario())){
                debitosSomados.add(debito);
            }

            if(i + 1 == debitosOrdenados.size() && debitoSomar.getProprietario().equals(usuario.getUsername())){
                debitosSomados.add(debitoSomar);
            }

            i++;
        }
        while (i < debitosOrdenados.size());
        return  debitosSomados;
    }
}
