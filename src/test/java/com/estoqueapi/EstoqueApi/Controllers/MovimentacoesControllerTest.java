package com.estoqueapi.EstoqueApi.Controllers;

import com.estoqueapi.EstoqueApi.Entidades.Estoque;
import com.estoqueapi.EstoqueApi.Entidades.Itens;
import com.estoqueapi.EstoqueApi.Entidades.Movimentacoes;
import com.estoqueapi.EstoqueApi.Entidades.Usuarios;
import com.estoqueapi.EstoqueApi.Enums.PerfilUsuario;
import com.estoqueapi.EstoqueApi.Repositorios.MovimentacoesRepository;
import com.estoqueapi.EstoqueApi.Servicos.MovimentacoesService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import javax.persistence.EntityNotFoundException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Testar seguintes endpoints:
 * GET /movimentacoes/
 * GET /movimentacoes/{idItem}
 * GET /movimentacoes/{idItemInexistente}
 * POST /movimentacoes/entrada
 * POST /movimentacoes/saida
 */

@SpringBootTest
@AutoConfigureMockMvc
public class MovimentacoesControllerTest {

    private Movimentacoes m1;
    private Movimentacoes m2;
    private Movimentacoes m3;
    private Itens i1;
    private Itens i2;
    private Estoque e1;
    private Estoque e2;
    private Usuarios u;
    private Long idItemExistente1;
    private Long idItemExistente2;
    private Long idItemInexistente;
    private List<Movimentacoes> listaTotal;

    private List<Movimentacoes> listaPorItem1;
    private List<Movimentacoes> listaPorItem2;
    Instant instante;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MovimentacoesService service;

    @MockBean
    private MovimentacoesRepository repository;

    @BeforeEach
    void setup(){

        idItemExistente1 = 1000l;
        idItemExistente2 = 2000l;
        idItemInexistente = 3000l;

        listaTotal = new ArrayList<>();

        listaPorItem1 = new ArrayList<>();
        listaPorItem2 = new ArrayList<>();

        instante = Instant.now();

        i1 = new Itens();
        i1.setIdItem(idItemExistente1);
        i1.setDescricao("Item 1");
        i1.setFamilia("Teste");
        i1.setGrupo("Teste");
        i1.setEstoqueSeguranca(20f);
        i1.setUnidade("PC");

        i2 = new Itens();
        i2.setIdItem(idItemExistente2);
        i2.setDescricao("Item 2");
        i2.setFamilia("Teste");
        i2.setGrupo("Teste");
        i2.setEstoqueSeguranca(20f);
        i2.setUnidade("PC");

        e1 = new Estoque();
        e1.setItem(i1);
        e1.setEstoqueReal(40f);
        e1.setLocalizacao("AA1234");
        e1.setIdEstoque(10l);

        e2 = new Estoque();
        e2.setItem(i2);
        e2.setEstoqueReal(40f);
        e2.setLocalizacao("AA4321");
        e2.setIdEstoque(10l);

        u = new Usuarios();
        u.setNome("Usuário de teste");
        u.setEmail("usuario@email.com");
        u.setPerfil(PerfilUsuario.COMUM);

        m1 = new Movimentacoes();
        m1.setIdMovimentacao(0l);
        m1.setDataMovimentacao(instante);
        m1.setTipo("IN");
        m1.setQuantidade(5f);
        m1.setEstoque(e1);
        m1.setItem(i1);
        m1.setUsuario(u);
        m1.setOrigemDestino("CP1010");

        m2 = new Movimentacoes();
        m2.setIdMovimentacao(1l);
        m2.setDataMovimentacao(instante);
        m2.setTipo("IN");
        m2.setQuantidade(5f);
        m2.setEstoque(e2);
        m2.setItem(i2);
        m2.setUsuario(u);
        m2.setOrigemDestino("CP1020");

        m3 = new Movimentacoes();
        m3.setIdMovimentacao(3l);
        m3.setDataMovimentacao(instante);
        m3.setTipo("IN");
        m3.setQuantidade(5f);
        m3.setEstoque(e2);
        m3.setItem(i2);
        m3.setUsuario(u);
        m3.setOrigemDestino("CP1030");

        listaTotal.add(m1);
        listaTotal.add(m2);
        listaTotal.add(m3);

        listaPorItem1.add(m1); // 1 movimentação ligada ao item 1
        listaPorItem2.add(m2);
        listaPorItem2.add(m3); // 2 movimentações ligadas ao item 2

        Mockito.when(service.consultar()).thenReturn(listaTotal);
        Mockito.when(service.consultarByIdItem(idItemExistente1)).thenReturn(listaPorItem1);
        Mockito.when(service.consultarByIdItem(idItemExistente2)).thenReturn(listaPorItem2);
        Mockito.when(service.consultarByIdItem(idItemInexistente)).thenThrow(EntityNotFoundException.class);
        Mockito.when(service.entradaItem(any())).thenReturn(m1);
        Mockito.when(service.saidaItem(any())).thenReturn(m2);

    }
    @Test
    public void retornaFoundQuandoConsultaMovimentacoes() throws Exception {
        ResultActions resultado =
                mockMvc.perform(get("/movimentacoes").accept(MediaType.APPLICATION_JSON));

        resultado.andExpect(status().isFound());
        resultado.andExpect(jsonPath("$.size()").value(listaTotal.size()));
//        resultado.andDo(print());
    }

