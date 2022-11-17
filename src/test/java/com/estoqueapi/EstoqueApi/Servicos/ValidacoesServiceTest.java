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
    private Optional<Usuarios> optUsuario;
    private Optional<Itens> optItem;
    private Optional<Estoque> optEstoque;

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
        user.setIdUsuario(1);

        m = new Movimentacoes();
        m.setItem(item);
        m.setEstoque(estoque);
        m.setDataMovimentacao(Instant.now());
        m.setQuantidade(25);
        m.setUsuario(user);
        m.setOrigemDestino("ordem1234");

        //Retornos dos repositories
        optUsuario = Optional.of(user);
        optItem = Optional.of(item);
        optEstoque = Optional.of(estoque);

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
    @DisplayName("Lanca Excecao se quantidade menor ou igual a zero ")
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
    @DisplayName("Deve retornar lista de previsoes para movimentacao")
    void retornaListaPrevisoesParaMovimentacao(){
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
        Mockito.when(previsoesService.consultarByIdItem(item.getIdItem())).thenReturn(listMock);

        //Testando
        Assertions.assertEquals(listMock,validacoesService.consultaPrevisoesByMovimentacao(m));
    }

    @Test
    @DisplayName("Nao retorna previsoes se item ou ordem ou quantidade diferente")
    void naoRetornaPrevisoesParaMovimentacaoSeNaoEncontrada(){
        //Mockando lista para retorno
        Previsoes pMock = new Previsoes();
        pMock.setItem(new Itens()); //Valor falso para forcar erro
        pMock.setOrdem(m.getOrigemDestino());
        pMock.setFinalizada(false);
        pMock.setQuantidadePrevista(m.getQuantidade());
        List<Previsoes> listMock = new ArrayList<>();
        listMock.add(pMock);

        //Mockando retorno
        Mockito.when(previsoesService.findByFinalizada(false)).thenReturn(listMock);

        //Testando
        List<Previsoes> listaVaziaEsperada = new ArrayList<>();
        Assertions.assertEquals(listaVaziaEsperada,validacoesService.consultaPrevisoesByMovimentacao(m));
    }

    @Test
    @DisplayName("Deve retornar lista de reservas para movimentacao")
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
        Mockito.when(reservasService.consultarByIdItem(item.getIdItem())).thenReturn(listMock);

        //Testando
        Assertions.assertEquals(listMock,validacoesService.consultaReservasByMovimentacao(m));
    }

    @Test
    @DisplayName("Nao retorna reservas se ordem ou quantidade diferente")
    void naoRetornaReservasParaMovimentacaoSeNaoEncontrada(){
        //Mockando lista para retorno
        Reservas rMock = new Reservas();
        Itens itemMock = item;
        String ordemMock = "ordem4321";  //Valor falso para forcar erro
        float qtdMock = m.getQuantidade();
        rMock.setItem(itemMock);
        rMock.setOrdem(ordemMock);
        rMock.setFinalizada(false);
        rMock.setQuantidadeReserva(qtdMock);
        List<Reservas> listMock = new ArrayList<>();
        listMock.add(rMock);

        //Mockando retorno
        Mockito.when(reservasService.consultarByIdItem(item.getIdItem())).thenReturn(listMock);

        //TESTANDO
        List<Previsoes> listaVaziaEsperada = new ArrayList<>();

        Assertions.assertEquals(listaVaziaEsperada,validacoesService.consultaReservasByMovimentacao(m));
    }
}