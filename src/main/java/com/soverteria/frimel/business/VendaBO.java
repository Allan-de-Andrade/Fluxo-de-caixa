package com.soverteria.frimel.business;

import com.soverteria.frimel.modelos.dto.VendaDTO;
import com.soverteria.frimel.modelos.entity.Venda;
import com.soverteria.frimel.modelos.entity.Produto;
import com.soverteria.frimel.modelos.entity.Usuario;
import com.soverteria.frimel.repositorios.VendaRepositorio;
import com.soverteria.frimel.security.Filtros.JWTAutenticacao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
public class VendaBO {

     @Autowired
     VendaRepositorio vendaRepositorio;
     @Autowired
     ProdutoBO produtoBO;

     Usuario usuario = JWTAutenticacao.usuario;

    /**
     * esse metodo serve para mostrar todos os debitos cadastrados do usuario
     *
     * @return
     */
    public Page<Venda> listarDebitos(Pageable page , String proprietario) {
     return vendaRepositorio.findByProprietario(page,proprietario);
    }

    /**
     * esse metodo serve para pegar um debito atraves do id
     *
     * @param id
     * @return
     */
    public Venda getOne(Long id) {
        try {
            return vendaRepositorio.findById(id).get();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * esse metodo serve para registrar um Venda no banco de dados
     *
     * @param vendaDTO
     * @param produto
     * @return
     */
    public Venda save(VendaDTO vendaDTO, Produto produto) {

        if (vendaDTO != null) {
            Venda venda = new Venda();

            venda.setQuantidade(vendaDTO.getQuantidade());
            BigDecimal quantidade = BigDecimal.valueOf(venda.getQuantidade());

            venda.setProprietario(usuario.getUsername());
            venda.setValor(produto.getPreco().multiply(quantidade));
            venda.setProdutoVendido(vendaDTO.getProdutoVendido());
            venda.setData(criarLocalDate(vendaDTO.getData()));

            vendaRepositorio.save(venda);
            produtoBO.subtrairQuantidadeDoProduto(venda);
            return venda;
        }

        return null;
    }


    public Boolean deleteById(Long id) {

        try {
            Venda venda = vendaRepositorio.findById(id).get();
            produtoBO.aumentarQuantidadeDoProduto(venda);
            vendaRepositorio.deleteById(id);
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
     * esse metodo serve para atualizar um Venda com os dados do usuario
     *
     * @param id
     * @param vendaDTO
     * @param produto
     * @return
     */

    public Venda update(Long id, VendaDTO vendaDTO, Produto produto) {
        Venda venda = new Venda();

        if (id != null) {

            venda = getOne(id);
            Venda vendaOperar =  new Venda();

            BigDecimal quantidade = BigDecimal.valueOf(venda.getQuantidade());

            vendaOperar.setProdutoVendido(vendaDTO.getProdutoVendido());
            vendaOperar.setData(criarLocalDate(vendaDTO.getData()));
            vendaOperar.setValor(produto.getPreco().multiply(quantidade));
            vendaOperar.setQuantidade(venda.getQuantidade());

            venda.setData(criarLocalDate(vendaDTO.getData()));
            venda.setProdutoVendido(vendaDTO.getProdutoVendido());
            venda.setQuantidade(vendaDTO.getQuantidade());
            venda.setValor(produto.getPreco().multiply(quantidade));

            produtoBO.aumentarQuantidadeDoProduto(vendaOperar);
            produtoBO.subtrairQuantidadeDoProduto(venda);
        }

        return vendaRepositorio.save(venda);
    }



    /**
     * essa função serve para retornar uma lista de debitos somados conforme o mês e o ano
     * @return Page<Venda>
     */
    public List<Venda> somarVendas() {

        List<Venda> vendasOrdenadas = vendaRepositorio.findByProprietario(JWTAutenticacao.usuario.getUsername());
        ArrayList<Venda> vendasSomadas = new ArrayList<>();

        for(int index = 0;index < vendasOrdenadas.size();index++){

            int indexRapido = index + 1;

            Venda venda = vendasOrdenadas.get(index);
             Venda vendaSomar = (indexRapido == vendasOrdenadas.size())?new Venda():vendasOrdenadas.get(indexRapido);

             if(!vendaSomar.getProprietario().equals("")) {
                 BigDecimal valorDebito = venda.getValor();

                 valorDebito = (venda.getData().getMonth() == vendaSomar.getData().getMonth() && venda.getData().getYear() == vendaSomar.getData().getYear()) ?
                         valorDebito.add(vendaSomar.getValor()) : valorDebito;

                 venda.setValor(valorDebito);
                 vendasSomadas.add(venda);
             }

             else
                 vendasSomadas.add(venda);
        }
        return vendasSomadas;
    }
}
