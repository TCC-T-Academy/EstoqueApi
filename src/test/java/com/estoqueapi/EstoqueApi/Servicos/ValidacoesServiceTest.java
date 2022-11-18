package com.estoqueapi.EstoqueApi.Servicos;

import com.estoqueapi.EstoqueApi.Entidades.*;
import com.estoqueapi.EstoqueApi.Enums.PerfilUsuario;
import com.estoqueapi.EstoqueApi.Exceptions.MovimentacaoInvalidaException;
import com.estoqueapi.EstoqueApi.Repositorios.EstoqueRepository;
import com.estoqueapi.EstoqueApi.Repositorios.ItensRepository;
import com.estoqueapi.EstoqueApi.Repositorios.MovimentacoesRepository;
import com.estoqueapi.EstoqueApi.Repositorios.UsuariosRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.persistence.EntityNotFoundException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
class ValidacoesServiceTest {
    private Itens item;
    private Estoque estoque;
    private Movimentacoes m;
    private Usuarios user;

    @Mock
    private MovimentacoesRepository movimentacoesRepository;
    @Mock
    private UsuariosService usuariosService;

    @Mock
    private EstoqueService estoqueService;

    @Mock
    private ItensService itensService;

    @Mock
    private PrevisoesService previsoesService;

    @Mock
    private ReservasService reservasService;

    @InjectMocks
    private ValidacoesService validacoesService;



    @BeforeEach
    void setup(){
        item = new Itens();
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

        user = new Usuarios("JOSE","asdf", PerfilUsuario.COMUM,"jose@jose");
        user.setIdUsuario(1l);

        m = new Movimentacoes();
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

        Mockito.when(usuariosService.buscarUsuarioById(user.getIdUsuario())).thenReturn(user);
        Mockito.when(itensService.consultarItemById(item.getIdItem())).thenReturn(item);
        Mockito.when(estoqueService.buscarEstoqueById(estoque.getIdEstoque())).thenReturn(estoque);

        Assertions.assertEquals(m,validacoesService.validarMovimentacao(m));
    }

    @Test
    @DisplayName("Deve lancar Excecao se quantidade menor ou igual a zero ")
    void lancaExcecaoSeQuantidadeMenorOuIgualZero(){
        //Mockando o retorno para forcar erro
        m.setQuantidade(0);

        //Mockando retornos corretos
        Mockito.when(usuariosService.buscarUsuarioById(user.getIdUsuario())).thenReturn(user);
        Mockito.when(itensService.consultarItemById(item.getIdItem())).thenReturn(item);
        Mockito.when(estoqueService.buscarEstoqueById(estoque.getIdEstoque())).thenReturn(estoque);

        //Testanto a funcao com objeto movimentacoes
        MovimentacaoInvalidaException ex  = Assertions.assertThrowsExactly(MovimentacaoInvalidaException.class,() -> validacoesService.validarMovimentacao(m));
        Assertions.assertEquals("Quantidade Inválida!",ex.getMessage());
    }

    @Test
    @DisplayName("Deve retornar uma previsao encontrada para movimentacao")
    void retornaUmaPrevisaoEncontradaParaMovimentacao(){
        //Mockando lista para retorno
        Previsoes pMock = new Previsoes();
        Itens itemMock = item;
        String ordemMock = m.getOrigemDestino();
        float qtdMock = m.getQuantidade();
        pMock.setItem(itemMock);
        pMock.setOrdem(ordemMock);
        pMock.setFinalizada(false);
        pMock.setQuantidadePrevista(qtdMock);
        List<Previsoes> listMock = new ArrayList<>();
        listMock.add(pMock);

        //Mockando retorno
        Mockito.when(previsoesService.consultarPendentesByIdItem(item.getIdItem())).thenReturn(listMock);

        //Testando
        Previsoes pRetorno = pMock;
        Assertions.assertEquals(pRetorno,validacoesService.consultaPrevisoesByMovimentacao(m));
    }

    @Test
    @DisplayName("Deve lancar excecao se origem previsao invalida")
    void lancaExcecaoSeOrigemPrevisaoInvalida(){
        //Mockando lista para retorno
        Previsoes pMock = new Previsoes();
        pMock.setItem(item);
        pMock.setOrdem(m.getOrigemDestino());
        pMock.setFinalizada(false);
        pMock.setQuantidadePrevista(m.getQuantidade());
        List<Previsoes> listMock = new ArrayList<>();
        listMock.add(pMock);

        //Mockando retorno
        Mockito.when(previsoesService.consultarPendentesByIdItem(item.getIdItem())).thenReturn(listMock);

        //Testando ordem inexistente na lista
        m.setOrigemDestino("Ordem4321");
        Assertions.assertThrows(MovimentacaoInvalidaException.class,() -> validacoesService.consultaPrevisoesByMovimentacao(m));

        //Testando ordem vazia
        m.setOrigemDestino("");
        Assertions.assertThrows(MovimentacaoInvalidaException.class,() -> validacoesService.consultaPrevisoesByMovimentacao(m));

        //confirmando se o retorno mockado foi chamado 2 vezes
        Mockito.verify(previsoesService,Mockito.times(2)).consultarPendentesByIdItem(item.getIdItem());
    }

