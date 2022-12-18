package com.soverteria.frimel.business;

import com.soverteria.frimel.modelos.dto.DespesaDTO;
import com.soverteria.frimel.modelos.entity.Despesa;
import com.soverteria.frimel.modelos.entity.Usuario;
import com.soverteria.frimel.repositorios.DespesaRepositorio;
import com.soverteria.frimel.security.Filtros.JWTAutenticacao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;

import java.math.BigDecimal;
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

    Usuario usuario = JWTAutenticacao.usuario;
    /**
     * este metodo serve para mostrar todas as despesas
     * @return
     */
    public List<Despesa> findAll(){
        List<Despesa> despesasDoProprietario = new ArrayList<>();

        for(int id  = 0;id < despesaRepositorio.findAll().size();id++){
            Despesa despesa = despesaRepositorio.findAll().get(id);

            if(despesa.getProprietario().equals(usuario.getUsername())){
                despesasDoProprietario.add(despesa);
            }
        }
        return despesasDoProprietario;
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

    List<Despesa>despesasOrdenadas;

    /**
     * essa função serve para retornar uma lista de despesas somadas conforme o mês e o ano
     * @return ArrayList<Despesa>
     */
    public ArrayList<Despesa>somarDespesas(){
        List<Despesa> despesasOrdenadas = organizarListaDespesas();
        ArrayList<Despesa> despesasSomadas = new ArrayList<>();

        for(int index = 0;index < despesasOrdenadas.size();index++){

            Despesa despesa= despesasOrdenadas.get(index);
            Despesa despesaSomar = (index  +1 == despesasOrdenadas.size())?new Despesa():despesasOrdenadas.get(index + 1);

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
     * cria uma lista de despesas organizada com referencia ao username do usuario
     * @return List<Despesa>
     */
    private List<Despesa>organizarListaDespesas(){

        Sort ordenar = Sort.by("data").ascending();

        despesasOrdenadas = despesaRepositorio.findAll(ordenar);
        int index = 0;

        do {
            Despesa despesa = despesasOrdenadas.get(index);

            if(!despesa.getProprietario().equals(usuario.getUsername())){
                despesasOrdenadas.remove(index);
            }
            else
                index++;
        }
        while (index < despesasOrdenadas.size());

        return despesasOrdenadas;
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
