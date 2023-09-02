package com.allan.fluxo_de_caixa.business;

import com.allan.fluxo_de_caixa.modelos.dto.DespesaDTO;
import com.allan.fluxo_de_caixa.modelos.entity.Despesa;
import com.allan.fluxo_de_caixa.modelos.entity.Usuario;
import com.allan.fluxo_de_caixa.repositorios.DespesaRepositorio;
import com.allan.fluxo_de_caixa.security.Filtros.JWTAutenticacao;
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
 * Esta classe gerencia  todas as despesas da empresa
 */
@Service
public class DespesaBO {

    @Autowired
     DespesaRepositorio despesaRepositorio;

    Usuario usuario = JWTAutenticacao.usuario;
    /**
     * este metodo serve para mostrar todas as despesas do usuario
     * @return
     */
    public Page<Despesa> listarDespesas(Pageable pageable,String proprietario){
        return despesaRepositorio.findByProprietario(pageable,proprietario);
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
     * @param despesaDTO
     * @return
     */
    public Despesa save(DespesaDTO despesaDTO){

        if(despesaDTO != null){
            Despesa despesa = new Despesa();

            despesa.setProprietario(usuario.getUsername());
            despesa.setDescricao(despesaDTO.getDescricao());
            despesa.setValor(despesaDTO.getValor());
            despesa.setData(criarLocalDate(despesaDTO.getData()));

            return despesaRepositorio.save(despesa);
        }

        return null;
    }


    /**
     * essa função serve para retornar uma lista de despesas somadas conforme o mês e o ano
     * @return Page<Despesa>
     */
    public List<Despesa>somarDespesas(){
        List<Despesa> despesasOrdenadas = despesaRepositorio.findByProprietario(JWTAutenticacao.usuario.getUsername());
        ArrayList<Despesa> despesasSomadas = new ArrayList<>();

        for(int index = 0;index < despesasOrdenadas.size();index++){

            int indexRapido = index + 1;

            Despesa despesa= despesasOrdenadas.get(index);
            Despesa despesaSomar = (indexRapido == despesasOrdenadas.size())?new Despesa():despesasOrdenadas.get(indexRapido);

            if(!despesaSomar.getProprietario().equals("")) {
                BigDecimal valorDebito = despesa.getValor();

                valorDebito = (despesa.getData().getMonth() == despesaSomar.getData().getMonth() && despesa.getData().getYear() == despesaSomar.getData().getYear()) ?
                        valorDebito.add(despesaSomar.getValor()) : valorDebito;

                despesa.setValor(valorDebito);
                despesasSomadas.add(despesa);
            }

            else
                despesasSomadas.add(despesa);
        }
        return despesasSomadas;
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
