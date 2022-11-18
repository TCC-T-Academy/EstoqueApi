package com.estoqueapi.EstoqueApi.Servicos;

import com.estoqueapi.EstoqueApi.Entidades.*;
import com.estoqueapi.EstoqueApi.Enums.PerfilUsuario;
import com.estoqueapi.EstoqueApi.Exceptions.MovimentacaoInvalidaException;
import com.estoqueapi.EstoqueApi.Repositorios.EstoqueRepository;
import com.estoqueapi.EstoqueApi.Repositorios.ItensRepository;
import com.estoqueapi.EstoqueApi.Repositorios.MovimentacoesRepository;
import com.estoqueapi.EstoqueApi.Repositorios.UsuariosRepository;
import com.estoqueapi.EstoqueApi.Servicos.MovimentacoesService;
import com.estoqueapi.EstoqueApi.Servicos.ValidacoesService;
import org.assertj.core.internal.bytebuddy.dynamic.DynamicType;
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
import java.text.DateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
public class MovimentacoesServiceTest {

    private Itens item;
    private Estoque estoque;
    private Movimentacoes m;
    private Usuarios user;
    private Previsoes previsao;

    private Reservas reserva;

    @Mock
    private MovimentacoesRepository movimentacoesRepository;

    @Mock
    private  EstoqueService estoqueService;

    @Mock
    private ValidacoesService validacoesService;

    @Mock
    private PrevisoesService previsoesService;

    @Mock
    private ReservasService reservasService;

    @InjectMocks
    private MovimentacoesService movimentacoesService;


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

        m = new Movimentacoes();
        m.setItem(new Itens());
        m.setEstoque(estoque);
        m.setDataMovimentacao(Instant.now());
        m.setQuantidade(25);
        m.setUsuario(user);

        previsao = new Previsoes();
        previsao.setFinalizada(false);
        previsao.setQuantidadePrevista(10);
        previsao.setOrdem("CP1000");
        previsao.setItem(item);
        previsao.setDataPrevista(new Date(2022-11-16));
        previsao.setUsuario(user);
        previsao.setIdPrevisao(10l);

        reserva = new Reservas();
        reserva.setFinalizada(false);
        reserva.setQuantidadeReserva(10);
        reserva.setOrdem("PO1000");
        reserva.setItem(item);
        reserva.setDataPrevista(new Date(2022-11-16));
        reserva.setUsuario(user);
        reserva.setIdReserva(10l);

    }

    @Test
    @DisplayName("RetornaMovimentacaoAoSalvar")
    public void retornaMovimentacaoAoSalvar(){

        Mockito.when(movimentacoesRepository.save(m)).thenReturn(m);
        Assertions.assertNotNull(movimentacoesService.salvar(m));
        Mockito.verify(movimentacoesRepository,Mockito.times(1)).save(m);

    }

    @Test
    @DisplayName("RetornaMovimentacaoAoFazerEntradaItem")
    public void RetornaMovimentacaoAoFazerEntradaItem(){
        m.setTipo("");
        m.setDataMovimentacao(null);
        String tipoEsperado = "IN";

        //Mockando as funcoes
        Mockito.when(movimentacoesRepository.save(m)).thenReturn(m);
        Mockito.when(validacoesService.validarMovimentacao(m)).thenReturn(m);
        Mockito.when(validacoesService.consultaPrevisoesByMovimentacao(m)).thenReturn(previsao);
        Mockito.when(estoqueService.adicionarEstoque(item.getIdItem(), m.getQuantidade())).thenReturn(estoque);
        Mockito.when(previsoesService.alterarPrevisao(previsao.getIdPrevisao(),previsao)).thenReturn(previsao);

        //Testando
        Assertions.assertEquals(tipoEsperado, movimentacoesService.entradaItem(m).getTipo());
        Assertions.assertNotNull(movimentacoesService.entradaItem(m).getDataMovimentacao());
    }

    @Test
    @DisplayName("RetornaMovimentacaoAoFazerSaidaItem")
    public void RetornaMovimentacaoAoFazerSaidaItem(){
        m.setTipo("");
        m.setDataMovimentacao(null);
        String tipoEsperado = "OUT";

        //Mockando as funcoes
        Mockito.when(movimentacoesRepository.save(m)).thenReturn(m);
        Mockito.when(validacoesService.validarMovimentacao(m)).thenReturn(m);
        Mockito.when(validacoesService.consultaReservasByMovimentacao(m)).thenReturn(reserva);
        Mockito.when(estoqueService.adicionarEstoque(item.getIdItem(), m.getQuantidade())).thenReturn(estoque);
        Mockito.when(reservasService.alterar(reserva.getIdReserva(),reserva)).thenReturn(reserva);

        //Testando
        Assertions.assertEquals(tipoEsperado, movimentacoesService.saidaItem(m).getTipo());
        Assertions.assertNotNull(movimentacoesService.saidaItem(m).getDataMovimentacao());
    }

    @Test
    @DisplayName("RetornaListaMovimentacoes")
    public void RetornaListaMovimentacoes(){
        List<Movimentacoes> lista = new ArrayList<>();
        lista.add(m);
        Mockito.when(movimentacoesRepository.findAll()).thenReturn(lista);

        Assertions.assertEquals(lista.size(),movimentacoesService.consultar().size());
    }

    @Test
    @DisplayName("RetornaListaMovimentacoesPorIdItem")
    public void RetornaListaMovimentacoesPorId(){
        List<Movimentacoes> lista = new ArrayList<>();
        lista.add(m);
        Mockito.when(movimentacoesRepository.findAllByIdItem(item.getIdItem())).thenReturn(lista);

        Assertions.assertEquals(lista.size(),movimentacoesService.consultarByIdItem(item.getIdItem()).size());
    }
}
