package com.soverteria.frimel.business;

import com.soverteria.frimel.modelos.dto.DespesaDTO;
import com.soverteria.frimel.modelos.dto.UsuarioDTO;
import com.soverteria.frimel.modelos.entity.Despesa;
import com.soverteria.frimel.modelos.entity.Usuario;
import com.soverteria.frimel.repositorios.DespesaRepositorio;
import com.soverteria.frimel.repositorios.UsuarioRepositorio;
import com.soverteria.frimel.security.Filtros.JWTAutenticacao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Esta classe gerencia  todas as despesas da empresa
 */
@Service
public class DespesaBO {

    @Autowired
     DespesaRepositorio despesaRepositorio;

    @Autowired
    UsuarioRepositorio usuarioRepositorio;

    Usuario usuario = JWTAutenticacao.usuario;

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

            usuario.getDadosDespesa().add(despesas);
            usuarioRepositorio.save(usuario);

            return despesaRepositorio.save(despesas);
        }

        return null;
    }
    List<Despesa>despesasOrdenadas;
    public ArrayList<Despesa>addValueOfExpensesByMesAndYear(){

        Sort ordenar = Sort.by("data").ascending();
        despesasOrdenadas = usuario.getDadosDespesa();
        despesasOrdenadas.sort(Comparator.comparing(Despesa::getData));

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
     * @param despesaDTO
     * @return
     */
   public Despesa update(DespesaDTO despesaDTO){



         Despesa despesa = despesaRepositorio.findByDescricao();

         despesa.setDescricao(despesaDTO.getDescricao());
         despesa.setData(criarLocalDate(despesaDTO.getData()));
         despesa.setValor(despesaDTO.getValor());

         for(int i = 0;i < usuario.getDadosDespesa().size(); i++){

             if(usuario.getDadosDespesa().get(i).getDescricao().equals(despesa.getDescricao())){
                 usuario.getDadosDespesa().set(i,despesa);
             }
         }

         usuarioRepositorio.save(usuario);
         return despesaRepositorio.save(despesa);
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
             Despesa despesa = getOne(id);

             for(int i = 0;i < usuario.getDadosDespesa().size();i++) {
                 usuario.getDadosDespesa().set(i, despesa);
             }
             
            despesaRepositorio.deleteById(id);
            return  Boolean.TRUE;
        }
        catch (Exception e) {
            return  Boolean.FALSE;
        }
    }
}
