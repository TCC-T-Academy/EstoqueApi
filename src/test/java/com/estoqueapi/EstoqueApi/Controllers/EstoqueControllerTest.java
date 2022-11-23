package com.estoqueapi.EstoqueApi.Controllers;

import com.estoqueapi.EstoqueApi.Entidades.Estoque;
import com.estoqueapi.EstoqueApi.Entidades.Item;
import com.estoqueapi.EstoqueApi.Servicos.EstoqueService;
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
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Testar seguintes endpoints:
 * GET /estoque/
 * GET /estoque/{idItemExistente}
 * GET /estoque/{idItemInexistente}
 */

@SpringBootTest
@AutoConfigureMockMvc
public class EstoqueControllerTest {

    private Item item;
    private Long idItemExistente;
    private Long idItemNaoExistente;
    private List<Estoque> lista;
    private Estoque e;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EstoqueService estoqueService;



    @BeforeEach
    void setup(){

        idItemExistente = 1000l;
        idItemNaoExistente = 2000l;
        lista = new ArrayList<>();

        item = new Item();
        item.setIdItem(idItemExistente);
        item.setDescricao("Item teste para teste");
        item.setFamilia("Teste");
        item.setGrupo("Teste");
        item.setEstoqueSeguranca(20f);
        item.setUnidade("PC");

        e = new Estoque();
        e.setItem(item);
        e.setEstoqueReal(40f);
        e.setLocalizacao("AA1234");
        e.setIdEstoque(10l);

        lista.add(e);

        Mockito.when(estoqueService.listarEstoque()).thenReturn(lista);
        Mockito.when(estoqueService.buscarEstoqueIdItem(idItemExistente)).thenReturn(e);
        Mockito.when(estoqueService.buscarEstoqueIdItem(idItemNaoExistente)).thenThrow(EntityNotFoundException.class);

    }

    @Test
    public void retornaFoundQuandoConsultaEstoque() throws Exception {

        ResultActions resultado =
                mockMvc.perform(get("/estoque").accept(MediaType.APPLICATION_JSON));

        resultado.andExpect(status().isFound());
        resultado.andExpect(jsonPath("$.size()").value(lista.size()));
        resultado.andExpect(jsonPath("$[0].idEstoque").value(10));
//        resultado.andDo(print());
    }

    @Test
    public void retornaFoundQuandoConsultaEstoquePorIdItem() throws Exception {
        ResultActions resultado =
                mockMvc.perform(get("/estoque/{idItem}", idItemExistente).accept(MediaType.APPLICATION_JSON));

        resultado.andExpect(status().isFound());
        resultado.andExpect(jsonPath("$.idEstoque").value(10));
//        resultado.andDo(print());
    }

    @Test
    public void retornaNotFoundQuandoConsultaEstoquePorIdItemInexistente() throws Exception {
        ResultActions resultado =
                mockMvc.perform(get("/estoque/{idItemInexistente}", idItemNaoExistente).accept(MediaType.APPLICATION_JSON));

        resultado.andExpect(res -> assertTrue(res.getResolvedException() instanceof EntityNotFoundException));
        resultado.andExpect(status().isNotFound());
        resultado.andExpect(content().contentType(MediaType.APPLICATION_JSON)).andExpect(jsonPath("$.error").value(
                "Recurso não encontrado"));
        resultado.andDo(print());
    }
}
