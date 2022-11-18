package com.estoqueapi.EstoqueApi.Controllers;

import com.estoqueapi.EstoqueApi.Entidades.Itens;
import com.estoqueapi.EstoqueApi.Servicos.ItensService;
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

import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Testar seguintes endpoints:
 * GET /itens/
 */

@SpringBootTest
@AutoConfigureMockMvc
public class ItensControllerTest {

    private Itens item;
    private List<Itens> listaDeItens;
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ItensService service;

    @BeforeEach
    void setup(){

        listaDeItens = new ArrayList<>();

        item = new Itens();
        item.setIdItem(1000l);
        item.setDescricao("Item teste para teste");
        item.setFamilia("Teste");
        item.setGrupo("Teste");
        item.setEstoqueSeguranca(20f);
        item.setUnidade("PC");

        listaDeItens.add(item);

    }

    @Test
    public void retornaFoundQuandoConsultaItens() throws Exception {
        Mockito.when(service.listarItens()).thenReturn(listaDeItens);

        ResultActions resultado = mockMvc.perform(get("/itens").accept(MediaType.APPLICATION_JSON));

        resultado.andExpect(status().isFound()); // Resultado não é 200 (ok), mas 302 (found)
        resultado.andExpect(jsonPath("$.size()").value(listaDeItens.size()));
        resultado.andExpect(jsonPath("$[0].idItem").value(1000));
        resultado.andDo(print());
    }
}
