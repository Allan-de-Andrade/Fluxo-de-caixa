package com.soverteria.frimel;

import com.soverteria.frimel.business.InsumoBO;
import com.soverteria.frimel.business.ProdutoBO;
import com.soverteria.frimel.modelos.dto.InsumoDTO;
import com.soverteria.frimel.modelos.dto.ProdutoDTO;
import com.soverteria.frimel.modelos.entity.Insumo;
import com.soverteria.frimel.modelos.entity.Produto;
import com.soverteria.frimel.repositorios.VendaRepositorio;
import com.soverteria.frimel.repositorios.InsumoRepositorio;
import com.soverteria.frimel.repositorios.ProdutoRepositorio;


import com.soverteria.frimel.security.Filtros.JWTAutenticacao;
import org.junit.Before;
import org.junit.jupiter.api.Assertions;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.BeanUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.asList;

@ExtendWith(MockitoExtension.class)
public class InsumoServiceTeste {
    
    @InjectMocks
    private InsumoBO insumoBO;


    @Mock
    private InsumoRepositorio insumoRepositorio;
    
    @Mock
    private ProdutoRepositorio produtoRepositorio;



    

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        produtoRepositorio = Mockito.mock(ProdutoRepositorio.class);
        insumoRepositorio = Mockito.mock(InsumoRepositorio.class);
        insumoBO = new InsumoBO(insumoRepositorio,produtoRepositorio);
    }

    @Test
    public void naoDeletarInsumoSeProdutoTerOInsumo() {

        long id = 1;
        long idComparar = 2;

        InsumoDTO insumoDTO = new InsumoDTO("blabla",new BigDecimal(3.40),"blabla");
        InsumoDTO insumoDTOComparar = new InsumoDTO("blablabla",new BigDecimal(3.40),"blabla");


        Insumo insumo = new Insumo();
        insumo.setId(id);

        Insumo insumoComparar = new Insumo();
        insumoComparar.setId(idComparar);

        ProdutoDTO produtoDTO = new ProdutoDTO("blçalblblskgosjgo",300,new BigDecimal(50.00),asList(insumoComparar));
        Produto produto = new Produto();

        produto.setId(id);
        produto.setNome(produtoDTO.getNome());
        produto.setInsumosUsados(produtoDTO.getInsumosUsados());
        produto.setQuantidade(produtoDTO.getQuantidade());
        produto.setPreco(produtoDTO.getPreco());
        produto.setProprietario("Allan");

        BeanUtils.copyProperties(insumoDTO,insumo);
        BeanUtils.copyProperties(insumoDTOComparar,insumoComparar);

        List<Produto> produtosFind = new ArrayList<>();
        produtosFind.add(produto);

        Mockito.when(insumoRepositorio.getOne(Mockito.anyLong())).thenReturn(insumo);
        Mockito.when(produtoRepositorio.findByProprietario(JWTAutenticacao.usuario.getUsername())).thenReturn(produtosFind);

        Assertions.assertFalse(insumoBO.deletarInsumo(id));
    }


}