    @Test
    @DisplayName("Deve lancar excecao se quantidade previsao diferente da movimentacao")
    void lancaExcecaoSeQuantidadePrevisaoDiferenteMovimentacao(){
        //Mockando lista para retorno
        Previsoes pMock = new Previsoes();
        pMock.setItem(item);
        pMock.setOrdem(m.getOrigemDestino());
        pMock.setFinalizada(false);
        pMock.setQuantidadePrevista(m.getQuantidade());
        List<Previsoes> listMock = new ArrayList<>();
        listMock.add(pMock);

        //Mockando retorno
        Mockito.when(previsoesService.consultarPendentesByIdItem(item.getIdItem())).thenReturn(listMock);

        //Testando quantidade movimentada diferente
        m.setQuantidade(1000f);
        Assertions.assertThrows(MovimentacaoInvalidaException.class,() -> validacoesService.consultaPrevisoesByMovimentacao(m));

        //confirmando se o retorno mockado foi chamado 1 vezes
        Mockito.verify(previsoesService,Mockito.times(1)).consultarPendentesByIdItem(item.getIdItem());
    }

    @Test
    @DisplayName("Deve retornar reserva encontrada para movimentacao")
    void retornaListaReservasParaMovimentacao(){
        //Mockando lista para retorno
        Reservas rMock = new Reservas();
        Itens itemMock = item;
        String ordemMock = m.getOrigemDestino();
        float qtdMock = m.getQuantidade();
        rMock.setItem(itemMock);
        rMock.setOrdem(ordemMock);
        rMock.setFinalizada(false);
        rMock.setQuantidadeReserva(qtdMock);
        List<Reservas> listMock = new ArrayList<>();
        listMock.add(rMock);

        //Mockando retorno
        Mockito.when(reservasService.consultarPendentesByIdItem(item.getIdItem())).thenReturn(listMock);

        //Testando
        Reservas rRetornoEsperado = rMock;
        Assertions.assertEquals(rRetornoEsperado,validacoesService.consultaReservasByMovimentacao(m));

        //confirmando se o retorno mockado foi chamado 1 vez
        Mockito.verify(reservasService,Mockito.times(1)).consultarPendentesByIdItem(item.getIdItem());
    }

    @Test
    @DisplayName("Deve lancar excecao se destino reserva invalida")
    void lancaExcecaoSeDestinoReservaInvalida(){
        //Mockando lista para retorno
        Reservas rMock = new Reservas();
        Itens itemMock = item;
        String ordemMock = "OP1234";
        float qtdMock = m.getQuantidade();
        rMock.setItem(itemMock);
        rMock.setOrdem(ordemMock);
        rMock.setFinalizada(false);
        rMock.setQuantidadeReserva(qtdMock);
        List<Reservas> listMock = new ArrayList<>();
        listMock.add(rMock);

        //Mockando retorno
        Mockito.when(reservasService.consultarPendentesByIdItem(item.getIdItem())).thenReturn(listMock);

        //Testando destino inexistente na lista
        m.setOrigemDestino("OP4321");
        Assertions.assertThrows(MovimentacaoInvalidaException.class,() -> validacoesService.consultaReservasByMovimentacao(m));

        //Testando ordem vazia
        m.setOrigemDestino("");
        Assertions.assertThrows(MovimentacaoInvalidaException.class,() -> validacoesService.consultaReservasByMovimentacao(m));

        //confirmando se o retorno mockado foi chamado 2 vezes
        Mockito.verify(reservasService,Mockito.times(2)).consultarPendentesByIdItem(item.getIdItem());
    }

    @Test
    @DisplayName("Deve lancar excecao se quantidade reserva diferente da movimentacao")
    void lancaExcecaoSeQuantidadeReservaDiferenteMovimentacao(){
        //Mockando lista para retorno
        Reservas rMock = new Reservas();
        rMock.setItem(item);
        rMock.setOrdem(m.getOrigemDestino());
        rMock.setFinalizada(false);
        rMock.setQuantidadeReserva(m.getQuantidade());
        List<Reservas> listMock = new ArrayList<>();
        listMock.add(rMock);

        //Mockando retorno
        Mockito.when(reservasService.consultarPendentesByIdItem(item.getIdItem())).thenReturn(listMock);

        //Testando destino inexistente na lista
        m.setQuantidade(1000f);
        Assertions.assertThrows(MovimentacaoInvalidaException.class,() -> validacoesService.consultaReservasByMovimentacao(m));

        //confirmando se o retorno mockado foi chamado 1 vezes
        Mockito.verify(reservasService,Mockito.times(1)).consultarPendentesByIdItem(item.getIdItem());
    }
}