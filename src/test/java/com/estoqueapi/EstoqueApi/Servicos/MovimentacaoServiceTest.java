package com.estoqueapi.EstoqueApi.Servicos;

import com.estoqueapi.EstoqueApi.Entidades.*;
import com.estoqueapi.EstoqueApi.Enums.PerfilUsuario;
import com.estoqueapi.EstoqueApi.Repositorios.MovimentacaoRepository;
import com.estoqueapi.EstoqueApi.Utils.ConversorData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.sql.Date;
import java.time.Instant;
import java.time.LocalDateTime;
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

        user = new Usuario("JOSE","asdf", PerfilUsuario.COMUM,"jose@jose");

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
        previsao.setDataPrevista(ConversorData.toInstant(LocalDateTime.parse("2022-11-16T00:00:00")));
        previsao.setUsuario(user);
        previsao.setIdPrevisao(10l);

        reserva = new Reserva();
        reserva.setFinalizada(false);
        reserva.setQuantidadeReserva(10);
        reserva.setOrdem("PO1000");
        reserva.setItem(item);
        reserva.setDataPrevista(ConversorData.toInstant(LocalDateTime.parse("2022-11-16T00:00:00")));
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

        //Mockando as funcoes
        Mockito.when(movimentacaoRepository.save(m)).thenReturn(m);
        Mockito.when(validacaoService.validarMovimentacao(m)).thenReturn(m);
        Mockito.when(validacaoService.consultaPrevisoesByMovimentacao(m)).thenReturn(previsao);
        Mockito.when(estoqueService.adicionarEstoque(item.getIdItem(), m.getQuantidade())).thenReturn(estoque);
        Mockito.when(previsaoService.alterarPrevisao(previsao.getIdPrevisao(),previsao)).thenReturn(previsao);

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

        //Mockando as funcoes
        Mockito.when(movimentacaoRepository.save(m)).thenReturn(m);
        Mockito.when(validacaoService.validarMovimentacao(m)).thenReturn(m);
        Mockito.when(validacaoService.consultaReservasByMovimentacao(m)).thenReturn(reserva);
        Mockito.when(estoqueService.adicionarEstoque(item.getIdItem(), m.getQuantidade())).thenReturn(estoque);
        Mockito.when(reservaService.alterar(reserva.getIdReserva(),reserva)).thenReturn(reserva);

        //Testando
        Assertions.assertEquals(tipoEsperado, movimentacaoService.saidaItem(m).getTipo());
        Assertions.assertNotNull(movimentacaoService.saidaItem(m).getDataMovimentacao());
    }

    @Test
    @DisplayName("RetornaListaMovimentacoes")
    public void RetornaListaMovimentacoes(){
        List<Movimentacao> lista = new ArrayList<>();
        lista.add(m);
        Mockito.when(movimentacaoRepository.findAll()).thenReturn(lista);

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
