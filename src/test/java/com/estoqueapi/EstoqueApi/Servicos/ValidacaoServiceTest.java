package com.estoqueapi.EstoqueApi.Servicos;

import com.estoqueapi.EstoqueApi.Entidades.*;
import com.estoqueapi.EstoqueApi.Exceptions.MovimentacaoInvalidaException;
import com.estoqueapi.EstoqueApi.Repositorios.MovimentacaoRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@ExtendWith(SpringExtension.class)
class ValidacaoServiceTest {
    private Item item;
    private Estoque estoque;
    private Movimentacao m;
    private Usuario user;

    @Mock
    private MovimentacaoRepository movimentacoesRepository;
    @Mock
    private UsuarioService usuarioService;

    @Mock
    private EstoqueService estoqueService;

    @Mock
    private ItemService itemService;

    @Mock
    private PrevisaoService previsaoService;

    @Mock
    private ReservaService reservaService;

    @InjectMocks
    private ValidacaoService validacaoService;



    @BeforeEach
    void setup(){
        item = new Item();
        item.setIdItem(1000l);
        item.setDescricao("Item teste para teste");
        item.setFamilia("Teste");
        item.setGrupo("Teste");
        item.setEstoqueSeguranca(20f);
        item.setUnidade("PC");

        estoque = new Estoque();
        estoque.setItem(item);
        estoque.setEstoqueReal(40f);
        estoque.setLocalizacao("AA1234");
        estoque.setIdEstoque(10l);

        user = new Usuario("JOSE","asdf","jose@jose");
        user.setIdUsuario(1l);

        m = new Movimentacao();
        m.setItem(item);
        m.setEstoque(estoque);
        m.setDataMovimentacao(Instant.now());
        m.setQuantidade(25);
        m.setUsuario(user);
        m.setOrigemDestino("ordem1234");


    }

    @Test
    @DisplayName("Deve retornar objeto Movimentacoes se valido")
    void retornaObjetoMovimentacoesSeForValida() {

        Mockito.when(usuarioService.buscarUsuarioById(user.getIdUsuario())).thenReturn(user);
        Mockito.when(itemService.consultarItemById(item.getIdItem())).thenReturn(item);
        Mockito.when(estoqueService.buscarEstoqueById(estoque.getIdEstoque())).thenReturn(estoque);

        Assertions.assertEquals(m, validacaoService.validarMovimentacao(m));
    }

    @Test
    @DisplayName("Deve lancar Excecao se quantidade menor ou igual a zero ")
    void lancaExcecaoSeQuantidadeMenorOuIgualZero(){
        //Mockando o retorno para forcar erro
        m.setQuantidade(0);

        //Mockando retornos corretos
        Mockito.when(usuarioService.buscarUsuarioById(user.getIdUsuario())).thenReturn(user);
        Mockito.when(itemService.consultarItemById(item.getIdItem())).thenReturn(item);
        Mockito.when(estoqueService.buscarEstoqueById(estoque.getIdEstoque())).thenReturn(estoque);

        //Testanto a funcao com objeto movimentacoes
        MovimentacaoInvalidaException ex  = Assertions.assertThrowsExactly(MovimentacaoInvalidaException.class,() -> validacaoService.validarMovimentacao(m));
        Assertions.assertEquals("Quantidade Inválida!",ex.getMessage());
    }

    @Test
    @DisplayName("Deve retornar uma previsao encontrada para movimentacao")
    void retornaUmaPrevisaoEncontradaParaMovimentacao(){
        //Mockando lista para retorno
        Previsao pMock = new Previsao();
        Item itemMock = item;
        String ordemMock = m.getOrigemDestino();
        float qtdMock = m.getQuantidade();
        pMock.setItem(itemMock);
        pMock.setOrdem(ordemMock);
        pMock.setFinalizada(false);
        pMock.setQuantidadePrevista(qtdMock);
        List<Previsao> listMock = new ArrayList<>();
        listMock.add(pMock);

        //Mockando retorno
        Mockito.when(previsaoService.consultarPendentesByIdItem(item.getIdItem())).thenReturn(listMock);

        //Testando
        Previsao pRetorno = pMock;
        Assertions.assertEquals(pRetorno, validacaoService.consultaPrevisoesByMovimentacao(m));
    }

    @Test
    @DisplayName("Deve lancar excecao se origem previsao invalida")
    void lancaExcecaoSeOrigemPrevisaoInvalida(){
        //Mockando lista para retorno
        Previsao pMock = new Previsao();
        pMock.setItem(item);
        pMock.setOrdem(m.getOrigemDestino());
        pMock.setFinalizada(false);
        pMock.setQuantidadePrevista(m.getQuantidade());
        List<Previsao> listMock = new ArrayList<>();
        listMock.add(pMock);

        //Mockando retorno
        Mockito.when(previsaoService.consultarPendentesByIdItem(item.getIdItem())).thenReturn(listMock);

        //Testando ordem inexistente na lista
        m.setOrigemDestino("Ordem4321");
        Assertions.assertThrows(MovimentacaoInvalidaException.class,() -> validacaoService.consultaPrevisoesByMovimentacao(m));

        //Testando ordem vazia
        m.setOrigemDestino("");
        Assertions.assertThrows(MovimentacaoInvalidaException.class,() -> validacaoService.consultaPrevisoesByMovimentacao(m));

        //confirmando se o retorno mockado foi chamado 2 vezes
        Mockito.verify(previsaoService,Mockito.times(2)).consultarPendentesByIdItem(item.getIdItem());
    }

