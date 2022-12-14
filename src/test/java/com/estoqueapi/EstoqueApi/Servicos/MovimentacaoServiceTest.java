package com.estoqueapi.EstoqueApi.Servicos;

import com.estoqueapi.EstoqueApi.Entidades.*;
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
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@ExtendWith(SpringExtension.class)
public class MovimentacaoServiceTest {

    private Item item;
    private Estoque estoque;
    private Movimentacao m;
    private Usuario user;
    private Previsao previsao;

    private Reserva reserva;

    @Mock
    private MovimentacaoRepository movimentacaoRepository;

    @Mock
    private EstoqueService estoqueService;

    @Mock
    private ValidacaoService validacaoService;

    @Mock
    private PrevisaoService previsaoService;

    @Mock
    private ReservaService reservaService;

    @Mock
    private ItemService itemService;

    @Mock
    private LogFuturoService logFuturoService;

    @InjectMocks
    private MovimentacaoService movimentacaoService;


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

        user = new Usuario("JOSE","asdf", "jose@jose");

        m = new Movimentacao();
        m.setItem(new Item());
        m.setEstoque(estoque);
        m.setDataMovimentacao(Instant.now());
        m.setQuantidade(25);
        m.setUsuario(user);

        previsao = new Previsao();
        previsao.setFinalizada(false);
        previsao.setQuantidadePrevista(10);
        previsao.setOrdem("CP1000");
        previsao.setItem(item);
        previsao.setDataPrevista(LocalDate.parse("2022-11-16"));
        previsao.setUsuario(user);
        previsao.setIdPrevisao(10l);

        reserva = new Reserva();
        reserva.setFinalizada(false);
        reserva.setQuantidadeReserva(10);
        reserva.setOrdem("PO1000");
        reserva.setItem(item);
        reserva.setDataPrevista(LocalDate.parse("2022-11-16"));
        reserva.setUsuario(user);
        reserva.setIdReserva(10l);

    }

    @Test
    @DisplayName("RetornaMovimentacaoAoSalvar")
    public void retornaMovimentacaoAoSalvar(){

        Mockito.when(movimentacaoRepository.save(m)).thenReturn(m);
        Assertions.assertNotNull(movimentacaoService.salvar(m));
        Mockito.verify(movimentacaoRepository,Mockito.times(1)).save(m);

    }

    @Test
    @DisplayName("RetornaMovimentacaoAoFazerEntradaItem")
    public void RetornaMovimentacaoAoFazerEntradaItem(){
        m.setTipo("");
        m.setDataMovimentacao(null);
        String tipoEsperado = "IN";

        Previsao mockPrevisao = new Previsao();
        mockPrevisao.setUsuario(previsao.getUsuario());
        mockPrevisao.setItem(previsao.getItem());
        mockPrevisao.setOrdem(previsao.getOrdem());
        mockPrevisao.setFinalizada(previsao.getFinalizada());
        mockPrevisao.setQuantidadePrevista(previsao.getQuantidadePrevista());
        mockPrevisao.setDataPrevista(previsao.getDataPrevista());
        mockPrevisao.setIdPrevisao(previsao.getIdPrevisao());

        //Mockando as funcoes
        Mockito.when(movimentacaoRepository.save(m)).thenReturn(m);
        Mockito.when(validacaoService.validarMovimentacao(m)).thenReturn(m);
        Mockito.when(validacaoService.consultaPrevisoesByMovimentacao(m)).thenReturn(previsao);
        Mockito.when(estoqueService.adicionarEstoque(item.getIdItem(), m.getQuantidade())).thenReturn(estoque);
        Mockito.when(previsaoService.alterarPrevisao(previsao.getIdPrevisao(),previsao)).thenReturn(previsao);
        Mockito.when(previsaoService.clonar(previsao)).thenReturn(mockPrevisao);

        //Testando
        Assertions.assertEquals(tipoEsperado, movimentacaoService.entradaItem(m).getTipo());
        Assertions.assertNotNull(movimentacaoService.entradaItem(m).getDataMovimentacao());
    }

    @Test
    @DisplayName("RetornaMovimentacaoAoFazerSaidaItem")
    public void RetornaMovimentacaoAoFazerSaidaItem(){
        m.setTipo("");
        m.setDataMovimentacao(null);
        String tipoEsperado = "OUT";

        Reserva mockReserva = new Reserva();
        mockReserva.setIdReserva(reserva.getIdReserva());
        mockReserva.setQuantidadeReserva(reserva.getQuantidadeReserva());
        mockReserva.setItem(reserva.getItem());
        mockReserva.setFinalizada(reserva.isFinalizada());
        mockReserva.setUsuario(reserva.getUsuario());
        mockReserva.setDataPrevista(reserva.getDataPrevista());
        mockReserva.setOrdem(reserva.getOrdem());

        LogFuturo mockLog = new LogFuturo("Reserva",reserva.getOrdem(),LocalDate.now().plus(10, ChronoUnit.DAYS),20f);
        List<LogFuturo> mockLogs = new ArrayList<>();
        mockLogs.add(mockLog);

        //Mockando as funcoes
        Mockito.when(movimentacaoRepository.save(m)).thenReturn(m);
        Mockito.when(validacaoService.validarMovimentacao(m)).thenReturn(m);
        Mockito.when(validacaoService.consultaReservasByMovimentacao(m)).thenReturn(reserva);
        Mockito.when(estoqueService.adicionarEstoque(item.getIdItem(), m.getQuantidade())).thenReturn(estoque);
        Mockito.when(reservaService.alterar(reserva.getIdReserva(),reserva)).thenReturn(reserva);
        Mockito.when(reservaService.clonar(reserva)).thenReturn(mockReserva);
        Mockito.when(estoqueService.atualizarEstoqueFuturo(reserva.getItem().getIdItem(),mockLogs)).thenReturn(new Estoque());

        //Testando
        Assertions.assertEquals(tipoEsperado, movimentacaoService.saidaItem(m).getTipo());
        Assertions.assertNotNull(movimentacaoService.saidaItem(m).getDataMovimentacao());
    }

    @Test
    @DisplayName("RetornaListaMovimentacoes")
    public void RetornaListaMovimentacoes(){
        List<Movimentacao> lista = new ArrayList<>();
        lista.add(m);
        Mockito.when(movimentacaoRepository.findAllOrderByDesc()).thenReturn(lista);

        Assertions.assertEquals(lista.size(), movimentacaoService.consultar().size());
    }

    @Test
    @DisplayName("RetornaListaMovimentacoesPorIdItem")
    public void RetornaListaMovimentacoesPorId(){
        List<Movimentacao> lista = new ArrayList<>();
        lista.add(m);
        Mockito.when(movimentacaoRepository.findAllByIdItem(item.getIdItem())).thenReturn(lista);

        Assertions.assertEquals(lista.size(), movimentacaoService.consultarByIdItem(item.getIdItem()).size());
    }
}
