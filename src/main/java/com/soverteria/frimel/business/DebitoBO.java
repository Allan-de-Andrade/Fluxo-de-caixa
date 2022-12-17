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



    /**
     * essa função serve para retornar uma lista de debitos somados conforme o mês e o ano
     * @return ArrayList<Debito>
     */
    public ArrayList<Debito> somarDebitos() {

        List<Debito> debitosOrdenados = organizarListaDebitos();
        ArrayList<Debito> debitosSomados = new ArrayList<>();

        for(int index = 0;index < debitosOrdenados.size();index++){

            Debito debito = debitosOrdenados.get(index);
             Debito debitoSomar = (index  +1 == debitosOrdenados.size())?new Debito():debitosOrdenados.get(index++);

             if(!debitoSomar.getProprietario().equals("")) {
                 BigDecimal valorDebito = debito.getValor();

                 valorDebito = (debito.getData().getMonth() == debitoSomar.getData().getMonth() && debito.getData().getYear() == debitoSomar.getData().getYear()) ?
                         valorDebito.add(debitoSomar.getValor()) : valorDebito;

                 debito.setValor(valorDebito);
                 debitosSomados.add(debito);
             }

             else
                 debitosSomados.add(debito);
        }
        return debitosSomados;
    }
    /**
     * cria uma lista de debitos organizada com referencia ao username do usuario
     * @return List<Despesa>
     */
    private List<Debito>organizarListaDebitos(){

        Sort ordenar = Sort.by("data").ascending();

        List<Debito> debitosOrdenados;
        debitosOrdenados = debitoRepositorio.findAll(ordenar);
        ArrayList<Debito> debitosSomados = new ArrayList<>();

        int index = 0;


        do {
            Debito debito = debitosOrdenados.get(index);

            if(!debito.getProprietario().equals(usuario.getUsername())){
                debitosOrdenados.remove(index);
            }
            else
                index++;
        }
        while (index < debitosOrdenados.size());

        return debitosOrdenados;
    }
}
