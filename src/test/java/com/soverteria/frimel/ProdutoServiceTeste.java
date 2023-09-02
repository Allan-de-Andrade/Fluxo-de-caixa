package com.allan.fluxo_de_caixa;

import com.allan.fluxo_de_caixa.business.ProdutoBO;
import com.allan.fluxo_de_caixa.modelos.dto.InsumoDTO;
import com.allan.fluxo_de_caixa.modelos.entity.Insumo;
import com.allan.fluxo_de_caixa.modelos.entity.Produto;
import com.allan.fluxo_de_caixa.repositorios.ProdutoRepositorio;
import com.allan.fluxo_de_caixa.repositorios.VendaRepositorio;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.BeanUtils;

import java.math.BigDecimal;

import static java.util.Arrays.asList;

@ExtendWith({MockitoExtension.class})
public class ProdutoServiceTeste {

    @InjectMocks
    private ProdutoBO produtoBO;

    @Mock
    private ProdutoRepositorio produtoRepositorio;

    @Mock
    private VendaRepositorio vendaRepositorio;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        produtoRepositorio = Mockito.mock(ProdutoRepositorio.class);
        vendaRepositorio = Mockito.mock(VendaRepositorio.class);
        produtoBO = new ProdutoBO(produtoRepositorio,vendaRepositorio);
    }

    @Test
    public void setarPrecoProduto(){
        long id = 1;
        long idNext = 2;

        InsumoDTO insumoDTO = new InsumoDTO("blabla",new BigDecimal(2),"blabla");

        InsumoDTO insumoDTOSomar = new InsumoDTO("blablabla",new BigDecimal(3.5),"blabla");
        Insumo insumo = new Insumo();
        Insumo insumoSomar = new Insumo();

        BeanUtils.copyProperties(insumoDTO,insumo);
        BeanUtils.copyProperties(insumoDTOSomar,insumoSomar);

        insumo.setId(id);
        insumoSomar.setId(idNext);



        Produto produto = new Produto();
        produto.setNome("blabla");
        produto.setInsumosUsados(asList(insumo,insumoSomar));
        produto.setPreco(produtoBO.definirValorDoProduto(produto,produto.getInsumosUsados()));
        produto.setQuantidade(20);
        produto.setProprietario("blabla");

        Assertions.assertEquals(new BigDecimal(5.5),produto.getPreco());
    }
}
