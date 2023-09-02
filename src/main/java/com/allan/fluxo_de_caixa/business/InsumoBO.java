package com.allan.fluxo_de_caixa.business;

import com.allan.fluxo_de_caixa.modelos.dto.InsumoDTO;
import com.allan.fluxo_de_caixa.modelos.entity.Produto;
import com.allan.fluxo_de_caixa.modelos.entity.Insumo;
import com.allan.fluxo_de_caixa.repositorios.ProdutoRepositorio;
import com.allan.fluxo_de_caixa.repositorios.InsumoRepositorio;
import com.allan.fluxo_de_caixa.security.Filtros.JWTAutenticacao;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InsumoBO {

    private final InsumoRepositorio insumoRepositorio;
    private final ProdutoRepositorio produtoRepositorio;

    public InsumoBO(InsumoRepositorio insumoRepositorio, ProdutoRepositorio produtoRepositorio) {
        this.insumoRepositorio = insumoRepositorio;
        this.produtoRepositorio = produtoRepositorio;
    }

    /**
     * serve para retornar uma lista de insumos  do usuario paginados
     * @param
     * @return List<Insumo>
     */
    public Page<Insumo> listarInsumos(PageRequest pageRequest,String username){
        return insumoRepositorio.findByIndentificadorUsuario(pageRequest,username);
    }

    /**
     * este metodo serve para buscar um Insumo pelo id
     * @param id
     * @return
     */
    public Insumo pegarInsumoPeloId(Long id){
        return insumoRepositorio.getOne(id);
    }

    /**
     * este metodo serve para salvar um insumo
     * @param insumoDTO
     * @return
     */
    public Insumo salvarInsumo(InsumoDTO insumoDTO){

        Insumo insumo = new Insumo();

        insumo.setValor(insumoDTO.getValor());
        insumo.setNome(insumoDTO.getNome());
        insumo.setIndentificadorUsuario(JWTAutenticacao.usuario.getUsername());

        return insumoRepositorio.save(insumo);
    }

    /**
     * este metodo serve para editar um insumo
     * @param insumoDTO
     * @param id
     * @return
     */
    public Insumo editarInsumo(InsumoDTO insumoDTO,Long id){

        Insumo insumo = pegarInsumoPeloId(id);
        insumo.setNome(insumoDTO.getNome());
        insumo.setValor(insumoDTO.getValor());
        insumo.setIndentificadorUsuario(JWTAutenticacao.usuario.getUsername());

        return insumoRepositorio.save(insumo);
    }

    /**
     * este metodo serve para deletar um insumo caso nenhum produto esteja usando ele
     * @param id
     */
    public Boolean  deletarInsumo(Long id){
        try {
            Insumo insumo = insumoRepositorio.getOne(id);
            List<Produto> produtos = produtoRepositorio.findByProprietario(JWTAutenticacao.usuario.getUsername());
            Boolean podeDeletar = true;

                for (Produto produto : produtos) {
                    int index = 0;

                if (produto.getInsumosUsados().get(index).getNome().equals(insumo.getNome())){
                    podeDeletar = false;
                }
                index++;
            }

            if(podeDeletar) {
                insumoRepositorio.deleteById(id);
                return Boolean.TRUE;
            }
            else
                return Boolean.FALSE;
        }

        catch (Exception e){
            e.printStackTrace();
            return Boolean.FALSE;
        }
    }
}
