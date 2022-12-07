package com.estoqueapi.EstoqueApi.Controllers;

import com.estoqueapi.EstoqueApi.Dtos.MovimentacaoDTO;
import com.estoqueapi.EstoqueApi.Dtos.MovimentacaoNovaDTO;
import com.estoqueapi.EstoqueApi.Dtos.UsuarioPublicoDTO;
import com.estoqueapi.EstoqueApi.Entidades.Estoque;
import com.estoqueapi.EstoqueApi.Entidades.Item;
import com.estoqueapi.EstoqueApi.Entidades.Movimentacao;
import com.estoqueapi.EstoqueApi.Entidades.Usuario;
import com.estoqueapi.EstoqueApi.Mapper.Mapper;
import com.estoqueapi.EstoqueApi.Servicos.MovimentacaoService;
import com.estoqueapi.EstoqueApi.Utils.ConversorData;
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
public class MovimentacaoControllerTest {

    private Movimentacao m1;
    private Movimentacao m2;
    private Movimentacao m3;
    private MovimentacaoNovaDTO mNovaDTO1;
    private MovimentacaoDTO mDTO1;
    private MovimentacaoNovaDTO mNovaDTO2;
    private MovimentacaoDTO mDTO2;

    private Item i1;
    private Item i2;
    private Estoque e1;
    private Estoque e2;
    private Usuario u;
    private Long idItemExistente1;
    private Long idItemExistente2;
    private Long idItemInexistente;
    private List<Movimentacao> listaTotal;

    private List<Movimentacao> listaPorItem1;
    private List<Movimentacao> listaPorItem2;
    Instant instante;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MovimentacaoService service;

    @MockBean
    private Mapper mapper;

    @BeforeEach
    void setup(){

        idItemExistente1 = 1000l;
        idItemExistente2 = 2000l;
        idItemInexistente = 3000l;

        listaTotal = new ArrayList<>();

        listaPorItem1 = new ArrayList<>();
        listaPorItem2 = new ArrayList<>();

        instante = Instant.now();

        i1 = new Item();
        i1.setIdItem(idItemExistente1);
        i1.setDescricao("Item 1");
        i1.setFamilia("Teste");
        i1.setGrupo("Teste");
        i1.setEstoqueSeguranca(20f);
        i1.setUnidade("PC");

        i2 = new Item();
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

        u = new Usuario();
        u.setNome("Usuário de teste");
        u.setEmail("usuario@email.com");

        m1 = new Movimentacao();
        m1.setIdMovimentacao(0l);
        m1.setDataMovimentacao(instante);
        m1.setTipo("IN");
        m1.setQuantidade(5f);
        m1.setEstoque(e1);
        m1.setItem(i1);
        m1.setUsuario(u);
        m1.setOrigemDestino("CP1010");

        m2 = new Movimentacao();
        m2.setIdMovimentacao(1l);
        m2.setDataMovimentacao(instante);
        m2.setTipo("IN");
        m2.setQuantidade(5f);
        m2.setEstoque(e2);
        m2.setItem(i2);
        m2.setUsuario(u);
        m2.setOrigemDestino("CP1020");

        m3 = new Movimentacao();
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

        mNovaDTO1 = new MovimentacaoNovaDTO(m1.getOrigemDestino(),m1.getQuantidade(),m1.getItem().getIdItem(),m1.getUsuario().getIdUsuario());
        mDTO1 = new MovimentacaoDTO(m1.getIdMovimentacao(),
                ConversorData.toLocalDateTime(m1.getDataMovimentacao()),
                m1.getTipo(),
                m1.getOrigemDestino(),
                m1.getQuantidade(),
                new UsuarioPublicoDTO(u.getIdUsuario(), u.getNome()),
                m1.getEstoque());
        mNovaDTO2 = new MovimentacaoNovaDTO(m2.getOrigemDestino(),m2.getQuantidade(),m2.getItem().getIdItem(),m2.getUsuario().getIdUsuario());
        mDTO2 = new MovimentacaoDTO(m2.getIdMovimentacao(),
                ConversorData.toLocalDateTime(m2.getDataMovimentacao()),
                m2.getTipo(),
                m2.getOrigemDestino(),
                m2.getQuantidade(),
                new UsuarioPublicoDTO(u.getIdUsuario(), u.getNome()),
                m2.getEstoque());

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

        resultado.andExpect(status().isOk());
        resultado.andExpect(jsonPath("$.size()").value(listaTotal.size()));
//        resultado.andDo(print());
    }

    @Test
    public void retornaFoundQuandoConsultaMovimentacaoIdItemExistente1() throws Exception {
        ResultActions resultado =
                mockMvc.perform(get("/movimentacoes/{idItemExistente1}", idItemExistente1).accept(MediaType.APPLICATION_JSON));

        resultado.andExpect(status().isOk());
        resultado.andExpect(jsonPath("$.size()").value(listaPorItem1.size()));
//        resultado.andDo(print());
    }

    @Test
    public void retornaFoundQuandoConsultaMovimentacaoIdItemExistente2() throws Exception {
        ResultActions resultado =
                mockMvc.perform(get("/movimentacoes/{idItemExistente2}", idItemExistente2).accept(MediaType.APPLICATION_JSON));

        resultado.andExpect(status().isOk());
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
        Mockito.when(mapper.toMovimentacaoDto(m1)).thenReturn(mDTO1);
        Mockito.when(mapper.toMovimentacao(mDTO1)).thenReturn(m1);

        String jsonBody = objectMapper.writeValueAsString(mNovaDTO1);
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
        Mockito.when(mapper.toMovimentacaoDto(m2)).thenReturn(mDTO2);
        Mockito.when(mapper.toMovimentacao(mDTO2)).thenReturn(m2);

        String jsonBody = objectMapper.writeValueAsString(mNovaDTO2);
        ResultActions resultado = mockMvc.perform(post("/movimentacoes/saida")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonBody));

        resultado.andExpect(status().isCreated());
        resultado.andExpect(jsonPath("$.idMovimentacao").exists());
        resultado.andExpect(jsonPath("$.idMovimentacao").value(1));
//        resultado.andDo(print());
    }
}