    @Test
    public void retornaFoundQuandoConsultaMovimentacaoIdItemExistente1() throws Exception {
        ResultActions resultado =
                mockMvc.perform(get("/movimentacoes/{idItemExistente1}", idItemExistente1).accept(MediaType.APPLICATION_JSON));

        resultado.andExpect(status().isFound());
        resultado.andExpect(jsonPath("$.size()").value(listaPorItem1.size()));
//        resultado.andDo(print());
    }

    @Test
    public void retornaFoundQuandoConsultaMovimentacaoIdItemExistente2() throws Exception {
        ResultActions resultado =
                mockMvc.perform(get("/movimentacoes/{idItemExistente2}", idItemExistente2).accept(MediaType.APPLICATION_JSON));

        resultado.andExpect(status().isFound());
        resultado.andExpect(jsonPath("$.size()").value(listaPorItem2.size()));
//        resultado.andDo(print());
    }

    @Test
    public void retornaNotFoundQuandoConsultaMovimentacaoPorIdItemInexistente() throws Exception {
        ResultActions resultado =
                mockMvc.perform(get("/movimentacoes/{idItem}", idItemInexistente).accept(MediaType.APPLICATION_JSON));

        resultado.andExpect(res -> assertTrue(res.getResolvedException() instanceof EntityNotFoundException));
        resultado.andExpect(status().isNotFound());
        resultado.andExpect(content().contentType(MediaType.APPLICATION_JSON)).andExpect(jsonPath("$.error").value(
                "Recurso não encontrado"));
//        resultado.andDo(print());
    }

    @Test
    public void retornaCreatedQuandoPostMovimentacaoEntrada() throws Exception {
        String jsonBody = objectMapper.writeValueAsString(m1);
        ResultActions resultado = mockMvc.perform(post("/movimentacoes/entrada")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonBody));
        resultado.andExpect(status().isCreated());
        resultado.andExpect(jsonPath("$.idMovimentacao").exists());
        resultado.andExpect(jsonPath("$.idMovimentacao").value(0));
//        resultado.andDo(print());
    }
    @Test
    public void retornaCreatedQuandoPostMovimentacaoSaida() throws Exception {
        String jsonBody = objectMapper.writeValueAsString(m2);
        ResultActions resultado = mockMvc.perform(post("/movimentacoes/saida")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonBody));

        resultado.andExpect(status().isCreated());
        resultado.andExpect(jsonPath("$.idMovimentacao").exists());
        resultado.andExpect(jsonPath("$.idMovimentacao").value(1));
//        resultado.andDo(print());
    }
}