    @Test
    @DisplayName("Deve lancar excecao se quantidade previsao diferente da movimentacao")
    void lancaExcecaoSeQuantidadePrevisaoDiferenteMovimentacao(){
        //Mockando lista para retorno
        Previsao pMock = new Previsao();
        pMock.setItem(item);
        pMock.setOrdem(m.getOrigemDestino());
        pMock.setFinalizada(false);
        pMock.setQuantidadePrevista(m.getQuantidade());
        List<Previsao> listMock = new ArrayList<>();
        listMock.add(pMock);

        //Mockando retorno
        Mockito.when(previsaoService.consultarPendentesByIdItem(item.getIdItem())).thenReturn(listMock);

        //Testando quantidade movimentada diferente
        m.setQuantidade(1000f);
        Assertions.assertThrows(MovimentacaoInvalidaException.class,() -> validacaoService.consultaPrevisoesByMovimentacao(m));

        //confirmando se o retorno mockado foi chamado 1 vezes
        Mockito.verify(previsaoService,Mockito.times(1)).consultarPendentesByIdItem(item.getIdItem());
    }

    @Test
    @DisplayName("Deve retornar reserva encontrada para movimentacao")
    void retornaListaReservasParaMovimentacao(){
        //Mockando lista para retorno
        Reserva rMock = new Reserva();
        Item itemMock = item;
        String ordemMock = m.getOrigemDestino();
        float qtdMock = m.getQuantidade();
        rMock.setItem(itemMock);
        rMock.setOrdem(ordemMock);
        rMock.setFinalizada(false);
        rMock.setQuantidadeReserva(qtdMock);
        List<Reserva> listMock = new ArrayList<>();
        listMock.add(rMock);

        //Mockando retorno
        Mockito.when(reservaService.consultarPendentesByIdItem(item.getIdItem())).thenReturn(listMock);

        //Testando
        Reserva rRetornoEsperado = rMock;
        Assertions.assertEquals(rRetornoEsperado, validacaoService.consultaReservasByMovimentacao(m));

        //confirmando se o retorno mockado foi chamado 1 vez
        Mockito.verify(reservaService,Mockito.times(1)).consultarPendentesByIdItem(item.getIdItem());
    }

    @Test
    @DisplayName("Deve lancar excecao se destino reserva invalida")
    void lancaExcecaoSeDestinoReservaInvalida(){
        //Mockando lista para retorno
        Reserva rMock = new Reserva();
        Item itemMock = item;
        String ordemMock = "OP1234";
        float qtdMock = m.getQuantidade();
        rMock.setItem(itemMock);
        rMock.setOrdem(ordemMock);
        rMock.setFinalizada(false);
        rMock.setQuantidadeReserva(qtdMock);
        List<Reserva> listMock = new ArrayList<>();
        listMock.add(rMock);

        //Mockando retorno
        Mockito.when(reservaService.consultarPendentesByIdItem(item.getIdItem())).thenReturn(listMock);

        //Testando destino inexistente na lista
        m.setOrigemDestino("OP4321");
        Assertions.assertThrows(MovimentacaoInvalidaException.class,() -> validacaoService.consultaReservasByMovimentacao(m));

        //Testando ordem vazia
        m.setOrigemDestino("");
        Assertions.assertThrows(MovimentacaoInvalidaException.class,() -> validacaoService.consultaReservasByMovimentacao(m));

        //confirmando se o retorno mockado foi chamado 2 vezes
        Mockito.verify(reservaService,Mockito.times(2)).consultarPendentesByIdItem(item.getIdItem());
    }

    @Test
    @DisplayName("Deve lancar excecao se quantidade reserva diferente da movimentacao")
    void lancaExcecaoSeQuantidadeReservaDiferenteMovimentacao(){
        //Mockando lista para retorno
        Reserva rMock = new Reserva();
        rMock.setItem(item);
        rMock.setOrdem(m.getOrigemDestino());
        rMock.setFinalizada(false);
        rMock.setQuantidadeReserva(m.getQuantidade());
        List<Reserva> listMock = new ArrayList<>();
        listMock.add(rMock);

        //Mockando retorno
        Mockito.when(reservaService.consultarPendentesByIdItem(item.getIdItem())).thenReturn(listMock);

        //Testando destino inexistente na lista
        m.setQuantidade(1000f);
        Assertions.assertThrows(MovimentacaoInvalidaException.class,() -> validacaoService.consultaReservasByMovimentacao(m));

        //confirmando se o retorno mockado foi chamado 1 vezes
        Mockito.verify(reservaService,Mockito.times(1)).consultarPendentesByIdItem(item.getIdItem());
    }
}